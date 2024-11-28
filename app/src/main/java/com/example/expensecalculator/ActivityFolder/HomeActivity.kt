package com.example.expensecalculator.ActivityFolder


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.expensecalculator.databinding.ActivityHomeBinding


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Set click listeners using View Binding
        binding.btnAdd.setOnClickListener {
            startActivity(Intent(this, AddExpenseActivity::class.java))
        }

        binding.btnView.setOnClickListener {
            startActivity(Intent(this, ViewExpenseActivity::class.java))
        }

        binding.btnLogOut.setOnClickListener{
            val intent=Intent(this,RegistrationActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

}