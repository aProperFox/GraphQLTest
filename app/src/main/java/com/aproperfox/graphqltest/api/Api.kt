package com.aproperfox.graphqltest.api

import com.aproperfox.graphqltest.api.adapter.GraphQLAdapterFactory
import com.google.gson.Gson
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object Api {
  val gson: Gson = Gson()

  private val retrofit: Retrofit by lazy {
    Retrofit.Builder()
        .baseUrl("https://www.graphqlhub.com/graphql")
        //.addConverterFactory()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(GraphQLAdapterFactory.createWithScheduler(Schedulers.io()))
        .build()
  }
}