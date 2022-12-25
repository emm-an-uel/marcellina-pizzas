package com.example.marcellinapizzas

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class FragmentQuiz : Fragment() {
    lateinit var rv: RecyclerView
    lateinit var rvAdapter: RVAdapterQuiz
    lateinit var btnCheck: Button
    lateinit var btnRetry: Button
    lateinit var listOfToppings: List<String>
    lateinit var rvMapOfToppings: MutableMap<String, Int>
    lateinit var userMapOfToppings: MutableMap<String, Boolean>
    lateinit var listOfPizzas: List<Pizza>
    lateinit var listOfColors: ArrayList<Int>
    lateinit var viewModel: ViewModelMain
    lateinit var tvPizza: TextView
    lateinit var tvScore: TextView
    var score = 0
    var correct = true
    var numQuestions = 0
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

        score = 0
        numQuestions = 0

        // create listOfColors for rv items
        listOfColors = arrayListOf()
        createListOfColors()

        // define viewModel and get list of pizzas
        viewModel = ViewModelProvider(requireActivity())[ViewModelMain::class.java]
        listOfPizzas = viewModel.getListOfPizzas(true)

        // create lists and maps of toppings
        listOfToppings = resources.getStringArray(R.array.listOfToppings).toMutableList()
        rvMapOfToppings = mutableMapOf()
        setupRVMapOfToppings()
        userMapOfToppings = mutableMapOf()
        setupUserMapOfToppings()

        rv = view.findViewById(R.id.rvToppings)
        createRV()

        tvScore = view.findViewById(R.id.tvScore)
        tvScore.text = "Score: 0/0"

        // load first pizza
        tvPizza = view.findViewById(R.id.tvPizza)
        nextPizza()

        // buttons
        btnCheck = view.findViewById(R.id.btnCheck)
        btnCheck.text = "check answers"
        btnCheck.setOnClickListener {
            if (!answersChecked) { // if answers have not been checked
                checkAnswers()
                updateScore()
                btnCheck.text = "next pizza"
                answersChecked = true

            } else { // if answers have been checked
                nextPizza()
                btnCheck.text = "check answers"
                answersChecked = false
            }
        }

        btnRetry = view.findViewById(R.id.btnRetry)
        btnRetry.setOnClickListener {
            if (btnCheck.visibility == View.GONE) { // if user has answered all questions
                restartQuiz()

            } else { // if user has not answered all questions
                // give the option to retry everything or just retry that pizza
                val alertDialog: AlertDialog = requireContext().let {
                    val builder = AlertDialog.Builder(it)
                    builder.apply {
                        setPositiveButton("Restart Quiz"
                        ) { _, _ ->
                            restartQuiz()
                        }
                        setNegativeButton("Retry Pizza"
                        ) { _, _ ->
                            retryPizza()
                        }
                    }
                    builder.setMessage("Which would you like to do?")
                    builder.create()
                }
                alertDialog.show()
            }
        }
    }

    private fun retryPizza() {
        if (answersChecked && correct) {
            score-- // remove the point that the user got from the pizza that they're retrying
        }
        numQuestions--
        tvScore.text = "Score: $score/$numQuestions"
        btnCheck.text = "Check Answers"
        answersChecked = false
        nextPizza()
    }

    private fun restartQuiz() {
        score = 0
        correct = true
        numQuestions = 0
        listOfPizzas = viewModel.getListOfPizzas(true)
        tvScore.text = "Score: 0/0"
        btnRetry.text = "retry"
        btnCheck.visibility = View.VISIBLE
        nextPizza()

        Snackbar.make(rv, "Quiz restarted", Snackbar.LENGTH_SHORT).show()
    }

    private fun updateScore() {
        tvScore.text = "Score: $score/$numQuestions"
    }

    private fun setupRVMapOfToppings() {
        for (topping in listOfToppings) {
            rvMapOfToppings[topping] = 0 // default rv item bg color
        }
    }

    private fun createListOfColors() {
        listOfColors.apply {
            add(
                getColor(
                    requireContext(),
                    com.google.android.material.R.attr.colorPrimaryContainer
                )
            ) // default rv item bg color
            add(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.yellow
                )
            ) // present, user not selected
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
        if (numQuestions < listOfPizzas.size) { // only when user hasn't gone through all pizzas
            val pizza = listOfPizzas[numQuestions]
            tvPizza.text = pizza.name
            correctToppings = pizza.toppings

            // reset bg color and 'unselected'
            for (topping in rvMapOfToppings.keys) {
                rvMapOfToppings[topping] = 0 // resets to default bg color
            }
            for (topping in userMapOfToppings.keys) {
                userMapOfToppings[topping] = false // resets to 'unselected'
            }

            numQuestions++
            rvAdapter.notifyDataSetChanged()
        }
    }

    private fun createRV() {
        val layoutManager = GridLayoutManager(requireContext(), 2)
        rvAdapter = RVAdapterQuiz(rvMapOfToppings, listOfToppings, listOfColors)
        rvAdapter.setOnItemClickListener(object : RVAdapterQuiz.onItemClickListener {
            override fun onItemClick(position: Int) {
                val topping = listOfToppings[position]
                userMapOfToppings[topping] =
                    userMapOfToppings[topping] != true // if currently true, set to false; vice versa
            }
        })
        rv.adapter = rvAdapter
        rv.layoutManager = layoutManager
    }

    private fun checkAnswers() {
        correct = true
        // check against correctToppings: List
        for (topping in userMapOfToppings.keys) {
            if (userMapOfToppings[topping] == true) { // user selected
                if (correctToppings.contains(topping)) { // present, user selected (green)
                    rvMapOfToppings[topping] = 3

                } else { // not present, user selected (red)
                    rvMapOfToppings[topping] = 2
                    if (correct) { // makes a note that user got this pizza wrong
                        correct = false
                    }
                }
            } else { // user not selected
                if (correctToppings.contains(topping)) { // present, user not selected (yellow)
                    rvMapOfToppings[topping] = 1
                    if (correct) { // makes a note that user got this pizza wrong
                        correct = false
                    }
                }
            }
        }
        rvAdapter.notifyDataSetChanged()

        if (correct) {
            score++ // adds 1 to score if user got pizza correct
        }

        if (numQuestions == listOfPizzas.size) { // if user has gone through all pizzas
            btnCheck.visibility = View.GONE
            btnRetry.text = "restart"
        }
    }
}