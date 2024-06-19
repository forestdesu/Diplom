package com.example.diplom

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class Fragment_dish_detail(private val dish_id: Int, private val mealtime_id: Int?) : Fragment() {

    private lateinit var dataDb: db_Dish
    private lateinit var dataTabs: List<db_Recipes_and_dish>
    private var currentTab: Int = 0
    private var coef: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_dish_detail, container, false)
        val content = rootView.findViewById<ScrollView>(R.id.content)
        val loadingView = rootView.findViewById<View>(R.id.loading_page)
        val viewPager = rootView.findViewById<ViewPager2>(R.id.viewPager)
        val tabLayout = rootView.findViewById<TabLayout>(R.id.tabLayout)
        val addButton = rootView.findViewById<Button>(R.id.button2)

        lifecycleScope.launch {
            try {
                val supabaseInstance = Supabase()
                dataDb = supabaseInstance.getCurrentDish(dish_id)
                dataTabs = supabaseInstance.getRecipes(dish_id)
                if (dataTabs.size == 0) {
                    addButton.visibility = View.GONE
                }

                Log.d("supabase", dataTabs.toString())
                Log.d("supabase", dataDb.toString())
                LoadData(rootView)

                val adapter = Adapter_dish_detail_tabs_pager(requireActivity(), dataTabs)
                viewPager.adapter = adapter

                if (dataTabs.size == 1) {
                    tabLayout.tabMode = TabLayout.MODE_FIXED
                } else {
                    tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
                }

                val tabLayoutMediator = TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                    tab.text = "${dataTabs[position].recipes.name}"
                }
                tabLayoutMediator.attach()

                // Добавляем слушатель для отслеживания текущего таба
                viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        currentTab = position
                        coef = dataTabs[position].recipes.count
                    }
                })

                addButton.setOnClickListener {
                    val data = db_Dish_add_fragment(null, mealtime_id, dataDb, dataTabs.map { it.recipes }, currentTab, dataDb.measurement!!, coef)
                    val bottomSheetDialogFragment = Fragment_dish_detail_add(data, null)
                    bottomSheetDialogFragment.show(childFragmentManager, bottomSheetDialogFragment.tag)
                }
            } catch (e: Exception) {
                Log.e("supabase", "Ошибка при получении данных", e)
                Toast.makeText(requireContext(), "Ошибка при получении данных", Toast.LENGTH_SHORT).show()
            } finally {
                loadingView.visibility = View.GONE
                content.visibility = View.VISIBLE
            }
        }

        return rootView
    }

    fun LoadData(rootView: View) {
        val IngredientImg = rootView.findViewById<ImageView>(R.id.imageView)
        val IngredientName = rootView.findViewById<TextView>(R.id.Name)
        val IngredientDesc = rootView.findViewById<TextView>(R.id.Desc)
        val IngredientMoreDetail1 = rootView.findViewById<TextView>(R.id.MoreDetail1)
        val IngredientMoreDetail2 = rootView.findViewById<TextView>(R.id.MoreDetail2)
        val IngredientMoreDetail3 = rootView.findViewById<TextView>(R.id.MoreDetail3)
        val IngredientMoreDetail4 = rootView.findViewById<TextView>(R.id.MoreDetail4)

        IngredientName.text = dataDb.name
        IngredientDesc.text = dataDb.desc
        //IngredientMoreDetail1.text = dataDb.category_id.name.toString()
        //IngredientMoreDetail4.text = dataDb.shelf_life.toString()
        Picasso.get().load(dataDb.img).into(IngredientImg)
    }
}