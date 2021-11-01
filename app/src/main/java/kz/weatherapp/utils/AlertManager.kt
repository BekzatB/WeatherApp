package kz.weatherapp.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import kz.weatherapp.R


object AlertManager {

    fun showOKAlert(context: Context, title: String? = null, message: String? = null, ok: (() -> Unit)? = null) {
        AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.yes) { _, _ ->
                    ok?.invoke()
                }
                .create()
                .show()
    }

    fun showOKCancelAlert(context: Context, title: String? = null, message: String? = null, ok: (() -> Unit)? = null, cancel: (() -> Unit)? = null) {
        AlertDialog.Builder(context, R.style.AlertDialogCustom)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.yes) { _, _ ->
                    ok?.invoke()
                }
                .setNegativeButton(R.string.no) { _, _ ->
                    cancel?.invoke()
                }
                .create()
                .show()
    }

    fun showErrorAlert(context: Context, message: String? = null, ok: (() -> Unit)? = null) {

        AlertDialog.Builder(context, R.style.AlertDialogCustom)
                .setCancelable(false)
                .setTitle(context.getString(R.string.error))
                .setMessage(message ?: context.getString(R.string.general_error_message))
                .setPositiveButton(R.string.ok) { _, _ ->
                    ok?.invoke()
                }
                .create()
                .show()
    }


}