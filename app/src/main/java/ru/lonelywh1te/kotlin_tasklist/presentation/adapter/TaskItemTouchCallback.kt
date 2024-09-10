package ru.lonelywh1te.kotlin_tasklist.presentation.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.core.graphics.red
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE
import androidx.recyclerview.widget.ItemTouchHelper.DOWN
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.UP
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors
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

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return 0.5f
    }

    override fun onChildDraw(c: Canvas, rv: RecyclerView, vh: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        if (actionState == ACTION_STATE_SWIPE) {
            val itemView = vh.itemView

            // background
            val cornerRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, context.resources.displayMetrics)
            val rect = RectF(
                itemView.left.toFloat() + dX,
                itemView.top.toFloat() + itemView.paddingTop,
                itemView.right.toFloat(),
                itemView.bottom.toFloat() - itemView.paddingBottom
            )
            val paint = Paint().apply {
                color = MaterialColors.getColor(context, R.attr.ktErrorColor, color.red)
                isAntiAlias = true
            }

            c.drawRoundRect(rect, cornerRadius, cornerRadius, paint)

            // icon
            val icon = ContextCompat.getDrawable(context, R.drawable.ic_delete)
            val iconSize = icon?.intrinsicHeight ?: 0
            val iconRightMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, context.resources.displayMetrics)
            val iconTop = itemView.top + (itemView.height - iconSize) / 2
            icon?.setBounds(
                itemView.right - iconSize - iconRightMargin.toInt(),
                iconTop,
                itemView.right - iconRightMargin.toInt(),
                iconTop + iconSize
            )

            icon?.draw(c)
        }

        super.onChildDraw(c, rv, vh, dX, dY, actionState, isCurrentlyActive)
    }

    fun setOnItemMovedListener(callback: ((oldPos: Int, newPos: Int) -> Unit)) {
        onItemMoved = callback
    }

    fun setOnLeftSwipeListener(callback: (taskItem: TaskItem) -> Unit) {
        onLeftSwipe = callback
    }


}