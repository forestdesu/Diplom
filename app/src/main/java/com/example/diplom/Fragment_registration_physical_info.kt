package com.example.diplom

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.textfield.TextInputLayout
import kotlinx.datetime.LocalDate
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Fragment_registration_physical_info : Fragment() {
    private lateinit var nextButton: Button
    private lateinit var viewPager: ViewPager2

    private val viewModel: RegistrationViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_registration_physical_info, container, false)

        nextButton = view.findViewById(R.id.nextButton)
        viewPager = requireActivity().findViewById(R.id.viewPager)

        val dateOfBirthLayout = view.findViewById<TextInputLayout>(R.id.textInputLayout3)
        val weightLayout = view.findViewById<TextInputLayout>(R.id.textInputLayout2)
        val heightLayout = view.findViewById<TextInputLayout>(R.id.textInputLayout1)
        val maleRadioButton = view.findViewById<RadioButton>(R.id.radioButton)


        nextButton.setOnClickListener {
            if (validateFields(dateOfBirthLayout, weightLayout, heightLayout)) {
                saveDataToViewModel(dateOfBirthLayout, weightLayout, heightLayout, !maleRadioButton.isChecked)
                viewPager.currentItem += 1
            } else {
                Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
            }
        }

        dateOfBirthLayout.editText?.setOnClickListener {
            showDatePickerDialog(dateOfBirthLayout)
        }

        return view
    }

    private fun validateFields(vararg fields: TextInputLayout): Boolean {
        return fields.all { validateField(it) }
    }

    private fun validateField(layout: TextInputLayout): Boolean {
        val editText = layout.editText!!
        val text = editText.text.toString()

        return when (layout.id) {
            R.id.textInputLayout2 -> validateWeight(layout, text)
            R.id.textInputLayout1 -> validateHeight(layout, text)
            R.id.textInputLayout3 -> validateDate(layout, text)
            else -> text.isNotEmpty().also {
                layout.error = if (it) null else "Это поле необходимо заполнить"
            }
        }
    }

    private fun validateWeight(layout: TextInputLayout, text: String): Boolean {
        return if (text.isEmpty()) {
            layout.error = "Введите ваш вес"
            false
        } else {
            val weight = text.toIntOrNull()
            if (weight == null || weight !in 30..150) {
                layout.error = "Введите вес от 30 до 150 кг"
                false
            } else {
                layout.error = null
                true
            }
        }
    }

    private fun validateHeight(layout: TextInputLayout, text: String): Boolean {
        return if (text.isEmpty()) {
            layout.error = "Введите ваш рост"
            false
        } else {
            val height = text.toIntOrNull()
            if (height == null || height !in 50..250) {
                layout.error = "Введите рост от 50 до 250 см"
                false
            } else {
                layout.error = null
                true
            }
        }
    }

    private fun validateDate(layout: TextInputLayout, text: String): Boolean {
        return if (text.isEmpty()) {
            layout.error = "Выберите дату рождения"
            false
        } else {
            layout.error = null
            true
        }
    }

    private fun saveDataToViewModel(dateOfBirthLayout: TextInputLayout, weightLayout: TextInputLayout, heightLayout: TextInputLayout, isMale: Boolean) {
        try {
            viewModel.physicalInfo = RegistrationViewModel.PhysicalInfo(
                dateOfBirth = LocalDate.parse(dateOfBirthLayout.editText?.text.toString()),
                weight = weightLayout.editText?.text.toString().toDouble(),
                height = heightLayout.editText?.text.toString().toInt(),
                isMale = isMale
            )
        } catch (e: Exception) {
            Log.e("Supabase", "error: ", e)
        }
    }

    private fun showDatePickerDialog(dateOfBirthLayout: TextInputLayout) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                val calendar = Calendar.getInstance()
                calendar.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date = dateFormat.format(calendar.time)
                dateOfBirthLayout.editText?.setText(date)
            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.show()
    }
}
