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
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import com.example.niyuktikotlin.R
import com.example.niyuktikotlin.all_courses.AllCoursesListActivity
import com.example.niyuktikotlin.free_material_tests.FreeMaterialTestActivity
import com.example.niyuktikotlin.home_page.HomeActivity
import com.example.niyuktikotlin.how_to_use.HowToUseActivity
import com.example.niyuktikotlin.menu_pages.AboutUsActivity
import com.example.niyuktikotlin.menu_pages.SettingsActivity
import com.example.niyuktikotlin.old_papers.OldPapersActivity
import com.example.niyuktikotlin.pdf_revision.PdfRevisionActivity
import com.example.niyuktikotlin.referals.ReferRewardsActivity
import com.example.niyuktikotlin.wallet.WalletActivity
import io.appwrite.Client
import io.appwrite.exceptions.AppwriteException
import io.appwrite.services.Account
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentMainMenu : DialogFragment() {
    var profileImg: CardView? = null
    var settings: LinearLayout? = null
    var aboutUs: LinearLayout? = null
    lateinit var pdfRevision: LinearLayout
    lateinit var freeMaterialTest: LinearLayout
    lateinit var oldPapers: LinearLayout
    lateinit var explore: LinearLayout
    lateinit var myCourses: LinearLayout
    lateinit var referEarn: LinearLayout
    lateinit var myWallet: LinearLayout
    lateinit var usage: LinearLayout

    private lateinit var client: Client
    private lateinit var account: Account
    private val scope = CoroutineScope(Dispatchers.IO)

    lateinit var welcomeName: TextView

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

        client = Client(view.context, "https://fra.cloud.appwrite.io/v1")
            .setProject(getString(R.string.APPWRITE_PROJECT_ID))
        account = Account(client)

        fetchUserName(view)
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

        pdfRevision = view.findViewById(R.id.main_menu_fragment_revision_item)
        pdfRevision.setOnClickListener { v ->
            goTo(PdfRevisionActivity::class.java, v)
        }

        freeMaterialTest = view.findViewById(R.id.main_menu_fragment_material_item)
        freeMaterialTest.setOnClickListener { v ->
            goTo(FreeMaterialTestActivity::class.java, v)
        }

        oldPapers = view.findViewById(R.id.main_menu_fragment_papers_item)
        oldPapers.setOnClickListener { v ->
            goTo(OldPapersActivity::class.java, v)
        }

        explore = view.findViewById(R.id.main_menu_fragment_explore_item)
        explore.setOnClickListener { v ->
            goTo(HomeActivity::class.java, v)
        }

        myCourses = view.findViewById(R.id.main_menu_fragment_course_item)
        myCourses.setOnClickListener { v ->
            goTo(AllCoursesListActivity::class.java, v)
        }

        referEarn = view.findViewById(R.id.main_menu_fragment_refer_item)
        referEarn.setOnClickListener { v ->
            goTo(ReferRewardsActivity::class.java, v)
        }

        myWallet = view.findViewById(R.id.main_menu_fragment_wallet_item)
        myWallet.setOnClickListener { v ->
            goTo(WalletActivity::class.java, v)
        }

        usage = view.findViewById(R.id.main_menu_fragment_tutorial_item)
        usage.setOnClickListener { v ->
            goTo(HowToUseActivity::class.java, v)
        }
    }

    private fun goTo(activityClass: Class<*>, view: View) {
        val intent = Intent(view.context, activityClass)
        startActivity(intent)
    }

    companion object {
        const val TAG = "FragmentMainMenu" // A tag for finding the fragment
    }

    private fun fetchUserName(view: View) {
        welcomeName = view.findViewById(R.id.main_menu_fragment_user_name)
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
                    Toast.makeText(view.context, "Could not load profile. Please relog.", Toast.LENGTH_LONG).show()
                    welcomeName.text = "Hi, Guest"
                }
            } catch (e: Exception) {
                // do nth.
            }
        }
    }
}