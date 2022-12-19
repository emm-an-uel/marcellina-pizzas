package com.example.marcellinapizzas

import androidx.lifecycle.ViewModel

class ViewModelMain: ViewModel() {
    lateinit var listOfPizzas: ArrayList<Pizza>

    fun createListOfPizzas() {
        listOfPizzas = arrayListOf()

        listOfPizzas.apply {
            add(Pizza("Marcellina Special", listOf("Ham", "Capsicum", "Mushroom", "Olives", "Anchovies")))
            add(Pizza("Margherita", listOf("Tomato", "Basil", "Oregano")))
            add(Pizza("Ham & Pineapple", listOf("Ham", "Pineapple")))
            add(Pizza("Americana", listOf("Salami")))
            add(Pizza("BBQ Chicken", listOf("Chicken", "BBQ Sauce", "Mushroom", "Onion")))
            add(Pizza("Siciliana", listOf("Salami", "Mushroom", "Capsicum", "Anchovies", "Olives")))
            add(Pizza("Slimmers", listOf("Mushroom", "Capsicum", "Onion", "Olives", "Tomato", "Herbs")))
            add(Pizza("Vegetarian", listOf("Mushroom", "Capsicum", "Pineapple", "Onion", "Olives", "Basil")))
            add(Pizza("Mexicana", listOf("Salami", "Capsicum", "Onion", "Olives", "Chili")))
            add(Pizza("Chicken Mexicana", listOf("Chicken", "Capsicum", "Onion", "Olives", "Garlic", "Herbs")))
            add(Pizza("Chicken Hawaiian", listOf("Chicken", "Pineapple")))
            add(Pizza("Seafood", listOf("Prawns", "Oysters", "Anchovies")))
            add(Pizza("Marinara", listOf("Prawns", "Garlic", "Parsley")))
            add(Pizza("Meat Lovers", listOf("Ham", "Salami", "Bacon")))
            add(Pizza("The Lot", listOf("Ham", "Salami", "Mushroom", "Capsicum", "Olives", "Pineapple", "Onion")))
            add(Pizza("Calabrese", listOf("Salami", "Olives", "Tomato", "Herbs")))
            add(Pizza("Roo & Ditts", listOf("Ham", "Bacon", "Salami", "Chicken", "BBQ Sauce")))
            add(Pizza("Calzone Rustico", listOf("Ham", "Mushroom", "Capsicum", "Salami", "Olives")))
        }
    }

    @JvmName("getListOfPizzas1")
    fun getListOfPizzas(): ArrayList<Pizza> {
        return listOfPizzas
    }
}