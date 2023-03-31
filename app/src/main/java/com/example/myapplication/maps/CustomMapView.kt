package com.example.myapplication.maps

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.ui.main.Quest
import kotlin.math.sqrt

@RequiresApi(Build.VERSION_CODES.R)
class CustomMapView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    var pointsOfInterest: List<PointOfInterest> = listOf()


    private var offsetX: Float = 0f
    private var offsetY: Float = 0f

    private var lastTouchX: Float = 0f
    private var lastTouchY: Float = 0f

    private val POINT_RADIUS = 50f // You can adjust this value according to your needs

    private var mapDrawable: Drawable? = null
    private val screenSize: Point = Point()
    private val mapSize: Point = Point()

    init {
        mapDrawable = ContextCompat.getDrawable(context, R.drawable.temp_map)
        mapDrawable?.setBounds(0, 0, mapDrawable?.intrinsicWidth ?: 0, mapDrawable?.intrinsicHeight ?: 0)

        val displayMetrics = DisplayMetrics()
        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).currentWindowMetrics.bounds.apply {
            screenSize.x = width()
            screenSize.y = height()
        }

        mapSize.x = mapDrawable?.intrinsicWidth ?: 0
        mapSize.y = mapDrawable?.intrinsicHeight ?: 0
    }

    // Other properties and methods for drawing the map and points...

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastTouchX = event.x
                lastTouchY = event.y
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val newOffsetX = offsetX + event.x - lastTouchX
                val newOffsetY = offsetY + event.y - lastTouchY

                offsetX = newOffsetX.coerceIn((screenSize.x - mapSize.x).toFloat(), 0f)
                offsetY = newOffsetY.coerceIn((screenSize.y - mapSize.y).toFloat(), 0f)

                lastTouchX = event.x
                lastTouchY = event.y

                // Invalidate the view to trigger a redraw with the new offsets
                invalidate()

                return true
            }
            MotionEvent.ACTION_UP -> {
                val clickedPoint = pointsOfInterest.findClickedPoint(event.x, event.y, offsetX, offsetY, POINT_RADIUS)

                if (clickedPoint != null) {
                    onPointClickListener?.invoke(clickedPoint)
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }


    private fun distance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1))
    }

    fun List<PointOfInterest>.findClickedPoint(x: Float, y: Float, offsetX: Float, offsetY: Float, radius: Float): PointOfInterest? {
        return this.firstOrNull { point ->
            val isLocked = point.requires.any { requiredId ->
                this.find { it is PointOfInterest && it.visited && it is BattlePoint && it.battleId == requiredId } == null
                        && this.find { it is PointOfInterest && it.visited && it is QuestPoint && it.questId == requiredId } == null
            }
            !isLocked && distance(x, y, point.location.x + offsetX, point.location.y + offsetY) <= radius
        }
    }





    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        Log.d("CustomMapView", "Drawing points...") // Add this line
        canvas.translate(offsetX, offsetY)

        // Draw the map image
        mapDrawable?.draw(canvas)

        val paint = Paint()
        paint.isAntiAlias = true

        pointsOfInterest.forEach { point ->
            val x = point.location.x
            val y = point.location.y

            when (point) {
                is BattlePoint -> {
                    paint.color = Color.RED
                    canvas.drawCircle(x, y, POINT_RADIUS, paint)
                }
                is QuestPoint -> {
                    paint.color = Color.GREEN
                    canvas.drawCircle(x, y, POINT_RADIUS, paint)
                }
                else -> {
                    paint.color = Color.BLACK
                    canvas.drawCircle(x, y, POINT_RADIUS, paint)}
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val mapWidth = mapDrawable?.intrinsicWidth ?: 0
        val mapHeight = mapDrawable?.intrinsicHeight ?: 0

        val width = View.resolveSize(mapWidth, widthMeasureSpec)
        val height = View.resolveSize(mapHeight, heightMeasureSpec)

        setMeasuredDimension(width, height)
    }

    private var onPointClickListener: ((PointOfInterest) -> Unit)? = null

    fun setOnPointClickListener(listener: (PointOfInterest) -> Unit) {
        onPointClickListener = listener
    }



}
