package com.krillsson.sysapi.graphql.scalars

import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import java.time.DateTimeException
import java.time.ZonedDateTime
import java.time.format.DateTimeParseException
import java.time.temporal.TemporalAccessor
import java.util.*
import java.util.function.Function

class ZonedDateTimeCoercing : Coercing<ZonedDateTime, ZonedDateTime> {
    override fun serialize(input: Any): ZonedDateTime {
        val zonedDateTime: Optional<ZonedDateTime>
        zonedDateTime = if (input is String) {
            Optional.of(parseZonedDateTime(input.toString(), Function { message: String? -> CoercingSerializeException(message) }))
        } else {
            toZonedDateTime(input, Function { message: String? -> CoercingSerializeException(message) })
        }
        if (zonedDateTime.isPresent) {
            return zonedDateTime.get()
        }
        throw CoercingSerializeException("Expected a 'ZonedDateTime' like object but was '" + input::class.java.simpleName.toString() + "'.")
    }

    override fun parseValue(input: Any): ZonedDateTime {
        if (input is String) {
            return parseZonedDateTime(input.toString(), Function { message: String? -> CoercingParseValueException(message) })
        }
        val zonedDateTime = toZonedDateTime(input, Function { message: String? -> CoercingParseValueException(message) })
        if (!zonedDateTime.isPresent) {
            throw CoercingParseValueException("Expected a 'ZonedDateTime' like object but was '" + input::class.java.simpleName.toString() + "'.")
        }
        return zonedDateTime.get()
    }

    override fun parseLiteral(input: Any): ZonedDateTime {
        if (input !is StringValue) {
            throw CoercingParseLiteralException("Expected AST type 'StringValue' but was '" + input::class.java.simpleName.toString() + "'.")
        }
        return parseZonedDateTime(input.value, Function { message: String? -> CoercingParseLiteralException(message) })
    }

    companion object {
        private fun parseZonedDateTime(input: String, exceptionMaker: Function<String?, RuntimeException>): ZonedDateTime {
            return try {
                ZonedDateTime.parse(input)
            } catch (e: DateTimeParseException) {
                throw exceptionMaker.apply(e.message)
            }
        }

        private fun toZonedDateTime(input: Any, exceptionMaker: Function<String?, RuntimeException>): Optional<ZonedDateTime> {
            try {
                if (input is TemporalAccessor) {
                    return Optional.of(ZonedDateTime.from(input))
                }
            } catch (e: DateTimeException) {
                throw exceptionMaker.apply(e.message)
            }
            return Optional.empty()
        }
    }
}