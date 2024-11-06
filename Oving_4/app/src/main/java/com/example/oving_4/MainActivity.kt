// MainActivity.kt
package com.example.oving_4

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager

class MainActivity : AppCompatActivity(), ListFragment.OnItemSelectedListener {

    private var currentPosition = 0
    private var totalItems = 0
    private lateinit var detailFragment: DetailFragment
    private lateinit var listFragment: ListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inflate the unified layout
        val inflater = layoutInflater
        val mainFrame = findViewById<FrameLayout>(R.id.main_frame)

        // Remove any existing views
        mainFrame.removeAllViews()

        val fragmentContainer = inflater.inflate(R.layout.fragment_container, mainFrame, false)
        mainFrame.addView(fragmentContainer)

        // Get total number of items
        totalItems = resources.getStringArray(R.array.item_titles).size

        // Add fragments to the containers
        addFragments()

        // Adjust the layout based on orientation
        adjustLayout()

        // Restore state
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt("currentPosition", 0)
        }

        // Use the detailFragment instance directly
        detailFragment.updateContent(currentPosition)
    }

    private fun addFragments() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        // Add ListFragment
        listFragment = ListFragment()
        fragmentTransaction.replace(R.id.list_fragment_container, listFragment, "ListFragment")

        // Add DetailFragment
        detailFragment = DetailFragment()
        fragmentTransaction.replace(R.id.detail_fragment_container, detailFragment, "DetailFragment")

        fragmentTransaction.commit()
    }

    private fun adjustLayout() {
        val orientation = resources.configuration.orientation
        val fragmentContainer = findViewById<LinearLayout>(R.id.fragment_container)
        val listContainer = findViewById<FrameLayout>(R.id.list_fragment_container)
        val detailContainer = findViewById<FrameLayout>(R.id.detail_fragment_container)

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Vertical orientation
            fragmentContainer.orientation = LinearLayout.VERTICAL

            // Adjust layout weights
            val listParams = listContainer.layoutParams as LinearLayout.LayoutParams
            listParams.width = LinearLayout.LayoutParams.MATCH_PARENT
            listParams.height = 0
            listParams.weight = 1F
            listContainer.layoutParams = listParams

            val detailParams = detailContainer.layoutParams as LinearLayout.LayoutParams
            detailParams.width = LinearLayout.LayoutParams.MATCH_PARENT
            detailParams.height = 0
            detailParams.weight = 2F
            detailContainer.layoutParams = detailParams
        } else {
            // Horizontal orientation
            fragmentContainer.orientation = LinearLayout.HORIZONTAL

            // Adjust layout weights
            val listParams = listContainer.layoutParams as LinearLayout.LayoutParams
            listParams.width = 0
            listParams.height = LinearLayout.LayoutParams.MATCH_PARENT
            listParams.weight = 1F
            listContainer.layoutParams = listParams

            val detailParams = detailContainer.layoutParams as LinearLayout.LayoutParams
            detailParams.width = 0
            detailParams.height = LinearLayout.LayoutParams.MATCH_PARENT
            detailParams.weight = 2F
            detailContainer.layoutParams = detailParams
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Adjust the layout when configuration changes (e.g., orientation change)
        adjustLayout()
    }

    override fun onItemSelected(position: Int) {
        currentPosition = position
        detailFragment.updateContent(position)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.previous_item -> {
                if (currentPosition > 0) {
                    currentPosition--
                } else {
                    currentPosition = totalItems - 1
                }
                detailFragment.updateContent(currentPosition)
                true
            }
            R.id.next_item -> {
                if (currentPosition < totalItems - 1) {
                    currentPosition++
                } else {
                    currentPosition = 0
                }
                detailFragment.updateContent(currentPosition)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("currentPosition", currentPosition)
        super.onSaveInstanceState(outState)
    }
}
