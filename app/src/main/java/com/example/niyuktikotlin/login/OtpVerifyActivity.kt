package com.example.niyuktikotlin.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.niyuktikotlin.R
import com.example.niyuktikotlin.home_page.HomeActivity // Assuming this is your next activity
import com.example.niyuktikotlin.login.LoginOtpActivity.Companion.EXTRA_PHONE_NUMBER
import com.example.niyuktikotlin.login.LoginOtpActivity.Companion.EXTRA_TOKEN_ID
import io.appwrite.Client
import io.appwrite.exceptions.AppwriteException
import io.appwrite.services.Account
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OtpVerifyActivity : AppCompatActivity() {
    private lateinit var verifyButton: CardView
    private lateinit var contactTextView: TextView
    private lateinit var otpEditText: EditText
    private lateinit var account: Account
    private var phone: String? = null
    private var tokenId: String? = null
    private lateinit var backBtn: TextView

    private val scope = CoroutineScope(Dispatchers.IO)

    companion object {
        private const val TAG = "OtpVerifyActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verify)

        // Initialize Appwrite Client
        // Note: The project ID is assumed to be in res/values/strings.xml
        val client = Client(this, "https://fra.cloud.appwrite.io/v1")
            .setProject(getString(R.string.APPWRITE_PROJECT_ID))
        account = Account(client)

        // Retrieve data passed from LoginOtpActivity
        phone = intent.getStringExtra(EXTRA_PHONE_NUMBER)
        tokenId = intent.getStringExtra(EXTRA_TOKEN_ID)

        // Find UI elements (Ensure these IDs match your activity_otp_verify.xml)
        verifyButton = findViewById(R.id.verify_otp_btn)
        contactTextView = findViewById(R.id.contact_text_view)
        otpEditText = findViewById(R.id.otp_verify_edit_text)
        backBtn = findViewById(R.id.tvReEnterPhone);

        // Display the contact number
        if (!phone.isNullOrEmpty()) {
            contactTextView.text = "Code sent to: $phone"
        } else {
            contactTextView.text = "Error: Phone number not received."
            Toast.makeText(this, "Error: Missing phone number.", Toast.LENGTH_LONG).show()
        }

        Log.d(TAG, "Token ID received: $tokenId")

        verifyButton.setOnClickListener {
            verifyOtpAndLogin()
        }
        backBtn.setOnClickListener {
            finish()
        }
    }

    private fun verifyOtpAndLogin() {
        val otp = otpEditText.text.toString().trim()

        if (TextUtils.isEmpty(otp) || otp.length < 4) { // Basic OTP length check
            Toast.makeText(this, "Please enter the OTP.", Toast.LENGTH_SHORT).show()
            return
        }

        val currentTokenId = tokenId

        if (currentTokenId.isNullOrEmpty()) {
            Toast.makeText(this, "Verification token missing. Please restart login.", Toast.LENGTH_LONG).show()
            return
        }

        scope.launch {
            try {
                val session = account.createSession(
                    userId = currentTokenId,
                    secret = otp
                )

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@OtpVerifyActivity, "Login successful! Welcome.", Toast.LENGTH_LONG).show()
                    Log.d(TAG, "Session created: ${session.id}")

                    // Navigate to the home screen and clear the back stack
                    val intent = Intent(this@OtpVerifyActivity, HomeActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                }

            } catch (e: AppwriteException) {
                Log.e(TAG, "Appwrite Exception during OTP verification: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    // This catches errors like "Invalid token" (wrong OTP, expired token)
                    Toast.makeText(this@OtpVerifyActivity, "Verification failed: Invalid token or OTP.", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected error during OTP verification: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@OtpVerifyActivity, "An unexpected error occurred.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
