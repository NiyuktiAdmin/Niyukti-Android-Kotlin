package com.example.niyuktikotlin.navbar_fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.niyuktikotlin.MainActivity
import com.example.niyuktikotlin.R
import com.example.niyuktikotlin.all_courses.AllCoursesListActivity
import com.example.niyuktikotlin.home_page.HomeActivity
import com.example.niyuktikotlin.referals.ReferRewardsActivity
import com.example.niyuktikotlin.wallet.WalletActivity

class NavbarFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_navbar, container, false)
        setupNavigation(view)
        return view
    }

    private fun setupNavigation(view: View) {
        val homeBtn: LinearLayout = view.findViewById(R.id.home_navbar_home_btn)
        val myCoursesBtn: LinearLayout = view.findViewById(R.id.home_navbar_mycourses_btn)
        val referEarnBtn: LinearLayout = view.findViewById(R.id.home_navbar_refer_earn_btn)
        val walletBtn: LinearLayout = view.findViewById(R.id.home_navbar_wallet_btn)
        val exploreBtn: LinearLayout = view.findViewById(R.id.home_navbar_explore_btn)

        homeBtn.setOnClickListener {v ->
            goTo(MainActivity::class.java, v)
        }

        myCoursesBtn.setOnClickListener {v ->
            goTo(AllCoursesListActivity::class.java, v)
        }

        referEarnBtn.setOnClickListener {v ->
            goTo(ReferRewardsActivity::class.java, v)
        }

        walletBtn.setOnClickListener {v ->
            goTo(WalletActivity::class.java, v)
        }

        exploreBtn.setOnClickListener {v ->
            goTo(HomeActivity::class.java, v)
        }
    }

    private fun goTo(activityClass: Class<*>, view: View) {
        val intent = Intent(view.context, activityClass)
        startActivity(intent)
    }

    companion object {
        @JvmStatic
        fun newInstance() = NavbarFragment()
    }
}
