package com.group.pocketshelf

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.GenericTypeIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LibraryShelvesScreen : AppCompatActivity(), LibraryShelfAdapter.MyItemClickListener {

    lateinit var myAdapter: LibraryShelfAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_library_shelves)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Config action bar (top bar)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);

        // Config menu bar (bottom bar)
        var fabButton = findViewById<FloatingActionButton>(R.id.fab)
        fabButton.setOnClickListener {
            val intent = Intent(this, AddNewShelfScreen::class.java)
            startActivity(intent);

        }

        // Config top bar
        setSupportActionBar(findViewById(R.id.my_toolbar))
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(false) // always hide back button
//        if (shelfname != "NONE") {
//            supportActionBar?.title = "Back to \""+shelfname+"\""
//        }


        val layoutManager = LinearLayoutManager(this)
        val rv = findViewById<RecyclerView>(R.id.books)
        rv.hasFixedSize()
        rv.layoutManager = layoutManager


        val myShelves = ArrayList<ShelfData>()
        var auth = FirebaseAuth.getInstance()
        val query = FirebaseDatabase.getInstance().reference.child("users").child(auth.uid!!).child("shelves")
        query.get().addOnSuccessListener { snapshot ->
            for (child in snapshot.children) {
                val shelf = child.getValue(ShelfData::class.java)
                if (shelf != null) {
                    myShelves.add(shelf)
                }
            }

            myAdapter = LibraryShelfAdapter(myShelves)
            myAdapter.setMyItemClickListener(this)
            rv.adapter = myAdapter
        }




    }


    // The click handlers, for when a user clicks the "See more" on a shelf

    override fun onItemClickedFromAdapter(shelf: ShelfData) {


        //Toast.makeText(this, "This will open the ${shelf.name} shelf.", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, BooksScreen::class.java)
        intent.putExtra("SHELF_NAME", shelf.name)
        startActivity(intent);

    }

    override fun onItemLongClickedFromAdapter(shelf: ShelfData) {
        Toast.makeText(this, "You long Click the ${shelf.name} see more!", Toast.LENGTH_SHORT).show()
    }


    override fun onItemClickedFromAdapter(book: BookData) {
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

    override fun onResume() {
        super.onResume()
        val layoutManager = LinearLayoutManager(this)
        val rv = findViewById<RecyclerView>(R.id.books)
        rv.hasFixedSize()
        rv.layoutManager = layoutManager

        val thisScope = this

        CoroutineScope(Dispatchers.IO).launch {

            val myShelves = ArrayList<ShelfData>()
            var auth = FirebaseAuth.getInstance()
            val query = FirebaseDatabase.getInstance().reference.child("users").child(auth.uid!!).child("shelves")
            query.get().addOnSuccessListener { snapshot ->
                for (child in snapshot.children) {
                    val shelf = child.getValue(ShelfData::class.java)
                    if (shelf != null) {
                        myShelves.add(shelf)
                    }
                }

                myAdapter = LibraryShelfAdapter(myShelves)
                myAdapter.setMyItemClickListener(thisScope)
                rv.adapter = myAdapter
            }
        }
    }


}