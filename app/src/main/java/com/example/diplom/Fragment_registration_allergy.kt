package com.example.diplom

import android.content.Intent
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
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.launch

class Fragment_registration_allergy : Fragment() {
    private lateinit var nextButton: Button
    private lateinit var viewPager: ViewPager2
    private lateinit var chipGroup: ChipGroup

    private val viewModel: RegistrationViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_registration_allergy, container, false)

        nextButton = view.findViewById(R.id.nextButton)
        viewPager = requireActivity().findViewById(R.id.viewPager)
        chipGroup = view.findViewById(R.id.chipGroup)

        nextButton.setOnClickListener {
            saveDataToViewModel()
            Log.d("test", "Выбранные аллергии: ${viewModel.allergyInfo?.allergies}")

            val registerActivity = activity as Register
            lifecycleScope.launch {
                registerActivity.SingIn()
            }
        }

        return view
    }

    private fun saveDataToViewModel() {
        val selectedAllergies = mutableListOf<String>()

        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as Chip
            if (chip.isChecked) {
                selectedAllergies.add(chip.text.toString())
            }
        }

        viewModel.allergyInfo = RegistrationViewModel.AllergyInfo(selectedAllergies)
    }
}
