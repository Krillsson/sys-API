package com.krillsson.sysapi.graphql.scalars

import graphql.language.IntValue
import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import java.time.DateTimeException
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.TemporalAccessor
import java.util.*
import java.util.function.Function

class InstantCoercing : Coercing<Instant?, String> {
    override fun serialize(input: Any): String {
        val instant: Optional<Instant>
        instant = if (input is String) {
            Optional.ofNullable(
                parseInstant(
                    input.toString(),
                    Function { message: String? -> CoercingSerializeException(message) })
            )
        } else {
            toInstant(input, Function { message: String? -> CoercingSerializeException(message) })
        }
        if (instant.isPresent) {
            return DateTimeFormatter.ISO_INSTANT.format(instant.get())
        }
        throw CoercingSerializeException("Expected a 'String' or 'Long' but was '" + input::class.java.simpleName.toString() + "'.")
    }

    override fun parseValue(input: Any): Instant {
        if (input is String) {
            return parseInstant(input.toString(), Function { message: String? -> CoercingParseValueException(message) })
        }
        val instant = toInstant(input, Function { message: String? -> CoercingParseValueException(message) })
        if (!instant.isPresent) {
            throw CoercingParseValueException("Expected a 'Instant' like object but was '" + input::class.java.simpleName.toString() + "'.")
        }
        return instant.get()
    }

    override fun parseLiteral(input: Any): Instant {
        if (input is StringValue) {
            return parseInstant(input.value, Function { message: String? -> CoercingParseLiteralException(message) })
        }
        if (input is IntValue) {
            return parseInstant(
                input.value.longValueExact(),
                Function { message: String? -> CoercingParseLiteralException(message) })
        }
        throw CoercingParseLiteralException("Expected AST type 'StringValue' or 'IntValue' but was '" + input::class.java.simpleName.toString() + "'.")
    }

    companion object {
        private fun parseInstant(input: Any, exceptionMaker: Function<String?, RuntimeException>): Instant {
            return try {
                when (input) {
                    is String -> Instant.parse(input)
                    is Long -> Instant.ofEpochSecond(input)
                    else -> throw exceptionMaker.apply("Unable to parse $input")
                }
            } catch (e: DateTimeParseException) {
                throw exceptionMaker.apply(e.message)
            }
        }

        private fun toInstant(input: Any, exceptionMaker: Function<String?, RuntimeException>): Optional<Instant> {
            try {
                if (input is TemporalAccessor) {
                    return Optional.of(Instant.from(input))
                } else if (input is Long) {
                    return Optional.of(Instant.ofEpochSecond(input))
                }
            } catch (e: DateTimeException) {
                throw exceptionMaker.apply(e.message)
            }
            return Optional.empty()
        }
    }
}