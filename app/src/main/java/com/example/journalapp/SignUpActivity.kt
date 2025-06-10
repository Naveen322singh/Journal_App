package com.example.journalapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.journalapp.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        auth = Firebase.auth

        binding.accSignUpButton.setOnClickListener {
            createUser()
        }
    }

    private fun createUser() {
        val email = binding.emailCreate.text.toString().trim()
        val password = binding.passwordCreate.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(this, "Password should be at least 6 characters", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("TAGY", "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)

                    // Navigate to JournalList directly since user is authenticated
                    startActivity(Intent(this, JournalList::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                    finish()

                    Toast.makeText(
                        baseContext,
                        "Account created successfully!",
                        Toast.LENGTH_SHORT,
                    ).show()
                } else {
                    Log.w("TAGY", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Sign up failed: ${task.exception?.message ?: "Unknown error"}",
                        Toast.LENGTH_LONG,
                    ).show()
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        // Additional UI updates if needed
    }
}