package com.aleet.chattleroyale.utils

sealed class Result<out S, out F : Exception> {

    data class Success<out S>(val successType: S) : Result<S, Nothing>()
    open class Failure(val ex: Exception) : Result<Nothing, Exception>()

    fun result(
        onSuccess: (S) -> Unit = {},
        onFailure: (Exception) -> Unit = {}

    ): Unit = when (this) {
        is Success -> onSuccess(successType)
        is Failure -> onFailure(ex)
    }
}
