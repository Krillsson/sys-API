package com.krillsson.sysapi.graphql.scalars

import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import java.time.DateTimeException
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeParseException
import java.time.temporal.TemporalAccessor
import java.util.*
import java.util.function.Function

class LocalDateTimeCoercing : Coercing<LocalDateTime, LocalDateTime> {
    override fun serialize(input: Any): LocalDateTime {
        val zonedDateTime: Optional<LocalDateTime>
        zonedDateTime = if (input is String) {
            Optional.of(
                    parseLocalDateTime(
                            input.toString(),
                            Function { message: String? -> CoercingSerializeException(message) })
            )
        } else {
            toLocalDateTime(input, Function { message: String? -> CoercingSerializeException(message) })
        }
        if (zonedDateTime.isPresent) {
            return zonedDateTime.get()
        }
        throw CoercingSerializeException("Expected a 'LocalDateTime' like object but was '" + input::class.java.simpleName.toString() + "'.")
    }

    override fun parseValue(input: Any): LocalDateTime {
        if (input is String) {
            return parseLocalDateTime(
                    input.toString(),
                    Function { message: String? -> CoercingParseValueException(message) })
        }
        val zonedDateTime =
                toLocalDateTime(input, Function { message: String? -> CoercingParseValueException(message) })
        if (!zonedDateTime.isPresent) {
            throw CoercingParseValueException("Expected a 'LocalDateTime' like object but was '" + input::class.java.simpleName.toString() + "'.")
        }
        return zonedDateTime.get()
    }

    override fun parseLiteral(input: Any): LocalDateTime {
        if (input !is StringValue) {
            throw CoercingParseLiteralException("Expected AST type 'StringValue' but was '" + input::class.java.simpleName.toString() + "'.")
        }
        return parseLocalDateTime(input.value, Function { message: String? -> CoercingParseLiteralException(message) })
    }

    companion object {
        private fun parseLocalDateTime(
                input: String,
                exceptionMaker: Function<String?, RuntimeException>
        ): LocalDateTime {
            return try {
                LocalDateTime.parse(input)
            } catch (e: DateTimeParseException) {
                throw exceptionMaker.apply(e.message)
            }
        }

        private fun toLocalDateTime(
                input: Any,
                exceptionMaker: Function<String?, RuntimeException>
        ): Optional<LocalDateTime> {
            try {
                if (input is TemporalAccessor) {
                    return Optional.of(LocalDateTime.from(input))
                }
            } catch (e: DateTimeException) {
                throw exceptionMaker.apply(e.message)
            }
            return Optional.empty()
        }
    }
}