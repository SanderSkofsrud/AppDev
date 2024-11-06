// MainActivity.kt
package com.example.oving_3

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var friendsListView: ListView
    private lateinit var addFriendButton: Button

    private val friendsList = mutableListOf<Friend>()
    private lateinit var adapter: FriendAdapter

    companion object {
        const val REQUEST_CODE_ADD_FRIEND = 1
        const val REQUEST_CODE_EDIT_FRIEND = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        friendsListView = findViewById(R.id.friendsListView)
        addFriendButton = findViewById(R.id.addFriendButton)

        adapter = FriendAdapter(this, friendsList)
        friendsListView.adapter = adapter

        addFriendButton.setOnClickListener {
            val intent = Intent(this, AddEditFriendActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_FRIEND)
        }

        friendsListView.setOnItemClickListener { _, _, position, _ ->
            val friend = friendsList[position]
            val intent = Intent(this, AddEditFriendActivity::class.java)
            intent.putExtra("friend", friend)
            intent.putExtra("position", position)
            startActivityForResult(intent, REQUEST_CODE_EDIT_FRIEND)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {
            val friend = data.getSerializableExtra("friend") as Friend
            when (requestCode) {
                REQUEST_CODE_ADD_FRIEND -> {
                    friendsList.add(friend)
                }
                REQUEST_CODE_EDIT_FRIEND -> {
                    val position = data.getIntExtra("position", -1)
                    if (position != -1) {
                        friendsList[position] = friend
                    }
                }
            }
            updateList()
        }
    }

    private fun updateList() {
        adapter.notifyDataSetChanged()
    }
}
