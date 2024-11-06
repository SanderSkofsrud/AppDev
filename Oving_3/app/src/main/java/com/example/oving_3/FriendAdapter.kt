// FriendAdapter.kt
package com.example.oving_3

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class FriendAdapter(context: Context, private val friends: MutableList<Friend>) :
    ArrayAdapter<Friend>(context, 0, friends) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get the data item for this position
        val friend = friends[position]

        // Check if an existing view is being reused, otherwise inflate the view
        val view = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.friend_list_item, parent, false
        )

        // Lookup view for data population
        val nameTextView = view.findViewById<TextView>(R.id.friendNameTextView)
        val dobTextView = view.findViewById<TextView>(R.id.friendDOBTextView)

        // Populate the data into the template view using the data object
        nameTextView.text = friend.name
        dobTextView.text = friend.birthDate

        // Return the completed view to render on screen
        return view
    }
}
