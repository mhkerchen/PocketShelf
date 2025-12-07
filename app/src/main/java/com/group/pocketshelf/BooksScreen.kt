package com.group.pocketshelf

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.ScanOptions
import com.journeyapps.barcodescanner.ScanContract
import android.widget.EditText
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import android.graphics.pdf.PdfDocument
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException



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

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.selectedItemId = R.id.nav_home // highlight current tab
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    //On this screen
                    true
                }
                R.id.nav_export -> {
                    exportBooks()
                    true
                }

                R.id.nav_scan -> {
                    checkCameraPermission()
                    true
                }
                R.id.nav_settings -> {
                    // Empty
                    true
                }
                else -> false
            }
        }

    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show()
            // Launch barcode scanner
            val options = ScanOptions()
            options.setPrompt("Scan a book barcode")
            options.setBeepEnabled(true)
            options.setOrientationLocked(true)
            barcodeLauncher.launch(options)
        } else {
            Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkCameraPermission() {
        cameraLauncher.launch(Manifest.permission.CAMERA)
    }

    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            // User scanned a barcode
            val intent = Intent(this, AddNewBookScreen::class.java)
            intent.putExtra("ADD_TO_SHELF", "shelf1")
            intent.putExtra("SCANNED_ISBN", result.contents)
            startActivity(intent)
        } else {
            // User pressed back or cancelled the scan
            Toast.makeText(this, "Scan cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onItemClickedFromAdapter(book: BookData) {
        val intent = Intent(this, BookTemplateActivity::class.java)
        intent.putExtra("BOOK", book)
        startActivity(intent);
        //Toast.makeText(this, "This will open the ${book.title} book detail pane.", Toast.LENGTH_SHORT).show()
    }
    override fun onItemLongClickedFromAdapter(book: BookData) {
        var auth = FirebaseAuth.getInstance()

        val query = FirebaseDatabase.getInstance().reference.child("users").child(auth.uid!!).child("books")
        query.get().addOnSuccessListener { snapshot ->
            var delkey = ""
            for (child in snapshot.children) {
                val foundbook = child.getValue(BookData::class.java)
                if (foundbook?.title == book?.title) {
                    delkey = child.key ?: ""
                }
            }

            if (delkey != "") {
                val deleteme = FirebaseDatabase.getInstance().reference
                    .child("users").child(auth.uid!!).child("books").child(delkey)

                deleteme.removeValue()
                    .addOnSuccessListener {

                        reload()  // need to reload the adapter from firebase
                    }
            }
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




    override fun onResume() {
        super.onResume()

        reload()
    }

    fun reload() {

        val NUMBER_COLUMNS = 4
        val layoutManager = GridLayoutManager(this, NUMBER_COLUMNS)
        val thisScope = this

        val rv = findViewById<RecyclerView>(R.id.books)
        rv.hasFixedSize()
        rv.layoutManager = layoutManager


        var auth = FirebaseAuth.getInstance()

        val shelfname: String = intent.getStringExtra("SHELF_NAME") ?: "shelf1"
        var shelfname_widget = findViewById<TextView>(R.id.shelf_name)
        shelfname_widget.text = shelfname
        // Get the list of book ids that go in this shelf
        var bookIDs = ArrayList<String>()
        var books = ArrayList<BookData>()

        CoroutineScope(Dispatchers.IO).launch {
            val query = FirebaseDatabase.getInstance().reference.child("users").child(auth.uid!!)
                .child("shelves")
            query.get().addOnSuccessListener { snapshot ->
                // find the shelf that's named the same as the given shelf
                val typeIndicator = object : GenericTypeIndicator<ArrayList<String>>() {}
                for (child in snapshot.children) {
                    if (child.key == shelfname) {
                        val bookidlist =
                            child.child("books").getValue(typeIndicator) ?: ArrayList<String?>()

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
                val query =
                    FirebaseDatabase.getInstance().reference.child("users").child(auth.uid!!)
                        .child("books")
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
                    myAdapter.setMyItemClickListener(thisScope)
                    rv.adapter = myAdapter
                }
            }
        }
    }

    fun createPdf(books: List<BookData>) {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4

        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        var y = 40
        val paint = android.graphics.Paint()
        paint.textSize = 12f

        paint.textAlign = android.graphics.Paint.Align.CENTER
        canvas.drawText("My Book Shelf", pageInfo.pageWidth / 2f, y.toFloat(), paint)

        paint.textAlign = android.graphics.Paint.Align.LEFT
        y += 40

        for (book in books) {
            canvas.drawText("Title: ${book.title}", 40f, y.toFloat(), paint)
            y += 20
            canvas.drawText("Author: ${book.author}", 40f, y.toFloat(), paint)
            y += 20
            canvas.drawText("Pages: ${book.page_count}", 40f, y.toFloat(), paint)
            y += 30
        }

        pdfDocument.finishPage(page)

        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "books_export.pdf")
        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(this, "PDF saved to Downloads", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error creating PDF", Toast.LENGTH_SHORT).show()
        }

        pdfDocument.close()
    }

    fun exportBooks() {
        val auth = FirebaseAuth.getInstance()
        val userBooksRef = FirebaseDatabase.getInstance().reference.child("users").child(auth.uid!!).child("books")

        userBooksRef.get().addOnSuccessListener { snapshot ->
            val books = ArrayList<BookData>()
            for (item in snapshot.children) {
                val book = item.getValue(BookData::class.java)
                if (book != null) books.add(book)
            }

            createPdf(books)
        }
    }

}