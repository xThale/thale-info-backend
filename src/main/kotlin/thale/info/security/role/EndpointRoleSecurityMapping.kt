package thale.info.security.role

val EndpointRoleMap = linkedMapOf(
    "GET /auth/users/{uuid}/roles" to listOf(Roles.ADMIN.name)
).apply {
    this.keys
        .filter { it.contains("{") }
        .forEach {
            this[it.replace("\\{[^}]*}".toRegex(), "([^/]*)")] = this[it] ?: emptyList()
            this.remove(it)
        }
}