package thale.info.exception.authentication

import org.http4k.core.Status
import thale.info.api.model.LoginMethodType
import thale.info.exception.BaseProblem

/**
 * Thrown when trying to login with an unsupported [LoginMethodType]
 */
class UnsupportedLoginMethodProblem(method: LoginMethodType)
    : BaseProblem(Status.BAD_REQUEST, "Login method ${method.name} is not supported", "")