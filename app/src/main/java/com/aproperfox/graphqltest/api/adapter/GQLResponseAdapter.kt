package com.aproperfox.graphqltest.api.adapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter


class GQLResponseAdapter: TypeAdapter<String>() {
  override fun write(out: JsonWriter?, value: String?) {
  }

  override fun read(obj: JsonReader?): String {
    TODO()
  }

  private fun StringBuilder.addWhitespace(count: Int) {
    (0..count)
        .forEach {
          append("\\s")
        }
  }

  private fun StringBuilder.newLine() = append("\n")
}