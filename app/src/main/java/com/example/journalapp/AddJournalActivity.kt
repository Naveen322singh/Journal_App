package com.example.journalapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.journalapp.databinding.ActivityAddJournalBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.Date

class AddJournalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddJournalBinding

    // Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var storageReference: StorageReference
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference: CollectionReference = db.collection("Journal")

    // User Info
    private var currentUserId: String = ""
    private var currentUserName: String = ""

    // Image URI
    private var imageUri: Uri? = null

    // Image picker result launcher
    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data?.data != null) {
            imageUri = result.data!!.data
            binding.postImageView.setImageURI(imageUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_journal)

        auth = FirebaseAuth.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        user = auth.currentUser ?: run {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        currentUserId = user.uid
        currentUserName = user.displayName ?: "Anonymous"

        binding.apply {
            postProgressBar.visibility = View.INVISIBLE

            postCameraButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                    type = "image/*"
                }
                imagePickerLauncher.launch(intent)
            }

            postSaveJournalButton.setOnClickListener {
                saveJournal()
            }
        }
    }

    private fun saveJournal() {
        val title = binding.postTitleEt.text.toString().trim()
        val thoughts = binding.postDescriptionEt.text.toString().trim()

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(thoughts) || imageUri == null) {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show()
            return
        }

        binding.postProgressBar.visibility = View.VISIBLE

        val filePath = storageReference.child("journal_images")
            .child("my_image_" + Timestamp.now().seconds)

        filePath.putFile(imageUri!!)
            .addOnSuccessListener {
                filePath.downloadUrl.addOnSuccessListener { uri ->
                    val journal = Journal(
                        title,
                        thoughts,
                        uri.toString(),
                        currentUserId,
                        Timestamp(Date()),
                        currentUserName
                    )

                    collectionReference.add(journal)
                        .addOnSuccessListener {
                            binding.postProgressBar.visibility = View.INVISIBLE
                            startActivity(Intent(this, JournalList::class.java))
                            finish()
                        }
                        .addOnFailureListener {
                            binding.postProgressBar.visibility = View.INVISIBLE
                            Toast.makeText(this, "Failed to save journal", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener {
                binding.postProgressBar.visibility = View.INVISIBLE
                Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
            }
    }
}
