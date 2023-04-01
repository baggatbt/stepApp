package com.example.myapplication.ui.main

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import com.example.myapplication.R

class TimingCircleView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }
    private var radius = 0f

    fun setRadius(radius: Float) {
        this.radius = radius
        invalidate()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = resources.getColor(R.color.timingCircleColor, context.theme) // replace with your desired circle color
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius, paint)
    }
}
