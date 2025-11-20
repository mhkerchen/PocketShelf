package com.group.pocketshelf

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import android.widget.Toast

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "shelf_name"
private const val ARG_PARAM2 = "shelf_contents"


// A single shelf, as displayed from the User Library screen.

class LibraryShelf : Fragment() {
    private var shelf_name: String = "DEFAULT"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            shelf_name = it.getString(ARG_PARAM1) ?: ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_library_shelf, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("myTag", "name of shelf is "+shelf_name);
        //val books_rv = requireActivity().findViewById<RecyclerView>(R.id.horizontal_shelf_view)
//        val shelfnamewidget = requireActivity().findViewById<TextView>(R.id.shelf_name)
//        shelfnamewidget.text = "TESTER"//this.shelf_name

//        val layoutManager = LinearLayoutManager(view.context)
//
//        rv.hasFixedSize()
//        rv.layoutManager = layoutManager
//
//
//
//        val myShelves = ArrayList<ShelfData>()
//
//
//        val book1 = BookData("Test Book 1", "Description")
//        val book2 = BookData("Test Book 2", "Description")
//        val book3 = BookData("Test Book 3", "Description")
//        val book4 = BookData("Test Book 4", "Description")
//
//        val bookslist1 = ArrayList<BookData>()
//        bookslist1.add(book1)
//        bookslist1.add(book2)
//        bookslist1.add(book3)
//        val shelf1 = ShelfData("Favorite Books", bookslist1)
//        val bookslist2 = ArrayList<BookData>()
//        bookslist1.add(book2)
//        bookslist1.add(book3)
//        bookslist1.add(book4)
//        val shelf2 = ShelfData("New Books", bookslist1)
//
//        myShelves.add(shelf1)
//        myShelves.add(shelf2)
//        // still need to make this adapter
//        myAdapter = LibraryShelfAdapter(myShelves)
//        myAdapter.setMyItemClickListener(this)
//        rv.adapter = myAdapter

//        val query = FirebaseDatabase.getInstance().reference.child("shelf_"+this.shelf_name).limitToFirst(50)
//        var movieList = ArrayList<FirebaseMovie>()
//
//        query.get().addOnSuccessListener {
//            for (item in it.children) {
//                movieList.add(item.getValue<FirebaseMovie>()!!)
//            }
//            myAdapter = RecyclerViewAdapter(movieList)
//            myAdapter.setMyItemClickListener(this)
//            rv.adapter = myAdapter
//        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LibraryShelf.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
            LibraryShelf().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}