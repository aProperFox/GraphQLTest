package com.aproperfox.graphqltest.api.adapter

import retrofit2.Response


class Result<T>(private val response: Response<T>?,
                private val error: Throwable?) {

  companion object {
    @JvmStatic fun <T> response(response: Response<T>): Result<T> =
        Result(response, null)

    @JvmStatic fun <T> error(error: Throwable): Result<T> =
      Result(null, error)
  }

  fun response(): Response<T>? = response

  fun error(): Throwable? = error

  fun isError(): Boolean = error != null
}