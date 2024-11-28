package com.example.expensecalculator.ActivityFolder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensecalculator.DataFolders.Expense
import com.example.expensecalculator.ExpenseAdapter
import com.example.expensecalculator.databinding.ActivityViewExpenseBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject


class ViewExpenseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewExpenseBinding
    private val db = FirebaseFirestore.getInstance() // Initialize Firestore
    private val expenses = mutableListOf<Expense>() // List to store expenses

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up Data Binding
        binding = ActivityViewExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize RecyclerView
        binding.recyclerExpenses.layoutManager = LinearLayoutManager(this)
        val adapter = ExpenseAdapter(expenses)
        binding.recyclerExpenses.adapter = adapter

        // Fetch expenses from Firestore
        db.collection("expenses")
            .get()
            .addOnSuccessListener { documents ->
                expenses.clear() // Clear the list before adding new items
                for (document in documents) {
                    val expense = document.toObject<Expense>()
                    expenses.add(expense)
                }
                adapter.notifyDataSetChanged() // Notify adapter of data change
            }
            .addOnFailureListener { exception ->
                Log.e("ViewExpenseActivity", "Error getting documents: ", exception)
            }
        binding.btnBack.setOnClickListener{
            val intent= Intent(this,HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

}
