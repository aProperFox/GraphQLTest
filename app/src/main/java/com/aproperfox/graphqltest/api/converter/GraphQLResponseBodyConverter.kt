package com.aproperfox.graphqltest.api.converter

import okhttp3.ResponseBody
import retrofit2.Converter


class GraphQLResponseBodyConverter<T> : Converter<ResponseBody, T> {

  override fun convert(value: ResponseBody?): T {
    TODO()
  }
}
