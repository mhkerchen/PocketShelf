package com.group.pocketshelf

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Autostart login screen
        auth = FirebaseAuth.getInstance()
        auth.signOut()
        val intent = Intent(this, StartingActivity::class.java)
        startActivity(intent);

        // computer security is my passion
        // XSVhqqutpvPIpScdLr0jkLBzGs42
//        val email = "test@y.com"
//        val password = "123456"

//
//        auth = FirebaseAuth.getInstance()
//        auth.signInWithEmailAndPassword(email, password)
//
//        createNewUser("test1@test1.org", "1234567890")

//        // Autostarts the AddNewBook, for troubleshooting :)
//        val intent = Intent(this, AddNewBookScreen::class.java)
//        startActivity(intent);

//        // Autostarts the user library, for troubleshooting :)
//        val intent = Intent(this, LibraryShelvesScreen::class.java)
//        startActivity(intent);
//
//        // Autostarts the shelf view, for troubleshooting :)
//        val intent = Intent(this, BooksScreen::class.java)
//        startActivity(intent);
    }



}