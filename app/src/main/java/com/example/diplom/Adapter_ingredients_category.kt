package com.example.diplom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class Adapter_ingredients_category(
    private val itemList: List<db_Ingredients_category>,
    private var clickListener: IngredientClickListener // Добавлено поле для слушателя кликов
) : RecyclerView.Adapter<Adapter_ingredients_category.ViewHolder>() {

    fun setIngredientClickListener(listener: IngredientClickListener) {
        clickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_ingredients_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)

        holder.itemView.setOnClickListener {
            clickListener.onIngredientClick(item.id) // Вызов метода интерфейса при клике
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageButton)
        private val textView: TextView = itemView.findViewById(R.id.textView)

        fun bind(item: db_Ingredients_category) {
            Picasso.get().load(item.img).into(imageView)

            textView.text = item.name
        }
    }

    interface IngredientClickListener {
        fun onIngredientClick(category: Int)
    }
}
