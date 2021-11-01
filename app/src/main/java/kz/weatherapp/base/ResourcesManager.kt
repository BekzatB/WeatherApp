package kz.weatherapp.base

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

class ResourcesManager(private val context: Context) : BaseResourcesManager {

    override fun getResources(): Resources = context.resources
    override fun getString(@StringRes resId: Int): String = context.getString(resId)
    override fun getString(@StringRes resId: Int, vararg formatArgs: Any?): String =
        context.getString(resId, *formatArgs)

    override fun getColor(@ColorRes resId: Int): Int = ContextCompat.getColor(context, resId)
    override fun getDimen(@DimenRes resId: Int): Float = context.resources.getDimension(resId)
    override fun getDrawable(@DrawableRes resId: Int, theme: Resources.Theme?): Drawable? =
        ResourcesCompat.getDrawable(context.resources, resId, theme)

}