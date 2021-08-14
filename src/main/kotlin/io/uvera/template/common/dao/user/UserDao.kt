@file:JvmName("UserDao")

package io.uvera.template.common.dao.user

import org.jdbi.v3.sqlobject.config.RegisterColumnMapper
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.UseRowReducer

interface UserDao {
    @RegisterConstructorMapper(value = User::class, prefix = "u")
    @RegisterColumnMapper(RoleEnumColumnMapper::class)
    @UseRowReducer(UserRowReducer::class)
    @SqlQuery("""
            select u.id as u_id,
            u.email as u_email,
            u.password as u_password,
            u.active as u_active,
            ur.role as ur_role,
            ur.user_id as ur_id
            from users u left join users_roles ur
            on u.id = ur.user_id
            where u.email = :email
        """)
    fun findByEmail(@Bind email: String): User?
}