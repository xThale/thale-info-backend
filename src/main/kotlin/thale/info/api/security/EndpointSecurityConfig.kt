package thale.info.api.security

import org.http4k.core.Method

/**
 * Returns the required roles for an endpoint. If the endpoint is not secured by roles, returns null
 */
fun getRequiredRolesForEndpoint(endpointSignature: EndpointSignature) =
    EndpointRoleMap[EndpointRoleMap.keys.firstOrNull{ it.toRegex().matches(endpointSignature.signature) }]

/**
 * A map containing one of the required roles for an endpoint. The user needs to have at least one of those roles.
 * The endpoint string should have the format of [EndpointSignature.signature], e. g. "METHOD PATH"
 * The endpoint string is a [Regex] which should match the corresponding endpoint signature mentioned above.
 * E. g. in this map "POST /test/{id}/endpoint" matches the endpoint for a POST request to the uri /test/2123/endpoint
 */
private val EndpointRoleMap = linkedMapOf(
    "POST /users/{uuid}/roles" to listOf(Roles.ADMIN.name)
).apply {

    // Replaces "{xxx}" with a regex which allows every content in between the brackets.
    this.keys
        .filter { it.contains("{") }
        .forEach {
            this[it.replace("\\{[^}]*}".toRegex(), "([^/]*)")] = this[it] ?: emptyList()
            this.remove(it)
        }
}

/**
 * Check if the endpoint is secured, by looking up [unsecuredEndpoints].
 */
fun isSecuredEndpoint(endpointSignature: EndpointSignature) = unsecuredEndpoints.contains(endpointSignature)

/**
 * List of all endpoints, which should be unsecured. Meaning no authentication or authorization.
 */
private val unsecuredEndpoints = listOf(
    EndpointSignature(Method.POST, "/auth/login")
)