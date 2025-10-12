import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.niyuktikotlin.R

abstract class BaseActivity : AppCompatActivity() {
    var isTablet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val orientationString = resources.getString(R.string.screen_orientation)

        if (orientationString == "unspecified") {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            isTablet = true
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            isTablet = false
        }
    }
}