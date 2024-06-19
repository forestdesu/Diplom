package com.example.diplom

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class Fragment_ingredients_types(private val category_id: Int) : Fragment(), Adapter_ingredients_types.IngredientClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var editText: TextInputEditText
    private lateinit var progressBar: ProgressBar
    private lateinit var ingredientAdapter: Adapter_ingredients_types
    private lateinit var data: List<db_Ingredients_types>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_ingredients_types, container, false)

        recyclerView = rootView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        editText = rootView.findViewById(R.id.editTextText)
        progressBar = rootView.findViewById(R.id.progressBar)

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
                data = supabaseInstance.getTypeIngredients(category_id)
                ingredientAdapter = Adapter_ingredients_types(data, this@Fragment_ingredients_types)
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

    override fun onIngredientClick(type_id: Int) {
        Log.d("supabase", "Click!")
        val fragment = Fragment_ingredients(type_id)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}