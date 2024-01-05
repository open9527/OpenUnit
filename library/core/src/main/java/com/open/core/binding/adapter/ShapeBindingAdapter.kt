package com.open.core.binding.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import com.open.core.SizeUtils

object ShapeBindingAdapter {


    @BindingAdapter(
        value = [
            "bindShapeDrawableSolidColor",
            "bindShapeDrawableSolidStartColor",
            "bindShapeDrawableSolidCenterColor",
            "bindShapeDrawableSolidEndColor",
            "bindShapeDrawableOrientation",
            "bindShapeDrawableRadius",
            "bindShapeDrawableRadiusLT",
            "bindShapeDrawableRadiusRT",
            "bindShapeDrawableRadiusLB",
            "bindShapeDrawableRadiusRB",
            "bindShapeDrawableStrokeWidth",
            "bindShapeDrawableStrokeColor",
            "bindShapeDrawableDashWith",
            "bindShapeDrawableDashGap",
        ], requireAll = false
    )
    @JvmStatic
    fun View.setBindingShapeDrawableBackground(
        @ColorInt
        solidColor: Int = Color.TRANSPARENT,
        @ColorInt
        startColor: Int = Color.TRANSPARENT,
        @ColorInt
        centerColor: Int = Color.TRANSPARENT,
        @ColorInt
        endColor: Int = Color.TRANSPARENT,
        gradientOrientation: Int = 1,
        radius: Int = 0,
        radiusLT: Int = 0,
        radiusRT: Int = 0,
        radiusLB: Int = 0,
        radiusRB: Int = 0,
        strokeWidth: Int = 0,
        @ColorInt
        strokeColor: Int = Color.TRANSPARENT,
        dashWith: Int = 0,
        dashGap: Int = 0

    ) {
        val drawable = GradientDrawable()

        //1.设置圆角
        if (radius > 0) {
            drawable.cornerRadius = SizeUtils.dp2px(radius)
        } else {
            drawable.cornerRadii = floatArrayOf(
                SizeUtils.dp2px(radiusLT),
                SizeUtils.dp2px(radiusLT),
                SizeUtils.dp2px(radiusRT),
                SizeUtils.dp2px(radiusRT),
                SizeUtils.dp2px(radiusRB),
                SizeUtils.dp2px(radiusRB),
                SizeUtils.dp2px(radiusLB),
                SizeUtils.dp2px(radiusLB),
            )
        }
        //2.设置渐变方向
        when (gradientOrientation) {
            1 -> drawable.orientation = GradientDrawable.Orientation.TOP_BOTTOM
            2 -> drawable.orientation = GradientDrawable.Orientation.TR_BL
            3 -> drawable.orientation = GradientDrawable.Orientation.RIGHT_LEFT
            4 -> drawable.orientation = GradientDrawable.Orientation.BR_TL
            5 -> drawable.orientation = GradientDrawable.Orientation.BOTTOM_TOP
            6 -> drawable.orientation = GradientDrawable.Orientation.BL_TR
            7 -> drawable.orientation = GradientDrawable.Orientation.LEFT_RIGHT
            8 -> drawable.orientation = GradientDrawable.Orientation.TL_BR
        }

        //3.设置颜色
        if (solidColor != Color.TRANSPARENT) {
            drawable.setColor(solidColor)
        } else {
            drawable.colors = if (centerColor != Color.TRANSPARENT) {
                intArrayOf(
                    startColor,
                    centerColor,
                    endColor
                )
            } else {
                intArrayOf(startColor, endColor)
            }
        }
        //4.描边
        if (strokeColor != Color.TRANSPARENT) {
            drawable.setStroke(
                SizeUtils.dp2px(strokeWidth).toInt(),
                strokeColor,
                dashWith.toFloat(),
                dashGap.toFloat()
            )
        }
        //5.其他
        //drawable.gradientType = GradientDrawable.LINEAR_GRADIENT//线性
        //drawable.shape = GradientDrawable.RECTANGLE//矩形方正
        background = drawable
    }
}