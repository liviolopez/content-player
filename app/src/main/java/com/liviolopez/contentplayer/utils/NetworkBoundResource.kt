package com.liviolopez.contentplayer.utils

import kotlinx.coroutines.flow.*

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline loadFromDb: () -> Flow<ResultType>,
    crossinline createCall: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {
    val data = loadFromDb().first()

    val flow = if (shouldFetch(data)) {
        emit(Resource.loading(data))

        try {
            saveFetchResult(createCall())
            loadFromDb().map { Resource.success(it) }
        } catch (t: Throwable) {
            loadFromDb().map { Resource.error(t, it) }
        }
    } else {
        loadFromDb().map { Resource.success(it) }
    }

    emitAll(flow)
}

suspend inline fun <RequestType> syncNetworkResource(
    crossinline shouldFetch: suspend () -> Boolean,
    crossinline runFetchCall: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline onFetchFailed: (Throwable) -> Unit = {},
) {
    if (shouldFetch()) {
        try {
            saveFetchResult(runFetchCall())
        } catch (t: Throwable) {
            onFetchFailed(t)
        }
    }
}