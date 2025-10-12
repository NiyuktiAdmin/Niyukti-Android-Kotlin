package com.example.niyuktikotlin.banner

import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.niyuktikotlin.R
import com.example.niyuktikotlin.test_conducting.TestWarningDialogFragment

class BuyBannerFragment : Fragment() {
    lateinit var buyBtn: CardView
    lateinit var cardNoAmt: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_buy_banner, container, false)
        setupView(view)
        return view
    }

    private fun setupView(view: View) {
        buyBtn = view.findViewById(R.id.fragment_bb_buy_btn)
        cardNoAmt = view.findViewById(R.id.fragment_bb_no_cost)

        buyBtn.setOnClickListener {
            val buyDialog = CoursePurchaseFragment()
            buyDialog.show(parentFragmentManager, "CoursePurchaseDialog")
        }

        cardNoAmt.paintFlags = cardNoAmt.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }
}