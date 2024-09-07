package ru.lonelywh1te.kotlin_tasklist.presentation.adapter

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class TaskItemTouchCallback(
    private val adapter: TaskAdapter
): ItemTouchHelper.Callback() {
    private var oldPosition: Int? = null
    private var newPosition: Int? = null

    private var onItemMoved: ((oldPos: Int, newPos: Int) -> Unit)? = null
    private var onSwiped: (() -> Unit)? = null

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragDirs = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeDirs = 0
        return makeMovementFlags(dragDirs, swipeDirs)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        if (viewHolder.itemViewType != target.itemViewType) return false

        if (oldPosition == null) oldPosition = viewHolder.absoluteAdapterPosition
        newPosition = target.absoluteAdapterPosition

        adapter.itemMoved(viewHolder.absoluteAdapterPosition, target.absoluteAdapterPosition)
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
            onItemMoved?.invoke(oldPosition ?: 0, newPosition ?: 0)
            oldPosition = null
            newPosition = null
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onSwiped?.invoke()
    }

    fun setOnItemMovedListener(callback: ((oldPos: Int, newPos: Int) -> Unit)) {
        onItemMoved = callback
    }

    fun setOnSwipeListener(callback: () -> Unit) {
        onSwiped = callback
    }


}