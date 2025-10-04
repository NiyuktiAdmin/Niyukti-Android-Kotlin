package com.example.niyuktikotlin.test_conducting
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import com.example.niyuktikotlin.R

class TestWarningDialogFragment : DialogFragment() {

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Apply the custom dialog style here
        setStyle(STYLE_NO_FRAME, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(@NonNull inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.dialogue_test_warning, container, false)

        val backToTestButton: TextView? = view.findViewById(R.id.dialog_warning_back_btn)
        backToTestButton?.setOnClickListener { dismiss() }

        val closeIcon: ImageView? = view.findViewById(R.id.dialog_warning_close_icon)
        closeIcon?.setOnClickListener { dismiss() }

        dialog?.window?.apply {
            // These properties are largely handled by the style now, but ensuring them doesn't hurt.
            requestFeature(Window.FEATURE_NO_TITLE)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // Ensure transparent background
            attributes = attributes.apply {
                gravity = Gravity.BOTTOM
                width = WindowManager.LayoutParams.MATCH_PARENT
                height = WindowManager.LayoutParams.WRAP_CONTENT
            }
        }

        return view
    }
}