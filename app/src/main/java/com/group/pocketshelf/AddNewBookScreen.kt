package com.group.pocketshelf

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.group.pocketshelf.bookapi.DCGoogleBook
import com.group.pocketshelf.bookapi.GoogleBooksAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.squareup.picasso.Picasso
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.ScanOptions
import com.journeyapps.barcodescanner.ScanContract
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat


class AddNewBookScreen : AppCompatActivity() {
    private lateinit var gbInterface: GoogleBooksAPI.GoogleBooksApiService

    private var isImageUrl : Boolean? = null
    private var imageSource : String? = null
    private var imageSourceURI : String? = null
    private var shelfname : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_new_book)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        shelfname = intent.getStringExtra("ADD_TO_SHELF") ?: "NONE"
        // Config action bar (top bar)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        if (shelfname != "NONE") {
            supportActionBar?.title = "Back to \""+shelfname+"\""
        }

        // Config menu bar (bottom bar)
        var fabButton = findViewById<FloatingActionButton>(R.id.fab)
        fabButton.setOnClickListener {
            finish()
        }

        gbInterface = RetrofitObject.provideRetrofit()

        // Interface buttons
        val searchButton = findViewById<Button>(R.id.isbn_search)
        searchButton.setOnClickListener {
            val search_text = findViewById<EditText>(R.id.isbn_input).text
            searchAPI(search_text.toString())
        }

        val submitButton = findViewById<Button>(R.id.submit_button)
        submitButton.setOnClickListener {
            addBook() // also finishes activity BTW
        }


        // Image picker code

        var selectedImage: ImageView = findViewById(R.id.cover_preview_image)
        val changeImage = registerForActivityResult( ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data
                val imgUri = data?.data
                selectedImage.setImageURI(imgUri)
                //isImageUrl = false
                imageSourceURI = imgUri.toString()

            }
        }
        val pickImgButton = findViewById<Button>(R.id.pick_image_button)
        pickImgButton.setOnClickListener {
            val pickImg = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI
            )
            changeImage.launch(pickImg)
        }

//        searchAPI("harry potter")
        //searchAPI("isbn:9781949846416")

        val scannedIsbn = intent.getStringExtra("SCANNED_ISBN")
        if (scannedIsbn != null) {
            findViewById<EditText>(R.id.isbn_input).setText("isbn:$scannedIsbn")
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



    fun searchAPI(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = gbInterface.searchBooks(query)
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()
                    val book = body?.items?.get(0)?.volumeInfo
                    Log.d("API result", book?.title ?: "NULL")

                    if (book != null) {
                        setFields(book)
                    }

                } else {
                    Log.e("Error", "Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("Error", "Network Error: ${e.localizedMessage}")
            }
        }
    }

    suspend fun setFields(book: DCGoogleBook) {
        val bookTitle = findViewById<TextInputEditText>(R.id.enter_book_title)
        val author = findViewById<TextInputEditText>(R.id.enter_author)
        val pagecount = findViewById<TextInputEditText>(R.id.enter_pagecount)
        val synopsis = findViewById<TextInputEditText>(R.id.enter_synopsis)
        val cover = findViewById<ImageView>(R.id.cover_preview_image)

        var syn : String = ""
        book.description?.length?.let {
            if (it > 250) {
                syn = book.description!!.substring(0,250) + "..."
            } else {
                syn = book.description?: ""
            }
        }

        this.isImageUrl = true
        this.imageSource = book.imageLinks?.get("smallThumbnail")
        var imgSource = this.imageSource + "key=AIzaSyC7w1uzil51oTDLmMNEDhLFfEgV7QReSG8"

        withContext(Dispatchers.Main) {

            bookTitle.setText(book.title?: "")
            author.setText(book.authors?.get(0) ?: "")
            pagecount.setText(book.pageCount.toString())
            synopsis.setText(syn)
            Log.d("Trying to load", imgSource)

            Picasso.get().load(imgSource).into(cover)

        }
    }

    // Create book from fields and this book to the shelf + DB
    fun addBook() {
        val bookTitle = findViewById<TextInputEditText>(R.id.enter_book_title)
        val author = findViewById<TextInputEditText>(R.id.enter_author)
        val pagecount = findViewById<TextInputEditText>(R.id.enter_pagecount)
        val synopsis = findViewById<TextInputEditText>(R.id.enter_synopsis)
        val tags_obj = findViewById<TextInputEditText>(R.id.enter_tags)
        val lang = findViewById<TextInputEditText>(R.id.enter_language)

        var tags : List<String> = tags_obj.text.toString().split(",")

        var book = BookData(
            title = bookTitle.text.toString(),
            img_url = imageSource,
            img_path=imageSourceURI,
            cover_is_url = isImageUrl ?: true,
            author = author.text.toString(),
            page_count = pagecount.text.toString(),
            synopsis = synopsis.text.toString(),
            language = lang.text.toString(),
            tags = ArrayList<String>(tags)
        )

        var auth = FirebaseAuth.getInstance()
        val users = FirebaseDatabase.getInstance().reference.child("users").child(auth.uid!!).child("books")
        var new: DatabaseReference = users.push()
        book.key = new.key
        new.setValue(book)

        if (shelfname != null) {
            val shelf = FirebaseDatabase.getInstance().reference.child("users").child(auth.uid!!).child("shelves").child(shelfname!!).child("books")

            var newShelf = ArrayList<String>()
            shelf.get().addOnSuccessListener { snapshot ->
                for (item in snapshot.children) {
                    val shelfentry = item.getValue(String::class.java)
                    if (shelfentry != null) {
                        newShelf.add(shelfentry)
                    }
                }
                newShelf.add(new.key ?: "")

                FirebaseDatabase.getInstance().reference
                    .child("users")
                    .child(auth.uid!!)
                    .child("shelves")
                    .child(shelfname!!)
                    .child("books")
                    .setValue(newShelf)

                Log.w("Finishing", "Writing data")

                finish()

            }
        }
    }
}