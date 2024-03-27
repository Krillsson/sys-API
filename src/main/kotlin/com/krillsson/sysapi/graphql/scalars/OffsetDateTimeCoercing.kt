package com.krillsson.sysapi.graphql.scalars

import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import java.time.DateTimeException
import java.time.OffsetDateTime
import java.time.format.DateTimeParseException
import java.time.temporal.TemporalAccessor
import java.util.*
import java.util.function.Function

class OffsetDateTimeCoercing : Coercing<OffsetDateTime, OffsetDateTime> {
    override fun serialize(input: Any): OffsetDateTime {
        val zonedDateTime: Optional<OffsetDateTime>
        zonedDateTime = if (input is String) {
            Optional.of(
                parseOffsetDateTime(
                    input.toString(),
                    Function { message: String? -> CoercingSerializeException(message) })
            )
        } else {
            toOffsetDateTime(input, Function { message: String? -> CoercingSerializeException(message) })
        }
        if (zonedDateTime.isPresent) {
            return zonedDateTime.get()
        }
        throw CoercingSerializeException("Expected a 'OffsetDateTime' like object but was '" + input::class.java.simpleName.toString() + "'.")
    }

    override fun parseValue(input: Any): OffsetDateTime {
        if (input is String) {
            return parseOffsetDateTime(
                input.toString(),
                Function { message: String? -> CoercingParseValueException(message) })
        }
        val zonedDateTime =
            toOffsetDateTime(input, Function { message: String? -> CoercingParseValueException(message) })
        if (!zonedDateTime.isPresent) {
            throw CoercingParseValueException("Expected a 'OffsetDateTime' like object but was '" + input::class.java.simpleName.toString() + "'.")
        }
        return zonedDateTime.get()
    }

    override fun parseLiteral(input: Any): OffsetDateTime {
        if (input !is StringValue) {
            throw CoercingParseLiteralException("Expected AST type 'StringValue' but was '" + input::class.java.simpleName.toString() + "'.")
        }
        return parseOffsetDateTime(input.value, Function { message: String? -> CoercingParseLiteralException(message) })
    }

    companion object {
        private fun parseOffsetDateTime(
            input: String,
            exceptionMaker: Function<String?, RuntimeException>
        ): OffsetDateTime {
            return try {
                OffsetDateTime.parse(input)
            } catch (e: DateTimeParseException) {
                throw exceptionMaker.apply(e.message)
            }
        }

        private fun toOffsetDateTime(
            input: Any,
            exceptionMaker: Function<String?, RuntimeException>
        ): Optional<OffsetDateTime> {
            try {
                if (input is TemporalAccessor) {
                    return Optional.of(OffsetDateTime.from(input))
                }
            } catch (e: DateTimeException) {
                throw exceptionMaker.apply(e.message)
            }
            return Optional.empty()
        }
    }
}