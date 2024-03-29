package com.example.marcellinapizzas.solutions

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.marcellinapizzas.Pizza
import com.example.marcellinapizzas.R

class RVAdapterSolutions(
    private var listOfPizzas: List<Pizza>,
    private val mapOfToppings: Map<String, String> // <Topping, Category>
) : RecyclerView.Adapter<RVAdapterSolutions.NewViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewViewHolder { // inflate the layout for task_rv_item.xml
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.rv_item_solutions,
            parent, false
        )

        return NewViewHolder(itemView, mListener)
    }

    class NewViewHolder(itemView: View, listener: onItemClickListener) :
        RecyclerView.ViewHolder(itemView) { // initialize views
        val tvPizza: TextView = itemView.findViewById(R.id.tvPizza)
        val layoutMain: LinearLayout = itemView.findViewById(R.id.layoutMain)
        val layoutMeats: LinearLayout = itemView.findViewById(R.id.layoutMeats)
        val layoutVeg: LinearLayout = itemView.findViewById(R.id.layoutVeg)
        val layoutOthers: LinearLayout = itemView.findViewById(R.id.layoutOthers)
        val context: Context = tvPizza.context

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)

                // color change based on selected/unselected
                val colorSelected: ColorStateList = ColorStateList.valueOf(ContextCompat.getColor(context,
                    R.color.light_blue
                ))
                val colorUnselected: ColorStateList = ColorStateList.valueOf(getColor(context, com.google.android.material.R.attr.colorPrimaryContainer))

                if (itemView.backgroundTintList == colorSelected) { // item selected -> unselected
                    itemView.backgroundTintList = colorUnselected
                    tvPizza.setTextColor(getColor(tvPizza.context, com.google.android.material.R.attr.colorOnPrimarySurface))
                    layoutMain.visibility = View.GONE

                } else { // item unselected -> selected
                    itemView.backgroundTintList = colorSelected
                    tvPizza.setTextColor(ContextCompat.getColor(tvPizza.context, R.color.black))
                    layoutMain.visibility = View.VISIBLE
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
    }

    override fun onBindViewHolder(
        holder: NewViewHolder,
        position: Int
    ) {
        holder.setIsRecyclable(false)

        val pizza = listOfPizzas[position]
        holder.tvPizza.text = pizza.name
        val context = holder.context

        // populate linear layouts - classify topping by category
        val toppings = pizza.toppings
        for (topping in toppings) {
            val textView = TextView(context)
            textView.apply {
                text = topping
                setTextColor(ContextCompat.getColor(holder.context, R.color.black))
            }

            if (mapOfToppings[topping] == "Meats") {
                holder.layoutMeats.addView(textView)

            } else if (mapOfToppings[topping] == "Veg") {
                holder.layoutVeg.addView(textView)

            } else {
                holder.layoutOthers.addView(textView)
            }
        }

        // bottom margins only for last item
        if (position == listOfPizzas.size-1) {
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(dpToPx(context, 5F), dpToPx(context, 10F), dpToPx(context, 5F), dpToPx(context, 10F))
            holder.itemView.layoutParams = layoutParams
        }
    }

    private fun dpToPx(context: Context, dp: Float): Int {
        val r: Resources = context.resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            r.displayMetrics
        ).toInt()
    }

    override fun getItemCount(): Int { // this function is required
        return listOfPizzas.size
    }

    // prevent view recycling so toppings lists don't get messed up
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // filtering (searching)
    fun filterList(listOfFilteredPizzas: ArrayList<Pizza>) {
        listOfPizzas = listOfFilteredPizzas
        notifyDataSetChanged()
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
