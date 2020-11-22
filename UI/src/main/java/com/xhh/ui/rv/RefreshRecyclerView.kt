package com.xhh.ui.rv

import android.animation.ValueAnimator
import android.content.Context
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
class RefreshLayout : FrameLayout, NestedScrollingParent2 {
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

    private val DRAG_RATE: Float = .75f

    private val range: Int by lazy {
        resources.displayMetrics.heightPixels / 5
    }
    lateinit var recyclerView: RecyclerView
    private var interrupted: Boolean = false
    private var scaledTouchSlop: Int = 0
    private lateinit var reboundAnimator: ReboundAnimator
    private fun init() {
        recyclerView = RecyclerView(context)
        addView(recyclerView, -1, -1)
        setBackgroundResource(android.R.color.holo_orange_light)
        scaledTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        reboundAnimator = ReboundAnimator()

    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        return target is RecyclerView
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {

    }


    override fun onStopNestedScroll(target: View, type: Int) {
        if (target.scrollY !=0){
            target.scrollTo(0,0)
        }
        interrupted = false
        if (scrollY !=0){
            reboundAnimator.springBack()
        }


    }

    private var currentDragPercent: Float = 0f
    private var currentScrollY: Float = 0f
    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {

        if (interrupted && target is RecyclerView && type == ViewCompat.TYPE_TOUCH) {
            currentScrollY = abs(dyUnconsumed) * DRAG_RATE
            currentDragPercent = currentScrollY / range * 1f
            if (currentDragPercent > 1 || currentDragPercent < 0) {
                return
            }
            val boundedDragPercent = min(1f, abs(currentDragPercent))
            val extraOS = currentScrollY - range
            val tensionSlingshotPercent = max(0f, min(extraOS, range * 1f * 2) / range * 1f)
            val tensionPercent =
                (tensionSlingshotPercent / 4 - (tensionSlingshotPercent / 4).toDouble().pow(
                    2.0
                )) * 2f
            val extraMove = range * tensionPercent / 2f
            val targetY = (range * boundedDragPercent + extraMove).toInt()

            scrollBy(0, -targetY)

        }

    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {

        interrupted =  target is RecyclerView && type == ViewCompat.TYPE_TOUCH && !target.canScrollVertically(-1) && dy < 0 && abs(
            dy
        ) > scaledTouchSlop


    }

    override fun scrollTo(x: Int, y: Int) {
        val realY = if (abs(y) > range) {
            -range
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
                if (animatedFraction == 1f){
                    scrollTo(0,0)
                    cancel()
                }else{
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