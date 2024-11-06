// ListFragment.kt
package com.example.oving_4

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ListView
import androidx.fragment.app.ListFragment
import android.widget.ArrayAdapter

class ListFragment : ListFragment() {

    private var listener: OnItemSelectedListener? = null

    interface OnItemSelectedListener {
        fun onItemSelected(position: Int)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = if (context is OnItemSelectedListener) {
            context
        } else {
            throw RuntimeException("$context must implement OnItemSelectedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val titles = resources.getStringArray(R.array.item_titles)

        // Use the custom layout for list items
        val adapter = activity?.let {
            ArrayAdapter(it, R.layout.list_item_button, R.id.movie_button, titles)
        }
        listAdapter = adapter
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        listener?.onItemSelected(position)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
