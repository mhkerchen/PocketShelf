package com.group.pocketshelf

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.UUID

class AddNewShelfScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_new_shelf_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Config action bar (top bar)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.title = "Add New Shelf to Library"

        // Config menu bar (bottom bar)
        var fabButton = findViewById<FloatingActionButton>(R.id.fab)
        fabButton.setOnClickListener {
            finish()
        }



        val submitButton = findViewById<Button>(R.id.submit_button)
        submitButton.setOnClickListener {
            addShelf() // also finishes activity BTW
        }



    }
    // Sets up the back/up button
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() == android.R.id.home) {
            finish()  // pops this activity off the back stack
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    fun addShelf() {
        val shelfTitle = findViewById<TextInputEditText>(R.id.enter_shelf_title)

        var shelf = ShelfData(
            name = shelfTitle.text.toString(),
            books = ArrayList<String>()
        )

        var shelfname = shelf.name!!

        var auth = FirebaseAuth.getInstance()
        val shelves = FirebaseDatabase.getInstance().reference.child("users").child(auth.uid!!).child("shelves")
        var exists = false
        shelves.get().addOnSuccessListener { snapshot ->
            for (item in snapshot.children) {
                val shelfentry = item.getValue(ShelfData::class.java)
                if (shelfentry != null) {
                    if (shelfentry.name == shelfname) {
                        exists = true
                    }
                }
            }

            if (exists) {
                Toast.makeText(this, "That shelf already exists. Please pick a new name", Toast.LENGTH_SHORT).show()
            } else {
                shelves.child(shelfname ).setValue(shelf)
                finish()
            }
        }
    }
}