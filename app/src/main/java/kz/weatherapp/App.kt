package kz.weatherapp

import android.app.Application
import com.google.android.libraries.places.api.Places
import kz.data.di.dataModule
import kz.data.retrofitModule
import kz.weatherapp.di.baseModule
import kz.weatherapp.di.domainModule
import kz.weatherapp.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        Places.initialize(applicationContext, BuildConfig.PLACES_API_KEY)

        startKoin {
            androidContext(this@App)
            modules(
                listOf(retrofitModule, domainModule, dataModule, presentationModule, baseModule)
            )
        }
    }
}