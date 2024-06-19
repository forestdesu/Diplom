package com.example.diplom

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class Fragment_ingredients(private val Type_id: Int) : Fragment(), Adapter_ingredients.IngredientClickListener {

    private lateinit var progressBar: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var editText: TextInputEditText
    private lateinit var ingredientAdapter: Adapter_ingredients
    private lateinit var data: List<db_Ingredients>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_ingredients, container, false)

//        val categoryTextView = rootView.findViewById<TextView>(R.id.category_ingredient)
//        categoryTextView.text = Type

        progressBar = rootView.findViewById(R.id.loading_page)
        recyclerView = rootView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        editText = rootView.findViewById(R.id.editTextText)
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterData(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        fetchData()

        return rootView
    }

    private fun fetchData() {
        lifecycleScope.launch {
            try {
                val supabaseInstance = Supabase()
                data = supabaseInstance.getIngredients(Type_id)
                ingredientAdapter = Adapter_ingredients(data, this@Fragment_ingredients)
                recyclerView.adapter = ingredientAdapter
            } catch (e: Exception) {
                Log.e("supabase", "Ошибка при получении данных", e)
                Toast.makeText(requireContext(), "Ошибка при получении данных", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE // Выключение прелоадера после загрузки данных
            }
        }
    }

    private fun filterData(query: String) {
        val filteredData = data.filter { ingredient ->
            ingredient.name.contains(query, ignoreCase = true)
        }
        ingredientAdapter.updateData(filteredData)
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
