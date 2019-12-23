package com.myrobot.org.util

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.util.TypedValue



/**
 * view点击效果动画
 *
 * @author Lixingxing
 */
class BaseViewOnClickAnimUtils {
    private var mView: View? = null

    fun initView(view: View): BaseViewOnClickAnimUtils {
        this.mView = view
        return this
    }

    // 点击时改变亮度-->仅针对ImageView
    fun lightImageView(clickLight: Float = 0.8f): BaseViewOnClickAnimUtils {
        if (mView != null && mView is ImageView) {
            mView!!.isClickable = true
            mView!!.setOnTouchListener { v, event ->
                when (event!!.action) {
                    MotionEvent.ACTION_DOWN -> {
                        changeLight(clickLight)
                    }
                    MotionEvent.ACTION_UP -> {
                        (mView as ImageView).clearColorFilter()
                    }
                }
                false
            }
        }
        return this
    }

    fun lightImageView(): BaseViewOnClickAnimUtils {
        return lightImageView( 0.8f)
    }
    //改变图片的亮度方法 1--原样  >1---调亮  <1---调暗  0-2
    private fun changeLight(brightness: Float) {
        val brightnessMatrix = ColorMatrix()
        brightnessMatrix.setScale(brightness, brightness, brightness, 1f)
        (mView as ImageView).colorFilter = ColorMatrixColorFilter(brightnessMatrix)
    }

    // 点击时改变亮度-->针对普通View
    fun lightView(clickLight: Float = 0.5f): BaseViewOnClickAnimUtils {
        mView?.apply {
            isClickable = true
            setOnTouchListener { _, event ->
                when (event!!.action) {
                    MotionEvent.ACTION_DOWN -> {
                        alpha = clickLight
                    }
                    MotionEvent.ACTION_UP -> {
                        alpha = 1f
                    }
                }
                false
            }
        }
        return this
    }

    fun lightView(): BaseViewOnClickAnimUtils {
        return lightView( 0.8f)
    }


    fun ripple(): BaseViewOnClickAnimUtils {
        mView?.apply{
            val typedValue = TypedValue()
            context?.theme?.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)
            val attribute = intArrayOf(android.R.attr.selectableItemBackground)
            var typedArray = mView?.context?.theme?.obtainStyledAttributes(typedValue.resourceId, attribute)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                foreground = typedArray!!.getDrawable(0)
            }else{
                background = typedArray!!.getDrawable(0)
            }
        }
        return this
    }


}
