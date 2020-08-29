package com.krillsson.sysapi.graphql.scalars

import graphql.schema.GraphQLScalarType

interface ScalarTypes {
    companion object {

        val uuid = GraphQLScalarType.newScalar()
                .name("UUID")
                .description("UUID GraphQL ScalarType")
                .coercing(UuidCoercing())
                .build()

        val zonedDateTime = GraphQLScalarType.newScalar()
                .name("ZonedDateTime")
                .description("JDK8 ZonedDateTime GraphQL ScalarType")
                .coercing(ZonedDateTimeCoercing())
                .build()

        val duration = GraphQLScalarType.newScalar()
                .name("Duration")
                .description("JDK8 Duration GraphQL ScalarType")
                .coercing(DurationCoercing())
                .build()

        val period = GraphQLScalarType.newScalar()
                .name("Period")
                .description("JDK8 Period GraphQL ScalarType")
                .coercing(PeriodCoercing())
                .build()

        val long = GraphQLScalarType.newScalar()
            .name("Long")
            .description("Long GraphQL ScalarType")
            .coercing(LongCoercing())
            .build()

        val scalars = listOf(uuid, zonedDateTime, duration, period, long)
    }


}