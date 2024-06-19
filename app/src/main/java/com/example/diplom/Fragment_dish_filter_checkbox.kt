package com.example.diplom

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText

class Fragment_dish_filter_checkbox(private val dataCategory: List<db_Dish_category>, private var categoryArr: MutableLiveData<MutableList<Int>>) : BottomSheetDialogFragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var editText: TextInputEditText
    private lateinit var checkboxAdapter: Adapter_dish_filter_checkbox
    private var copyCategoryArr: MutableList<Int> = categoryArr.value ?: mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_filter_checkbox, container, false)
        val closeButton = rootView.findViewById<Button>(R.id.button)
        val reset = rootView.findViewById<TextView>(R.id.reset)
        recyclerView = rootView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        reset.setOnClickListener {
            resetData()
        }

        editText = rootView.findViewById(R.id.editTextText)
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterData(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        closeButton.setOnClickListener {
            dismiss()
            categoryArr.value = copyCategoryArr
        }


        fetchData()

        return rootView
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        // Вызываем GetCategory перед закрытием
        (parentFragment as? Fragment_dish_filter)?.setData()
    }

    private fun fetchData() {
        checkboxAdapter = Adapter_dish_filter_checkbox(dataCategory, copyCategoryArr)
        recyclerView.adapter = checkboxAdapter
    }

    private fun filterData(query: String) {
        val filteredData = dataCategory.filter { ingredient ->
            ingredient.name.contains(query, ignoreCase = true)
        }
        checkboxAdapter.updateData(filteredData)
    }

    private fun resetData() {
        copyCategoryArr.clear()
        checkboxAdapter.clearAllChecks()
    }
}