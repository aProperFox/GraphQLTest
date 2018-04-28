package com.aproperfox.graphqltest.api.adapter

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.exceptions.CompositeException
import io.reactivex.exceptions.Exceptions
import io.reactivex.plugins.RxJavaPlugins
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CallObservable<T>(private val originalCall: Call<T>) : Observable<Response<T>>() {

  companion object {
    private class CallCallback<T>(
        private val call: Call<T>,
        private val observer: Observer<in Response<T>>
    ) : Disposable, Callback<T> {
      @Volatile
      private var disposed: Boolean = false
      private var terminated = false

      override fun isDisposed(): Boolean = disposed

      override fun dispose() {
        disposed = true
        call.cancel()
      }

      override fun onFailure(call: Call<T>, t: Throwable) {
        if (call.isCanceled) return

        try {
          observer.onError(t)
        } catch (inner: Throwable) {
          Exceptions.throwIfFatal(inner)
          RxJavaPlugins.onError(CompositeException(t, inner))
        }
      }

      override fun onResponse(call: Call<T>, response: Response<T>) {
        if (disposed) return
        try {
          observer.onNext(response)

          if (!isDisposed) {
            terminated = true
            observer.onComplete()
          }
        } catch (t: Throwable) {
          if (terminated) {
            RxJavaPlugins.onError(t)
          } else if (!disposed) {
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
  }

  override fun subscribeActual(observer: Observer<in Response<T>>) {
    val call = originalCall.clone()
    val callback = CallCallback(call, observer)
    observer.onSubscribe(callback)
    call.enqueue(callback)
  }
}