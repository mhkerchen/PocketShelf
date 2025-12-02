package com.group.pocketshelf

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator

class BooksScreen : AppCompatActivity(), BooksAdapter.MyItemClickListener {

    lateinit var myAdapter: BooksAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_books)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Config action bar (top bar)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);



        val NUMBER_COLUMNS=  4
        val layoutManager = GridLayoutManager(this, NUMBER_COLUMNS)
        val rv = findViewById<RecyclerView>(R.id.books)
        rv.hasFixedSize()
        rv.layoutManager = layoutManager


        var auth = FirebaseAuth.getInstance()

        val shelfname : String = intent.getStringExtra("SHELF_NAME") ?: "shelf1"
        var shelfname_widget = findViewById<TextView>(R.id.shelf_name)
        shelfname_widget.text = shelfname
        // Get the list of book ids that go in this shelf
        var bookIDs = ArrayList<String>()
        var books = ArrayList<BookData>()
        val query = FirebaseDatabase.getInstance().reference.child("users").child(auth.uid!!).child("shelves")
        query.get().addOnSuccessListener { snapshot ->
            // find the shelf that's named the same as the given shelf
            val typeIndicator = object : GenericTypeIndicator<ArrayList<String>>() {}
            for (child in snapshot.children) {
                if (child.key == shelfname) {
                    val bookidlist = child.child("books").getValue(typeIndicator) ?: ArrayList<String?>()

                    for (i in bookidlist.indices) {
                        val bookID = bookidlist?.get(i)
                        // ^ don't trust android studio, the ?. is load bearing
                        if (bookID != null) {
                            bookIDs.add(bookID)
                        }
                    }
                    break
                }
            }

            // look thru all the books and get the books that belong to this shelf
            val query = FirebaseDatabase.getInstance().reference.child("users").child(auth.uid!!).child("books")
            query.get().addOnSuccessListener { snapshot ->
                for (item in snapshot.children) {
                    if (item.key in bookIDs) {
                        val book = item.getValue(BookData::class.java)
                        if (book != null) {
                            books.add(book)
                        }
                    }
                }


                myAdapter = BooksAdapter(books)
                myAdapter.setMyItemClickListener(this)
                rv.adapter = myAdapter
            }
        }




        // Config menu bar (bottom bar)
        var fabButton = findViewById<FloatingActionButton>(R.id.fab)
        fabButton.setOnClickListener {
            val intent = Intent(this, AddNewBookScreen::class.java)
            intent.putExtra("ADD_TO_SHELF", shelfname)
            startActivity(intent);
        }
    }


    override fun onItemClickedFromAdapter(book: BookData) {

        Toast.makeText(this, "This will open the ${book.title} book detail pane.", Toast.LENGTH_SHORT).show()
    }
    override fun onItemLongClickedFromAdapter(book: BookData) {

        Toast.makeText(this, "This will open the ${book.title} book detail pane.", Toast.LENGTH_SHORT).show()

    }


    // Sets up the back/up button
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() == android.R.id.home) {
            finish()  // pops this activity off the back stack
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}