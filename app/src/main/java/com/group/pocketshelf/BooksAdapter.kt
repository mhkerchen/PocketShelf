package com.group.pocketshelf

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.cardview.widget.CardView
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class BooksAdapter(val items: ArrayList<BookData>):
        RecyclerView.Adapter<BooksAdapter.BookViewHolder>(){

        var myListener: MyItemClickListener? = null

        interface MyItemClickListener {
            fun onItemClickedFromAdapter(book: BookData)
            fun onItemLongClickedFromAdapter(book: BookData)
        }
        fun setMyItemClickListener(listener: MyItemClickListener) {
            this.myListener = listener
        }

        inner class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val bookCard = view.findViewById<CardView>(R.id.cv)
            val bookImg = view.findViewById<ImageView>(R.id.cover)
            val bookTitle = view.findViewById<TextView>(R.id.book_title)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.fragment_single_book, parent, false)
            return BookViewHolder(view)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
            val book = items[position]
            holder.bookTitle.text = book.title

            if (book.cover_is_url) {
                var path = book.img_url
                Picasso.get().load(path)
                    .into(holder.bookImg)
            } else {
                // TODO get book cover from firebase storage?
            }

            holder.bookCard.setOnClickListener {
                myListener!!.onItemClickedFromAdapter(book)
            }
            holder.bookCard.setOnLongClickListener {
                myListener!!.onItemLongClickedFromAdapter(book)
                true
            }
        }
    }
