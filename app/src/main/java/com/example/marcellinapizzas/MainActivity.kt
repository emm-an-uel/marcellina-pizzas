package com.example.marcellinapizzas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    lateinit var rv: RecyclerView
    lateinit var adapter: Adapter
    lateinit var btnCheck: Button
    lateinit var btnNext: Button
    lateinit var listOfToppings: List<String>
    lateinit var listOfPizzas: List<Pizza>
    lateinit var viewModel: ViewModelMain

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // instantiate viewModel and create list of pizzas
        viewModel = ViewModelProvider(this)[ViewModelMain::class.java]
        viewModel.createListOfPizzas()

        listOfToppings = resources.getStringArray(R.array.listOfToppings).toMutableList()
        listOfPizzas = viewModel.getListOfPizzas()

        rv = findViewById(R.id.rvToppings)
        createRV()
    }

    private fun createRV() {
        adapter = Adapter(listOfToppings)
        adapter.setOnItemClickListener(object: Adapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                // TODO: save as selected/unselected topping
            }
        })
        rv.adapter = adapter
    }
}