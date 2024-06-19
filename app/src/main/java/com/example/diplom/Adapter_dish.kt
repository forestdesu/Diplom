package com.example.diplom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class Adapter_dish(
    private var data: List<db_Dish>,
    private var clickListener: DishClickListener
) : RecyclerView.Adapter<Adapter_dish.ViewHolder>() {

    fun setDishClickListener(listener: Adapter_dish.DishClickListener) {
        clickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_dish, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dish = data[position]
        holder.bind(dish)

        holder.itemView.setOnClickListener {
            clickListener.onDishClick(dish.id) // Вызов метода интерфейса при клике
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(dish: db_Dish) {
            val nameTextView = itemView.findViewById<TextView>(R.id.nameTextView)
            val timeTextView = itemView.findViewById<TextView>(R.id.timeTextView)
            val imageView = itemView.findViewById<ImageView>(R.id.imageView)
            val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
            val time = LocalTime.parse(dish.time, formatter)
            nameTextView.text = dish.name
            timeTextView.text = "${time.minute} мин"
            Picasso.get().load(dish.img).into(imageView)
        }
    }

    fun updateData(newData: List<db_Dish>) {
        data = newData
        notifyDataSetChanged()
    }

    interface DishClickListener {
        fun onDishClick(dish_id: Int)
    }
}