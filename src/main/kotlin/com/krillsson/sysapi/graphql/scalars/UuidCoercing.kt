package com.krillsson.sysapi.graphql.scalars

import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import java.util.*
import java.util.function.Function

class UuidCoercing : Coercing<UUID, UUID> {
    override fun serialize(input: Any): UUID {
        val uuid: Optional<UUID>
        uuid = if (input is String) {
            Optional.of(
                    parseUUID(
                            input.toString(),
                            Function { message: String? -> CoercingSerializeException(message) })
            )
        } else {
            toUUID(input)
        }
        if (uuid.isPresent) {
            return uuid.get()
        }
        throw CoercingSerializeException("Expected a 'UUID' like object but was '" + input::class.java.simpleName.toString() + "'.")
    }

    override fun parseValue(input: Any): UUID {
        return if (input is String) {
            parseUUID(input.toString(), Function { message: String? -> CoercingParseValueException(message) })
        } else {
            val uuid = toUUID(input)
            if (!uuid.isPresent) {
                throw CoercingParseValueException("Expected a 'UUID' like object but was '" + input::class.java.simpleName.toString() + "'.")
            }
            uuid.get()
        }
    }

    override fun parseLiteral(input: Any): UUID {
        if (input !is StringValue) {
            throw CoercingParseLiteralException("Expected AST type 'StringValue' but was '" + input::class.java.simpleName.toString() + "'.")
        }
        return parseUUID(input.value, Function { message: String? -> CoercingParseLiteralException(message) })
    }

    companion object {
        private fun parseUUID(input: String, exceptionMaker: Function<String?, RuntimeException>): UUID {
            return try {
                UUID.fromString(input)
            } catch (e: IllegalArgumentException) {
                throw exceptionMaker.apply(e.message)
            }
        }

        private fun toUUID(input: Any): Optional<UUID> {
            return if (input is UUID) {
                Optional.of(input)
            } else Optional.empty()
        }
    }
}