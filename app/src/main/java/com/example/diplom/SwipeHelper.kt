package com.example.diplom

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class SwipeHelper(
    context: Context,
    private val onChangeClick: (db_Dish_add_fragment, Adapter_main) -> Unit,
    private val onDeleteClick: (Int, Adapter_main) -> Unit
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    private val colorChange = ContextCompat.getColor(context, android.R.color.holo_green_dark)
    private val colorDelete = ContextCompat.getColor(context, android.R.color.holo_red_dark)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val item = (viewHolder as Adapter_main.ViewHolder).getItem()
        val recyclerView = viewHolder.itemView.parent as RecyclerView
        val adapter = recyclerView.adapter as Adapter_main
        when (direction) {
            ItemTouchHelper.LEFT -> {
                onChangeClick(
                    db_Dish_add_fragment(
                        id = item.id,
                        mealtime_id = item.id_mealtime,
                        dish = item.dish!!,
                        recipe = listOf(item.recipes!!),
                        recipe_position = item.id_recipes,
                        measure = item.dish!!.measurement!!,
                        item.count
                    ), adapter
                )
            }
            ItemTouchHelper.RIGHT -> {
                onDeleteClick(position, adapter)
            }
        }
        viewHolder.itemView.post {
            viewHolder.itemView.parent?.let { parent ->
                (parent as RecyclerView).adapter?.notifyItemChanged(position)
            }
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            .addSwipeLeftBackgroundColor(colorChange)
            .addSwipeLeftActionIcon(R.drawable.icon_change)
            .addSwipeRightBackgroundColor(colorDelete)
            .addSwipeRightActionIcon(R.drawable.icon_trash)
            .create()
            .decorate()

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}