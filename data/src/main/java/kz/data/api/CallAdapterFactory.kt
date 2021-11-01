package kz.data.api

import okhttp3.Request
import okio.Timeout
import retrofit2.*
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


abstract class CallDelegate<TIn, TOut>(protected val proxy: Call<TIn>) : Call<TOut> {
    override fun execute(): Response<TOut> = throw NotImplementedError()
    final override fun enqueue(callback: Callback<TOut>) = enqueueImpl(callback)
    final override fun clone(): Call<TOut> = cloneImpl()

    override fun cancel() = proxy.cancel()
    override fun request(): Request = proxy.request()
    override fun isExecuted() = proxy.isExecuted
    override fun isCanceled() = proxy.isCanceled

    abstract fun enqueueImpl(callback: Callback<TOut>)
    abstract fun cloneImpl(): Call<TOut>
}

class CallAdapterFactory : CallAdapter.Factory() {
    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit) =
        when (getRawType(returnType)) {
            Call::class.java -> {
                val callType = getParameterUpperBound(0, returnType as ParameterizedType)
                when (getRawType(callType)) {
                    NetResult::class.java -> {
                        val resultType = getParameterUpperBound(0, callType as ParameterizedType)
                        ResultAdapter(resultType)
                    }
                    else -> null
                }
            }
            else -> null
        }
}

private val SUCCESSFULLY = 200 until 300

class ResultCall<T>(proxy: Call<T>) : CallDelegate<T, NetResult<T>>(proxy) {

    override fun enqueueImpl(callback: Callback<NetResult<T>>) =
        proxy.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val code = response.code()
                val result = if (code in SUCCESSFULLY) {
                    val body = response.body()
                    NetResult.Success(body)
                } else {
                    // todo Add error conversion from the server to the model. If necessary
                    NetResult.Error.Failure(code, response.body().toString())
                }

                callback.onResponse(this@ResultCall, Response.success(result))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val result = when (t) {
                    is IOException -> {
                        NetResult.Error.NetworkError
                    }
                    is HttpException -> {
                        val code = t.code()
                        // todo Add error conversion from the server to the model. If necessary
                        NetResult.Error.Failure(code, t.response()?.errorBody().toString())
                    }
                    else -> {
                        NetResult.Error.Failure(null, t.message ?: "")
                    }
                }

                t.printStackTrace()
                callback.onResponse(this@ResultCall, Response.success(result))
            }
        })

    override fun cloneImpl() = ResultCall(proxy.clone())

    override fun timeout(): Timeout {
        return Timeout()
    }
}

class ResultAdapter(private val type: Type) : CallAdapter<Type, Call<NetResult<Type>>> {
    override fun responseType() = type
    override fun adapt(call: Call<Type>): Call<NetResult<Type>> = ResultCall(call)
}