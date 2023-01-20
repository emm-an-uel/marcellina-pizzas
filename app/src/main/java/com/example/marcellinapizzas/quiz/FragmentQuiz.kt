package com.example.marcellinapizzas.quiz

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.example.marcellinapizzas.MainViewModel
import com.example.marcellinapizzas.Pizza
import com.example.marcellinapizzas.R
import com.example.marcellinapizzas.databinding.FragmentQuizBinding
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
    lateinit var viewModel: MainViewModel
    lateinit var tvPizza: TextView
    lateinit var tvScore: TextView
    lateinit var tvHighScore: TextView
    var score = 0
    var highScore = 0
    var correct = true
    var numQuestions = 0
    lateinit var correctToppings: List<String>
    var answersChecked = false

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // define viewModel and get list of pizzas
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        listOfPizzas = viewModel.getListOfPizzas(true)

        score = 0
        numQuestions = 0
        highScore = viewModel.getHighScore()

        // create listOfColors for rv items
        listOfColors = arrayListOf()
        createListOfColors()

        // create lists and maps of toppings
        listOfToppings = resources.getStringArray(R.array.listOfToppings).toMutableList()
        rvMapOfToppings = mutableMapOf()
        setupRVMapOfToppings()
        userMapOfToppings = mutableMapOf()
        setupUserMapOfToppings()

        rv = binding.rvToppings
        createRV()

        tvScore = binding.tvScore
        tvScore.text = getString(R.string.current_score, 0, 0)

        tvHighScore = binding.tvHighScore
        tvHighScore.text = getString(R.string.high_score, highScore)

        // load first pizza
        tvPizza = binding.tvPizza
        nextPizza()

        // buttons
        btnCheck = binding.btnCheck
        btnCheck.text = getString(R.string.check_answers)
        btnCheck.setOnClickListener {
            if (!answersChecked) { // if answers have not been checked
                checkAnswers()
                updateScore()
                btnCheck.text = getString(R.string.next_pizza)
                answersChecked = true

            } else { // if answers have been checked
                nextPizza()
                btnCheck.text = getString(R.string.check_answers)
                answersChecked = false
            }
        }

        btnRetry = binding.btnRetry
        btnRetry.setOnClickListener {
            if (btnCheck.visibility == View.GONE) { // if user has answered all questions
                restartQuiz()

            } else { // if user has not answered all questions
                // give the option to retry everything or just retry that pizza
                createDialog()
            }
        }
    }

    private fun createDialog() {
        val builder = AlertDialog.Builder(requireContext()).create()
        if (builder.window != null) {
            builder.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        val view = layoutInflater.inflate(R.layout.dialog_custom, null, false)
        val tvPrimary: TextView = view.findViewById(R.id.tvPrimary)
        val tvSecondary: TextView = view.findViewById(R.id.tvSecondary)
        val btnRetryPizza: Button = view.findViewById(R.id.btnCancel)
        val btnRestartQuiz: Button = view.findViewById(R.id.btnConfirm)

        tvPrimary.text = "Which would you like to do?"
        tvSecondary.visibility = View.GONE

        btnRetryPizza.apply {
            text = "Retry Pizza"
            setTextColor(ContextCompat.getColor(requireContext(), R.color.light_blue))
        }
        btnRestartQuiz.apply {
            text = "Restart Quiz"
            setTextColor(ContextCompat.getColor(requireContext(), R.color.light_blue))
        }

        btnRetryPizza.setOnClickListener {
            retryPizza()
            builder.dismiss()
        }
        btnRestartQuiz.setOnClickListener {
            restartQuiz()
            builder.dismiss()
        }
        builder.apply {
            setView(view)
            setCanceledOnTouchOutside(true)
            show()
        }
    }

    private fun retryPizza() {
        if (answersChecked && correct) {
            score-- // remove the point that the user got from the pizza that they're retrying
        }
        numQuestions--
        tvScore.text = getString(R.string.current_score, score, numQuestions)
        btnCheck.text = "Check Answers"
        answersChecked = false
        nextPizza()
    }

    private fun restartQuiz() {
        // reset quiz data
        highScore = viewModel.getHighScore()
        score = 0
        correct = true
        answersChecked = false
        numQuestions = 0

        // re-shuffle listOfPizzas
        listOfPizzas = viewModel.getListOfPizzas(true)

        // reset tvScore and buttons
        tvScore.text = getString(R.string.current_score, 0, 0)
        btnRetry.text = "retry"
        btnCheck.apply {
            visibility = View.VISIBLE
            text = "Check Answers"
        }

        // start quiz
        nextPizza()
        createSnackbar()
    }

    private fun createSnackbar() {
        val snack = Snackbar.make(rv, "", Snackbar.LENGTH_SHORT)
        val customSnackView = layoutInflater.inflate(R.layout.snackbar_custom, null, false)
        if (snack.view.background != null) {
            snack.view.setBackgroundColor(ContextCompat.getColor(requireContext(), com.google.android.material.R.color.mtrl_btn_transparent_bg_color))
        }

        val snackbarLayout: Snackbar.SnackbarLayout = snack.view as Snackbar.SnackbarLayout
        snackbarLayout.setPadding(5, 0, 5, 15)
        snackbarLayout.addView(customSnackView)

        val tvPrimary: TextView = customSnackView.findViewById(R.id.tvPrimary)
        val btnAction: Button = customSnackView.findViewById(R.id.btnAction)

        tvPrimary.text = "Quiz restarted"
        btnAction.visibility = View.GONE // btnAction is not needed here

        snack.show()
    }

    private fun updateScore() {
        tvScore.text = getString(R.string.current_score, score, numQuestions)
    }

    private fun setupRVMapOfToppings() {
        for (topping in listOfToppings) {
            rvMapOfToppings[topping] = 0 // default rv item bg color
        }
    }

    private fun createListOfColors() {
        listOfColors.apply {
            add(getColor(requireContext(),com.google.android.material.R.attr.colorPrimaryContainer)) // default rv item bg color
            add(ContextCompat.getColor(requireContext(), R.color.yellow)) // present, user not selected
            add(ContextCompat.getColor(requireContext(), R.color.red)) // not present, user selected
            add(ContextCompat.getColor(requireContext(), R.color.green)) // present, user selected
            add(ContextCompat.getColor(requireContext(), R.color.light_blue)) // answers unchecked, user selected
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
                if (userMapOfToppings[topping] == true) { // if currently selected
                    userMapOfToppings[topping] = false // set to unselected
                    rvMapOfToppings[topping] = 0

                } else { // if currently not selected
                    userMapOfToppings[topping] = true // set to selected
                    rvMapOfToppings[topping] = 4
                }
                rvAdapter.notifyDataSetChanged()
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
            checkHighScore()
        }
    }

    private fun checkHighScore() {
        if (score > highScore) {
            viewModel.updateHighScore(score)
            tvHighScore.text = getString(R.string.high_score, highScore)
            Snackbar.make(tvScore, "You beat your previous high score of $highScore", Snackbar.LENGTH_SHORT).show()
        }
    }
}