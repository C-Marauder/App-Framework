package com.hd.framework

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class CadPlaceView:View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)
    private val paint: Paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
        }
    }
    private val rect:Rect by lazy {
        Rect(0, 0, width, height)
    }
    private val coverBitmap:Bitmap by lazy {
        Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888)
        //Canvas()
    }
    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
//            canvas.drawColor(Color.parseColor("#b3000000"))
            paint.color = Color.parseColor("#b3000000")
            val saveCount = canvas.saveLayer(0f,0f,measuredWidth*1f,measuredHeight*1f ,paint)
            canvas.drawBitmap(coverBitmap,0f,0f,paint)
            canvas.drawRect(rect,paint)
            paint.xfermode = xfermode
//          paint.color = Color.parseColor("#00000000")
            it.drawRoundRect(
                width / 4f,
                height / 4f,
                width * 3 / 4f,
                height * 3 / 4f,
                16f,
                16f,
                paint
            )
            paint.xfermode = null
            canvas.restoreToCount(saveCount)
        }
    }
}