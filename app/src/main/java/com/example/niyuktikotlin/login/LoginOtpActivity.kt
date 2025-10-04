package com.example.niyuktikotlin.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.niyuktikotlin.R
import io.appwrite.Client
import io.appwrite.extensions.tryJsonCast
import io.appwrite.exceptions.AppwriteException
import io.appwrite.models.Token
import io.appwrite.services.Account
import io.appwrite.ID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginOtpActivity : AppCompatActivity() {

    private lateinit var loginBtn: CardView
    private lateinit var phoneEditText: EditText
    private lateinit var account: Account

    private val scope = CoroutineScope(Dispatchers.IO)

    companion object {
        private const val TAG = "LoginOtpActivity"
        // Constants for Intent extras to pass data safely
        const val EXTRA_PHONE_NUMBER = "com.example.niyuktikotlin.login.PHONE_NUMBER"
        const val EXTRA_TOKEN_ID = "com.example.niyuktikotlin.login.TOKEN_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_otp)

        // Initialize Appwrite Client
        // Ensure that APPWRITE_PROJECT_ID is correctly defined in res/values/strings.xml
        val client = Client(this, "https://fra.cloud.appwrite.io/v1")
            .setProject(getString(R.string.APPWRITE_PROJECT_ID))
        account = Account(client)

        loginBtn = findViewById(R.id.login_otp_btn)
        phoneEditText = findViewById(R.id.login_otp_contact_field)

        loginBtn.setOnClickListener {
            sendOtp()
        }
    }

    private fun sendOtp() {
        // Appwrite requires the phone number to start with '+' and include the country code (E.164 format)
        var phone = phoneEditText.text.toString().trim()

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please enter your mobile number.", Toast.LENGTH_LONG).show()
            return
        }

        // Auto-prepend "+91" if the user hasn't included the country code already.
        // You might want to use a country code picker in a production app.
        if (!phone.startsWith("+")) {
            phone = "+91" + phone
        }

        // Simple check for minimum length after formatting
        if (phone.length < 10) {
            Toast.makeText(this, "Please enter a valid phone number including country code.", Toast.LENGTH_LONG).show()
            return
        }

        scope.launch {
            try {
                val token = account.createPhoneToken(
                    userId = ID.unique(),
                    phone = phone
                )

                val tokenId = token.id

                withContext(Dispatchers.Main) {
                    if (tokenId != null) {
                        Toast.makeText(this@LoginOtpActivity, "OTP sent successfully to $phone", Toast.LENGTH_LONG).show()

                        val intent = Intent(this@LoginOtpActivity, OtpVerifyActivity::class.java).apply {
                            putExtra(EXTRA_PHONE_NUMBER, phone)
                            putExtra(EXTRA_TOKEN_ID, tokenId)
                        }
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@LoginOtpActivity, "Failed to get token ID. Check Appwrite logs.", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: AppwriteException) {
                Log.e(TAG, "Appwrite Exception during OTP send: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginOtpActivity, "Failed to send OTP: ${e.message}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected error during OTP send: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginOtpActivity, "An unexpected error occurred.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
