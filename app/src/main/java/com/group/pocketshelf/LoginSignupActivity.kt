package com.group.pocketshelf

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.group.pocketshelf.databinding.ActivityLoginSignupBinding

class LoginSignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginSignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginSignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Navigate to Starting Page
        binding.buttonContinue.setOnClickListener {
            val intent = Intent(this, StartingActivity::class.java)
            startActivity(intent)
        }
    }
}
