package kz.weatherapp.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kz.domain.RemoteUseCaseResult
import kz.weatherapp.R
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.CoroutineContext

open class  BaseViewModel : ViewModel(), KoinComponent {

    protected val resourcesManager: BaseResourcesManager by inject()

    suspend fun <T : Any> RemoteUseCaseResult<T>.doOnSuccess(
        context: CoroutineContext = Dispatchers.Main,
        block: suspend CoroutineScope.(T) -> Unit
        ): RemoteUseCaseResult<T> {
        if (this is RemoteUseCaseResult.Success) {
            withContext(context) { block(data) }
        }

        return this
    }

    suspend fun <T : Any> RemoteUseCaseResult<T>.doOnComplete(
        context: CoroutineContext = Dispatchers.Main,
        block: suspend CoroutineScope.() -> Unit
    ): RemoteUseCaseResult<T> {
        withContext(context) { block() }
        return this
    }

    suspend fun <T : Any> RemoteUseCaseResult<T>.doOnError(
        context: CoroutineContext = Dispatchers.Main,
        block: suspend CoroutineScope.(String) -> Unit
    ): RemoteUseCaseResult<T> {
        if (this is RemoteUseCaseResult.Error) {
            val defaultMessage = resourcesManager.getString(R.string.general_error_message)

            val message = when (this) {
                is RemoteUseCaseResult.Error.ServerError -> message
                is RemoteUseCaseResult.Error.RequestError -> resourcesManager.getString(R.string.request_error)
                is RemoteUseCaseResult.Error.Failure -> exception?.message
                is RemoteUseCaseResult.Error.NetworkError -> resourcesManager.getString(R.string.no_network_message)
                else -> defaultMessage
            } ?: defaultMessage

            withContext(context) {
                block(message)
            }
        }
        return this
    }
}