package com.example.expensecalculator

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RegistrationViewModel : ViewModel() {
    var name = MutableLiveData<String>()
    var email = MutableLiveData<String>()
    var phone = MutableLiveData<String>()
    var password = MutableLiveData<String>()

    private val _registrationStatus = MutableLiveData<String>()
    val registrationStatus: LiveData<String> get() = _registrationStatus

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val databaseReference = FirebaseDatabase.getInstance().reference.child("users")

    fun isInputValid(): Boolean {
        return when {
            name.value.isNullOrEmpty() -> {
                _registrationStatus.value = "Name is required."
                false
            }
            email.value.isNullOrEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email.value!!).matches() -> {
                _registrationStatus.value = "Valid email is required."
                false
            }
            phone.value.isNullOrEmpty() || phone.value!!.length != 10 -> {
                _registrationStatus.value = "Valid 10-digit phone number is required."
                false
            }
            password.value.isNullOrEmpty() || password.value!!.length < 6 -> {
                _registrationStatus.value = "Password should be at least 6 characters."
                false
            }
            else -> true
        }
    }

    fun registerUser() {
        if (!isInputValid()) return

        val email = email.value
        val password = password.value

        if (email != null && password != null) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        if (userId != null) {
                            checkIfUserExists(userId, phone.value!!)

                        }
                    } else {
                        _registrationStatus.value = "Registration failed: ${task.exception?.message}"
                    }
                }
        }
    }

    private fun checkIfUserExists(userId: String, phone: String) {
        databaseReference.orderByChild("phone")
            .equalTo(phone)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        _registrationStatus.value = "Phone number is already registered."
                    } else {
                        saveUserData(userId)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    _registrationStatus.value = "Failed to check phone number: ${databaseError.message}"
                }
            })
    }

    private fun saveUserData(userId: String) {
        val user = mapOf(
            "name" to name.value,
            "email" to email.value,
            "phone" to phone.value,
            "password" to password.value
            // Exclude password for security
        )

        databaseReference.child(userId)
            .setValue(user)
            .addOnSuccessListener {
                _registrationStatus.value = "Registration successful!"
            }
            .addOnFailureListener {
                _registrationStatus.value = "Failed to save user data: ${it.message}"
            }
    }
}
