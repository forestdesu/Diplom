package com.example.diplom

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class Adapter_dish_detail_tabs_pager(private val activity: FragmentActivity, private val data: List<db_Recipes_and_dish>) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return data.size
    }

    override fun createFragment(position: Int): Fragment {
        return Fragment_dish_detail_tabs(data[position].recipes)
    }
}
