package com.krillsson.sysapi.graphql.scalars

import graphql.language.IntValue
import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import java.time.DateTimeException
import java.time.Duration
import java.time.temporal.TemporalAmount
import java.util.*
import java.util.function.Function

class DurationCoercing : Coercing<Duration?, String> {
    override fun serialize(input: Any): String {
        val duration: Optional<Duration> = if (input is String) {
            Optional.ofNullable(parseDuration(
                input.toString()
            ) { message: String? -> CoercingSerializeException(message) })
        } else {
            toDuration(input) { message: String? -> CoercingSerializeException(message) }
        }
        if (duration.isPresent) {
            return duration.get().toString()
        }
        throw CoercingSerializeException("Expected one of 'String' 'Integer' 'Long' 'TemporalAmount' but was '" + input::class.java.simpleName.toString() + "'.")
    }

    override fun parseValue(input: Any): Duration {
        if (input is String) {
            return parseDuration(input) { message: String? -> CoercingParseValueException(message) }
        }
        val duration = toDuration(input) { message: String? -> CoercingParseValueException(message) }
        if (!duration.isPresent) {
            throw CoercingParseValueException("Expected a 'Duration' like object but was '" + input::class.java.simpleName.toString() + "'.")
        }
        return duration.get()
    }

    override fun parseLiteral(input: Any): Duration {
        if (input is StringValue) {
            return parseDuration(input.value, Function { message: String? -> CoercingParseLiteralException(message) })
        }
        if (input is IntValue) {
            return parseDuration(
                input.value.longValueExact(),
                Function { message: String? -> CoercingParseLiteralException(message) })
        }
        throw CoercingParseLiteralException("Expected AST type 'StringValue' or 'IntValue' but was '" + input::class.java.simpleName.toString() + "'.")
    }

    companion object {
        private fun parseDuration(input: Any, exceptionMaker: Function<String?, RuntimeException>): Duration {
            return try {
                when (input) {
                    is String -> Duration.parse(input)
                    is Long -> Duration.ofSeconds(input)
                    else -> throw exceptionMaker.apply("Unable to parse $input")
                }
            } catch (e: Exception) {
                throw exceptionMaker.apply(e.message)
            }
        }

        private fun toDuration(input: Any, exceptionMaker: Function<String?, RuntimeException>): Optional<Duration> {
            try {
                when (input) {
                    is TemporalAmount -> return Optional.of(Duration.from(input))
                    is Int -> return Optional.of(Duration.ofSeconds(input.toLong()))
                    is Long -> return Optional.of(Duration.ofSeconds(input))
                }
            } catch (e: ArithmeticException) {
                throw exceptionMaker.apply(e.message)
            } catch (e: DateTimeException) {
                throw exceptionMaker.apply(e.message)
            }
            return Optional.empty()
        }
    }
}