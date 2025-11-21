package com.group.pocketshelf

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        // Autostarts the AddNewBook, for troubleshooting :)
//        val intent = Intent(this, AddNewBookScreen::class.java)
//        startActivity(intent);

//        // Autostarts the user library, for troubleshooting :)
//        val intent = Intent(this, LibraryShelvesScreen::class.java)
//        startActivity(intent);
//
        // Autostarts the shelf view, for troubleshooting :)
        val intent = Intent(this, BooksScreen::class.java)
        startActivity(intent);
    }
}