package io.uvera.template.security.configuration

enum class RoleEnum {
    USER,
    ADMIN;

    companion object {
        const val ROLE_PREFIX_VALUE = "ROLE_"
    }
}
