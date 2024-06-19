package com.example.diplom

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textfield.TextInputEditText

class Fragment_dish_select : Fragment() {

    private lateinit var sharedViewModel: Filters
    private lateinit var supabaseInstance: Supabase
    private lateinit var filterButton: FloatingActionButton
    private lateinit var addButton: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_select, container, false)

        supabaseInstance = App.supabaseInstance
        filterButton = rootView.findViewById(R.id.filters)
        addButton = rootView.findViewById(R.id.fab_add)

        filterButton.setOnClickListener {
            // При нажатии на изображение отобразите BottomSheetDialogFragment
            val bottomSheetDialogFragment = Fragment_dish_filter()
            bottomSheetDialogFragment.show(childFragmentManager, bottomSheetDialogFragment.tag)
        }

        val viewPager: ViewPager2 = rootView.findViewById(R.id.viewPager)
        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        val tabLayout: TabLayout = rootView.findViewById(R.id.tabLayout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                addButton.visibility = if (position == 2) View.VISIBLE else View.GONE
            }
        })

        sharedViewModel = ViewModelProvider(requireActivity())[Filters::class.java]

        val editText = rootView.findViewById<TextInputEditText>(R.id.editTextText)
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                sharedViewModel.setFilterSearchText(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        return rootView
    }
}

class Filters : ViewModel() {
    private val _filterData = MutableLiveData<db_FilterData>().apply {
        value = db_FilterData(userId = App.sessionData!!.id) // Инициализация начальным значением
    }
    val filterData: LiveData<db_FilterData> get() = _filterData

    fun setFilterData(filterData: db_FilterData) {
        _filterData.value = filterData
    }

    fun setFilterSearchText(searchText: String) {
        val currentFilterData = _filterData.value ?: db_FilterData()
        val updatedFilterData = currentFilterData.copy(searchText = searchText)
        _filterData.value = updatedFilterData
    }
}

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3 // Количество вкладок

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> Fragment_dish(null)
            1 -> Fragment_dish_bookmark(null)
            2 -> Fragment_dish_user(null)
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

    fun getTabTitle(position: Int): String {
        return when (position) {
            0 -> "Все"
            1 -> "Избранное"
            2 -> "Созданное"
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
