package com.group.pocketshelf

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.group.pocketshelf.databinding.ActivityLoginSignupBinding

class StartingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starting)
        // This connects to res/layout/activity_starting.xml

        // Finds the "Get Started" button
        val getStartedBtn = findViewById<Button>(R.id.btnGetStarted)

        var auth = FirebaseAuth.getInstance()

        // When the button is clicked, move to the login/signup screen
        getStartedBtn.setOnClickListener {
            if (auth.uid == null) {
                val intent = Intent(this, LoginSignupActivity::class.java)
            } else {
                val intent = Intent(this, LibraryShelvesScreen::class.java)
            }

            startActivity(intent)
        }
    }
}
