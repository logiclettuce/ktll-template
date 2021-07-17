package io.uvera.template.security.service

import io.uvera.template.repository.UserRepository
import io.uvera.template.security.configuration.CustomUserDetails
import org.springframework.context.annotation.Primary
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Primary
@Service
class CustomUserDetailsService(
    private val repo: UserRepository,
) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        val user = username?.let { u -> repo.findByIdOrNull(u) }
            ?: throw UsernameNotFoundException("User [username: $username] not found")
        return CustomUserDetails(user)
    }
}

