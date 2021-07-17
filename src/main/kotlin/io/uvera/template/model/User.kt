package io.uvera.template.model

import org.hibernate.annotations.NaturalId
import javax.persistence.*

@Entity
@Table(name = "Users")
class User(
    @Id
    @NaturalId
    @Column(name = "email", nullable = false)
    var email: String,

    @Column(nullable = false)
    var password: String,

    @Column(nullable = false)
    var active: Boolean,

    @ElementCollection(fetch = FetchType.EAGER, targetClass = UserRole::class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "users_roles")
    @Column(name = "role")
    var roleList: MutableList<UserRole> = mutableListOf(),
)

enum class UserRole {
    USER,
    ADMIN;

    companion object {
        const val ROLE_PREFIX_VALUE = "ROLE_"
    }
}
