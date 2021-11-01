package kz.data.api

import android.util.Log
import kz.domain.RemoteUseCaseResult

sealed class NetResult<out T> {
    data class Success<T>(val data: T?) : NetResult<T>()

    sealed class Error : NetResult<Nothing>() {
        data class Failure(
            val statusCode: Int?,
            val errorResponse: String,
            val exception: Throwable? = null
        ) : Error()

        object NetworkError : Error()
    }

    fun doOnSuccess(block: (T?) -> Unit): NetResult<T> {
        if (this is Success) {
            block(data)
        }
        return this
    }

    fun doOnError(block: (Error) -> Unit): NetResult<T> {
        if (this is Error) {
            block(this)
        }
        return this
    }

}

private val INTERNAL_SERVER_ERROR = 500 until 527
private val BAD_REQUEST = 400 until 500

@JvmName("primitiveToRemoteUseCaseResult")
fun <M : Any> NetResult<M>.toRemoteUseCaseResult(): RemoteUseCaseResult<M> {
	return when (this) {
		is NetResult.Success -> {
			if (this.data != null) {
				RemoteUseCaseResult.Success(this.data)
			} else {
				RemoteUseCaseResult.Error.EmptyResponse
			}
		}

		is NetResult.Error -> {
			this.parseError()
		}
	}
}

fun <M : Any, JR : JsonResponse<M>> NetResult<JR>.toRemoteUseCaseResult(): RemoteUseCaseResult<M> {
    return when (this) {
        is NetResult.Success -> {
            if (this.data != null) {
                RemoteUseCaseResult.Success(this.data.toModel())
            } else {
                RemoteUseCaseResult.Error.EmptyResponse
            }
        }

        is NetResult.Error -> {
            this.parseError()
        }
    }
}

@JvmName("toListRemoteUseCaseResult")
fun <M : Any, JR : JsonResponse<M>> NetResult<List<JR>>.toRemoteUseCaseResult(): RemoteUseCaseResult<List<M>> {
    return when (this) {
        is NetResult.Success -> {
            if ( this.data == null || this.data.isEmpty()) {
                RemoteUseCaseResult.Error.EmptyResponse
            } else {
                val mappedList = this.data
                    .map {
                        it.toModel()
                    }
                RemoteUseCaseResult.Success(mappedList)
            }
        }

        is NetResult.Error -> {
            this.parseError()
        }
    }
}

fun <M : Any> NetResult.Error.parseError(): RemoteUseCaseResult<M> {
    return when (this) {
        is NetResult.Error.Failure -> {
            Log.d("M", "${this.errorResponse}")
            when (this.statusCode) {
                in INTERNAL_SERVER_ERROR -> {
                    RemoteUseCaseResult.Error.ServerError(this.errorResponse)
                }
                in BAD_REQUEST -> {
                    RemoteUseCaseResult.Error.RequestError(this.errorResponse.toString())
                }
                else -> {
                    RemoteUseCaseResult.Error.Failure(
                        statusCode = this.statusCode,
                        errorResponse = this.errorResponse,
                        exception = this.exception
                    )
                }
            }
        }

        is NetResult.Error.NetworkError -> {
            RemoteUseCaseResult.Error.NetworkError
        }
    }

}
