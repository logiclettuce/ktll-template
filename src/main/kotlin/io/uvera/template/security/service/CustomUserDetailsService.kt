package io.uvera.template.security.service

import io.uvera.template.dao.user.UserDao
import io.uvera.template.security.configuration.CustomUserDetails
import io.uvera.template.util.loggerDelegate
import org.springframework.context.annotation.Primary
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Primary
@Service
class CustomUserDetailsService(
    protected val repo: UserDao,
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        val user = username?.let { u -> repo.findByEmail(u) }
            ?: throw UsernameNotFoundException("User [username: $username] not found")
        return CustomUserDetails(user)
    }
}

