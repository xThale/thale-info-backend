package thale.info.service

import thale.info.database.DatabaseService
import thale.info.exception.UnknownUserProblem
import java.util.*

class UserService(private val databaseService: DatabaseService) {

    fun addRolesToUser(uuid: UUID, roles: List<String>) : List<String> {
        val user = getUserForUUID(uuid)
        val adjustedRoles = user.roles.toMutableList()

        roles.forEach {
            if (it !in user.roles) {
                adjustedRoles.add(it)
            }
        }

        if (user.roles.size != adjustedRoles.size) {
            databaseService.user.updateRolesByEmail(user.email, adjustedRoles)
        }

        return adjustedRoles
    }

    fun getUserForUUID(uuid: UUID) = databaseService.user.findUserByUUID(uuid) ?: throw UnknownUserProblem(uuid)

}