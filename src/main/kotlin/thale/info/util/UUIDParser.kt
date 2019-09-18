package thale.info.util

import thale.info.exception.FormatProblem
import java.util.*

fun parseUUID(uuid: String) : UUID {
    try {
        return UUID.fromString(uuid)
    } catch (e: IllegalArgumentException) {
        throw FormatProblem("$uuid does not match the format of an UUID")
    }
}