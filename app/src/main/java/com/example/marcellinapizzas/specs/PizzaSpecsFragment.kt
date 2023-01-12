package com.example.marcellinapizzas.specs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.marcellinapizzas.MainViewModel

class PizzaSpecsFragment : Fragment() {

    private var _binding: com.example.marcellinapizzas.databinding.FragmentPizzaSpecsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel
    private lateinit var pizzaSpecs: List<PizzaSpecifications>
    private lateinit var rvAdapter: RVAdapterSpecs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        pizzaSpecs = viewModel.getPizzaSpecs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = com.example.marcellinapizzas.databinding.FragmentPizzaSpecsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createRV()
    }

    private fun createRV() {
        rvAdapter = RVAdapterSpecs(pizzaSpecs)
        val mLayoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvSpecs.apply {
            adapter = rvAdapter
            layoutManager = mLayoutManager
        }
    }
}