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
import com.example.niyuktikotlin.menu_fragment.FragmentMainMenu
import com.example.niyuktikotlin.referals.ReferRewardsActivity
import com.example.niyuktikotlin.wallet.WalletActivity

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

    private lateinit var navbarHome: LinearLayout
    private lateinit var navbarReferEarn: LinearLayout
    private lateinit var navbarWallet: LinearLayout

    private lateinit var layoutManager: LinearLayoutManager
    private val handler = Handler()
    private var currentCardIndex = 0
    private val AUTO_SCROLL_DELAY: Long = 3000
    private lateinit var offerAdapter: HomePageOfferAdapter

    private val myRefCode = "QWERTY"

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

        initialiseVariables()

        // --------- RECYCLER VIEWS SETUP ------------
        setOfferRv()
        setConstableRv()
        setRecentlyAddedRv()
        setPackagesRv()

        welcomeName.text = "Hi, Tarush Gupta"
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
        allCoursesCard.setOnClickListener { goTo(MockTestListActivity::class.java) }
        subjectCourseCard.setOnClickListener { goTo(MockTestListActivity::class.java) }
    }

    private fun goTo(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }

    private fun setRecentlyAddedRv() {
        val recentlyAddedLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recentlyAddedRv.layoutManager = recentlyAddedLayoutManager
        val recentlyAddedAdapter = HomePageConstableAdapter()
        recentlyAddedRv.adapter = recentlyAddedAdapter
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recentlyAddedRv)
    }

    private fun setConstableRv() {
        val constableLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        constableRv.layoutManager = constableLayoutManager
        val constableAdapter = HomePageConstableAdapter()
        constableRv.adapter = constableAdapter
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(constableRv)
    }

    private fun setPackagesRv() {
        val constableLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        packagesRv.layoutManager = constableLayoutManager
        val packagesAdapter = HomePageConstableAdapter()
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
        offerAdapter = HomePageOfferAdapter()
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

        navbarReferEarn = findViewById(R.id.home_navbar_refer_earn_btn)
        navbarWallet = findViewById(R.id.home_navbar_wallet_btn)

        navigationMenuBtn = findViewById(R.id.home_activity_menu_btn)
    }

    private fun setNavbarButtons() {
        setNavigationMenuBtn()
        navbarWallet.setOnClickListener { goTo(WalletActivity::class.java) }
        navbarReferEarn.setOnClickListener { goTo(ReferRewardsActivity::class.java) }
    }

    private fun setNavigationMenuBtn() {
        navigationMenuBtn.setOnClickListener {
            val mainMenuFragment = FragmentMainMenu()
            mainMenuFragment.show(supportFragmentManager, FragmentMainMenu.TAG)
        }
    }
}