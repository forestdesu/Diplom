package com.example.diplom

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.launch

class AppLauncher : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val isUserLoggedIn = checkLogin()
            val targetActivity = if (isUserLoggedIn) Main::class.java else Login::class.java
            startActivity(Intent(this@AppLauncher, targetActivity))
            finish()
        }
    }

    suspend fun checkLogin(): Boolean {
        val token = App.supabaseInstance.getToken(this)

        return if (!token.isNullOrEmpty()) {
            try {
                supabase.auth.retrieveUser(token)
                supabase.auth.refreshCurrentSession()
                App.supabaseInstance.saveToken(this)
                App.sessionData = App.supabaseInstance.getUserDetail()
                App.getFilters()
                true // Пользователь авторизован
            } catch (e: Exception) {
                Log.e("supabaseAuthLogin", "Error: ", e)
                false // Ошибка при авторизации
            }
        } else {
            false // Токен пустой
        }
    }
}