package thale.info.api.security.authorization

import thale.info.api.security.EndpointSignature
import thale.info.dataaccess.User

interface AuthorizationService {

    /**
     * Checks if the user has one of the required roles for the endpoint signature.
     * @return True if the user has at least one required role, false if not
     */
    fun authorized(user: User, endpointSignature: EndpointSignature): Boolean

}