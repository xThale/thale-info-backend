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
import thale.info.mapper.toResponse
import thale.info.service.UserService
import thale.info.util.extractUUID
import thale.info.validation.UserPermissionValidator

private val businessValidator = UserPermissionValidator()

fun securityRoute(userContext :  RequestContextLens<User>,
                  service: UserService): RoutingHttpHandler = "/auth" bind routes(



    "/user" bind Method.GET to { request ->
        val user = userContext(request)
        Response(Status.OK).with(Lenses.userResponse of user.toResponse())
    },
    "/users/{uuid}/roles" bind Method.POST to { request ->
        val rolesRequest = Lenses.addRolesRequest(request).roles
        val user = userContext(request)
        val uuid = extractUUID(request)

        businessValidator.validatePostRoles(user)
        val updatedRoles = service.addRolesToUser(uuid, rolesRequest)

        Response(Status.OK).with(Lenses.rolesResponse of RolesResponse().roles(updatedRoles))
    },
    "/users/{uuid}/roles" bind Method.GET to { request ->
        val user = userContext(request)
        val pathUUID = extractUUID(request)
        businessValidator.validateGetRoles(pathUUID, user)
        val requestedUser = service.getUserForUUID(pathUUID)
        Response(Status.OK).with(Lenses.rolesResponse of RolesResponse().roles(requestedUser.roles))
    }

)
