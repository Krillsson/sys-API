package com.krillsson.sysapi.graphql.scalars

import graphql.language.IntValue
import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import java.time.DateTimeException
import java.time.Period
import java.time.format.DateTimeParseException
import java.time.temporal.TemporalAmount
import java.util.*
import java.util.function.Function

class PeriodCoercing : Coercing<Period?, String> {
    override fun serialize(input: Any): String {
        val period: Optional<Period>
        period = if (input is String) {
            Optional.ofNullable(
                    parsePeriod(
                            input.toString(),
                            Function { message: String? -> CoercingSerializeException(message) })
            )
        } else {
            toPeriod(input, Function { message: String? -> CoercingParseValueException(message) })
        }
        if (period.isPresent) {
            return period.get().toString()
        }
        throw CoercingSerializeException("Expected a 'String' or 'TemporalAmount' or 'Integer' but was '" + input::class.java.simpleName.toString() + "'.")
    }

    override fun parseValue(input: Any): Period {
        return if (input is String) {
            parsePeriod(input.toString(), Function { message: String? -> CoercingParseValueException(message) })
        } else {
            val period = toPeriod(input, Function { message: String? -> CoercingParseValueException(message) })
            if (!period.isPresent) {
                throw CoercingParseValueException("Expected a 'Period' like object but was '" + input::class.java.simpleName.toString() + "'.")
            }
            period.get()
        }
    }

    override fun parseLiteral(input: Any): Period {
        if (input is StringValue) {
            return parsePeriod(input.value, Function { message: String? -> CoercingParseLiteralException(message) })
        }
        if (input is IntValue) {
            return parsePeriod(
                    input.value.intValueExact(),
                    Function { message: String? -> CoercingParseLiteralException(message) })
        }
        throw CoercingParseLiteralException("Expected AST type 'StringValue' or 'IntValue' but was '" + input::class.java.simpleName.toString() + "'.")
    }

    companion object {
        private fun parsePeriod(input: Any, exceptionMaker: Function<String?, RuntimeException>): Period {
            return try {
                when (input) {
                    is String -> Period.parse(input)
                    is Int -> Period.ofDays(input)
                    else -> throw exceptionMaker.apply("Unable to parse $input")
                }
            } catch (e: DateTimeParseException) {
                throw exceptionMaker.apply(e.message)
            }
        }

        private fun toPeriod(input: Any, exceptionMaker: Function<String?, RuntimeException>): Optional<Period> {
            try {
                if (input is TemporalAmount) {
                    return Optional.of(Period.from(input))
                } else if (input is Int) {
                    return Optional.of(Period.ofDays(input))
                }
            } catch (e: DateTimeException) {
                throw exceptionMaker.apply(e.message)
            }
            return Optional.empty()
        }
    }
}