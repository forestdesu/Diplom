package com.example.diplom

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat

class App : Application() {
    companion object {
        var sessionData: db_Users? = null
        var dbFilters: List<db_Dish_category>? = null
        lateinit var supabaseInstance: Supabase
        lateinit var colors: List<Int>

        fun initializeColors(context: Context) {
            colors = listOf(
                ContextCompat.getColor(context, R.color.progress_color_1),
                ContextCompat.getColor(context, R.color.progress_color_2),
                ContextCompat.getColor(context, R.color.progress_color_3),
                ContextCompat.getColor(context, R.color.progress_color_4),
                ContextCompat.getColor(context, R.color.progress_color_5),
                ContextCompat.getColor(context, R.color.progress_color_6),
                ContextCompat.getColor(context, R.color.progress_color_7),
                ContextCompat.getColor(context, R.color.progress_color_8),
                ContextCompat.getColor(context, R.color.progress_color_9),
                ContextCompat.getColor(context, R.color.progress_color_10)
            )
        }


        suspend fun getFilters() {
            try {
                dbFilters = supabaseInstance.getDishCategory()
            } catch (e: Exception) {
                Log.e("supabase", "Ошибка при получении данных", e)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        supabaseInstance = Supabase()
        initializeColors(this)
    }
}
