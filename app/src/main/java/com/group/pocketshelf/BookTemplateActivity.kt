package com.group.pocketshelf

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.group.pocketshelf.databinding.ActivityBookTemplateBinding

class BookTemplateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookTemplateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookTemplateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // This screen displays the book layout template only.
        // No logic or navigation required unless you want to add it.
    }
}
