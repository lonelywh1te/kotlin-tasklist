package ru.lonelywh1te.kotlin_tasklist.presentation.adapter

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.DOWN
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.UP
import androidx.recyclerview.widget.RecyclerView
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import ru.lonelywh1te.kotlin_tasklist.R
import ru.lonelywh1te.kotlin_tasklist.domain.models.TaskItem

class TaskItemTouchCallback(
    private val context: Context,
    private val adapter: TaskAdapter
): ItemTouchHelper.Callback() {
    private var oldPosition: Int? = null
    private var newPosition: Int? = null

    private var onItemMoved: ((oldPos: Int, newPos: Int) -> Unit)? = null
    private var onLeftSwipe: ((taskItem: TaskItem) -> Unit)? = null

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragDirs = UP or DOWN
        val swipeDirs = LEFT
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
        if (direction == LEFT) {
            onLeftSwipe?.invoke(adapter.getItem(viewHolder.absoluteAdapterPosition))
        }
    }

    override fun onChildDraw(c: Canvas, rv: RecyclerView, vh: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        RecyclerViewSwipeDecorator.Builder(c, rv, vh, dX, dY, actionState, isCurrentlyActive)
            .addBackgroundColor(ContextCompat.getColor(context, R.color.red))
            .addCornerRadius(1, 10)
            .addSwipeLeftActionIcon(R.drawable.ic_delete)
            .addPadding(1, 5f, 0f, 5f)
            .create()
            .decorate()

        super.onChildDraw(c, rv, vh, dX, dY, actionState, isCurrentlyActive)
    }

    fun setOnItemMovedListener(callback: ((oldPos: Int, newPos: Int) -> Unit)) {
        onItemMoved = callback
    }

    fun setOnLeftSwipeListener(callback: (taskItem: TaskItem) -> Unit) {
        onLeftSwipe = callback
    }


}