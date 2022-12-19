package com.example.marcellinapizzas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter(
    private val listOfToppings: ArrayList<String>
) : RecyclerView.Adapter<Adapter.NewViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewViewHolder { // inflate the layout for task_rv_item.xml
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.rv_item,
            parent, false
        )

        return NewViewHolder(itemView, mListener)
    }

    class NewViewHolder(itemView: View, listener: onItemClickListener) :
        RecyclerView.ViewHolder(itemView) { // initialize views
        val tvTopping: TextView = itemView.findViewById(R.id.tvTopping)

        init {
            itemView.setOnClickListener() {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onBindViewHolder(
        holder: NewViewHolder,
        position: Int
    ) { // populate views with data from list
        holder.tvTopping.text = listOfToppings[position]
    }

    override fun getItemCount(): Int { // this function is required
        return listOfToppings.size
    }

    // click listener

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }
}
