package thale.info.util

import thale.info.exception.validation.FormatValidationProblem
import java.util.*

/**
 * Parses a string into a [UUID].
 * Throws a [FormatValidationProblem], if the string has not the correct format for an [UUID]
 */
fun parseUUID(uuid: String) : UUID {
    try {
        return UUID.fromString(uuid)
    } catch (e: IllegalArgumentException) {
        throw FormatValidationProblem("$uuid does not match the format of an UUID")
    }
}

/**
 * Creates a new random [UUID]
 */
fun createUUID() : UUID = UUID.randomUUID()