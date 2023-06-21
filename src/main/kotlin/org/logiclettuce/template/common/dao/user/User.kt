package org.logiclettuce.template.common.dao.user

import org.logiclettuce.template.security.configuration.RoleEnum

data class User(
    var login: String,
    var email: String,
    var password: String,
    var active: Boolean,
    var id: Long? = null,
) {
    var roleList: MutableList<RoleEnum> = mutableListOf()
}
