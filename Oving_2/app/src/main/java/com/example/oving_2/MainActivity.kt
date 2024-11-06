package com.example.oving_2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Call the parent constructor
        super.onCreate(savedInstanceState)
        // Set the content view to the activity's layout
        setContentView(R.layout.activity_main)

        // Find the buttons by their IDs
        val buttonTaskA = findViewById<Button>(R.id.button_task_a)
        val buttonTaskB = findViewById<Button>(R.id.button_task_b)

        // Set click listeners for the buttons
        buttonTaskA.setOnClickListener {
            // Create an intent to start TaskA
            val intent = Intent(this, TaskA::class.java)
            startActivity(intent)
        }

        buttonTaskB.setOnClickListener {
            // Create an intent to start TaskB
            val intent = Intent(this, TaskB::class.java)
            startActivity(intent)
        }
    }
}
