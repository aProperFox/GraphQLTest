package com.aproperfox.graphqltest.api.adapter

import io.reactivex.*
import io.reactivex.schedulers.Schedulers
import retrofit2.CallAdapter
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class GraphQLAdapterFactory(private val scheduler: Scheduler) : CallAdapter.Factory() {

  companion object {
    @JvmStatic
    fun createWithScheduler(scheduler: Scheduler = Schedulers.io()): GraphQLAdapterFactory =
        GraphQLAdapterFactory(scheduler)

    val TYPES = listOf(Observable::class.java, Single::class.java, Flowable::class.java, Maybe::class.java, Completable::class.java)
  }

  override fun get(returnType: Type, annotations: Array<out Annotation>, retrofit: Retrofit): CallAdapter<*, *> {
    val rawType = getRawType(returnType)

    if (rawType == Completable::class.java) {
      return GraphQLAdapter<Void>(Void::class.java, scheduler, false, true, rawType)
    }

    if (rawType !in TYPES) {
      throw IllegalArgumentException("Return type must be $TYPES")
    }

    if (returnType !is ParameterizedType) {
      throw IllegalStateException("$rawType return type must be parameterized")
    }

    var isResult = false
    var isBody = false
    val responseType: Type
    val observableType: Type = getParameterUpperBound(0, returnType)
    val rawObservableType = getRawType(observableType)
    when (rawObservableType) {
      Response::class.java -> {
        require(observableType is ParameterizedType, { throw IllegalStateException("Response must be parameterized") })
        responseType = getParameterUpperBound(0, observableType as ParameterizedType)
      }
      Result::class.java -> {
        require(observableType is ParameterizedType, { throw IllegalStateException("Result must be parameterized") })
        responseType = getParameterUpperBound(0, observableType as ParameterizedType)
        isResult = true
      }
      else -> {
        responseType = observableType
        isBody = true
      }
    }
    return GraphQLAdapter<Any>(responseType, scheduler, isResult, isBody, rawType)
  }
}