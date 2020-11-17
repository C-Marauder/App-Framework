package com.xhh.ui.rv

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE

/**
 *   @Author:小灰灰
 *   @Time:2020/11/15
 *   @Desc:
 */


inline fun <reified T : RecyclerView> T.init(
    layoutManager: RecyclerView.LayoutManager? = null,
    divider: Drawable? = null,
    onBindAdapter: T.() -> Unit,
    noinline onLoadMore: ((page: Int) -> Unit)? = null
) {

    setHasFixedSize(true)
    setLayoutManager(layoutManager ?: LinearLayoutManager(context))
    divider?.let {
        addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
            setDrawable(it)
        })

    }
    onLoadMore?.let {
        addOnScrollListener(LoadListener(this) { page ->
            it.invoke(page)
        })
    }
    onBindAdapter(this)


}

class LoadListener(recyclerView: RecyclerView, private val onLoading: (page: Int) -> Unit) :
    RecyclerView.OnScrollListener() {
    private var canLoad: Boolean = true

    private var currentPage: Int = 1
    private var isFirstEnter: Boolean = true

    init {
        recyclerView.adapter?.registerAdapterDataObserver(object :
            RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                canLoad = true
            }
        })
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (recyclerView.scrollState == SCROLL_STATE_IDLE) {
            val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
            val childCount = recyclerView.childCount
            Log.e("==", "===${childCount / 10f}")
            val hasMore = childCount / 10f > currentPage

            if (canLoad && hasMore && linearLayoutManager.findLastCompletelyVisibleItemPosition() == childCount - 1) {
                if (isFirstEnter) {
                    isFirstEnter = false

                } else {
                    canLoad = false
                    onLoading(currentPage)
                    currentPage += 1
                }

            }

        }

    }
}