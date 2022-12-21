package com.example.marcellinapizzas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: ViewModelMain

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // instantiate viewModel and create list of pizzas
        viewModel = ViewModelProvider(this)[ViewModelMain::class.java]
        viewModel.createListOfPizzas()

        // test
        supportFragmentManager.beginTransaction().replace(R.id.nav_host, FragmentQuiz()).commit()
    }
}