package io.uvera.template.dto.auth

import io.uvera.template.dao.user.User
import io.uvera.template.security.configuration.RoleEnum

class WhoAmIDTO(val email: String, val roles: List<String>) {
    companion object {
        fun fromUser(user: User) = WhoAmIDTO(
            email = user.email,
            roles = user.roleList.map(RoleEnum::toString)
        )
    }
}

