package com.krillsson.sysapi.graphql.scalars

import graphql.language.StringValue
import graphql.schema.Coercing

class LongCoercing : Coercing<Any?, String> {
    override fun serialize(input: Any): String {
        return (input as Long).toString()
    }

    override fun parseValue(input: Any): Any? {
        return serialize(input)
    }

    override fun parseLiteral(input: Any): Long? {
        return when (input) {
            is StringValue -> (input as Long).toLong()
            is Long -> input.toLong()
            else -> null
        }
    }
}