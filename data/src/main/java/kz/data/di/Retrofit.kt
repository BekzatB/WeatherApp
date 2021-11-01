package kz.data

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kz.data.api.CallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val retrofitModule = module {

    single<Gson> { GsonBuilder().create() }
    single<Converter.Factory> { GsonConverterFactory.create(get()) }

    single<WeatherApi> {

        val retrofit = Retrofit.Builder()
            .client(get())
            .baseUrl(BuildConfig.WEATHER_API)
            .addCallAdapterFactory(CallAdapterFactory())
            .addConverterFactory(get())
            .build()

        return@single retrofit.create(WeatherApi::class.java)
    }

    single { getHttpClient() }

}

private fun getHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(getLoggingInterceptor())
        .connectTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()
}


private fun getLoggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor { message -> Log.d("OkHttp", message) }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
}