package io.uvera.template.security.service

import io.jsonwebtoken.*
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*

interface JwtTokenService {
    fun generateToken(userDetails: UserDetails): String
    fun validateToken(token: String): Boolean
    fun getClaimsFromToken(token: String): Claims?
}

@Service
class GenericTokenService {
    fun generateToken(userDetails: UserDetails, expirationInMinutes: Int, secret: String): String {
        val subject = userDetails.username
        val claims = mutableMapOf<String, Any>()
        val issuedAt = Date(System.currentTimeMillis())
        val expiration = Calendar
            .getInstance()
            .also { calendar ->
                calendar.add(Calendar.MINUTE, expirationInMinutes)
            }
            .toInstant()
            .toEpochMilli()
            .let { millis ->
                Date(millis)
            }

        return Jwts
            .builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(issuedAt)
            .setExpiration(expiration)
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact()
    }

    fun validateToken(token: String, secret: String): Boolean = try {
        Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
        true
    } catch (ex: SignatureException) {
        false
    } catch (ex: MalformedJwtException) {
        false
    } catch (ex: UnsupportedJwtException) {
        false
    } catch (ex: IllegalArgumentException) {
        false
    } catch (ex: ExpiredJwtException) {
        false
    }

    fun getClaimsFromToken(token: String, secret: String): Claims? = try {
        Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .body
    } catch (ex: Exception) {
        null
    }
}