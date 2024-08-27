package com.example.oving_1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_firstname -> {
                Log.w("Oving_1", getString(R.string.fornavn) + " valgt")
                true
            }
            R.id.action_lastname -> {
                Log.e("Oving_1", getString(R.string.etternavn) + " valgt")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
