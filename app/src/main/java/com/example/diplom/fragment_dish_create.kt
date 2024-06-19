package com.example.diplom

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.google.android.material.internal.CheckableImageButton
import com.google.android.material.textfield.TextInputLayout

class fragment_dish_create : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_dish_create, container, false)

        val textField = rootView.findViewById<TextInputLayout>(R.id.TextInputLayout2)
        val startIcon = textField.findViewById<CheckableImageButton>(com.google.android.material.R.id.text_input_start_icon)

        val layoutParams = startIcon.layoutParams as LinearLayout.LayoutParams
        layoutParams.apply {
            gravity = Gravity.TOP or Gravity.LEFT
        }

        return rootView
    }
}