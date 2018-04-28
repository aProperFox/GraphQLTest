package com.aproperfox.graphqltest.api.adapter

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.exceptions.CompositeException
import io.reactivex.exceptions.Exceptions
import io.reactivex.plugins.RxJavaPlugins
import retrofit2.Response


class ResultObservable<T>(private val upstream: Observable<Response<T>>) : Observable<Result<T>>() {

  companion object {
    class ResultObserver<R>(private val observer: Observer<in Result<R>>) : Observer<Response<R>> {
      override fun onComplete() {
        observer.onComplete()
      }

      override fun onSubscribe(d: Disposable) {
        observer.onSubscribe(d)
      }

      override fun onNext(t: Response<R>) {
        observer.onNext(Result.response(t))
      }

      override fun onError(e: Throwable) {
        try {
          observer.onNext(Result.error(e))
        } catch (t: Throwable) {
          try {
            observer.onError(t)
          } catch (inner: Throwable) {
            Exceptions.throwIfFatal(inner)
            RxJavaPlugins.onError(CompositeException(t, inner))
          }
        }
      }
    }
  }

  override fun subscribeActual(observer: Observer<in Result<T>>) {
    upstream.subscribe(ResultObserver(observer))
  }

}