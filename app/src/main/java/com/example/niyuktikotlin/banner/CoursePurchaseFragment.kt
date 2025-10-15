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
import com.razorpay.Checkout
import io.appwrite.Client
import io.appwrite.services.Databases
import io.appwrite.Query
import io.appwrite.exceptions.AppwriteException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject // Import for creating payment options


class CoursePurchaseFragment : DialogFragment() {
    lateinit var proceedBtn : CardView
    lateinit var refCodeApplyBtn : TextView
    lateinit var refCodeInp : EditText
    lateinit var refUserName : TextView
    lateinit var finalPrice : TextView
    lateinit var discountPrice : TextView
    lateinit var applyCoupon : TextView

    private val scope = CoroutineScope(Dispatchers.IO)

    private lateinit var client: Client
    private lateinit var databases: Databases

    private lateinit var checkout: Checkout

    var discountAmt = 0

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(@NonNull inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_course_purchase, container, false)

        Checkout.preload(requireContext().applicationContext)
        checkout = Checkout()

        client = Client(requireContext(), getString(R.string.APPWRITE_ENDPOINT)).setProject(getString(R.string.APPWRITE_PROJECT_ID))
        databases = Databases(client)

        initialise(view)

        val razorpayKey = getString(R.string.RAZ_KEY)
        checkout.setKeyID(razorpayKey)

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

        finalPrice = v.findViewById(R.id.fragment_cp_final_amt_tv)
        applyCoupon = v.findViewById(R.id.fragment_cp_coupon_apply_btn)
        discountPrice = v.findViewById(R.id.fragment_cp_discount_tv)

        refUserName.visibility = View.GONE
        updatePrices(0)
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

        proceedBtn.setOnClickListener {
            startRazorpayPayment()
        }
    }

    private fun startRazorpayPayment() {
        try {
            val options = JSONObject()
            // Payment information
            // Amount is in currency subunits (e.g., paise for INR). 50000 paise = ₹500
            options.put("amount", "50000")
            options.put("currency", "INR")
            options.put("name", "Niyukti Course")
            options.put("description", "Course Purchase Test")
            options.put("image", "https://s3.ap-south-1.amazonaws.com/razorpay-sample/assets/rzp-logo.png")

            // Prefill customer details for better conversion
            val prefill = JSONObject()
            prefill.put("email", "customer@example.com")
            prefill.put("contact", "9876543210") // Format: +{country code}{phone number}
            options.put("prefill", prefill)

            // Open the Razorpay checkout form. Pass the activity and the options.
            // Note: Since this is a DialogFragment, we pass the hosting Activity or requireActivity().
            checkout.open(requireActivity(), options)

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Payment failed to start: ${e.message}", Toast.LENGTH_LONG).show()
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
            updatePrices(200)
        } else {
            refUserName.text = "User not found"

            refUserName.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.pure_red)
            )

            updatePrices(0)
        }
    }

    private fun updatePrices(i: Int) {
        discountPrice.text = "-₹$i"
        val amt = 2000 - i
        finalPrice.text = "₹$amt"
    }
}