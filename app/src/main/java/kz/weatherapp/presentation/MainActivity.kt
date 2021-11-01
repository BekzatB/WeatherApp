package kz.weatherapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kz.weatherapp.R
import kz.weatherapp.databinding.ActivityMainBinding
import kz.weatherapp.presentation.main.MainFragment
import kz.weatherapp.utils.AlertManager

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

    fun handleError(
        params: Bundle? = null,
        errorDetails: String? = null,
        okAction: (() -> Unit)? = null
    ) {
        AlertManager.showErrorAlert(this, errorDetails, okAction)
    }
}