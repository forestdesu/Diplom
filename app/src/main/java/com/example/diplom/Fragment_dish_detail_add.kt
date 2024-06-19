package com.example.diplom

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import kotlinx.datetime.toKotlinLocalDate
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Fragment_dish_detail_add(
        private val data: db_Dish_add_fragment,
        private val adapterMain: Adapter_main?
    ) : BottomSheetDialogFragment() {
    private val supabaseInstance: Supabase = App.supabaseInstance
    private var currentMealtime: Int? = data.mealtime_id
    private var currentRecipe: Int = data.recipe_position
    private lateinit var btn: Button
    private lateinit var progress: LinearLayout

    private lateinit var mealtimeFieldLayout: TextInputLayout
    private lateinit var dishFieldLayout: TextInputLayout
    private lateinit var recipeFieldLayout: TextInputLayout
    private lateinit var countFieldLayout: TextInputLayout
    private lateinit var measureFieldLayout: TextInputLayout

    private lateinit var mealtimeField: AutoCompleteTextView
    private lateinit var dishField: AutoCompleteTextView
    private lateinit var recipeField: AutoCompleteTextView
    private lateinit var countField: TextInputEditText
    private lateinit var measureField: AutoCompleteTextView

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_dish_detail_add, container, false)

        mealtimeField = rootView.findViewById(R.id.AutoCompleteTextView1)
        dishField = rootView.findViewById(R.id.AutoCompleteTextView2)
        recipeField = rootView.findViewById(R.id.AutoCompleteTextView3)
        countField = rootView.findViewById(R.id.editTextText1)
        measureField = rootView.findViewById(R.id.AutoCompleteTextView4)

        mealtimeFieldLayout = rootView.findViewById(R.id.TextInputLayout1)
        dishFieldLayout = rootView.findViewById(R.id.TextInputLayout2)
        recipeFieldLayout = rootView.findViewById(R.id.TextInputLayout3)
        countFieldLayout = rootView.findViewById(R.id.TextInputLayout4)
        measureFieldLayout = rootView.findViewById(R.id.TextInputLayout5)

        btn = rootView.findViewById(R.id.button)
        progress = rootView.findViewById(R.id.progressBarLayout)

        if (adapterMain == null) {
            val mealTimes = listOf(
                db_Dish_mealtime(1, "Завтрак"),
                db_Dish_mealtime(2, "Обед"),
                db_Dish_mealtime(3, "Полдник"),
                db_Dish_mealtime(4, "Ужин")
            )

            // Создание адаптера с использованием только имен для отображения
            val mealtimeAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                mealTimes.map { it.name }
            )

            mealtimeField.setAdapter(mealtimeAdapter)

            val recipeAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                data.recipe.map { it.name }
            )

            countField.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
                val input = dest.toString() + source.toString()
                try {
                    val number = input.toDouble()
                    if (number in 0.0..3000.0) null else ""
                } catch (e: NumberFormatException) {
                    ""
                }
            }, DecimalDigitsInputFilter(4, 2))

            measureField.setText(data.measure.name)
            if (data.mealtime_id != null) {
                mealtimeField.setText(mealTimes[data.mealtime_id - 1].name, false)

            }
            recipeField.setText(data.recipe[data.recipe_position].name, false)
            recipeField.setAdapter(recipeAdapter)
            countField.setText(data.countDish.toString())
            dishField.setText(data.dish.name)

            recipeField.setOnItemClickListener { parent, view, position, id ->
                val selectedRecipe = data.recipe[position]
                currentRecipe = selectedRecipe.id
            }

            mealtimeField.setOnItemClickListener { parent, view, position, id ->
                val selectedMealTime = mealTimes[position]
                currentMealtime = selectedMealTime.id
            }

            btn.setOnClickListener {
                lifecycleScope.launch {
                    Log.d("test", checkRecipe().toString())
                    Log.d("test", validateCountField().toString())

                    if (checkRecipe() and validateCountField()) {
                        // Предотвратить закрытие диалога
                        (dialog as? BottomSheetDialog)?.setCancelable(false)
                        (dialog as? BottomSheetDialog)?.setCanceledOnTouchOutside(false)
                        try {
                            progress.visibility = View.VISIBLE
                            InsertEat()
                            App.sessionData = supabaseInstance.getUserDetail()
                            dismiss()
                        } catch (e: Exception) {
                            Log.e("supabase", "Ошибка при выполнении InsertEat()", e)
                        } finally {
                            progress.visibility = View.GONE
                            // Вернуть возможность закрытия диалога в случае ошибки или успешного выполнения
                            (dialog as? BottomSheetDialog)?.setCancelable(true)
                            (dialog as? BottomSheetDialog)?.setCanceledOnTouchOutside(true)
                        }
                    }
                }
            }
        } else {
            mealtimeFieldLayout.visibility = View.GONE
            dishFieldLayout.visibility = View.GONE
            recipeFieldLayout.visibility = View.GONE
            btn.text = "Изменить"
            btn.setOnClickListener {
                if (validateCountField()) {
                    lifecycleScope.launch {
                        // Предотвратить закрытие диалога
                        (dialog as? BottomSheetDialog)?.setCancelable(false)
                        (dialog as? BottomSheetDialog)?.setCanceledOnTouchOutside(false)
                        try {
                            progress.visibility = View.VISIBLE
                            val newItem = UpdateEat() // Получаем новый элемент
                            if (newItem != null) {
                                App.sessionData = supabaseInstance.getUserDetail()
                                adapterMain.updateData(App.sessionData!!.user_and_eating.filter { it.id_mealtime == data.mealtime_id })
                                (parentFragment as? Fragment_main)?.loadDonutData()
                                dismiss()
                            }
                        } catch (e: Exception) {
                            Log.e("supabase", "Ошибка при выполнении updateEat()", e)
                        } finally {
                            progress.visibility = View.GONE
                            // Вернуть возможность закрытия диалога в случае ошибки или успешного выполнения
                            (dialog as? BottomSheetDialog)?.setCancelable(true)
                            (dialog as? BottomSheetDialog)?.setCanceledOnTouchOutside(true)
                        }
                    }
                }
            }
        }

        return rootView
    }

    class DecimalDigitsInputFilter(digitsBeforeZero: Int, digitsAfterZero: Int) : InputFilter {
        private val pattern = Regex("^[0-9]{0,$digitsBeforeZero}+((\\.[0-9]{0,$digitsAfterZero})?)||(\\.)?\\s?$")

        override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence? {
            val matcher = pattern.matchEntire(dest.toString() + source.toString())
            return if (matcher != null) null else ""
        }
    }

    suspend fun checkRecipe(): Boolean {
        var isValid = false

        val currentDate = LocalDate.now()
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDate = currentDate.format(dateFormatter)

        val recipes = rcp_Recipes(currentMealtime!!, data.dish.id, App.sessionData!!.id!!, formattedDate)
        Log.d("test234", recipes.toString())

        try {
            val dataRecipe = supabaseInstance.checkRecipe(recipes) //возращает список рецептов, которые уже добавил пользователь
            Log.d("test123", dataRecipe.toString())
            if (currentRecipe in dataRecipe) {
                recipeFieldLayout.error = "У вас уже есть такое блюдо в списке!"
            } else {
                recipeFieldLayout.error = null
                isValid = true
            }
        } catch (e: Exception) {
            Log.e("supabase", "Ошибка при получении данных", e)
            Toast.makeText(requireContext(), "Ошибка при получении данных", Toast.LENGTH_SHORT).show()
        }

        return isValid
    }

    suspend fun UpdateEat(): db_Users_and_eating?{
        val newCount = countField.text.toString().toDouble()
        Log.d("newCount", countField.text.toString())
        Log.d("newCount", newCount.toString())
        Log.d("newCount", data.id!!.toString())
        if (data.countDish != newCount) {
            return supabaseInstance.updateEat(data.id!!, newCount)
        }
        return null
    }

    suspend fun InsertEat(){
        val currentDate = LocalDate.now()
        val kotlinLocalDate = currentDate.toKotlinLocalDate()

        val newEat = db_Users_and_eating(
            id_user = App.sessionData!!.id!!,
            id_dish = data.dish.id,
            id_recipes = data.recipe[currentRecipe].id,
            id_mealtime = currentMealtime!!,
            count = countField.text.toString().toDouble(),
            date = kotlinLocalDate
        )
        supabaseInstance.insertEat(newEat)
    }

    private fun validateCountField(): Boolean {
        countFieldLayout.error = null

        var isValid = false

        if (countField.text.isNullOrEmpty()) {
            countFieldLayout.error = "Поле не должно быть пустым"
        } else {
            // Проверка, чтобы значение было больше нуля
            val countValue = countField.text.toString().toDoubleOrNull()
            if (countValue != null) {
                if (countValue <= 0) {
                    countFieldLayout.error = "Значение должно быть больше нуля"
                } else {
                    isValid = true
                }
            } else {
                countFieldLayout.error = "Значение должно быть корректным"
            }
        }

        return isValid
    }
}