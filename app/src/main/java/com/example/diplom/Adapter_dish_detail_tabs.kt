package com.example.diplom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class Adapter_dish_detail_tabs(
    private var data: List<db_Recipes_and_ingredients>,
    private var clickListener: IngredientClickListener
) : RecyclerView.Adapter<Adapter_dish_detail_tabs.ViewHolder>() {

    fun setIngredientClickListener(listener: Adapter_dish_detail_tabs.IngredientClickListener) {
        clickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_dish_detail_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = data[position]
        holder.bind(ingredient)

        holder.itemView.setOnClickListener {
            clickListener.onIngredientClick(ingredient.ingredients!!.id) // Вызов метода интерфейса при клике
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(ingredient: db_Recipes_and_ingredients) {
            val nameTextView = itemView.findViewById<TextView>(R.id.nameTextView1)
            val descTextView = itemView.findViewById<TextView>(R.id.nameTextView2)
            val priceTextView = itemView.findViewById<TextView>(R.id.nameTextView3)
            val imageView = itemView.findViewById<ImageView>(R.id.imageView)
            nameTextView.text = ingredient.ingredients!!.name
            priceTextView.text = String.format("%.2f руб", ingredient.ingredients.price / 100 * ingredient.count)
            descTextView.text = "${ingredient.count} ${ingredient.measurement!!.name}"
            Picasso.get().load(ingredient.ingredients!!.img).into(imageView)
        }
    }

    fun setData(newData: List<db_Recipes_and_ingredients>) {
        data = newData
    }

    interface IngredientClickListener {
        fun onIngredientClick(ingredient_id: Int)
    }
}