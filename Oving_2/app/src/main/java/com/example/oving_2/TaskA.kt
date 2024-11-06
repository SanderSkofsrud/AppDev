package com.example.oving_2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TaskA : AppCompatActivity() {

    companion object {
        const val EXTRA_UPPER_LIMIT = "EXTRA_UPPER_LIMIT"
        const val RANDOM_NUMBER = "RANDOM_NUMBER"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // No need to set a content view since this activity doesn't display a UI
        setContentView(R.layout.activity_task_a)  // Optional, if you have a layout

        // Get upper limit from Intent
        val upperLimit = intent.getIntExtra(EXTRA_UPPER_LIMIT, 100)

        // Generate random number
        val value = (0..upperLimit).random()

        // Task 1a: Display the result in a Toast (commented out as per Task 1d)
        Toast.makeText(this, "Random Number: $value", Toast.LENGTH_LONG).show()

        // Task 1c: Return the random number as the result
        val resultIntent = Intent()
        resultIntent.putExtra(RANDOM_NUMBER, value)
        setResult(Activity.RESULT_OK, resultIntent)

        // Task 1d: Call finish() and comment out the Toast
        finish()
    }
}
