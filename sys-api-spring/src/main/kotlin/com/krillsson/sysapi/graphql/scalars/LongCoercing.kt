package com.krillsson.sysapi.graphql.scalars

import graphql.language.IntValue
import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException

class LongCoercing : Coercing<Long?, String?> {
    override fun serialize(dataFetcherResult: Any): String? {
        return try {
            val value = dataFetcherResult as Long
            value.toString()
        } catch (e: Exception) {
            throw CoercingSerializeException(e)
        }
    }

    override fun parseValue(input: Any): Long {
        return parse(input)
    }

    override fun parseLiteral(input: Any): Long {
        return parse(input)
    }

    private fun parse(input: Any): Long {
        return try {
            when (input) {
                is String -> input.toLong()
                is StringValue -> input.value.toLong()
                is IntValue -> input.value.toLong()
                is Int -> input.toLong()
                is Long -> input
                else -> throw CoercingParseValueException("$input is not any known type")
            }
        } catch (e: Exception) {
            throw CoercingParseValueException(e)
        }
    }
}