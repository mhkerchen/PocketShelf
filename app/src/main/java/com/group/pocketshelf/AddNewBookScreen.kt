package com.group.pocketshelf

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class AddNewBookScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_new_book)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Config action bar (top bar)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);

        testPopulateDB()
    }


    // Sets up the back/up button
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() == android.R.id.home) {
            finish()  // pops this activity off the back stack
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    fun testPopulateDB() {
//        var book1 = BookData(
//            title = "Alice", cover_is_url = true, img_url = "https://ia.media-imdb.com/images/M/MV5BMTMwNjAxMTc0Nl5BMl5BanBnXkFtZTcwODc3ODk5Mg@@._V1_SY317_CR0,0,214,317_AL_.jpg"
//        )
//        var book2 = BookData(
//            title = "Avatar", cover_is_url = true, img_url = "https://ia.media-imdb.com/images/M/MV5BMTYwOTEwNjAzMl5BMl5BanBnXkFtZTcwODc5MTUwMw@@._V1_SY317_CR0,0,214,317_AL_.jpg"
//        )
//        var book3 = BookData(
//            title = "Frozen", cover_is_url = true, img_url = "https://ia.media-imdb.com/images/M/MV5BMTQ1MjQwMTE5OF5BMl5BanBnXkFtZTgwNjk3MTcyMDE@._V1_SX214_AL_.jpg"
//        )
//        var book4 = BookData(
//            title = "Titanic", cover_is_url = true, img_url = "https://ia.media-imdb.com/images/M/MV5BMjExNzM0NDM0N15BMl5BanBnXkFtZTcwMzkxOTUwNw@@._V1_SY317_CR0,0,214,317_AL_.jpg"
//        )
//
//        var auth = FirebaseAuth.getInstance()
//        val users = FirebaseDatabase.getInstance().reference.child("users").child(auth.uid!!).child("books")
//        var new: DatabaseReference = users.push()
//        new.setValue(book1)
//        new = users.push()
//        new.setValue(book2)
//        new = users.push()
//        new.setValue(book3)
//        new = users.push()
//        new.setValue(book4)

    }
}