package com.example.diplom

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomnavigation.BottomNavigationView


class Main : AppCompatActivity() {
    private lateinit var navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, Fragment_main())
            .commit()

        navView = findViewById(R.id.bottom_navigation)
        navView.menu.findItem(R.id.navMain)?.isChecked = true

        navView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navMain -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, Fragment_main())
                        .addToBackStack(null)
                        .commit()
                    true
                }
                R.id.navIngredients -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, Fragment_ingredients_category())
                        .addToBackStack(null)
                        .commit()
                    true
                }
                R.id.navReceipt -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, Fragment_dish(null))
                        .addToBackStack(null)
                        .commit()
                    true
                }
                R.id.navReport -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, Fragment_dish_select())
                        .addToBackStack(null)
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
}