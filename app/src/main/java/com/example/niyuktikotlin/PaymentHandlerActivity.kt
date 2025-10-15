package com.example.niyuktikotlin

import BaseActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.niyuktikotlin.home_page.HomeActivity
import com.razorpay.PaymentResultListener

abstract class PaymentHandlerActivity : BaseActivity(), PaymentResultListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    abstract fun handlePaymentSuccess(razorpayPaymentID: String?)

    abstract fun handlePaymentError(code: Int, response: String?)


    override fun onPaymentSuccess(razorpayPaymentID: String?) {
        try {
            Toast.makeText(this, "Payment Successful", Toast.LENGTH_LONG).show()

            handlePaymentSuccess(razorpayPaymentID)

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPaymentError(code: Int, response: String?) {
        try {
            Toast.makeText(this, "Payment Failed", Toast.LENGTH_LONG).show()
            handlePaymentError(code, response)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}