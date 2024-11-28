package com.example.expensecalculator.ActivityFolder

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.expensecalculator.LoginViewModel
import com.example.expensecalculator.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     

        // Inflate the layout with ActivityLoginBinding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Bind ViewModel to layout and set lifecycle owner for LiveData observation
        binding.viewModel = loginViewModel
        binding.lifecycleOwner = this


        // Observe login status from ViewModel to provide user feedback
        loginViewModel.loginStatus.observe(this, Observer { statusMessage ->
            Toast.makeText(this, statusMessage, Toast.LENGTH_SHORT).show()
            if (statusMessage == "Login successful!") {
                // Redirect to Home Activity if login is successful
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
        binding.txtRegister.setOnClickListener{
            val intent=Intent(this,RegistrationActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}
