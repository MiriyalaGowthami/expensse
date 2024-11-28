package com.example.expensecalculator.ActivityFolder

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.expensecalculator.databinding.ActivityAddExpenseBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar


class AddExpenseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddExpenseBinding
    private val db = FirebaseFirestore.getInstance() // Initialize Firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate layout with Data Binding
        binding = ActivityAddExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the spinner adapter for expense types
        val expenseTypes = listOf("Travel", "Food", "Shopping", "Groceries", "Vegetables", "Fruits")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, expenseTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.itemSpinner.adapter = adapter

        binding.edtDate.setOnClickListener {
            showDatePickerDialog()
        }

        // Handle Submit Button click using View Binding
        binding.buttonSubmit.setOnClickListener {
            val expenseType = binding.itemSpinner.selectedItem.toString()
            val date = binding.edtDate.text.toString()
            val amount = binding.edtAmount.text.toString()

            if (date.isEmpty() || amount.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Create an expense object
                val expense = hashMapOf(
                    "expenseType" to expenseType,
                    "date" to date,
                    "amount" to amount.toDoubleOrNull() // Ensure amount is a Double
                )

                // Save expense to Firestore
                db.collection("expenses")
                    .add(expense)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Expense Added", Toast.LENGTH_LONG).show()
                        clearInputFields()
                        finish() // Go back to HomeActivity
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to add expense", Toast.LENGTH_SHORT).show()
                    }
                    .addOnCompleteListener {
                        finish() // Ensure finish is after the success or failure
                    }
            }
        }
        binding.btnBack.setOnClickListener{
            val intent= Intent(this,HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Update the EditText with the selected date
                binding.edtDate.setText("$selectedDay/${selectedMonth + 1}/$selectedYear")
            },
            year, month, day
        )

        datePickerDialog.show()
    }
    private fun clearInputFields() {
        runOnUiThread {
            binding.edtDate.setText("")
            binding.edtAmount.setText("")
            binding.itemSpinner.setSelection(0)
        }
    }
}
