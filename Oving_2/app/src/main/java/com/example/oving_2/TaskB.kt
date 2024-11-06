package com.example.oving_2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class TaskB : AppCompatActivity() {

    private lateinit var number1TextView: TextView
    private lateinit var number2TextView: TextView
    private lateinit var answerEditText: EditText
    private lateinit var upperLimitEditText: EditText

    companion object {
        const val EXTRA_UPPER_LIMIT = "EXTRA_UPPER_LIMIT"
        const val REQUEST_CODE_NUMBER1 = 1
        const val REQUEST_CODE_NUMBER2 = 2
        const val RANDOM_NUMBER = "RANDOM_NUMBER"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_b)

        // Initialize views
        number1TextView = findViewById(R.id.number1TextView)
        number2TextView = findViewById(R.id.number2TextView)
        answerEditText = findViewById(R.id.answerEditText)
        upperLimitEditText = findViewById(R.id.upperLimitEditText)

        val addButton = findViewById<Button>(R.id.addButton)
        val multiplyButton = findViewById<Button>(R.id.multiplyButton)

        // Set onClick listeners
        addButton.setOnClickListener { onAddClicked() }
        multiplyButton.setOnClickListener { onMultiplyClicked() }
    }

    private fun onAddClicked() {
        val num1 = number1TextView.text.toString().toInt()
        val num2 = number2TextView.text.toString().toInt()
        val userAnswer = answerEditText.text.toString().toIntOrNull()
        val correctAnswer = num1 + num2

        if (userAnswer == correctAnswer) {
            Toast.makeText(this, getString(R.string.riktig), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
                this,
                "${getString(R.string.feil_riktig_svar_er)} $correctAnswer",
                Toast.LENGTH_SHORT
            ).show()
        }
        setRandomNumbers()
    }

    private fun onMultiplyClicked() {
        val num1 = number1TextView.text.toString().toInt()
        val num2 = number2TextView.text.toString().toInt()
        val userAnswer = answerEditText.text.toString().toIntOrNull()
        val correctAnswer = num1 * num2

        if (userAnswer == correctAnswer) {
            Toast.makeText(this, getString(R.string.riktig), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
                this,
                "${getString(R.string.feil_riktig_svar_er)} $correctAnswer",
                Toast.LENGTH_SHORT
            ).show()
        }
        setRandomNumbers()
    }

    private fun setRandomNumbers() {
        val upperLimitText = upperLimitEditText.text.toString()
        val upperLimit = if (upperLimitText.isNotEmpty()) upperLimitText.toInt() else 10

        // Start TaskA activity to get the first random number
        val intent1 = Intent(this, TaskA::class.java)
        intent1.putExtra(EXTRA_UPPER_LIMIT, upperLimit)
        startActivityForResult(intent1, REQUEST_CODE_NUMBER1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val randomNumber = data.getIntExtra(RANDOM_NUMBER, -1)
            if (randomNumber != -1) {
                when (requestCode) {
                    REQUEST_CODE_NUMBER1 -> {
                        number1TextView.text = randomNumber.toString()
                        // Now get the second random number
                        val upperLimitText = upperLimitEditText.text.toString()
                        val upperLimit = if (upperLimitText.isNotEmpty()) upperLimitText.toInt() else 10

                        val intent2 = Intent(this, TaskA::class.java)
                        intent2.putExtra(EXTRA_UPPER_LIMIT, upperLimit)
                        startActivityForResult(intent2, REQUEST_CODE_NUMBER2)
                    }
                    REQUEST_CODE_NUMBER2 -> {
                        number2TextView.text = randomNumber.toString()
                    }
                }
            }
        }
    }
}
