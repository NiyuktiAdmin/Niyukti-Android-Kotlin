package com.example.niyuktikotlin.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.niyuktikotlin.R
import com.example.niyuktikotlin.home_page.HomeActivity
import com.example.niyuktikotlin.util.FunctionIds
import io.appwrite.Client
import io.appwrite.exceptions.AppwriteException
import io.appwrite.models.User
import io.appwrite.services.Account
import io.appwrite.services.Functions
import io.appwrite.enums.OAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var googleBtn: CardView
    private lateinit var mobileBtn: CardView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: LoginCardAdapter

    private val handler = Handler(Looper.getMainLooper())
    private var currentCardIndex = 0
    private val AUTO_SCROLL_DELAY = 3000L

    // Appwrite services initialization
    private val client by lazy {
        Client(this)
            .setEndpoint("https://fra.cloud.appwrite.io/v1")
            .setProject(getString(R.string.APPWRITE_PROJECT_ID))
        // .setSelfSigned(true) // Remove this if not needed for self-signed certificates
    }

    private val account by lazy { Account(client) }
    private val functions by lazy { Functions(client) }

    private val autoScrollRunnable = object : Runnable {
        override fun run() {
            currentCardIndex = (currentCardIndex + 1) % 3 // Assuming 3 cards
            recyclerView.smoothScrollToPosition(currentCardIndex)
            handler.postDelayed(this, AUTO_SCROLL_DELAY)
        }
    }

    private fun startAutoScrolling() {
        handler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initializing views
        recyclerView = findViewById(R.id.login_activity_recycler_view)
        googleBtn = findViewById(R.id.login_activity_google)
        mobileBtn = findViewById(R.id.login_activity_mobile)

        // RecyclerView Setup
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        adapter = LoginCardAdapter()
        recyclerView.adapter = adapter

        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        checkSession()
        startAutoScrolling()

        setupListeners()
        setupScrollListener()
    }

    private fun setupListeners() {
        mobileBtn.setOnClickListener {
            val intent = Intent(this, LoginOtpActivity::class.java)
            startActivity(intent)
        }

        googleBtn.setOnClickListener {
            loginWithGoogle()
        }
    }

    private fun setupScrollListener() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    currentCardIndex = firstVisibleItemPosition

                    // Reset auto-scroll timer after manual interaction
                    handler.removeCallbacks(autoScrollRunnable)
                    handler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY)
                }
            }
        })
    }

    private fun loginWithGoogle() {
        // Use a coroutine to call the suspend function from Appwrite
        CoroutineScope(Dispatchers.Main).launch {
            try {
                account.createOAuth2Session(
                    activity = this@LoginActivity,
                    provider = OAuthProvider.GOOGLE
                )

                // If the code reaches here, the login was successful.
                // Appwrite has created a session.
                Toast.makeText(this@LoginActivity, "Login Successful!", Toast.LENGTH_SHORT).show()
                goTo(HomeActivity::class.java) // Navigate to your home screen

            } catch (e: AppwriteException) {
                Log.e("LoginActivity", "Google login failed: ${e.message}")
                Toast.makeText(this@LoginActivity, "Login failed. Please try again.", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("LoginActivity", "Unexpected error in Google login: ${e.message}")
                Toast.makeText(this@LoginActivity, "An unexpected error occurred.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkSession() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                account.get()

                goTo(HomeActivity::class.java)
            } catch (e: AppwriteException) {
                Log.w("LoginActivity", "No active session found or: ${e.message}")
            } catch (e: Exception) {
                Log.e("LoginActivity", "Unexpected Error during session check: ${e.message}")
                Toast.makeText(this@LoginActivity, "Unexpected Error Occurred", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goTo(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(autoScrollRunnable)
    }

    override fun onResume() {
        super.onResume()
        startAutoScrolling()
    }
}