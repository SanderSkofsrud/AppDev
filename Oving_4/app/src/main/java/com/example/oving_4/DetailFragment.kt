// DetailFragment.kt
package com.example.oving_4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class DetailFragment : Fragment() {

    private lateinit var movieTitleTextView: TextView
    private lateinit var itemImageView: ImageView
    private lateinit var itemDescriptionTextView: TextView

    private var currentPosition = 0
    private var pendingPosition: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the views
        movieTitleTextView = view.findViewById(R.id.movieTitleTextView)
        itemImageView = view.findViewById(R.id.itemImageView)
        itemDescriptionTextView = view.findViewById(R.id.itemDescriptionTextView)

        // Restore state
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt("currentPosition", 0)
        }

        // If there's a pending position update, apply it now
        if (pendingPosition != null) {
            updateContent(pendingPosition!!)
            pendingPosition = null
        } else {
            updateContent(currentPosition)
        }
    }


    fun updateContent(position: Int) {
        currentPosition = position

        // Check if views are initialized
        if (this::movieTitleTextView.isInitialized &&
            this::itemImageView.isInitialized &&
            this::itemDescriptionTextView.isInitialized) {

            val titles = resources.getStringArray(R.array.item_titles)
            val descriptions = resources.getStringArray(R.array.item_descriptions)
            val imageResIds = arrayOf(
                R.drawable.megashark,
                R.drawable.sharknado,
                R.drawable.velocipastor
            )

            if (position in titles.indices) {
                movieTitleTextView.text = titles[position]
                itemDescriptionTextView.text = descriptions[position]
                itemImageView.setImageResource(imageResIds[position])
            }
        } else {
            // Store the position to update later
            pendingPosition = position
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("currentPosition", currentPosition)
        super.onSaveInstanceState(outState)
    }
}
