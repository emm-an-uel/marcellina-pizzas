package com.example.marcellinapizzas

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
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

        // menu - for filtering pizzas
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.search_menu, menu)

                val searchItem: MenuItem = menu.findItem(R.id.actionSearch)
                val searchView: SearchView = searchItem.actionView as SearchView

                searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(p0: String?): Boolean {
                        return false
                    }
                    override fun onQueryTextChange(p0: String?): Boolean {
                        filter(p0)
                        return false
                    }
                    private fun filter(p0: String?) {
                        val filteredList: ArrayList<Pizza> = arrayListOf()
                        if (p0 != null) {
                            for (item in listOfPizzas) {
                                if (item.name.contains(p0, true)) {
                                    filteredList.add(item)
                                }
                            }
                        }
                        rvAdapter.filterList(filteredList)
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.actionSearch -> {
                        true
                    } else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun getColor(context: Context, colorResId: Int): Int {
        val typedValue = TypedValue()
        val typedArray = context.obtainStyledAttributes(typedValue.data, intArrayOf(colorResId))
        val color = typedArray.getColor(0, 0)
        typedArray.recycle()
        return color
    }


        private fun setupRV() {
        rvAdapter = RVAdapterSolutions(listOfPizzas, mapOfToppings)
        rvAdapter.setOnItemClickListener(object : RVAdapterSolutions.onItemClickListener {
            override fun onItemClick(position: Int) {
                // do nothing here (clickListener stuff is handled by RVAdapterSolutions)
            }
        })
        rv.adapter = rvAdapter
    }
}