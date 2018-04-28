package com.aproperfox.graphqltest.api.converter

import com.google.gson.Gson
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type


class GraphQLConverterFactory<Req, Res> : Converter.Factory() {

  override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>, retrofit: Retrofit): Converter<ResponseBody, Req>? {
    return GraphQLResponseBodyConverter()
  }

  override fun requestBodyConverter(type: Type, parameterAnnotations: Array<out Annotation>, methodAnnotations: Array<out Annotation>, retrofit: Retrofit): Converter<Res, RequestBody>? {
    return GraphQLRequestBodyConverter(Gson(), "", "")
  }
}