package com.example.diplom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter_main(
    private var mealtime_id: Int,
    private var data: MutableList<db_Users_and_eating>,
    private val onDishClick: (Int, Int) -> Unit
) : RecyclerView.Adapter<Adapter_main.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_main_dish, parent, false)
        return ViewHolder(view, onDishClick, mealtime_id)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eat = data[position]
        holder.bind(eat, position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(
        itemView: View,
        private val onDishClick: (Int, Int) -> Unit,
        private var mealtime_id: Int
    ) : RecyclerView.ViewHolder(itemView) {
        private lateinit var currentItem: db_Users_and_eating

        fun bind(eat: db_Users_and_eating, position: Int) {
            currentItem = eat
            val nameTextView = itemView.findViewById<TextView>(R.id.nameTextView)
            val descTextView = itemView.findViewById<TextView>(R.id.descTextView)
            val countTextView = itemView.findViewById<TextView>(R.id.countTextView)
            val circleView = itemView.findViewById<View>(R.id.circleView)
            nameTextView.text = eat.dish!!.name
            descTextView.text = eat.recipes!!.name
            countTextView.text = "${eat.count} ${eat.dish!!.measurement!!.name}"
            circleView.background.setTint(App.colors[if (position > 10) 10 else position])

            itemView.setOnClickListener {
                onDishClick(eat.dish!!.id, eat.id_mealtime)
            }
        }

        fun getItem(): db_Users_and_eating {
            return currentItem
        }
    }

    fun updateData(newData: List<db_Users_and_eating>) {
        data = newData.toMutableList()
        notifyDataSetChanged()
    }

    fun removeItem(position: Int): db_Users_and_eating {
        val oldItem = data[position]
        data.removeAt(position)
        notifyItemRemoved(position)
        return oldItem
    }

    fun updateItem(position: Int, newItem: db_Users_and_eating) {
        data[position] = newItem
        notifyItemChanged(position)
    }

    fun addItem(position: Int, item: db_Users_and_eating) {
        data.add(position, item)
        notifyItemInserted(position)
    }
}