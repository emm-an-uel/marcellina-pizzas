package com.example.marcellinapizzas

import android.content.Context
import android.content.res.ColorStateList
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class RVAdapterQuiz(
    private val mapOfToppings: Map<String, Int>,
    private val listOfToppings: List<String>,
    private val listOfColors: List<Int>
) : RecyclerView.Adapter<RVAdapterQuiz.NewViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewViewHolder { // inflate the layout for task_rv_item.xml
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.rv_item_quiz,
            parent, false
        )

        return NewViewHolder(itemView, mListener)
    }

    class NewViewHolder(itemView: View, listener: onItemClickListener) :
        RecyclerView.ViewHolder(itemView) { // initialize views
        val tvTopping: TextView = itemView.findViewById(R.id.tvTopping)
        val context = tvTopping.context

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onBindViewHolder(
        holder: NewViewHolder,
        position: Int
    ) {
        holder.setIsRecyclable(false)

        val topping = listOfToppings[position]
        holder.tvTopping.text = topping

        // bg color
        val colorIndex = mapOfToppings[topping]!!
        holder.itemView.backgroundTintList = ColorStateList.valueOf(listOfColors[colorIndex])

        // text color - for readability
        when (colorIndex) {
            1 -> { // yellow bg - black text
                holder.tvTopping.setTextColor(ContextCompat.getColor(holder.context, R.color.black))
            }
            2 -> { // red bg - white text
                holder.tvTopping.setTextColor(ContextCompat.getColor(holder.context, R.color.white))
            }
            else -> { // reset to default text color
                holder.tvTopping.setTextColor(getColor(holder.context, com.google.android.material.R.attr.colorOnPrimarySurface))
            }
        }
    }

    private fun getColor(context: Context, colorResId: Int): Int {
        val typedValue = TypedValue()
        val typedArray = context.obtainStyledAttributes(typedValue.data, intArrayOf(colorResId))
        val color = typedArray.getColor(0, 0)
        typedArray.recycle()
        return color
    }

    override fun getItemCount(): Int { // this function is required
        return listOfToppings.size
    }

    // prevent view recycling
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
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
