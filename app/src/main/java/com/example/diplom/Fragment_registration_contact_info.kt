package com.example.diplom

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

class Fragment_registration_contact_info : Fragment() {
    private lateinit var nextButton: Button
    private lateinit var viewPager: ViewPager2

    private val viewModel: RegistrationViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_registration_contact_info, container, false)

        nextButton = view.findViewById(R.id.nextButton)
        viewPager = requireActivity().findViewById(R.id.viewPager)

        val userName = view.findViewById<TextInputLayout>(R.id.textInputLayout0)
        val userEmail = view.findViewById<TextInputLayout>(R.id.textInputLayout1)
        val userPassword1 = view.findViewById<TextInputLayout>(R.id.textInputLayout2)
        val userPassword2 = view.findViewById<TextInputLayout>(R.id.textInputLayout3)

        nextButton.setOnClickListener {
            if (validateFields(userName, userEmail, userPassword1, userPassword2) && checkPasswords(userPassword1, userPassword2)) {
                lifecycleScope.launch {
                    handleNextButtonClick(userEmail, userName, userPassword1, userPassword2)
                }
            }
        }

        return view
    }

    private fun validateFields(vararg fields: TextInputLayout): Boolean {
        return fields.all { validateField(it) }
    }

    private fun validateField(layout: TextInputLayout): Boolean {
        val editText = layout.editText!!
        return if (editText.text.isNullOrBlank()) {
            layout.error = "Это поле необходимо заполнить"
            false
        } else {
            layout.error = null
            true
        }
    }

    private fun checkPasswords(pass1: TextInputLayout, pass2: TextInputLayout): Boolean {
        return if (pass1.editText!!.text.toString() != pass2.editText!!.text.toString()) {
            pass2.error = "Пароли должны совпадать"
            false
        } else {
            pass2.error = null
            true
        }
    }

    private fun TextInputLayout.getText(): String {
        return this.editText!!.text.toString().trimIndent()
    }

    private suspend fun handleNextButtonClick(userEmail: TextInputLayout, userName: TextInputLayout, userPassword1: TextInputLayout, userPassword2: TextInputLayout) {
        try {
            val supabaseInstance = Supabase()
            if (supabaseInstance.checkEmail(userEmail.getText())) {
                userEmail.error = "Такая почта уже используется!"
                return
            }
            viewPager.currentItem += 1
            viewModel.contactInfo = RegistrationViewModel.ContactInfo(
                name = userName.getText(),
                email = userEmail.getText(),
                password = userPassword1.getText()
            )
        } catch (e: Exception) {
            Log.e("supabaseAuth", "Error: ", e)
        }
    }
}