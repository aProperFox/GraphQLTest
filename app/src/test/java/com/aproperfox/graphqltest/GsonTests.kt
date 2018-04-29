package com.aproperfox.graphqltest

import com.aproperfox.graphqltest.api.converter.GraphQLRequestBodyConverter
import com.google.gson.Gson
import junit.framework.Assert.assertEquals
import okhttp3.internal.Util.UTF_8
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.OutputStreamWriter


@RunWith(JUnit4::class)
class GsonTests {

  companion object {
    val REGEX = Regex("[\\s\n\t]")
  }

  val gson = Gson()

  data class Request(
      val magic: String = "Stringular",
      val count: Int = 4,
      val bool: Boolean = true
  )

  val JSON = "{\"magic\": \"Stringular\",\"count\": 4,\"bool\": true}"
  val GQL_REQ = "magic:\"Stringular\",count:4.0,bool:true,"
  val GQL_RES = "dog"
  val GQL_QUERY = "{\"query\":\"{ doIt($GQL_REQ) { $GQL_RES }}\"}"

  val requestConverter = GraphQLRequestBodyConverter<Request>(gson, "dog", "doIt")

  @Test
  fun testGsonRead() {
    val res = gson.toJson(Request()).replace(REGEX, "")
    assertEquals(res, JSON.replace(REGEX, ""))
  }

  @Test
  fun testRequestConverter() {
    val res = requestConverter.jsonToPayload(Request())
    assertEquals(res, GQL_REQ)
  }

  @Test
  fun testRequestQueryConverter() {
    val buffer = okio.Buffer()
    val write = OutputStreamWriter(buffer.outputStream(), UTF_8)
    val payload = requestConverter.jsonToPayload(Request())
    requestConverter.writePayload(gson.newJsonWriter(write), payload)
    val res = buffer.readByteString().utf8()
    assertEquals(res.replace("\\", ""), GQL_QUERY)
  }

}