package com.aproperfox.graphqltest.api.converter

import com.google.gson.Gson
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type


class GraphQLConverterFactory<T>(private val gson: Gson) : Converter.Factory() {

  override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>, retrofit: Retrofit): Converter<ResponseBody, *>? {
    return GsonConverterFactory.create(gson).responseBodyConverter(type, annotations, retrofit)
  }

  override fun requestBodyConverter(type: Type, parameterAnnotations: Array<out Annotation>, methodAnnotations: Array<out Annotation>, retrofit: Retrofit): Converter<T, RequestBody>? {
    val method = methodAnnotations.first {
      it is GQLQuery || it is GQLMutation
    }.let {
      when (it) {
        is GQLQuery -> it.methodName
        is GQLMutation -> it.methodName
        else -> throw IllegalStateException("GraphQL methods must be annotated with either @GQLQuery(methodName: String) or @GQLMutation(methodName: String)")
      }
    }
    return GraphQLRequestBodyConverter(gson, "", method)
  }
}