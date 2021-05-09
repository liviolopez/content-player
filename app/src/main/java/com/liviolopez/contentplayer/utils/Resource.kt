package com.liviolopez.contentplayer.utils

data class Resource<T>(val status: Status, val data: T?, val throwable: Throwable?) {

    enum class Status { LOADING, ERROR, SUCCESS }

    companion object {
        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }

        fun <T> error(throwable: Throwable? = null, data: T? = null): Resource<T> {
            return Resource(Status.ERROR, data, throwable)
        }

        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }
    }
}