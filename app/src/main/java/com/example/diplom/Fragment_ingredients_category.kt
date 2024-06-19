package com.example.diplom

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class Fragment_ingredients_category: Fragment(), Adapter_ingredients_category.IngredientClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ingredients_category, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2) // 2 - количество элементов на строку
            //recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val progressBar = view.findViewById<View>(R.id.loading_page)

        lifecycleScope.launch {
            try {
                val supabaseInstance = Supabase()
                val data = supabaseInstance.getCategoryIngredients()
                Log.d("supabase", data.toString())
                val adapter = Adapter_ingredients_category(data, this@Fragment_ingredients_category)
                recyclerView.adapter = adapter
            } catch (e: Exception) {
                Log.e("supabase", "Ошибка при получении данных", e)
                Toast.makeText(requireContext(), "Ошибка при получении данных", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE // Выключение прелоадера после загрузки данных
            }
        }

        return view
    }

    override fun onIngredientClick(category_id: Int) {
        // Здесь можно выполнить переход на фрагмент ingredients, передав необходимые данные
        val fragment = Fragment_ingredients_types(category_id)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}