package com.example.niyuktikotlin.banner

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import com.example.niyuktikotlin.R
import androidx.core.content.ContextCompat
import android.widget.Toast
import io.appwrite.Client
import io.appwrite.services.Databases
import io.appwrite.Query
import io.appwrite.exceptions.AppwriteException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CoursePurchaseFragment : DialogFragment() {
    lateinit var proceedBtn : CardView
    lateinit var refCodeApplyBtn : TextView
    lateinit var refCodeInp : EditText
    lateinit var refUserName : TextView

    private val scope = CoroutineScope(Dispatchers.IO)

    private lateinit var client: Client
    private lateinit var databases: Databases

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(@NonNull inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_course_purchase, container, false)


        client = Client(requireContext(), getString(R.string.APPWRITE_ENDPOINT)).setProject(getString(R.string.APPWRITE_PROJECT_ID))

        databases = Databases(client)

        initialise(view)

        dialog?.window?.let { window ->
            window.requestFeature(Window.FEATURE_NO_TITLE)
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val wlp: WindowManager.LayoutParams = window.attributes.apply {
                gravity = Gravity.BOTTOM
                width = WindowManager.LayoutParams.MATCH_PARENT
                height = WindowManager.LayoutParams.WRAP_CONTENT
            }
            window.attributes = wlp
        }
        return view
    }

    private fun initialise(v: View) {
        proceedBtn = v.findViewById(R.id.fragment_cp_proceed_btn)
        refCodeApplyBtn = v.findViewById(R.id.fragment_cp_refer_code_btn)
        refCodeInp = v.findViewById(R.id.fragment_cp_refer_code_tv)
        refUserName = v.findViewById(R.id.fragment_cp_ref_username)

        refUserName.visibility = View.GONE

        setListeners()
    }

    private fun setListeners() {
        refCodeApplyBtn.setOnClickListener {
            val referralCode = refCodeInp.text.toString().trim()
            if (referralCode.isNotEmpty()) {
                checkReferralCode(referralCode)
            } else {
                refUserName.visibility = View.GONE
            }
        }
    }

    private fun checkReferralCode(code: String) {
        scope.launch {
            var userName: String? = null
            try {
                val response = databases.listDocuments(
                    databaseId = getString(R.string.DATABASE_ID),
                    collectionId = getString(R.string.USER_COLLECTION_ID),
                    queries = listOf(
                        Query.equal("referralCode", code),
                        Query.limit(1)
                    )
                )

                if (response.total > 0) {
                    val userDocument = response.documents.first()
                    userName = userDocument.data["name"] as? String
                }
            } catch (e: AppwriteException) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error applying code: ${e.message}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                withContext(Dispatchers.Main) {
                    updateReferralUserName(userName)
                }
            }
        }
    }

    private fun updateReferralUserName(userName: String?) {
        refUserName.visibility = View.VISIBLE

        if (userName != null && userName.isNotEmpty()) {
            refUserName.text = "name: $userName"

            refUserName.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.text_dim)
            )
        } else {
            refUserName.text = "User not found"

            refUserName.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.pure_red)
            )
        }
    }
}