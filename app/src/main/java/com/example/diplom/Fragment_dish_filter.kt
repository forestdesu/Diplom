package com.example.diplom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.slider.LabelFormatter
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.TextInputEditText


class Fragment_dish_filter() : BottomSheetDialogFragment() {
    private lateinit var categoryText: TextView
    private lateinit var price1: TextInputEditText
    private lateinit var price2: TextInputEditText
    private lateinit var timeSlider: Slider
    private lateinit var categoryArr: MutableLiveData<MutableList<Int>>
    private val sharedViewModel: Filters by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_dish_filter, container, false)
        val categoryTextView = rootView.findViewById<LinearLayout>(R.id.category)
        val closeButton = rootView.findViewById<Button>(R.id.button)
        val reset = rootView.findViewById<TextView>(R.id.reset)
        categoryText = rootView.findViewById(R.id.categoryText)
        price1 = rootView.findViewById(R.id.editTextText1)
        price2 = rootView.findViewById(R.id.editTextText2)
        timeSlider = rootView.findViewById(R.id.slider)
        categoryArr = MutableLiveData(sharedViewModel.filterData.value?.categoryArr?.toMutableList() ?: mutableListOf())

        setData()

        categoryArr.observe(viewLifecycleOwner, Observer { updatedCategoryArr ->
            var newText = "Любые"
            val selectedCategories = updatedCategoryArr.mapNotNull { categoryId ->
                App.dbFilters?.find { it.id == categoryId }
            }
            if (selectedCategories.isNotEmpty()) {
                newText = selectedCategories.joinToString(", ") { it.name }
            }
            categoryText.text = newText
        })


        reset.setOnClickListener {
            resetData()
        }

        categoryTextView.setOnClickListener {
            val bottomSheetDialogFragment = Fragment_dish_filter_checkbox(App.dbFilters!!, categoryArr)
            bottomSheetDialogFragment.show(childFragmentManager, bottomSheetDialogFragment.tag)
        }

        closeButton.setOnClickListener {
            val selectedTime = timeSlider.value.toInt()

            sharedViewModel.setFilterData(
                db_FilterData(
                categoryArr = categoryArr.value?.toList() ?: listOf(),
                priceBegin = price1.text?.toString()?.toDoubleOrNull(),
                priceEnd = price2.text?.toString()?.toDoubleOrNull(),
                time = if (selectedTime != 0) {
                    String.format("%02d:%02d:00", selectedTime / 60, selectedTime % 60)
                } else {
                    null
                },
                searchText = sharedViewModel.filterData.value!!.searchText
            ))
            dismiss()
        }

        val slider = rootView.findViewById<Slider>(R.id.slider)
        slider.setLabelFormatter(object : LabelFormatter {
            override fun getFormattedValue(value: Float): String {
                if (value == 0.0f) {
                    return " "
                }
                return "${value.toInt()} мин"
            }
        })

        return rootView
    }

//    override fun onDismiss(dialog: DialogInterface) {
//        super.onDismiss(dialog)
//        fd.priceArr = listOf(price1.text?.toString()?.toDoubleOrNull(), price2.text?.toString()?.toDoubleOrNull())
//
//        val selectedTime = timeSlider.value.toInt()
//        fd.time = if (selectedTime != 0) {
//            String.format("%02d:%02d:00", selectedTime / 60, selectedTime % 60)
//        } else {
//            null
//        }
//
//        //(parentFragment as? Fragment_dish)?.filter(fd)
//
//    }

    fun setData() {
        val filterData = sharedViewModel.filterData.value ?: db_FilterData()

        var newText = "Любые"
        val selectedCategories = filterData.categoryArr.mapNotNull { categoryId ->
            App.dbFilters!!.find { it.id == categoryId }
        }
        if (selectedCategories.isNotEmpty()) {
            newText = selectedCategories.joinToString(", ") { it.name }
        }
        categoryText.text = newText

        price1.setText(filterData.priceBegin?.toString() ?: "")
        price2.setText(filterData.priceEnd?.toString() ?: "")

        filterData.time?.let { time ->
            val timeComponents = time.split(":")
            if (timeComponents.size == 3) {
                val hours = timeComponents[0].toIntOrNull() ?: 0
                val minutes = timeComponents[1].toIntOrNull() ?: 0
                timeSlider.value = (hours * 60 + minutes).toFloat()
            }
        }
    }

    private fun resetData() {
        categoryArr.value?.clear()
        categoryText.text = "Любые"
        price1.setText(null)
        price2.setText(null)
        timeSlider.value = 0.0f
    }
}