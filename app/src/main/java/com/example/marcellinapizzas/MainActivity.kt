package com.example.marcellinapizzas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var rv: RecyclerView
    lateinit var rvAdapter: Adapter
    lateinit var btnCheck: Button
    lateinit var listOfToppings: List<String>
    lateinit var mapOfToppings: MutableMap<String, Boolean>
    lateinit var listOfPizzas: List<Pizza>
    lateinit var viewModel: ViewModelMain
    lateinit var tvPizza: TextView
    lateinit var correctToppings: List<String>
    var answersChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // instantiate viewModel and create list of pizzas
        viewModel = ViewModelProvider(this)[ViewModelMain::class.java]
        viewModel.createListOfPizzas()

        listOfToppings = resources.getStringArray(R.array.listOfToppings).toMutableList()
        listOfPizzas = viewModel.getListOfPizzas()
        mapOfToppings = mutableMapOf()
        setupMapOfToppings()

        rv = findViewById(R.id.rvToppings)
        createRV()

        // load first pizza
        tvPizza = findViewById(R.id.tvPizza)
        nextPizza()

        // buttons
        btnCheck = findViewById(R.id.btnCheck)
        btnCheck.text = "check answers"

        btnCheck.setOnClickListener {
            if (!answersChecked) { // if answers have not been checked
                checkAnswers()
                btnCheck.text = "next pizza"
                answersChecked = true

            } else { // if answers have been checked
                nextPizza()
                btnCheck.text = "check answers"
                answersChecked = false
            }
        }
    }

    private fun setupMapOfToppings() {
        for (topping in listOfToppings) {
            mapOfToppings[topping] = false
            // will be set to 'true' if user selects, 'false' if user deselects
        }
    }

    private fun nextPizza() {
        val randomGenerator = Random(System.currentTimeMillis())
        val index = randomGenerator.nextInt(listOfPizzas.size)
        tvPizza.text = listOfPizzas[index].name
        correctToppings = listOfPizzas[index].toppings
    }

    private fun createRV() {
        val layoutManager = GridLayoutManager(this, 2)
        rvAdapter = Adapter(listOfToppings)
        rvAdapter.setOnItemClickListener(object: Adapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val topping = listOfToppings[position]
                mapOfToppings[topping] = mapOfToppings[topping] != true // if currently true, set to false; vice versa
            }
        })
        rv.adapter = rvAdapter
        rv.layoutManager = layoutManager
    }

    private fun checkAnswers() {
        val userAnswers = arrayListOf<String>()
        for (topping in mapOfToppings.keys) {
            if (mapOfToppings[topping] == true) {
                userAnswers.add(topping)
            }
        }
    }
}