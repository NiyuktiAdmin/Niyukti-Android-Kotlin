package com.example.niyuktikotlin.menu_fragment

import com.example.niyuktikotlin.menu_pages.ProfileActivity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import com.example.niyuktikotlin.R
import com.example.niyuktikotlin.menu_pages.AboutUsActivity
import com.example.niyuktikotlin.menu_pages.SettingsActivity

class FragmentMainMenu : DialogFragment() {
    var profileImg: CardView? = null
    var settings: LinearLayout? = null
    var aboutUs: LinearLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.MainMenuDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initiateValues(view)
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null && dialog!!.window != null) {
            val window = dialog!!.window
            val wlp = window!!.attributes
            wlp.width = WindowManager.LayoutParams.WRAP_CONTENT
            wlp.height = WindowManager.LayoutParams.MATCH_PARENT
            wlp.gravity = Gravity.START
            window.attributes = wlp
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    private fun initiateValues(view: View) {
        profileImg = view.findViewById(R.id.main_menu_fragment_profile_card)
        profileImg?.setOnClickListener { v ->
            goTo(ProfileActivity::class.java, v)
        }

        settings = view.findViewById(R.id.main_menu_fragment_setting_item)
        settings?.setOnClickListener { v ->
            goTo(SettingsActivity::class.java, v)
        }

        aboutUs = view.findViewById(R.id.main_menu_fragment_about_item)
        aboutUs?.setOnClickListener { v ->
            goTo(AboutUsActivity::class.java, v)
        }
    }

    private fun goTo(activityClass: Class<*>, view: View) {
        val intent = Intent(view.context, activityClass)
        startActivity(intent)
    }

    companion object {
        const val TAG = "FragmentMainMenu" // A tag for finding the fragment
    }
}