package com.group.pocketshelf
import com.google.gson.Gson
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.group.pocketshelf.databinding.ActivityBookTemplateBinding

class BookTemplateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookTemplateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookTemplateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Config action bar (top bar)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);


        var book = intent.getSerializableExtra("BOOK") as BookData

        val jsonString = Gson().toJson(book)

        binding.content.text = jsonString

        // This screen displays the book layout template only.
        // No logic or navigation required unless you want to add it.
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
