package io.uvera.template.service.auth

import io.uvera.template.dto.auth.AuthenticationRequestDTO
import io.uvera.template.dto.auth.TokenResponseDTO
import io.uvera.template.dto.auth.WhoAmIDTO
import io.uvera.template.error.exception.UserNotFoundException
import io.uvera.template.security.service.JwtAccessService
import io.uvera.template.security.service.JwtRefreshService
import io.uvera.template.service.UserCachingService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class AuthService(
    protected val jwtAccessService: JwtAccessService,
    protected val jwtRefreshService: JwtRefreshService,
    protected val userDetailsService: UserDetailsService,
    protected val userService: UserCachingService,
    protected val authManager: AuthenticationManager,
) {
    fun whoAmI(email: String): WhoAmIDTO {
        val user =
            userService.findUserByEmail(email)
                ?: throw UserNotFoundException("User by specified email [$email] not found")
        return WhoAmIDTO(user)
    }

    fun authenticate(dto: AuthenticationRequestDTO) = authenticate(dto.email, dto.password)
    fun authenticate(email: String, password: String): TokenResponseDTO {
        // throws exception if failed
        authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                email, password
            )
        )
        return generateTokensByEmail(email)
    }

    fun generateTokensByEmail(email: String): TokenResponseDTO {
        // load userDetails from database
        val userDetails = userDetailsService.loadUserByUsername(email)
        // generate access token
        val accessToken: String = jwtAccessService.generateToken(userDetails)
        // generate longer lasting refresh token
        val refreshToken: String = jwtRefreshService.generateToken(userDetails)

        return TokenResponseDTO(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    fun generateTokensFromJwsRefreshToken(jws: String): TokenResponseDTO {
        // throw exception if token is invalid
        if (!jwtRefreshService.validateToken(jws))
            throw BadCredentialsException("Invalid refresh token")

        // fetch userDetails by subject parsed from refresh token
        val subject = jwtRefreshService.getClaimsFromToken(jws)?.subject
        val userDetails = userDetailsService.loadUserByUsername(subject)

        // throw if user's account is disabled otherwise return new token
        if (!userDetails.isEnabled) throw DisabledException("Account disabled")

        return TokenResponseDTO(
            accessToken = jwtAccessService.generateToken(userDetails),
            refreshToken = jwtRefreshService.generateToken(userDetails)
        )
    }
}