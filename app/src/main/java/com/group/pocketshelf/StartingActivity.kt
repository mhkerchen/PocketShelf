package com.group.pocketshelf

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class StartingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starting)
        // This connects to res/layout/activity_starting.xml

        // Finds the "Get Started" button
        val getStartedBtn = findViewById<Button>(R.id.btnGetStarted)

        // When the button is clicked, move to the login/signup screen
        getStartedBtn.setOnClickListener {
           // val intent = Intent(this, AuthActivity::class.java)
           // startActivity(intent)
        }
    }
}
