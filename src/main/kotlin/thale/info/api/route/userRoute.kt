package thale.info.api.route

import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.RequestContextLens
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import thale.info.api.lens.Lenses
import thale.info.api.model.RolesResponse
import thale.info.dataaccess.User
import thale.info.exception.validation.UserNotFoundProblem
import thale.info.service.UserService
import thale.info.util.extractUUID
import thale.info.validation.UserEndpointValidator

private val businessValidator = UserEndpointValidator()

fun userRoute(userContext :  RequestContextLens<User>,
              service: UserService): RoutingHttpHandler = "/users" bind routes(

    "/{uuid}/roles" bind Method.POST to { request ->
        val rolesRequest = Lenses.addRolesRequest(request).roles
        val uuid = extractUUID(request)
        val updatedRoles = service.addRolesToUser(uuid, rolesRequest)
        Response(Status.OK).with(Lenses.rolesResponse of RolesResponse().roles(updatedRoles))
    },
    "/{uuid}/roles" bind Method.GET to { request ->
        val user = userContext(request)
        val uuid = extractUUID(request)
        businessValidator.validateGetRoles(uuid, user)
        val requestedUser = service.getUserForUUID(uuid) ?: throw UserNotFoundProblem(uuid)
        Response(Status.OK).with(Lenses.rolesResponse of RolesResponse().roles(requestedUser.roles))
    }

)
