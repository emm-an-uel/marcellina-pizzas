package com.example.marcellinapizzas

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class FragmentQuiz : Fragment() {
    lateinit var rv: RecyclerView
    lateinit var rvAdapter: Adapter
    lateinit var btnCheck: Button
    lateinit var listOfToppings: List<String>
    lateinit var rvMapOfToppings: MutableMap<String, Int>
    lateinit var userMapOfToppings: MutableMap<String, Boolean>
    lateinit var listOfPizzas: List<Pizza>
    lateinit var listOfColors: ArrayList<Int>
    lateinit var viewModel: ViewModelMain
    lateinit var tvPizza: TextView
    lateinit var correctToppings: List<String>
    var answersChecked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // create listOfColors for rv items
        listOfColors = arrayListOf()
        createListOfColors()

        // define viewModel and get list of pizzas
        viewModel = ViewModelProvider(requireActivity())[ViewModelMain::class.java]
        listOfPizzas = viewModel.getListOfPizzas()

        // create lists and maps of toppings
        listOfToppings = resources.getStringArray(R.array.listOfToppings).toMutableList()
        rvMapOfToppings = mutableMapOf()
        setupRVMapOfToppings()
        userMapOfToppings = mutableMapOf()
        setupUserMapOfToppings()

        rv = view.findViewById(R.id.rvToppings)
        createRV()

        // load first pizza
        tvPizza = view.findViewById(R.id.tvPizza)
        nextPizza()

        // buttons
        btnCheck = view.findViewById(R.id.btnCheck)
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

    private fun setupRVMapOfToppings() {
        for (topping in listOfToppings) {
            rvMapOfToppings[topping] = 0 // default rv item bg color
        }
    }

    private fun createListOfColors() {
        listOfColors.apply {
            add(getColor(requireContext(), com.google.android.material.R.attr.colorPrimaryContainer)) // default rv item bg color
            add(ContextCompat.getColor(requireContext(), R.color.yellow)) // present, user not selected
            add(ContextCompat.getColor(requireContext(), R.color.red)) // not present, user selected
            add(ContextCompat.getColor(requireContext(), R.color.green)) // present, user selected
        }
    }

    private fun getColor(context: Context, colorResId: Int): Int {
        val typedValue = TypedValue()
        val typedArray = context.obtainStyledAttributes(typedValue.data, intArrayOf(colorResId))
        val color = typedArray.getColor(0, 0)
        typedArray.recycle()
        return color
    }

    private fun setupUserMapOfToppings() {
        for (topping in listOfToppings) {
            userMapOfToppings[topping] = false
            // will be set to 'true' if user selects, 'false' if user deselects
        }
    }

    private fun nextPizza() {
        val randomGenerator = Random(System.currentTimeMillis())
        val index = randomGenerator.nextInt(listOfPizzas.size)
        tvPizza.text = listOfPizzas[index].name
        correctToppings = listOfPizzas[index].toppings

        for (topping in rvMapOfToppings.keys) {
            rvMapOfToppings[topping] = 0 // reset to default bg color
        }

        for (topping in userMapOfToppings.keys) {
            userMapOfToppings[topping] = false // reset to unselected
        }

        rvAdapter.notifyDataSetChanged()
    }

    private fun createRV() {
        val layoutManager = GridLayoutManager(requireContext(), 2)
        rvAdapter = Adapter(rvMapOfToppings, listOfToppings, listOfColors)
        rvAdapter.setOnItemClickListener(object: Adapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val topping = listOfToppings[position]
                userMapOfToppings[topping] = userMapOfToppings[topping] != true // if currently true, set to false; vice versa
            }
        })
        rv.adapter = rvAdapter
        rv.layoutManager = layoutManager
    }

    private fun checkAnswers() {
        // check against correctToppings: List
        for (topping in userMapOfToppings.keys) {
            if (userMapOfToppings[topping] == true) { // user selected
                if (correctToppings.contains(topping)) { // present, user selected (green)
                    rvMapOfToppings[topping] = 3
                } else { // not present, user selected (red)
                    rvMapOfToppings[topping] = 2
                }
            } else { // user not selected
                if (correctToppings.contains(topping)) { // present, user not selected (yellow)
                    rvMapOfToppings[topping] = 1
                }
            }
        }

        rvAdapter.notifyDataSetChanged()
    }
}