package io.uvera.template.security.service

import io.jsonwebtoken.Claims
import io.uvera.template.configuration.properties.JwtRefreshTokenProperties
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class JwtRefreshTokenService(
    properties: JwtRefreshTokenProperties,
    private val genericTokenService: GenericTokenService,
) : JwtTokenService {
    private val secret = properties.secret
    private val jwtExpirationInMinutesProperty = properties.expirationInMinutes

    override fun generateToken(userDetails: UserDetails): String =
        genericTokenService.generateToken(userDetails, jwtExpirationInMinutesProperty, secret)

    override fun validateToken(token: String): Boolean =
        genericTokenService.validateToken(token, secret)

    override fun getClaimsFromToken(token: String): Claims? =
        genericTokenService.getClaimsFromToken(token, secret)
}