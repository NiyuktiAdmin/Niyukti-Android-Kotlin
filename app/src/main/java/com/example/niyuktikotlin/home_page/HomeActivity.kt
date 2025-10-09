package com.example.niyuktikotlin.home_page

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.niyuktikotlin.mock_test_list.MockTestListActivity
import com.example.niyuktikotlin.R
import com.example.niyuktikotlin.all_courses.AllCoursesListActivity
import com.example.niyuktikotlin.all_courses.CourseFoldersActivity
import com.example.niyuktikotlin.free_material_tests.FreeMaterialTestActivity
import com.example.niyuktikotlin.menu_fragment.FragmentMainMenu
import com.example.niyuktikotlin.util.CourseBuyAdapter
import com.example.niyuktikotlin.models.CourseModel
import com.example.niyuktikotlin.my_performance.MyPerformanceActivity
import com.example.niyuktikotlin.util.SimpleImageAdapter
import io.appwrite.Client
import io.appwrite.Query
import io.appwrite.exceptions.AppwriteException
import io.appwrite.services.Account
import io.appwrite.services.Databases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class HomeActivity : AppCompatActivity() {
    private lateinit var welcomeName: TextView
    private lateinit var referralCode: TextView
    private lateinit var referCodeCopy: ImageView
    private lateinit var referCodeShare: ImageView
    private lateinit var navigationMenuBtn: ImageView

    private lateinit var allCoursesCard: CardView
    private lateinit var subjectCourseCard: CardView
    private lateinit var mockTestCard: CardView
    private lateinit var freeMaterial: CardView
    private lateinit var niyuktiChatBotCard: CardView
    private lateinit var myPerformanceCard: CardView

    private lateinit var offerRv: RecyclerView
    private lateinit var constableRv: RecyclerView
    private lateinit var recentlyAddedRv: RecyclerView
    private lateinit var packagesRv: RecyclerView

    private lateinit var layoutManager: LinearLayoutManager
    private val handler = Handler()
    private var currentCardIndex = 0
    private val AUTO_SCROLL_DELAY: Long = 3000
    private lateinit var offerAdapter: SimpleImageAdapter

    private val myRefCode = "Niyukti1"
    private var currentUserId: String? = null

    private lateinit var client: Client
    private lateinit var account: Account
    private lateinit var databases: Databases
    private val scope = CoroutineScope(Dispatchers.IO)

    private val autoScrollRunnable = object : Runnable {
        override fun run() {
//            TODO : need to change the number of items in offer rv here
            currentCardIndex = (currentCardIndex + 1) % 3 // 3 = current number of offers
            offerRv.smoothScrollToPosition(currentCardIndex)
            handler.postDelayed(this, AUTO_SCROLL_DELAY)
        }
    }

    private fun startAutoScrolling() {
        handler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        client = Client(this, getString(R.string.APPWRITE_ENDPOINT)).setProject(getString(R.string.APPWRITE_PROJECT_ID))
        account = Account(client)
        databases = Databases(client)

        initialiseVariables()

        setOfferRv()

        fetchUserName { userId ->
            currentUserId = userId
            fetchAllCoursesAndSetRVs(userId)
        }

        referralCode.text = myRefCode
        setCardButtons()

        referCodeCopy.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Referral Code", referralCode.text.toString())
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchAllCoursesAndSetRVs(userId: String?) {
        scope.launch {
            try {
                val allCourses = fetchAllCoursesForDisplay(userId)

                val recentlyAddedCourses = allCourses.filter { it.tags.contains("recent") }
                val constableCourses = allCourses.filter { it.tags.contains("constable") }
                val packagesCourses = allCourses.filter { it.tags.contains("packages") }

                withContext(Dispatchers.Main) {
                    setRecentlyAddedRv(recentlyAddedCourses)
                    setConstableRv(constableCourses)
                    setPackagesRv(packagesCourses)
                }
            } catch (e: AppwriteException) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@HomeActivity, "Failed to load courses: ${e.message}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // do nth.
            }
        }
    }

    private suspend fun fetchAllCoursesForDisplay(userId: String?): List<CourseModel> {
        val queries = mutableListOf<String>()

        // Add a limit for efficiency (e.g., fetch up to 100 courses)
        queries.add(Query.limit(100))

        // Optionally, sort by $createdAt to ensure "recent" courses are included first
        queries.add(Query.orderDesc("\$createdAt"))

        val response = databases.listDocuments(
            databaseId = getString(R.string.DATABASE_ID),
            collectionId = getString(R.string.COURSES_COLLECTION_ID),
            queries = queries
        )

        val courses = mutableListOf<CourseModel>()

        response.documents.forEach { document ->
            val data = document.data

            if (userId != null) {
                val purchasedByList = data["purchasedBy"] as? List<String> ?: emptyList()
                if (purchasedByList.contains(userId)) {
                    return@forEach
                }
            }

            val finalPrice = (data["price"] as? Number)?.toInt() ?: 0
            val actualPrice = (data["PrevPrice"] as? Number)?.toInt() ?: 0
            val title = data["title"] as? String ?: "No Title"
            val tagsList = data["tags"] as? List<String> ?: emptyList()
            val desc = data["description"] as? String ?: "No Description"
            val isActive = data["isActive"] as? Boolean ?: false
            val discountPercent = if (actualPrice > 0) ((actualPrice - finalPrice) * 100 / actualPrice) else 0
            val syllabusIds = data["syllabusId"] as? List<String> ?: emptyList()
            val testsIds = data["testsId"] as? List<String> ?: emptyList()
            val plansIds = data["plansId"] as? List<String> ?: emptyList()

            courses.add(
                CourseModel(
                    document.id,
                    title, desc, actualPrice, finalPrice, isActive, discountPercent,
                    imageResId = R.drawable.course4,
                    category = tagsList.firstOrNull()?.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } ?: "General",
                    tagsList, syllabusIds, testsIds, plansIds
                )
            )
        }
        return courses
    }

    private fun setCardButtons() {
        mockTestCard.setOnClickListener { goTo(MockTestListActivity::class.java, "Mock Test") }
        allCoursesCard.setOnClickListener { goTo(AllCoursesListActivity::class.java, "All Courses") }
        subjectCourseCard.setOnClickListener { goTo(AllCoursesListActivity::class.java, "Subject wise Courses") }

        freeMaterial.setOnClickListener { goTo(FreeMaterialTestActivity::class.java, "Free Material + Test") }
        myPerformanceCard.setOnClickListener { goTo(MyPerformanceActivity::class.java, "My Performance") }

    }

    private fun goTo(activityClass: Class<*>, title: String) {
        val intent = Intent(this, activityClass)
        intent.putExtra("pageTitle", title)
        startActivity(intent)
    }

    private fun setRecentlyAddedRv(listOfItems : List<CourseModel>) {
        val recentlyAddedLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recentlyAddedRv.layoutManager = recentlyAddedLayoutManager
        val recentlyAddedAdapter = CourseBuyAdapter(listOfItems, CourseFoldersActivity::class.java, CourseBuyAdapter.ITEM_NAME)
        recentlyAddedRv.adapter = recentlyAddedAdapter
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recentlyAddedRv)
    }

    private fun setConstableRv(listOfItems : List<CourseModel>) {
        val constableLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        constableRv.layoutManager = constableLayoutManager
        val constableAdapter = CourseBuyAdapter(listOfItems, CourseFoldersActivity::class.java, CourseBuyAdapter.ITEM_NAME)
        constableRv.adapter = constableAdapter
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(constableRv)
    }

    private fun setPackagesRv(listOfItems : List<CourseModel>) {
        val constableLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        packagesRv.layoutManager = constableLayoutManager
        val packagesAdapter = CourseBuyAdapter(listOfItems, CourseFoldersActivity::class.java, CourseBuyAdapter.ITEM_NAME)
        packagesRv.adapter = packagesAdapter
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(packagesRv)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(autoScrollRunnable)
    }

    override fun onResume() {
        super.onResume()
        startAutoScrolling()
    }

    private fun setOfferRv() {
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        offerRv.layoutManager = layoutManager
        offerAdapter = SimpleImageAdapter(
            listOf(
                R.drawable.offer1,
                R.drawable.sample_home_offer,
                R.drawable.offer2,
                R.drawable.course4
            )
        )
        offerRv.adapter = offerAdapter

        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(offerRv)

        startAutoScrolling()

        offerRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    currentCardIndex = layoutManager.findFirstVisibleItemPosition()
                    handler.removeCallbacks(autoScrollRunnable)
                    handler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY)
                }
            }
        })
    }

    private fun initialiseVariables() {
        welcomeName = findViewById(R.id.home_activity_welcome_name)
        referralCode = findViewById(R.id.home_activity_refferal_code)
        referCodeCopy = findViewById(R.id.home_activity_copy_refer_code)
        referCodeShare = findViewById(R.id.homt_activity_share_refer_code)

        offerRv = findViewById(R.id.home_activity_offers_rv)
        recentlyAddedRv = findViewById(R.id.home_activity_recentlyadded_rv)
        constableRv = findViewById(R.id.home_activity_constable_rv)
        packagesRv = findViewById(R.id.home_activity_packages_rv)

        allCoursesCard = findViewById(R.id.home_activity_all_courses)
        subjectCourseCard = findViewById(R.id.home_activity_subject_course)
        mockTestCard = findViewById(R.id.home_activity_mock_test)
        freeMaterial = findViewById(R.id.home_activity_free_material)
        niyuktiChatBotCard = findViewById(R.id.home_activity_chat_bot)
        myPerformanceCard = findViewById(R.id.home_activity_my_performance)

        navigationMenuBtn = findViewById(R.id.home_activity_menu_btn)
        setNavigationMenuBtn()
    }

    private fun setNavigationMenuBtn() {
        navigationMenuBtn.setOnClickListener {
            val mainMenuFragment = FragmentMainMenu()
            mainMenuFragment.show(supportFragmentManager, FragmentMainMenu.TAG)
        }
    }

    private fun fetchUserName(onUserFetched: (String?) -> Unit) {
        scope.launch {
            var userId: String? = null
            try {
                val user = account.get()
                userId = user.id
                val userName = if (user.name.isEmpty() || user.name == user.id) {
                    "User"
                } else {
                    user.name
                }

                withContext(Dispatchers.Main) {
                    welcomeName.text = "Hi, $userName"
                }
            } catch (e: AppwriteException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@HomeActivity, "Could not load profile. Please relog.", Toast.LENGTH_LONG).show()
                    welcomeName.text = "Hi, Guest"
                }
            } catch (e: Exception) {
                // do nth.
            } finally {
                withContext(Dispatchers.Main) {
                    onUserFetched(userId)
                }
            }
        }
    }
}