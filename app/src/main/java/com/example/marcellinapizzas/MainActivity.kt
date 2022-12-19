package com.example.marcellinapizzas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    lateinit var rv: RecyclerView
    lateinit var adapter: Adapter
    lateinit var btnCheck: Button
    lateinit var btnNext: Button
    lateinit var listOfToppings: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listOfToppings = resources.getStringArray(R.array.listOfToppings).toMutableList() as ArrayList<String>



        rv = findViewById(R.id.rvToppings)
        createRV()
    }

    private fun createRV() {

    }
}