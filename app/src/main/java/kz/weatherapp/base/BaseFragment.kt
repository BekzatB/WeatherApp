package kz.weatherapp.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import kz.weatherapp.presentation.MainActivity

open class BaseFragment : Fragment() {


    protected fun <T> LiveData<T>.subscribe(observer: (result: T) -> Unit) {
        this.observe(this@BaseFragment.viewLifecycleOwner, observer)
    }

    open fun handleError(
        params: Bundle? = null,
        errorDetails: String? = null,
        okAction: (() -> Unit)? = null
    ) = (activity as? MainActivity)?.handleError(
        params = params,
        okAction = okAction,
        errorDetails = errorDetails
    )
}