package com.example.customlockscreen.view.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet


class CircleImageView(context: Context, attributeSet: AttributeSet):androidx.appcompat.widget.AppCompatImageView(
    context,attributeSet
){

    private val mPaint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mShader:BitmapShader?=null
    private var mBitmap:Bitmap?=null
    private var mMatrix:Matrix = Matrix()


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        if(drawable!=null){
            val rawBitmap:Bitmap = getBitmap(drawable)!!

            val viewMinSize = width.coerceAtMost(height)


            if(mShader==null|| rawBitmap != mBitmap){
                mBitmap = rawBitmap
                mShader = BitmapShader(rawBitmap,Shader.TileMode.CLAMP,Shader.TileMode.CLAMP)
            }


            mMatrix.setScale(viewMinSize.toFloat()/rawBitmap.width, viewMinSize.toFloat()/rawBitmap.height)
            mShader?.setLocalMatrix(mMatrix)


            mPaint.shader = mShader
            mPaint.isAntiAlias = true
            val radius = viewMinSize/2.0f
            canvas?.drawCircle(radius,radius,radius,mPaint)


        }else{
            super.onDraw(canvas)
        }


    }

    private fun getBitmap(drawable: Drawable): Bitmap? {
        when (drawable) {
            is BitmapDrawable -> {

                return drawable.bitmap

            }
            is ColorDrawable -> {

                val rect: Rect = drawable.bounds
                val width = rect.right - rect.left
                val height = rect.bottom - rect.top

                val color = drawable.color

                val bitmap:Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                canvas.drawARGB(
                        Color.alpha(color),
                        Color.red(color),
                        Color.green(color),
                        Color.blue(color)
                )

                return bitmap

            }
            else -> {
                return null
            }
        }
    }

}