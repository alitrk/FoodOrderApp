package com.example.foodorderapp.utils

sealed class Resource<out T> {
    object Loading : Resource<Nothing>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()

    companion object {
        fun <T> loading(): Resource<T> = Loading
        fun <T> success(data: T): Resource<T> = Success(data)
        fun <T> error(message: String): Resource<T> = Error(message)
    }
}