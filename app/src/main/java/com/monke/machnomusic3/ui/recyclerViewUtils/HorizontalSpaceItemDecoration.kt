package com.monke.machnomusic3.ui.recyclerViewUtils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Класс для вертикальных отступов в RecyclerView
 */
class HorizontalSpaceItemDecoration(
    private val verticalPadding: Int = 0,
    private val horizontalPadding: Int
): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.top = verticalPadding
        outRect.bottom = verticalPadding

        if (parent.getChildAdapterPosition(view) != parent.childCount - 1)
            outRect.right = horizontalPadding

        if (parent.getChildAdapterPosition(view) != 0)
            outRect.left = horizontalPadding
    }

}