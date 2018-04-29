package com.aproperfox.graphqltest.api.converter

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class GQLQuery(val methodName: String)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class GQLMutation(val methodName: String)