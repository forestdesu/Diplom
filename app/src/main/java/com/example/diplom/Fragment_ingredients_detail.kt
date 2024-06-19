package com.example.diplom

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class Fragment_ingredients_detail(private val ingredient_id: Int) : Fragment() {

    private lateinit var dataDb: db_Ingredient_detail

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_ingredients_detail, container, false)
        val content = rootView.findViewById<ScrollView>(R.id.content)
        val progressBar = rootView.findViewById<View>(R.id.loading_page)

        lifecycleScope.launch {
            try {
                val supabaseInstance = Supabase()
                dataDb = supabaseInstance.getCurrentIngredient(ingredient_id)
                Log.d("supabase", dataDb.toString())
                LoadData(rootView)
            } catch (e: Exception) {
                Log.e("supabase", "Ошибка при получении данных", e)
                Toast.makeText(requireContext(), "Ошибка при получении данных", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
                content.visibility = View.VISIBLE
            }
        }

        val categoryTextView = rootView.findViewById<TextView>(R.id.Name)
        categoryTextView.text = "test"

        return rootView
    }

    fun LoadData(rootView: View) {
        val IngredientImg = rootView.findViewById<ImageView>(R.id.imageView)
        val IngredientName = rootView.findViewById<TextView>(R.id.Name)
        val IngredientPrice = rootView.findViewById<TextView>(R.id.Price)
        val IngredientDesc = rootView.findViewById<TextView>(R.id.Desc)
        val IngredientTable1 = rootView.findViewById<TextView>(R.id.table1)
        val IngredientTable2 = rootView.findViewById<TextView>(R.id.table2)
        val IngredientTable3 = rootView.findViewById<TextView>(R.id.table3)
        val IngredientTable4 = rootView.findViewById<TextView>(R.id.table4)
        val IngredientMoreDetail1 = rootView.findViewById<TextView>(R.id.MoreDetail1)
        val IngredientMoreDetail2 = rootView.findViewById<TextView>(R.id.MoreDetail2)
        val IngredientMoreDetail3 = rootView.findViewById<TextView>(R.id.MoreDetail3)
        val IngredientMoreDetail4 = rootView.findViewById<TextView>(R.id.MoreDetail4)

        IngredientName.text = dataDb.name
        IngredientPrice.text = "Цена: ${dataDb.price} ₽"
        IngredientDesc.text = dataDb.detail
        IngredientTable1.text = dataDb.table1.toString()
        IngredientTable2.text = dataDb.table2.toString()
        IngredientTable3.text = dataDb.table3.toString()
        IngredientTable4.text = dataDb.table4.toString()
        IngredientMoreDetail1.text = dataDb.ingredients_category.name.toString()
//        IngredientMoreDetail2.text = dataDb.
//        IngredientMoreDetail3.text = dataDb.
        val days = dataDb.shelf_life
        var daysText = ""
        if (days == 1) {
            daysText = "${dataDb.shelf_life} день"
        } else if (days in 2..4) {
            daysText = "${dataDb.shelf_life} дня"
        } else {
            daysText = "${dataDb.shelf_life} дней"
        }
        IngredientMoreDetail4.text = daysText
        Picasso.get().load(dataDb.img).into(IngredientImg)
    }
}