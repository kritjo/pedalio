package in2000.pedalio

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

/**
 * Shown on first launch of the app.
 */
class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_welcome)
        findViewById<Button>(R.id.buttonDone).setOnClickListener {
            setContentView(R.layout.activity_welcome_2)
            findViewById<Button>(R.id.buttonDone2).setOnClickListener {
                finish()
            }

        }
    }
}