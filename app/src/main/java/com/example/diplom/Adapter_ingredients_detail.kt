package com.example.diplom

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter_ingredients_detail (private val context: Context) : RecyclerView.Adapter<Adapter_ingredients_detail.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_ingredients_types, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Bind data to views
        holder.imageView.setImageResource(R.drawable.milk)
        holder.textView.text = "Молоко обычное"
    }

    override fun getItemCount(): Int {
        // Return the number of items you want to display
        return 5 // for example, display 5 items
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textView: TextView = itemView.findViewById(R.id.textView)
    }
}