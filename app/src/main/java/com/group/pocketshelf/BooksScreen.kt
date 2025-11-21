package com.group.pocketshelf

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

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


        val books = ArrayList<BookData>()


        val book1 = BookData("Test Book 1", "Description", "https://ia.media-imdb.com/images/M/MV5BMTMwNjAxMTc0Nl5BMl5BanBnXkFtZTcwODc3ODk5Mg@@._V1_SY317_CR0,0,214,317_AL_.jpg")
        val book2 = BookData("Test Book 2", "Description", "https://ia.media-imdb.com/images/M/MV5BMTYwOTEwNjAzMl5BMl5BanBnXkFtZTcwODc5MTUwMw@@._V1_SY317_CR0,0,214,317_AL_.jpg")
        val book3 = BookData("Test Book 3", "Description", "https://ia.media-imdb.com/images/M/MV5BMTQ1MjQwMTE5OF5BMl5BanBnXkFtZTgwNjk3MTcyMDE@._V1_SX214_AL_.jpg")
        val book4 = BookData("Test Book 4", "Description", "https://ia.media-imdb.com/images/M/MV5BMjExNzM0NDM0N15BMl5BanBnXkFtZTcwMzkxOTUwNw@@._V1_SY317_CR0,0,214,317_AL_.jpg")

        books.add(book1)
        books.add(book2)
        books.add(book3)
        books.add(book4)

        myAdapter = BooksAdapter(books)
        myAdapter.setMyItemClickListener(this)
        rv.adapter = myAdapter
    }


    override fun onItemClickedFromAdapter(book: BookData) {

        Toast.makeText(this, "This will open the ${book.name} book detail pane.", Toast.LENGTH_SHORT).show()
    }
    override fun onItemLongClickedFromAdapter(book: BookData) {

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