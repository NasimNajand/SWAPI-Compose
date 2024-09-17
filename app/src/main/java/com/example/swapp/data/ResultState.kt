package com.example.swapp.data

sealed class RemoteResultState<out T> {
    data class Success<out T>(val data: T) : RemoteResultState<T>()
    data class Error(val exception: Throwable?) : RemoteResultState<Nothing>()
    data object Loading : RemoteResultState<Nothing>()
}