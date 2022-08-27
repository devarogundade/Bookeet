package team.pacify.bookeet.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ScrollAdapter(private val recyclerView: RecyclerView) {
    var itemCount = 0

    fun onScroll(hasReachedBottom: (Boolean) -> Unit) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager

        val totalItemCount = layoutManager.itemCount
        val lastVisible = layoutManager.findLastVisibleItemPosition()

        val endHasBeenReached = lastVisible.plus(5) >= totalItemCount
        if (totalItemCount > 0 && endHasBeenReached) {
            hasReachedBottom(true)
        } else {
            hasReachedBottom(false)
        }

        itemCount = totalItemCount
    }
}

fun ScrollAdapter?.init(load: () -> Unit) {
    if (this == null || this.itemCount == 0) {
        load()
    }
}