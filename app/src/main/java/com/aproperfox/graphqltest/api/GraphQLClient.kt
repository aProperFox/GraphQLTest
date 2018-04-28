package com.aproperfox.graphqltest.api


class GraphQLClient(val url: String) {
  fun <Req, Res> query(method: String, input: Req, payloadParams: Res): String {
    TODO()
  }

  fun <Req, Res> mutate(method: String, input: Req, payloadParams: Res): String {
    TODO()
  }
}