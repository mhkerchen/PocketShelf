package com.group.pocketshelf

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.cardview.widget.CardView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
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
            holder.shelfName.text = shelf.name
            holder.shelfCount.text = shelf.books.size.toString() +"\n books"
            if (shelf.books.size >= 1) {

                //holder.firstcover.text = shelf.books[0].url

                var path = shelf.books[0].url
                Picasso.get().load(path).into(holder.firstcover)
                holder.firstcover.setOnClickListener {
                    myListener!!.onItemClickedFromAdapter(shelf.books[0])
                }

                if (shelf.books.size >= 2) {

                    var path = shelf.books[1].url
                    Picasso.get().load(path)
                        .into(holder.secondcover)
                    //holder.secondcover.text = shelf.books[1].url
                    holder.secondcover.setOnClickListener {
                        myListener!!.onItemClickedFromAdapter(shelf.books[1])
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
