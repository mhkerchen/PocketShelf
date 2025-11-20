package com.group.pocketshelf

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.cardview.widget.CardView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button

    class LibraryShelfAdapter(val items: ArrayList<ShelfData>):
        RecyclerView.Adapter<LibraryShelfAdapter.ShelfViewHolder>(){

        var myListener: MyItemClickListener? = null

        interface MyItemClickListener {
            fun onItemClickedFromAdapter(shelf: ShelfData)
            fun onItemLongClickedFromAdapter(shelf: ShelfData)
        }
        fun setMyItemClickListener(listener: MyItemClickListener) {
            this.myListener = listener
        }

        inner class ShelfViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val shelfCard = view.findViewById<CardView>(R.id.cv)
            val shelfName = view.findViewById<TextView>(R.id.shelf_name)
            val shelfSeeMore = view.findViewById<Button>(R.id.see_more)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShelfViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.fragment_library_shelf, parent, false)
            return ShelfViewHolder(view)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: ShelfViewHolder, position: Int) {
            val shelf = items[position]
            holder.shelfName.text = shelf.name

            //Picasso.get().load(movie.url).into(holder.moviePoster)
            holder.shelfSeeMore.setOnClickListener {
                myListener!!.onItemClickedFromAdapter(shelf)
            }
            holder.shelfSeeMore.setOnLongClickListener {
                myListener!!.onItemLongClickedFromAdapter(shelf)
                true
            }
        }
    }
