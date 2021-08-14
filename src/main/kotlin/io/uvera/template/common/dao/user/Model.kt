package io.uvera.template.common.dao.user

import io.uvera.template.security.configuration.RoleEnum


data class User(
    val id: Long,
    var email: String,
    var password: String,
    var active: Boolean,
) {
    var roleList: MutableList<RoleEnum> = mutableListOf()
}