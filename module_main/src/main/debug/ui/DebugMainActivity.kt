package ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.handsome.module.main.ui.activity.MainActivity

class DebugMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivity.startAction(this)
        finish()
    }
}