package com.example.marcellinapizzas.specs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.marcellinapizzas.MainActivity
import com.example.marcellinapizzas.R

class RVAdapterSpecs(
    private val pizzaSpecs: List<PizzaSpecifications>
): RecyclerView.Adapter<RVAdapterSpecs.NewViewHolder>() {
    class NewViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvSize: TextView = itemView.findViewById(R.id.tvSize)
        val tvWeight: TextView = itemView.findViewById(R.id.tvWeight)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.pizza_specs_item,
            parent, false
        )
        return NewViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NewViewHolder, position: Int) {
        val context = holder.tvName.context
        val pizzaSpecifications = pizzaSpecs[position]
        holder.tvName.text = pizzaSpecifications.name
        holder.tvSize.text = (context as MainActivity).resources.getString(R.string.pizza_size, pizzaSpecifications.size.toString())
        holder.tvWeight.text = (context as MainActivity).resources.getString(R.string.pizza_weight, pizzaSpecifications.weight.toString())
    }

    override fun getItemCount(): Int {
        return pizzaSpecs.size
    }
}