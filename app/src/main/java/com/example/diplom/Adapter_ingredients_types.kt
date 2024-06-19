package com.example.diplom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class Adapter_ingredients_types (
    private var data: List<db_Ingredients_types>,
    private var clickListener: IngredientClickListener
) : RecyclerView.Adapter<Adapter_ingredients_types.ViewHolder>() {

    fun setIngredientClickListener(listener: Adapter_ingredients_types.IngredientClickListener) {
        clickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_ingredients_types, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val type = data[position]
        holder.bind(type)

        holder.itemView.setOnClickListener {
            clickListener.onIngredientClick(type.id) // Вызов метода интерфейса при клике
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(type: db_Ingredients_types) {
            val nameTextView = itemView.findViewById<TextView>(R.id.textView)
            val imageView = itemView.findViewById<ImageView>(R.id.imageView)
            nameTextView.text = type.name
            Picasso.get().load(type.img).into(imageView)
        }
    }

    fun updateData(newData: List<db_Ingredients_types>) {
        data = newData
        notifyDataSetChanged()
    }

    interface IngredientClickListener {
        fun onIngredientClick(type_id: Int)
    }
}