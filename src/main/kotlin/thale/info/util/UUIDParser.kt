package thale.info.util

import thale.info.exception.validation.FormatProblem
import java.util.*

/**
 * Parses a string into a [UUID].
 * Throws a [FormatProblem], if the string has not the correct format for an [UUID]
 */
fun parseUUID(uuid: String) : UUID {
    try {
        return UUID.fromString(uuid)
    } catch (e: IllegalArgumentException) {
        throw FormatProblem("$uuid does not match the format of an UUID")
    }
}