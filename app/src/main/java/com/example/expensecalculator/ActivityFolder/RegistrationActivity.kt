package com.example.expensecalculator.ActivityFolder

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.expensecalculator.R
import com.example.expensecalculator.RegistrationViewModel
import com.example.expensecalculator.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding
    private val viewModel: RegistrationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up Data Binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registration)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Observe registration status
        viewModel.registrationStatus.observe(this, Observer { statusMessage ->
            Toast.makeText(this, statusMessage, Toast.LENGTH_SHORT).show()
            if (statusMessage == "Registration successful!") {
                // Redirect to Login Activity after registration is complete
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()  // Ensures that the user cannot go back to the registration screen
            }
        })
        binding.txtLogin.setOnClickListener{
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()

        }

    }
}
