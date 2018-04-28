package com.aproperfox.graphqltest.api.adapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class GQLRequestAdapter(private val mutation: String, private val payload: String) : TypeAdapter<String>() {

  override fun write(out: JsonWriter, value: String) {
    out.beginObject()
    out.name("query")
    out.value("{ $mutation($value) { $payload }}")
    out.endObject()
  }

  override fun read(obj: JsonReader): String {
    val builder = StringBuilder()
    var count = 0
    while (obj.hasNext()) {
      val read = obj.peek()
      when (read) {
        JsonToken.BEGIN_ARRAY -> {
          count++
          builder.append("[")
          obj.beginArray()
        }
        JsonToken.END_ARRAY -> {
          count--
          builder.append("]")
          obj.endArray()
        }
        JsonToken.BEGIN_OBJECT -> {
          count++
          builder.append("{")
          obj.beginObject()
        }
        JsonToken.END_OBJECT -> {
          count--
          builder.append("}")
          obj.endObject()
        }
        JsonToken.NAME -> {
          val name = obj.nextName()
          if (obj.peek() != JsonToken.NULL) {
            builder.append(name).colon()
          } else {
            obj.skipValue()
          }
        }
        JsonToken.STRING -> builder.append("\"${obj.nextString()}\"").comma()
        JsonToken.NUMBER -> builder.append(obj.nextDouble()).comma()
        JsonToken.BOOLEAN -> builder.append(obj.nextBoolean()).comma()
        else -> { }
      }
    }
    return builder.toString()
  }

  private fun StringBuilder.colon() = append(':')
  private fun StringBuilder.comma() = append(',')

}