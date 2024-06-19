package com.example.diplom

import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.futured.donut.DonutProgressView
import app.futured.donut.DonutSection
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class Fragment_main : Fragment() {
    private lateinit var mainActivity: Main
    private lateinit var supabaseInstance: Supabase

    private lateinit var donutView1: DonutProgressView
    private lateinit var donutView2: DonutProgressView
    private lateinit var donutView3: DonutProgressView
    private lateinit var donutView4: DonutProgressView

    private lateinit var textViewProgress1: TextView
    private lateinit var textViewProgress2: TextView
    private lateinit var textViewProgress3: TextView
    private lateinit var textViewProgress4: TextView

    private lateinit var allPrice: TextView
    private lateinit var allCal: TextView
    private lateinit var allCalProgressBar: ProgressBar

    private lateinit var dataMealtime1: MutableList<db_Users_and_eating>
    private lateinit var dataMealtime2: MutableList<db_Users_and_eating>
    private lateinit var dataMealtime3: MutableList<db_Users_and_eating>
    private lateinit var dataMealtime4: MutableList<db_Users_and_eating>

    private var totalCalories: Float = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postponeEnterTransition()

        val rootView = inflater.inflate(R.layout.fragment_main, container, false)

        val plus1 = rootView.findViewById<FloatingActionButton>(R.id.plus1)
        val plus2 = rootView.findViewById<FloatingActionButton>(R.id.plus2)
        val plus3 = rootView.findViewById<FloatingActionButton>(R.id.plus3)
        val plus4 = rootView.findViewById<FloatingActionButton>(R.id.plus4)

        val recyclerView1 = rootView.findViewById<RecyclerView>(R.id.recyclerView1)
        val recyclerView2 = rootView.findViewById<RecyclerView>(R.id.recyclerView2)
        val recyclerView3 = rootView.findViewById<RecyclerView>(R.id.recyclerView3)
        val recyclerView4 = rootView.findViewById<RecyclerView>(R.id.recyclerView4)

        allPrice = rootView.findViewById(R.id.allPrice)
        allCal = rootView.findViewById(R.id.allCal)
        allCalProgressBar = rootView.findViewById(R.id.allCalProgressBar)

        donutView1 = rootView.findViewById(R.id.donut_view1)
        donutView2 = rootView.findViewById(R.id.donut_view2)
        donutView3 = rootView.findViewById(R.id.donut_view3)
        donutView4 = rootView.findViewById(R.id.donut_view4)

        textViewProgress1 = rootView.findViewById(R.id.textViewProgress1)
        textViewProgress2 = rootView.findViewById(R.id.textViewProgress2)
        textViewProgress3 = rootView.findViewById(R.id.textViewProgress3)
        textViewProgress4 = rootView.findViewById(R.id.textViewProgress4)

        recyclerView1.layoutManager = LinearLayoutManager(requireContext())
        recyclerView2.layoutManager = LinearLayoutManager(requireContext())
        recyclerView3.layoutManager = LinearLayoutManager(requireContext())
        recyclerView4.layoutManager = LinearLayoutManager(requireContext())

        mainActivity = requireActivity() as Main
        supabaseInstance = Supabase()

        loadDonutData()

        plus1.setOnClickListener { plusFunction(1) }
        plus2.setOnClickListener { plusFunction(2) }
        plus3.setOnClickListener { plusFunction(3) }
        plus4.setOnClickListener { plusFunction(4) }

        val dishAdapter1 = Adapter_main(1, dataMealtime1, ::onDishClick)
        val dishAdapter2 = Adapter_main(2, dataMealtime2, ::onDishClick)
        val dishAdapter3 = Adapter_main(3, dataMealtime3, ::onDishClick)
        val dishAdapter4 = Adapter_main(4, dataMealtime4, ::onDishClick)

        val swipeHelperCallback = SwipeHelper(requireContext(), ::onChangeClick, ::onDeleteClick)
        val itemTouchHelper1 = ItemTouchHelper(swipeHelperCallback)
        val itemTouchHelper2 = ItemTouchHelper(swipeHelperCallback)
        val itemTouchHelper3 = ItemTouchHelper(swipeHelperCallback)
        val itemTouchHelper4 = ItemTouchHelper(swipeHelperCallback)

        itemTouchHelper1.attachToRecyclerView(recyclerView1)
        itemTouchHelper2.attachToRecyclerView(recyclerView2)
        itemTouchHelper3.attachToRecyclerView(recyclerView3)
        itemTouchHelper4.attachToRecyclerView(recyclerView4)

        recyclerView1.adapter = dishAdapter1
        recyclerView2.adapter = dishAdapter2
        recyclerView3.adapter = dishAdapter3
        recyclerView4.adapter = dishAdapter4

        rootView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // Удаляем слушатель, чтобы он не вызывался повторно
                rootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                // Запускаем отложенный переход
                startPostponedEnterTransition()
            }
        })

        return rootView
    }

    fun loadDonutData() {
        totalCalories = App.sessionData!!.number_of_calories!!.toFloat()


        donutView1.clear()
        donutView2.clear()
        donutView3.clear()
        donutView4.clear()

        dataMealtime1 = App.sessionData!!.user_and_eating.filter { it.id_mealtime == 1 }.toMutableList()
        dataMealtime2 = App.sessionData!!.user_and_eating.filter { it.id_mealtime == 2 }.toMutableList()
        dataMealtime3 = App.sessionData!!.user_and_eating.filter { it.id_mealtime == 3 }.toMutableList()
        dataMealtime4 = App.sessionData!!.user_and_eating.filter { it.id_mealtime == 4 }.toMutableList()

        val (count1, price1) = setupDonutView(donutView1, textViewProgress1, dataMealtime1, totalCalories * 0.25f)
        val (count2, price2) = setupDonutView(donutView2, textViewProgress2, dataMealtime2, totalCalories * 0.35f)
        val (count3, price3) = setupDonutView(donutView3, textViewProgress3, dataMealtime3, totalCalories * 0.10f)
        val (count4, price4) = setupDonutView(donutView4, textViewProgress4, dataMealtime4, totalCalories * 0.30f)

        val totalConsumedPrice = price1 + price2 + price3 + price4
        val totalConsumedCalories = count1 + count2 + count3 + count4
        val progressPercent = (totalConsumedCalories / totalCalories) * 100

        allPrice.text = String.format("%.2f", totalConsumedPrice) + " ₽"
        allCalProgressBar.progress = progressPercent.coerceAtMost(100F).toInt()
        allCal.text = "$totalConsumedCalories / ${totalCalories.toInt()}"
    }

    private fun setupDonutView(donutView: DonutProgressView, donutText: TextView, data: List<db_Users_and_eating>, calories: Float): Pair<Int, Float> {
        // Считаем общие калории для каждого приема пищи
        val mealCalories = mutableListOf<DonutSection>()
        var totalConsumedCalories = 0 // Переменная для хранения общего количества потраченных калорий
        var totalPrice = 0f // Переменная для хранения общей цены

        for (n in data.indices) {
            if (data[n].recipes!!.table4 != 0f) {
                val consumedCalories = (data[n].count.toFloat() * data[n].recipes!!.table4 / 100) // Количество потраченных калорий для текущего приема пищи
                mealCalories.add(DonutSection(
                    name = "${data[n].id}",
                    color = App.colors[if (n > 9) 9 else n],
                    amount = consumedCalories / calories * 100f // Процент от общего количества калорий
                ))
                totalConsumedCalories += consumedCalories.toInt() // Добавляем потраченные калории к общему количеству
                totalPrice += data[n].count.toFloat() / 100 * data[n].recipes!!.price.toFloat() // Добавляем цену блюда к общей цене
            }
        }

        // Устанавливаем общий лимит калорийt
        donutView.cap = calories * 1f // Процентное представление (1f = 1%)

        // Подавать данные секций в DonutProgressView
        donutView.submitData(mealCalories)

        // Отображаем общее количество потраченных калорий
        animateTextView(donutText, totalConsumedCalories)

        // Возвращаем пару значений: количество потраченных калорий и общая цена
        return totalConsumedCalories to totalPrice
    }

    private fun deleteDonutSection(item: db_Users_and_eating) {
        val donutView: DonutProgressView = when (item.id_mealtime) {
            1 -> donutView1
            2 -> donutView2
            3 -> donutView3
            4 -> donutView4
            else -> {
                throw IllegalArgumentException("Invalid mealtime_id: ${item.id_mealtime}")
            }
        }

        donutView.removeAmount("${item.id}", 0f)
    }

    fun changeDonutSection(item: db_Users_and_eating) {
        Log.d("testCircle", "changeDonutSection called with item: $item")
        val donutView: DonutProgressView
        val calories: Float
        val donutText: TextView

        when (item.id_mealtime) {
            1 -> {
                donutView = donutView1
                calories = totalCalories * 0.25f
                donutText = textViewProgress1
            }
            2 -> {
                donutView = donutView2
                calories = totalCalories * 0.35f
                donutText = textViewProgress2
            }
            3 -> {
                donutView = donutView3
                calories = totalCalories * 0.10f
                donutText = textViewProgress3
            }
            4 -> {
                donutView = donutView4
                calories = totalCalories * 0.30f
                donutText = textViewProgress4
            }
            else -> {
                throw IllegalArgumentException("Invalid mealtime_id: ${item.id_mealtime}")
            }
        }

        val consumedCalories = (item.count.toFloat() * item.recipes!!.table4 / 100)
        var amount = consumedCalories / calories * 100f

        val oldTextCalories = parseCalories(donutText.text.toString())
        val oldItem = donutView.getData().firstOrNull { it.name == item.id.toString() }
        val oldItemCalories = oldItem?.amount?.let { it / 100 * calories }?.toInt() ?: 0
        val newTextCalories = oldTextCalories - oldItemCalories + consumedCalories.toInt()

        donutView.setAmount("${item.id}", amount)
        animateTextView(donutText, newTextCalories)
    }

    private fun animateTextView(textView: TextView, newValue: Int) {
        val oldValue = textView.text.toString().replace(" ккал", "").toIntOrNull() ?: 0
        val animator = ValueAnimator.ofInt(oldValue, newValue)
        animator.duration = 1000 // Продолжительность анимации в миллисекундах
        animator.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Int
            textView.text = "$animatedValue ккал"
        }
        animator.start()
    }

    private fun parseCalories(caloriesText: String): Int {
        val pattern = Pattern.compile("\\d+")
        val matcher = pattern.matcher(caloriesText)
        return if (matcher.find()) {
            matcher.group().toInt()
        } else {
            0 // Возвращаем 0, если число не найдено
        }
    }

    private fun plusFunction(id: Int) {
        val fragment = Fragment_dish(id)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun onDishClick(dish_id: Int, mealtime_id: Int) {
        Log.d("supabase", "Click!")
        val fragment = Fragment_dish_detail(dish_id, mealtime_id)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun onDeleteClick(position: Int, adapterMain: Adapter_main) {
        val oldItem = adapterMain.removeItem(position) //db_users_and_eating
        lifecycleScope.launch {
            try {
                supabaseInstance.deleteEat(oldItem.id!!)
                App.sessionData!!.user_and_eating.remove(oldItem)
                adapterMain.removeItem(position)
                deleteDonutSection(oldItem)
            } catch (e: Exception) {
                Log.e("supabase", "Ошибка при получении данных", e)
                Toast.makeText(requireContext(), "Ошибка при получении данных", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onChangeClick(data: db_Dish_add_fragment, adapterMain: Adapter_main) {
        Log.d("Test22", data.toString())
        val dialogFragment = Fragment_dish_detail_add(data, adapterMain)
        dialogFragment.show(childFragmentManager, dialogFragment.tag)
    }
}