package com.group.pocketshelf

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.cardview.widget.CardView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

    class LibraryShelfAdapter(val items: ArrayList<ShelfData>):
        RecyclerView.Adapter<LibraryShelfAdapter.ShelfViewHolder>(){

        var myListener: MyItemClickListener? = null

        interface MyItemClickListener {
            fun onItemClickedFromAdapter(shelf: ShelfData)
            fun onItemLongClickedFromAdapter(shelf: ShelfData)
            fun onItemClickedFromAdapter(book: BookData)
        }
        fun setMyItemClickListener(listener: MyItemClickListener) {
            this.myListener = listener
        }

        inner class ShelfViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val shelfCard = view.findViewById<CardView>(R.id.cv)
            val shelfName = view.findViewById<TextView>(R.id.shelf_name)
            val shelfSeeMore = view.findViewById<Button>(R.id.see_more)
            val shelfCount = view.findViewById<TextView>(R.id.book_count)
//
            val firstcover = view.findViewById<ImageView>(R.id.first_book)
            val secondcover = view.findViewById<ImageView>(R.id.second_book)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShelfViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.fragment_single_shelf, parent, false)
            return ShelfViewHolder(view)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: ShelfViewHolder, position: Int) {
            val shelf = items[position]
            holder.shelfName.text = shelf.name ?: "Untitled"
            var t = ""
            if ((shelf.books?.size ?: 0) > 2) {
                t =  (shelf.books?.size ?: 0).toString() +"\n books"
            }
            holder.shelfCount.text = t

            if ((shelf.books?.size ?: 0) > 0) {
                // find all books that belong to this shelf for previews
                var auth = FirebaseAuth.getInstance()
                val query = FirebaseDatabase.getInstance().reference.child("users").child(auth.uid!!).child("books")
                var foundBooks : ArrayList<BookData> = ArrayList<BookData>()
                query.get().addOnSuccessListener { snapshot ->
                    for (item in snapshot.children) {
                        val book = item.getValue(BookData::class.java)
                        if (book != null) {
                            if (item.key in shelf.books!!) {
                                foundBooks.add(book)
                            }
                        }
                    }

                    // Populate the cover previews of the shelf (first 2 books)
                    if (foundBooks.isNotEmpty()) {

                        if (foundBooks[0].cover_is_url) {
                            var path = foundBooks[0].img_url
                            Picasso.get().load(path).into(holder.firstcover)
                        } else {
                            // TODO
                        }
                        holder.firstcover.setOnClickListener {
                            myListener!!.onItemClickedFromAdapter(foundBooks[0])
                        }

                        if (foundBooks.size >= 2) {

                            if (foundBooks[1].cover_is_url) {
                                var path = foundBooks[1].img_url
                                Picasso.get().load(path).into(holder.secondcover)
                            } else {
                                // TODO
                            }

                            holder.secondcover.setOnClickListener {
                                myListener!!.onItemClickedFromAdapter(foundBooks[1])
                            }
                        }
                    }
                }
            }
            holder.shelfSeeMore.setOnClickListener {
                myListener!!.onItemClickedFromAdapter(shelf)
            }
            holder.shelfSeeMore.setOnLongClickListener {
                myListener!!.onItemLongClickedFromAdapter(shelf)
                true
            }
        }

    }
