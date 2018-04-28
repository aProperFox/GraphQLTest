package com.aproperfox.graphqltest

import com.aproperfox.graphqltest.api.converter.GraphQLRequestBodyConverter
import com.google.gson.Gson
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


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
  val json = "{\"magic\": \"Stringular\",\"count\": 4,\"bool\": true}"
  val gql = "doIt(magic:\"Stringular\",count:4,bool:true)"

  val requestConverter = GraphQLRequestBodyConverter<Request>(gson, "", "doIt")

  @Test
  fun testGsonRead() {
    val res = gson.toJson(Request()).replace(REGEX, "")
    assert(res == json.replace(REGEX, ""))
  }

  @Test
  fun testJsonParams() {
    val res = requestConverter.jsonToPayload(Request())
  }

}