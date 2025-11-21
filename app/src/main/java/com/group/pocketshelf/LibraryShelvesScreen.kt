package com.group.pocketshelf

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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


        val layoutManager = LinearLayoutManager(this)
        val rv = findViewById<RecyclerView>(R.id.books)
        rv.hasFixedSize()
        rv.layoutManager = layoutManager


        val myShelves = ArrayList<ShelfData>()


        val book1 = BookData("Test Book 1", "Description", "https://ia.media-imdb.com/images/M/MV5BMTMwNjAxMTc0Nl5BMl5BanBnXkFtZTcwODc3ODk5Mg@@._V1_SY317_CR0,0,214,317_AL_.jpg")
        val book2 = BookData("Test Book 2", "Description", "https://ia.media-imdb.com/images/M/MV5BMTYwOTEwNjAzMl5BMl5BanBnXkFtZTcwODc5MTUwMw@@._V1_SY317_CR0,0,214,317_AL_.jpg")
        val book3 = BookData("Test Book 3", "Description", "https://ia.media-imdb.com/images/M/MV5BMTQ1MjQwMTE5OF5BMl5BanBnXkFtZTgwNjk3MTcyMDE@._V1_SX214_AL_.jpg")
        val book4 = BookData("Test Book 4", "Description", "https://ia.media-imdb.com/images/M/MV5BMjExNzM0NDM0N15BMl5BanBnXkFtZTcwMzkxOTUwNw@@._V1_SY317_CR0,0,214,317_AL_.jpg")

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


        Toast.makeText(this, "This will open the ${shelf.name} shelf.", Toast.LENGTH_SHORT).show()
//        val manager = activity?.supportFragmentManager
//        val transaction = manager?.beginTransaction()
//        transaction?.replace(R.id.fg, MovieDetailFragment.newInstance(movie))
//        transaction?.addToBackStack(null)
//        transaction?.commit()
    }

    override fun onItemLongClickedFromAdapter(shelf: ShelfData) {
        Toast.makeText(this, "You long Click the ${shelf.name} see more!", Toast.LENGTH_SHORT).show()
    }


    override fun onItemClickedFromAdapter(book: BookData) {
        Toast.makeText(this, "This will open the ${book.name} book detail pane.", Toast.LENGTH_SHORT).show()
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