package com.example.marcellinapizzas

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.beust.klaxon.JsonReader
import com.beust.klaxon.Klaxon
import java.io.File
import java.io.StringReader

class ViewModelMain(val app: Application): AndroidViewModel(app) {
    lateinit var listOfPizzas: ArrayList<Pizza>
    lateinit var mapOfToppings: MutableMap<String, String>
    private var highScore = 0

    fun loadHighScore() {
        val file = File(app.filesDir, "fileHighScore")
        if (file.exists()) {
            val fileJson: String = file.readText()
            JsonReader(StringReader(fileJson)).use { reader ->
                reader.beginArray {
                    while (reader.hasNext()) {
                        highScore = Klaxon().parse<Int>(reader)!!
                    }
                }
            }
        }
    }

    @JvmName("getHighScore1")
    fun getHighScore(): Int {
        return highScore
    }

    fun updateHighScore(newScore: Int) {
        highScore = newScore

        // save locally
        val updatedScore = Klaxon().toJsonString(highScore)
        app.openFileOutput("fileHighScore", Context.MODE_PRIVATE).use {
            it.write(updatedScore.toByteArray())
        }
    }

    fun createMapOfToppings() {
        val listOfToppings: List<String> = app.resources.getStringArray(R.array.listOfToppings).toList()

        mapOfToppings = mutableMapOf()
        // classify each topping
        for (n in listOfToppings.indices) {
            val topping = listOfToppings[n]
            if (n < 7) { // note: list has been separated into the following sections in strings.xml
                mapOfToppings[topping] = "Meats"
            } else if (n < 13) {
                mapOfToppings[topping] = "Veg"
            } else {
                mapOfToppings[topping] = "Others"
            }
        }
    }

    @JvmName("getMapOfToppings1")
    fun getMapOfToppings(): MutableMap<String, String> {
        return mapOfToppings
    }

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
    fun getListOfPizzas(shuffle: Boolean): List<Pizza> {
        return if (shuffle) {
            listOfPizzas.shuffled()
        } else {
            listOfPizzas
        }
    }
}