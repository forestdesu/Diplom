package com.example.diplom

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch


class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_login)

        val userEmail = findViewById<TextInputEditText>(R.id.textInputEditText1)
        val userPassword = findViewById<TextInputEditText>(R.id.textInputEditText2)

        val loginButton = findViewById<Button>(R.id.button1)
        val registerButton = findViewById<Button>(R.id.button2)

        val supabaseInstance = Supabase()

        loginButton.setOnClickListener {
            lifecycleScope.launch {
                val signInResult = supabaseInstance.signIn(this@Login, userEmail.text.toString(), userPassword.text.toString())

                if (signInResult == null) {
                    App.sessionData = supabaseInstance.getUserDetail()
                    App.getFilters()
                    SingIn()
                }
            }
        }

        registerButton.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }

    fun SingIn() {
        val intent = Intent(this, Main::class.java)
        startActivity(intent)
    }
}