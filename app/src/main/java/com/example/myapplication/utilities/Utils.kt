// Utils.kt

package com.example.myapplication.utilities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

class Utils {
    companion object {
        // Add the resizeDrawable function here
        fun resizeDrawable(context: Context, drawable: Drawable, newWidth: Int, newHeight: Int): Drawable {
            if (drawable is BitmapDrawable) {
                val bitmap = drawable.bitmap
                val resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false)
                return BitmapDrawable(context.resources, resizedBitmap)
            } else {
                throw IllegalArgumentException("Invalid drawable: $drawable")
            }
        }

        // Add the resizeAnimationDrawable function here
        fun resizeAnimationDrawable(context: Context, animationDrawable: AnimationDrawable, newWidth: Int, newHeight: Int): AnimationDrawable {
            val resizedAnimation = AnimationDrawable()
            resizedAnimation.isOneShot = animationDrawable.isOneShot

            for (i in 0 until animationDrawable.numberOfFrames) {
                val frame = animationDrawable.getFrame(i)
                val duration = animationDrawable.getDuration(i)
                val resizedFrame = resizeDrawable(context, frame, newWidth, newHeight)
                resizedAnimation.addFrame(resizedFrame, duration)
            }

            return resizedAnimation
        }

        // Other utility functions go here
    }
}







