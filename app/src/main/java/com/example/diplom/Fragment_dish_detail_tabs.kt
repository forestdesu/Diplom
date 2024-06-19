package com.example.diplom

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class Fragment_dish_detail_tabs(private val recipes: db_Recipes) : Fragment(), Adapter_dish_detail_tabs.IngredientClickListener {
    private lateinit var originalData: List<db_Recipes_and_ingredients>
    private lateinit var data: List<db_Recipes_and_ingredients>
    data class TotalPrice(val allPrice: Double, val table1: Double, val table2: Double, val table3: Double, val table4: Double)
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var ingredientAdapter: Adapter_dish_detail_tabs
    private lateinit var RecipePrice: TextView
    private lateinit var RecipeDesc: TextView
    private lateinit var RecipeTable1: TextView
    private lateinit var RecipeTable2: TextView
    private lateinit var RecipeTable3: TextView
    private lateinit var RecipeTable4: TextView
    private lateinit var coefText: TextView
    private lateinit var content: ScrollView
    private var coef: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_dish_detail_adapter, container, false)
        content = rootView.findViewById(R.id.content)
        progressBar = rootView.findViewById(R.id.progressBar)
        recyclerView = rootView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        RecipePrice = rootView.findViewById(R.id.Price)
        RecipeDesc = rootView.findViewById(R.id.Desc)
        RecipeTable1 = rootView.findViewById(R.id.table1)
        RecipeTable2 = rootView.findViewById(R.id.table2)
        RecipeTable3 = rootView.findViewById(R.id.table3)
        RecipeTable4 = rootView.findViewById(R.id.table4)
        coefText = rootView.findViewById(R.id.textView2)

        val buttonAdd = rootView.findViewById<Button>(R.id.button1)
        val buttonSubtract = rootView.findViewById<Button>(R.id.button2)

        buttonAdd.setOnClickListener {
            adjustNutritionAndPrice(1)
        }

        buttonSubtract.setOnClickListener {
            adjustNutritionAndPrice(-1)
        }

        lifecycleScope.launch {
            try {
                val supabaseInstance = Supabase()
                originalData = supabaseInstance.getRecipesIngredients(recipes.id)
                Log.d("supabase", originalData.toString())
                LoadData()
                ingredientAdapter = Adapter_dish_detail_tabs(data, this@Fragment_dish_detail_tabs)
                recyclerView.adapter = ingredientAdapter
            } catch (e: Exception) {
                Log.e("supabase", "Ошибка при получении данных", e)
                Toast.makeText(requireContext(), "Ошибка при получении данных", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
                content.visibility = View.VISIBLE
            }
        }

        return rootView
    }

    override fun onResume() {
        super.onResume()
        recyclerView.requestLayout()
    }

    fun LoadData() {
        data = originalData.map { it.copy(ingredients = it.ingredients!!.copy()) }

        coefText.text = "Пищевая ценность (${coef * 100} г)"
        RecipePrice.text = "Цена: %.2f ₽".format(recipes.price * coef)
        RecipeDesc.text = recipes.desc
        RecipeTable1.text = "%.2f".format(recipes.table1 * coef)
        RecipeTable2.text = "%.2f".format(recipes.table2 * coef)
        RecipeTable3.text = "%.2f".format(recipes.table3 * coef)
        RecipeTable4.text = "%.2f".format(recipes.table4 * coef)
    }

    private fun adjustNutritionAndPrice(change: Int) {

        if ((coef > 1) or (change > 0)) {
            coef += change
        }
        LoadData()

        for (item in data) {
            item.count *= coef
            item.ingredients!!.price *= coef
            item.ingredients!!.table1 *= coef
            item.ingredients!!.table2 *= coef
            item.ingredients!!.table3 *= coef
            item.ingredients!!.table4 *= coef
        }

        RefreshRecyclerView()
    }

    private fun RefreshRecyclerView(){
        ingredientAdapter.setData(data)
        ingredientAdapter.notifyDataSetChanged()
    }

    override fun onIngredientClick(ingredient_id: Int) {
        Log.d("supabase", "Click!")
        val fragment = Fragment_ingredients_detail(ingredient_id)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}