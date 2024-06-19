package com.example.diplom

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class Fragment_dish_user(private val mealtime_id: Int?) : Fragment(), Adapter_dish.DishClickListener {
    private lateinit var supabaseInstance: Supabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var dishAdapter: Adapter_dish
    private var data: List<db_Dish> = emptyList()
    private lateinit var loadingView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_dish_user, container, false)
        supabaseInstance = Supabase()

        loadingView = rootView.findViewById(R.id.loading_page)

        recyclerView = rootView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val sharedViewModel = ViewModelProvider(requireActivity())[Filters::class.java]

        sharedViewModel.filterData.observe(viewLifecycleOwner) { filterData ->
            Log.d("ObserveTest", filterData.toString())
            filterRecyclerView(filterData)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            getDish(sharedViewModel.filterData.value!!)
            dishAdapter = Adapter_dish(data, this@Fragment_dish_user)
            recyclerView.adapter = dishAdapter
            loadingView.visibility = View.GONE
        }

        return rootView
    }

    suspend fun getDish(filterData: db_FilterData) {
        try {
            data = supabaseInstance.getUserDishWithFilter(filterData)
            Log.d("dataTest", data.toString())
        } catch (e: Exception) {
            Log.e("supabase", "Ошибка при получении данных", e)
        }
    }

    private fun filterRecyclerView(filterData: db_FilterData) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val filteredData = supabaseInstance.getUserDishWithFilter(filterData)
                dishAdapter.updateData(filteredData)
                loadingView.visibility = View.GONE
            } catch (ce: CancellationException) {
                // ignore
            } catch (e: Exception) {
                Log.e("supabase", "Ошибка при получении данных", e)
            }
        }
    }

    override fun onDishClick(type_id: Int) {
        Log.d("supabase", "Click!")
        val fragment = Fragment_dish_detail(type_id, mealtime_id)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}