package com.aproperfox.graphqltest.api.converter

import com.aproperfox.graphqltest.api.adapter.GQLRequestAdapter
import com.google.gson.Gson
import com.google.gson.stream.JsonWriter
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.internal.Util.UTF_8
import okio.Buffer
import retrofit2.Converter
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter


class GraphQLRequestBodyConverter<T>(
    private val gson: Gson,
    response: String,
    mutation: String
) : Converter<T, RequestBody> {

  private val adapter: GQLRequestAdapter = GQLRequestAdapter(mutation, response)

  override fun convert(value: T): RequestBody {
    val payload = jsonToPayload(value)

    val buffer = Buffer()
    val writer = OutputStreamWriter(buffer.outputStream(), UTF_8)
    val jsonWriter = gson.newJsonWriter(writer)
    writePayload(jsonWriter, payload)
    return RequestBody.create(MediaType.parse("application/json"), buffer.readByteString())
  }

  fun jsonToPayload(value: T): String {
    val json = gson.toJson(value)
    val stream = ByteArrayInputStream(json.toByteArray(UTF_8))
    val reader = InputStreamReader(stream, UTF_8)
    val jsonReader = gson.newJsonReader(reader)
    return adapter.read(jsonReader)
  }

  fun writePayload(writer: JsonWriter, payload: String) {
    adapter.write(writer, payload)
    writer.close()
  }

}