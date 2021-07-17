package io.uvera.template.dto.auth

import io.uvera.template.model.User
import io.uvera.template.model.UserRole

class WhoAmIDTO(val email: String, val roles: List<String>) {
    constructor(user: User) : this(
        email = user.email,
        roles = user.roleList.map(UserRole::toString)
    )
}
