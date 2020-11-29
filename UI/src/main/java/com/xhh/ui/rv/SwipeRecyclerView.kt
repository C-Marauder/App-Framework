package com.xhh.ui.rv

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.Transformation
import android.widget.FrameLayout
import androidx.core.view.NestedScrollingParent2
import androidx.core.view.ViewCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

/**
 *   @Author:小灰灰
 *   @Time:2020/11/21
 *   @Desc:
 */
class SwipeRecyclerView : FrameLayout, NestedScrollingParent2 {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private val DRAG_RATE: Float = .5f

    private val range: Int by lazy {
        resources.displayMetrics.heightPixels / 5
    }
    private lateinit var recyclerView: RecyclerView
    private var dragUp: Boolean = false
    private var dragDown: Boolean = false
    private var isLoading: Boolean = false
    private lateinit var reboundAnimator: ReboundAnimator
    private var dragPercent: Float = 0f
    private val refreshObserver: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    private var refreshListener: ((refreshObserver: MutableLiveData<Boolean>) -> Unit)? = null
    private fun init() {
        recyclerView = RecyclerView(context).apply {
            setBackgroundColor(Color.parseColor("#fafafa"))
        }
        addView(recyclerView, -1, -1)
        setBackgroundResource(android.R.color.holo_orange_light)
        reboundAnimator = ReboundAnimator()
        refreshObserver.observe(context as LifecycleOwner) {
            if (it) {
                if (scrollY != 0) {
                    reboundAnimator.springBack()
                }
                dragPercent = 0f
                isLoading = false
            }
        }
    }

    fun set(action: RecyclerView.() -> Unit): SwipeRecyclerView {
        action(recyclerView)
        return this
    }

    fun setRefreshListener(refreshListener: (observer: MutableLiveData<Boolean>) -> Unit) {
        this.refreshListener = refreshListener
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        return target is RecyclerView
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {

    }

    override fun onNestedFling(
        target: View,
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        return super.onNestedFling(target, velocityX, velocityY, consumed)
    }

    override fun onStopNestedScroll(target: View, type: Int) {

        if (scrollY != 0 && !isLoading) {
            reboundAnimator.springBack()
        }


    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {

        if (target is RecyclerView && type == ViewCompat.TYPE_TOUCH ) {
            if (dragDown or dragUp) {
                if (dragPercent == 1f && !isLoading) {
                    isLoading = true
                    refreshListener?.invoke(refreshObserver)
                } else {
                    val currentScrollY = dyUnconsumed * DRAG_RATE
                    scrollBy(0, currentScrollY.toInt())
                    dragPercent = abs(scrollY) / range * 1f

                }
            }else{
                scrollTo(0,0)
            }


        }

    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        if (isLoading) {
            consumed[1] = dy
            return
        }

        if ((dragUp && scrollY > 0) or (dragDown && scrollY <0)){
            scrollBy(0, dy)
            consumed[1] = dy
            return
        }
        if (target is RecyclerView && type == ViewCompat.TYPE_TOUCH) {
            dragDown = !target.canScrollVertically(1)
            dragUp = !target.canScrollVertically(-1)
            Log.e("onNestedPreScroll","==dragDown:$dragDown==dragUp:$dragUp")
            Log.e("onNestedPreScroll","==scrollY:$scrollY==dy:$dy")


        }

    }

    override fun scrollTo(x: Int, y: Int) {
        val realY = if (y < 0 && abs(y) > range) {
            -range
        } else if (y > 0 && abs(y) > range) {
            range
        } else {
            y
        }

        super.scrollTo(x, realY)
    }

    private inner class ReboundAnimator : ValueAnimator() {
        private var finalY: Float = 0f

        init {
            interpolator = DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR)
            duration = MAX_OFFSET_ANIMATION_DURATION
            addUpdateListener {
                val position = (finalY * (1 - animatedFraction)).toInt()
                if (animatedFraction == 1f) {
                    scrollTo(0, 0)
                    cancel()
                } else {
                    scrollTo(0, position)

                }
            }
        }

        fun springBack() {
            finalY = scrollY * 1f
            setFloatValues(finalY, 0f)
            start()
        }
    }

    companion object {
        private const val MAX_OFFSET_ANIMATION_DURATION: Long = 500L
        private const val DECELERATE_INTERPOLATION_FACTOR: Float = 2f
    }


}