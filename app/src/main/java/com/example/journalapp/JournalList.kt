package com.example.journalapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.journalapp.databinding.ActivityJournalListBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference

class JournalList : AppCompatActivity() {

    private lateinit var binding: ActivityJournalListBinding

    // Firebase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private val db = FirebaseFirestore.getInstance()
    private lateinit var storageReference: StorageReference
    private val collectionReference: CollectionReference = db.collection("Journal")

    private lateinit var journalList: MutableList<Journal>
    private lateinit var adapter: JournalRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_journal_list)
        setSupportActionBar(binding.toolbar)

        firebaseAuth = Firebase.auth
        user = firebaseAuth.currentUser!!

        journalList = arrayListOf()

        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@JournalList)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        menu?.findItem(R.id.action_signout)?.let { signOutItem ->
            val spannableString = SpannableString(signOutItem.title)
            spannableString.setSpan(
                ForegroundColorSpan(Color.BLACK),
                0,
                spannableString.length,
                0
            )
            signOutItem.title = spannableString
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                startActivity(Intent(this, AddJournalActivity::class.java))
                true
            }
            R.id.action_signout -> {
                firebaseAuth.signOut()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()

        // Clear old data to avoid duplication when refreshing
        journalList.clear()

        collectionReference.whereEqualTo("userId", user.uid)
            .get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.isEmpty) {
                    binding.listNoPosts.visibility = View.GONE

                    for (document in snapshot) {
                        val journal = Journal(
                            document.getString("title") ?: "",
                            document.getString("thoughts") ?: "",
                            document.getString("imageUrl") ?: "",
                            document.getString("userId") ?: "",
                            document.getTimestamp("timeAdded") ?: Timestamp.now(),
                            document.getString("username") ?: "Unknown"
                        )
                        journalList.add(journal)
                    }

                    adapter = JournalRecyclerAdapter(this, journalList)
                    binding.recyclerView.adapter = adapter
                } else {
                    binding.listNoPosts.visibility = View.VISIBLE
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Oops! Something went wrong!", Toast.LENGTH_SHORT).show()
            }
    }
}
