package com.example.diplom

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewpager2.widget.ViewPager2
import me.relex.circleindicator.CircleIndicator3

class Register : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: Adapter_registration
    private lateinit var indicator: CircleIndicator3

    private val viewModel: RegistrationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_register)

        viewPager = findViewById(R.id.viewPager)
        indicator = findViewById(R.id.indicator)
        adapter = Adapter_registration(this)

        adapter.addFragment(Fragment_registration_contact_info())
        adapter.addFragment(Fragment_registration_physical_info())
        adapter.addFragment(Fragment_registration_allergy())

        viewPager.adapter = adapter
        viewPager.setUserInputEnabled(false);
        indicator.setViewPager(viewPager)

    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            super.onBackPressed()
        } else {
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }

    suspend fun SingIn() {
        try {
            val supabaseInstance = Supabase()
            supabaseInstance.signUpNewUser(this, viewModel.contactInfo!!, viewModel.physicalInfo!!, viewModel.allergyInfo!!)
            val intent = Intent(this, Main::class.java)
            App.sessionData = supabaseInstance.getUserDetail()
            App.getFilters()
            startActivity(intent)
        } catch (e: Exception) {
            Log.e("supabase", "Ошибка при получении данных", e)
            Toast.makeText(this, "Ошибка при создании учетной записи", Toast.LENGTH_SHORT).show()
            restartActivity()
        }
    }

    private fun restartActivity() {
        val intent = Intent(this, Register::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}

