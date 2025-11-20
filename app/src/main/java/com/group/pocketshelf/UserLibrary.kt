package com.group.pocketshelf

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class UserLibrary : AppCompatActivity(), LibraryShelfAdapter.MyItemClickListener {

    lateinit var myAdapter: LibraryShelfAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_library)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val layoutManager = LinearLayoutManager(this)
        val rv = findViewById<RecyclerView>(R.id.all_shelves)
        rv.hasFixedSize()
        rv.layoutManager = layoutManager


        val myShelves = ArrayList<ShelfData>()


        val book1 = BookData("Test Book 1", "Description")
        val book2 = BookData("Test Book 2", "Description")
        val book3 = BookData("Test Book 3", "Description")
        val book4 = BookData("Test Book 4", "Description")

        val bookslist1 = ArrayList<BookData>()
        bookslist1.add(book1)
        bookslist1.add(book2)
        bookslist1.add(book3)
        val shelf1 = ShelfData("Favorite Books", bookslist1)
        val bookslist2 = ArrayList<BookData>()
        bookslist2.add(book2)
        bookslist2.add(book3)
        bookslist2.add(book4)
        val shelf2 = ShelfData("New Books", bookslist2)

        myShelves.add(shelf1)
        myShelves.add(shelf2)

        myAdapter = LibraryShelfAdapter(myShelves)
        myAdapter.setMyItemClickListener(this)
        rv.adapter = myAdapter
    }


    // The click handlers, for when a user clicks the "See more" on a shelf

    override fun onItemClickedFromAdapter(shelf: ShelfData) {


        Toast.makeText(this, "You click the ${shelf.name} see more!", Toast.LENGTH_SHORT).show()
//        val manager = activity?.supportFragmentManager
//        val transaction = manager?.beginTransaction()
//        transaction?.replace(R.id.fg, MovieDetailFragment.newInstance(movie))
//        transaction?.addToBackStack(null)
//        transaction?.commit()
    }

    override fun onItemLongClickedFromAdapter(shelf: ShelfData) {
        Toast.makeText(this, "You long Click the ${shelf.name} see more!", Toast.LENGTH_SHORT).show()
    }

}