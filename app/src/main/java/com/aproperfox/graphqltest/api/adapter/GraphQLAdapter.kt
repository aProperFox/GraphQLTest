package com.aproperfox.graphqltest.api.adapter

import io.reactivex.*
import io.reactivex.plugins.RxJavaPlugins
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type


class GraphQLAdapter<R>(private val responseType: Type,
                        private val scheduler: Scheduler,
                        private val isResult: Boolean = false,
                        private val isBody: Boolean = false,
                        private val rxType: Class<*>
) : CallAdapter<R, Any> {
  override fun adapt(call: Call<R>): Any {
    val responseObservable = CallObservable(call)

    val observable: Observable<*> = when {
      isResult -> ResultObservable(responseObservable)
      isBody -> BodyObservable(responseObservable)
      else -> responseObservable
    }.subscribeOn(scheduler).apply {
      when (rxType) {
        Flowable::class.java -> toFlowable(BackpressureStrategy.LATEST)
        Single::class.java -> singleOrError()
        Maybe::class.java -> singleElement()
        Completable::class.java -> ignoreElements()
      }
    }

    return RxJavaPlugins.onAssembly(observable)
  }

  override fun responseType(): Type = responseType

}