package com.example.nytimes.utils

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
): Resource<T> {
    return withContext(dispatcher) {
        try {
            Resource.Success(apiCall.invoke())

        } catch (throwable: Throwable) {
            Log.e("", "$throwable   ${throwable.localizedMessage}")
            when (throwable) {
                is IOException -> {
                    Resource.Error(throwable.message.toString(), null)
                }
                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse = convertErrorBody(throwable)
                    Resource.Error(errorResponse?.message.toString(), null)
                }
                is KotlinNullPointerException -> {
                    Resource.Error("Null pointer Exception")
                }
                else -> {
                    Resource.Error("Some error has occurred", null)
                }
            }
        }
    }
}

fun convertErrorBody(throwable: HttpException): ErrorResponse? {
    val s = throwable.response()?.errorBody()?.string()
    s?.let { Log.e("", it) }

    return if (s != null) {
        ErrorResponse(s)
    } else {
        null
    }
}
