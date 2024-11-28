package com.example.expensecalculator

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.expensecalculator.ActivityFolder.LoginActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    var phone = MutableLiveData<String>()
    var password = MutableLiveData<String>()

    private val _loginStatus = MutableLiveData<String>()
    val loginStatus: LiveData<String> get() = _loginStatus

    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("users")
    private val sharedPref = getApplication<Application>().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)

    fun loginUser() {
        val enteredPhone = phone.value
        val enteredPassword = password.value

        if (enteredPhone.isNullOrEmpty() || enteredPassword.isNullOrEmpty()) {
            _loginStatus.value = "Please enter both phone number and password."
            return
        }

        // Access user data directly by phone number
        databaseReference.orderByChild("phone").equalTo(enteredPhone).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Iterate over the results to find the user data (there should only be one result)
                    for (userSnapshot in dataSnapshot.children) {
                        val userPassword = userSnapshot.child("password").getValue(String::class.java)
                        if (userPassword == enteredPassword) {
                            _loginStatus.value = "Login successful!"
                            saveLoginState(true)
                        } else {
                            _loginStatus.value = "Incorrect phone number or password."
                        }
                        return // Exit after the first match
                    }
                } else {
                    _loginStatus.value = "User not found."
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                _loginStatus.value = "Login failed. Please try again."
            }
        })
    }

    private fun saveLoginState(isLoggedIn: Boolean) {
        sharedPref.edit().putBoolean("isLoggedIn", isLoggedIn).apply()
    }
}
