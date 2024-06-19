package com.example.diplom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class Adapter_ingredients(
    private var data: List<db_Ingredients>,
    private var clickListener: IngredientClickListener
) : RecyclerView.Adapter<Adapter_ingredients.ViewHolder>() {

    fun setIngredientClickListener(listener: Adapter_ingredients.IngredientClickListener) {
        clickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_ingredients, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = data[position]
        holder.bind(ingredient)

        holder.itemView.setOnClickListener {
            clickListener.onIngredientClick(ingredient.id) // Вызов метода интерфейса при клике
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(ingredient: db_Ingredients) {
            val nameTextView = itemView.findViewById<TextView>(R.id.nameTextView)
            val table1TextView = itemView.findViewById<TextView>(R.id.table1)
            val table2TextView = itemView.findViewById<TextView>(R.id.table2)
            val table3TextView = itemView.findViewById<TextView>(R.id.table3)
            val table4TextView = itemView.findViewById<TextView>(R.id.table4)
            val imageView = itemView.findViewById<ImageView>(R.id.imageView)
            Picasso.get().load(ingredient.img).into(imageView)
            nameTextView.text = ingredient.name
            table1TextView.text = ingredient.table1.toString()
            table2TextView.text = ingredient.table2.toString()
            table3TextView.text = ingredient.table3.toString()
            table4TextView.text = ingredient.table4.toString()

        }
    }

    fun updateData(newData: List<db_Ingredients>) {
        data = newData
        notifyDataSetChanged()
    }

    interface IngredientClickListener {
        fun onIngredientClick(ingredient_id: Int)
    }
}