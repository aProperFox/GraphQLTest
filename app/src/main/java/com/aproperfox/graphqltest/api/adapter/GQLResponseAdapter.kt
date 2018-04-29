package com.aproperfox.graphqltest.api.adapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter


class GQLResponseAdapter : TypeAdapter<String>() {
  override fun write(out: JsonWriter, value: String) {
    // no-op
  }

  override fun read(obj: JsonReader): String {
    val builder = StringBuilder()
    var count = 0
    while (obj.hasNext()) {
      val read = obj.peek()
      if (read != JsonToken.NULL) {
        builder.addWhitespace(count)
      }
      when (read) {
        JsonToken.BEGIN_ARRAY -> {
          count++
          builder.append("[").newLine()
          obj.beginArray()
        }
        JsonToken.END_ARRAY -> {
          count--
          builder.append("]").newLine()
          obj.endArray()
        }
        JsonToken.BEGIN_OBJECT -> {
          count++
          builder.append("{").newLine()
          obj.beginObject()
        }
        JsonToken.END_OBJECT -> {
          count--
          builder.append("}").newLine()
          obj.endObject()
        }
        JsonToken.NAME -> builder.append(obj.nextName()).comma().newLine()
        JsonToken.BOOLEAN, JsonToken.NUMBER, JsonToken.STRING -> obj.skipValue()
        else -> {
        }
      }
    }
    return builder.toString()
  }

  private fun StringBuilder.addWhitespace(count: Int) {
    append("  ".repeat(count))
  }

  private fun StringBuilder.newLine() = append("\n")

  private fun StringBuilder.comma() = append(',')
}