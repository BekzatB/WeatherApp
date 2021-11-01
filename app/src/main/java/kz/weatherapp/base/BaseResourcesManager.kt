package kz.weatherapp.base

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.text.SpannedString
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

interface BaseResourcesManager {

    fun getResources(): Resources
    fun getString(@StringRes resId: Int): String
    fun getString(@StringRes resId: Int, vararg formatArgs: Any?): String
    fun getColor(@ColorRes resId: Int): Int
    fun getDimen(@DimenRes resId: Int): Float
    fun getDrawable(@DrawableRes resId: Int, theme: Resources.Theme? = null): Drawable?
}