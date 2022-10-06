package com.example.commerce_test.domain.model

sealed class NetworkResult<out T, out R> {
    data class Success<out T>(val response: T) : NetworkResult<T, Nothing>()
    object Empty:NetworkResult<Nothing, Nothing>()
}
