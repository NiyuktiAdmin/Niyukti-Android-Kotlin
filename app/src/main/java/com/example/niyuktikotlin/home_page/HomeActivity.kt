package com.example.niyuktikotlin.home_page

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.LinearLayout
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
import com.example.niyuktikotlin.menu_fragment.FragmentMainMenu
import com.example.niyuktikotlin.referals.ReferRewardsActivity
import com.example.niyuktikotlin.subject_wise_course.SubjectWiseCourseListActivity
import com.example.niyuktikotlin.util.CourseBuyAdapter
import com.example.niyuktikotlin.models.CourseModel
import com.example.niyuktikotlin.wallet.WalletActivity
import io.appwrite.Client
import io.appwrite.exceptions.AppwriteException
import io.appwrite.services.Account
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    private lateinit var offerAdapter: HomePageOfferAdapter

    private val myRefCode = "Niyukti1"

    private lateinit var client: Client
    private lateinit var account: Account
    private val scope = CoroutineScope(Dispatchers.IO)

    private val autoScrollRunnable = object : Runnable {
        override fun run() {
            currentCardIndex = (currentCardIndex + 1) % 3
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

        client = Client(this, "https://fra.cloud.appwrite.io/v1")
            .setProject(getString(R.string.APPWRITE_PROJECT_ID))
        account = Account(client)

        initialiseVariables()

        val listOfItems = listOf(
            CourseModel(
                title = "Current Affairs 2025",
                actualPrice = 799,
                discountedPrice = 499,
                discountPercent = 38,
                imageResId = R.drawable.course4,
                category = "General Knowledge"
            ),
            CourseModel(
                title = "Constable Foundation Course",
                actualPrice = 1499,
                discountedPrice = 999,
                discountPercent = 33,
                imageResId = R.drawable.offer1,
                category = "Police Exam"
            ),
            CourseModel(
                title = "Reasoning Mastery",
                actualPrice = 999,
                discountedPrice = 699,
                discountPercent = 30,
                imageResId = R.drawable.offer3,
                category = "Aptitude"
            ),
            CourseModel(
                title = "Maths Booster",
                actualPrice = 1099,
                discountedPrice = 799,
                discountPercent = 27,
                imageResId = R.drawable.sample_home_offer,
                category = "Quantitative Aptitude"
            )
        )

        setOfferRv()
        setConstableRv(listOfItems)
        setRecentlyAddedRv(listOfItems)
        setPackagesRv(listOfItems)


        fetchUserName()
        referralCode.text = myRefCode

        setCardButtons()
        setNavbarButtons()

        referCodeCopy.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Referral Code", referralCode.text.toString())
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setCardButtons() {
        mockTestCard.setOnClickListener { goTo(MockTestListActivity::class.java) }
        allCoursesCard.setOnClickListener { goTo(AllCoursesListActivity::class.java) }
        subjectCourseCard.setOnClickListener { goTo(SubjectWiseCourseListActivity::class.java) }
    }

    private fun goTo(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }

    private fun setRecentlyAddedRv(listOfItems : List<CourseModel>) {
        val recentlyAddedLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recentlyAddedRv.layoutManager = recentlyAddedLayoutManager
        val recentlyAddedAdapter = CourseBuyAdapter(listOfItems)
        recentlyAddedRv.adapter = recentlyAddedAdapter
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recentlyAddedRv)
    }

    private fun setConstableRv(listOfItems : List<CourseModel>) {
        val constableLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        constableRv.layoutManager = constableLayoutManager
        val constableAdapter = CourseBuyAdapter(listOfItems)
        constableRv.adapter = constableAdapter
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(constableRv)
    }

    private fun setPackagesRv(listOfItems : List<CourseModel>) {
        val constableLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        packagesRv.layoutManager = constableLayoutManager
        val packagesAdapter = CourseBuyAdapter(listOfItems)
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
        offerAdapter = HomePageOfferAdapter(
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

    private fun setNavbarButtons() {

    }

    private fun setNavigationMenuBtn() {
        navigationMenuBtn.setOnClickListener {
            val mainMenuFragment = FragmentMainMenu()
            mainMenuFragment.show(supportFragmentManager, FragmentMainMenu.TAG)
        }
    }

    private fun fetchUserName() {
        scope.launch {
            try {
                val user = account.get()
                val userName = if (user.name.isNullOrEmpty() || user.name == user.id) {
                    // Todo : Fallback if the name is not set (e.g., if only phone was used)
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
            }
        }
    }
}