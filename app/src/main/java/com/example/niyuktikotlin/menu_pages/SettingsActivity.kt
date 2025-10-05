package com.example.niyuktikotlin.menu_pages

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.niyuktikotlin.R
import com.example.niyuktikotlin.login.LoginActivity
import io.appwrite.Client
import io.appwrite.exceptions.AppwriteException
import io.appwrite.services.Account
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsActivity : AppCompatActivity() {

    private lateinit var client: Client
    private lateinit var account: Account
    private lateinit var logoutCard: CardView
    private lateinit var backBtn: ImageView

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        logoutCard = findViewById(R.id.logout_card)
        backBtn = findViewById(R.id.settings_back_btn)

        client = Client(this, "https://fra.cloud.appwrite.io/v1")
            .setProject(getString(R.string.APPWRITE_PROJECT_ID))

        account = Account(client)

        logoutCard.setOnClickListener {
            logout()
        }

        backBtn.setOnClickListener {
            finish()
        }
    }

    private fun logout() {
        scope.launch {
            try {
                account.deleteSession(sessionId = "current")

                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@SettingsActivity,
                        "Successfully logged out!",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(this@SettingsActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            } catch (e: AppwriteException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@SettingsActivity,
                        "Logout failed: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    e.printStackTrace()
                }
            }
        }
    }
}
