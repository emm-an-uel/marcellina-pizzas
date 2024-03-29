package com.example.marcellinapizzas

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.beust.klaxon.JsonReader
import com.beust.klaxon.Klaxon
import com.example.marcellinapizzas.specs.PizzaSpecifications
import java.io.File
import java.io.StringReader

class MainViewModel(val app: Application): AndroidViewModel(app) {
    lateinit var listOfPizzas: ArrayList<Pizza>
    lateinit var mapOfToppings: MutableMap<String, String>
    lateinit var mapOfData: MutableMap<String, Int>
    private var highScore = 0
    private lateinit var pizzaSpecs: ArrayList<PizzaSpecifications>

    fun createPizzaSpecs() {
        pizzaSpecs = arrayListOf()
        for (n in 0 until 4) {
            var name = ""
            var weight = 0
            when (n) {
                0 -> {
                    name = "Regular"
                    weight = 180
                }
                1 -> {
                    name = "Large"
                    weight = 320
                }
                2 -> {
                    name = "Family"
                    weight = 500
                }
                3 -> {
                    name = "Party"
                    weight = 0
                }
            }
            val size = 9 + (3*n) // 3 inches difference between each pizza, so (9 + 3n) inches
            pizzaSpecs.add(PizzaSpecifications(name, weight, size))
        }
    }

    fun getPizzaSpecs(): List<PizzaSpecifications> {
        return pizzaSpecs
    }

    fun loadData() {
        mapOfData = mutableMapOf()
        val file = File(app.filesDir, "fileData")
        if (file.exists()) {
            val fileJson: String = file.readText()
            JsonReader(StringReader(fileJson)).use { reader ->
                reader.beginArray {
                    while (reader.hasNext()) {
                        val d = Klaxon().parse<Data>(reader)!!
                        mapOfData[d.name] = d.value
                    }
                }
            }
        }
    }

    @JvmName("getHighScore1")
    fun getHighScore(): Int {
        if (mapOfData.containsKey("highScore")) {
            highScore = mapOfData["highScore"]!!
        } // note: highScore = 0 otherwise (initialized in line 15 above)

        return highScore
    }

    fun updateHighScore(newScore: Int) {
        highScore = newScore
        mapOfData["highScore"] = highScore

        // save locally
        val list = arrayListOf<Data>()
        for ((k, v) in mapOfData) {
            val data = Data(k, v)
            list.add(data)
        }

        val updatedData = Klaxon().toJsonString(list)
        app.openFileOutput("fileData", Context.MODE_PRIVATE).use {
            it.write(updatedData.toByteArray())
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