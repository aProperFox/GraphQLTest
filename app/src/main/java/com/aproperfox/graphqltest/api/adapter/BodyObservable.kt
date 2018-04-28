package com.aproperfox.graphqltest.api.adapter

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.exceptions.CompositeException
import io.reactivex.exceptions.Exceptions
import io.reactivex.plugins.RxJavaPlugins
import retrofit2.HttpException
import retrofit2.Response


class BodyObservable<T>(private val upstream: Observable<Response<T>>) : Observable<T>() {

  companion object {
    class BodyObserver<R>(private val observer: Observer<in R>) : Observer<Response<R>> {
      private var terminated: Boolean = false

      override fun onComplete() {
        if (!terminated) {
          observer.onComplete()
        }
      }

      override fun onSubscribe(d: Disposable) {
        observer.onSubscribe(d)
      }

      override fun onNext(response: Response<R>) {
        if (response.isSuccessful) {
          observer.onNext(response.body()!!)
        } else {
          terminated = true
          val t = HttpException(response)
          try {
            observer.onError(t)
          } catch (inner: Throwable) {
            Exceptions.throwIfFatal(inner)
            RxJavaPlugins.onError(CompositeException(t, inner))
          }
        }
      }

      override fun onError(e: Throwable) {
        if (!terminated) {
          observer.onError(e)
        } else {
          val broken = AssertionError("This should never happen! Report as bug with stacktrace.")
          broken.initCause(e)
          RxJavaPlugins.onError(broken)
        }
      }

    }
  }

  override fun subscribeActual(observer: Observer<in T>) {
    upstream.subscribe(BodyObserver(observer))
  }
}