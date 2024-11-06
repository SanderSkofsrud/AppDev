package com.example.oving_3

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.app.DatePickerDialog
import java.util.Calendar

class AddEditFriendActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var birthDateEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button

    private var position: Int = -1
    private var isEditMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_friend)

        nameEditText = findViewById(R.id.nameEditText)
        birthDateEditText = findViewById(R.id.birthDateEditText)
        saveButton = findViewById(R.id.saveButton)
        cancelButton = findViewById(R.id.cancelButton)

        // Set OnClickListener after initializing birthDateEditText
        birthDateEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val dateString = String.format("%02d.%02d.%d", selectedDay, selectedMonth + 1, selectedYear)
                birthDateEditText.setText(dateString)
            }, year, month, day)
            datePicker.show()
        }

        // Sjekk om vi skal redigere en venn
        val friend = intent.getSerializableExtra("friend") as? Friend
        if (friend != null) {
            isEditMode = true
            position = intent.getIntExtra("position", -1)
            nameEditText.setText(friend.name)
            birthDateEditText.setText(friend.birthDate)
        }

        saveButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val birthDate = birthDateEditText.text.toString()

            if (name.isBlank() || birthDate.isBlank()) {
                Toast.makeText(this, "Alle feltene må fylles ut", Toast.LENGTH_SHORT).show()
            } else {
                val newFriend = Friend(name, birthDate)
                val resultIntent = Intent()
                resultIntent.putExtra("friend", newFriend)
                if (isEditMode) {
                    resultIntent.putExtra("position", position)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }

        cancelButton.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }
}
