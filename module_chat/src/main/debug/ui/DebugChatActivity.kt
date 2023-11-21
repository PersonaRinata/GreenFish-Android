package ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.handsome.module.chat.R

class DebugChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug_chat)
    }
}