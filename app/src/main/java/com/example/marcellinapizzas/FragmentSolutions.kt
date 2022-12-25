package com.example.marcellinapizzas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.marcellinapizzas.databinding.FragmentSolutionsBinding

class FragmentSolutions : Fragment() {

    private var _binding: FragmentSolutionsBinding? = null

    private val binding get() = _binding!!

    lateinit var viewModel: ViewModelMain
    lateinit var rvAdapter: RVAdapterSolutions
    lateinit var rv: RecyclerView
    lateinit var listOfPizzas: List<Pizza>
    lateinit var mapOfToppings: Map<String, String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSolutionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get listOfPizzas
        viewModel = ViewModelProvider(requireActivity())[ViewModelMain::class.java]
        listOfPizzas = viewModel.getListOfPizzas(false)

        // get mapOfToppings
        mapOfToppings = viewModel.getMapOfToppings()

        // setup rv
        rv = binding.rvPizzas
        setupRV()
    }

    private fun setupRV() {
        rvAdapter = RVAdapterSolutions(listOfPizzas, mapOfToppings)
        rvAdapter.setOnItemClickListener(object : RVAdapterSolutions.onItemClickListener {
            override fun onItemClick(position: Int) {
                // TODO: this
            }
        })
        rv.adapter = rvAdapter
    }
}