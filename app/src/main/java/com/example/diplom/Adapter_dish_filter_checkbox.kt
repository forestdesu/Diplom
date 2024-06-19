package com.example.diplom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView

class Adapter_dish_filter_checkbox (
    private var data: List<db_Dish_category>,
    private val categoryArr: MutableList<Int>
) : RecyclerView.Adapter<Adapter_dish_filter_checkbox.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_filter, parent, false)
        return ViewHolder(view, categoryArr)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = data[position]
        holder.bind(category)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(itemView: View, private val categoryArr: MutableList<Int>) : RecyclerView.ViewHolder(itemView) {
        fun bind(category: db_Dish_category) {
            val categoryCheckBox = itemView.findViewById<CheckBox>(R.id.checkbox)
            categoryCheckBox.text = category.name
            categoryCheckBox.isChecked = categoryArr.contains(category.id)

            categoryCheckBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    if (!categoryArr.contains(category.id)) {
                        categoryArr.add(category.id)
                    }
                } else {
                    categoryArr.remove(category.id)
                }
            }
        }
    }

    fun updateData(newData: List<db_Dish_category>) {
        data = newData
        notifyDataSetChanged()
    }

    fun clearAllChecks() {
        categoryArr.clear()
        notifyDataSetChanged()
    }
}
