package kz.domain

sealed class RemoteUseCaseResult<out T : Any> {

    data class Success<T : Any>(val data: T) : RemoteUseCaseResult<T>()

    sealed class Error : RemoteUseCaseResult<Nothing>() {
        object NetworkError : Error()
        object EmptyResponse : Error()
        data class ServerError(val message: String?) : Error()
        data class RequestError(val message: String?) : Error()
        data class Failure(
            val statusCode: Int? = null,
            val errorResponse: String,
            val exception: Throwable? = null
        ) : Error()
    }
}




inline fun <V1 : Any, V2 : Any> RemoteUseCaseResult<V1>.map(f: (V1) -> V2): RemoteUseCaseResult<V2> =
    when (this) {
        is RemoteUseCaseResult.Error -> this
        is RemoteUseCaseResult.Success -> f(data).asSuccess()
    }

fun <T:Any> T.asSuccess(): RemoteUseCaseResult<T> {
    return RemoteUseCaseResult.Success(this)
}

fun String.asFailure(): RemoteUseCaseResult<Nothing> {
    return RemoteUseCaseResult.Error.Failure(errorResponse = this)
}