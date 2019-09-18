package thale.info.exception

/**
 * The universal problem, which is returned as response on errors
 */
class Problem(val status: String,
              val message: String,
              val details: String,
              val timestamp: String)