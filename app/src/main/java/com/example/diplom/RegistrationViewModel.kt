package com.example.diplom

import androidx.lifecycle.ViewModel
import kotlinx.datetime.LocalDate

class RegistrationViewModel : ViewModel() {
    var contactInfo: ContactInfo? = null
    var physicalInfo: PhysicalInfo? = null
    var allergyInfo: AllergyInfo? = null

    // Эти классы представляют данные для каждого этапа регистрации
    data class ContactInfo(val name: String, val email: String, val password: String)
    data class PhysicalInfo(val height: Int, val weight: Double, val dateOfBirth: LocalDate, val isMale: Boolean)
    data class AllergyInfo(val allergies: List<String>)
}