package io.uvera.template.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import io.uvera.template.dto.auth.AuthenticationRequestDTO
import io.uvera.template.dto.auth.RefreshRequestDTO
import io.uvera.template.dto.auth.TokenResponseDTO
import io.uvera.template.dto.auth.WhoAmIDTO
import io.uvera.template.error.exception.DTOStateException
import io.uvera.template.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.security.Principal

//region SwaggerDoc
@Tag(description = "Endpoints for authenticating users.", name = "auth")
//endregion
@RestController
@RequestMapping("/api/auth")
class AuthController(
    protected val authManager: AuthenticationManager,
    protected val authService: AuthService,
) {

    //region SwaggerDoc
    @Operation(
        summary = "Authenticate",
        description = "Authenticated by email and password",
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Successful authentication",
            content = *[(Content(schema = Schema(implementation = TokenResponseDTO::class)))]
        ), ApiResponse(
            responseCode = "400", description = "Invalid DTO"
        ), ApiResponse(
            responseCode = "401",
            description = "Auth error",
        )
    )
    //endregion
    @PostMapping("/login")
    fun createAuthenticationToken(
        @Validated @RequestBody authenticationRequest: AuthenticationRequestDTO,
        br: BindingResult
    ): ResponseEntity<TokenResponseDTO> =
        if (br.hasErrors()) throw DTOStateException("Invalid DTO")
        else {
            // authenticate user from request body, will fail with exceptions
            authManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    authenticationRequest.email, authenticationRequest.password
                )
            )
            ok(authService.generateTokensByEmail(authenticationRequest.email))
        }


    //region SwaggerDoc
    @Operation(
        summary = "Refresh the tokens",
        description = "Refresh both access and the refresh token with older refresh token",
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Successful refresh",
            content = *[Content(schema = Schema(implementation = TokenResponseDTO::class))],
        ), ApiResponse(
            responseCode = "400", description = "Invalid DTO"
        ), ApiResponse(
            responseCode = "401",
            description = "Invalid token or account disabled",
        )
    )
    //endregion
    @PostMapping("/refresh")
    fun refreshToken(
        @Validated @RequestBody refreshRequest: RefreshRequestDTO,
        br: BindingResult
    ): ResponseEntity<TokenResponseDTO> =
        if (br.hasErrors())
            throw DTOStateException("Invalid DTO")
        else
            ok(authService.generateTokensFromJwsRefreshToken(refreshRequest.token))


    //region SwaggerDoc
    @Operation(
        summary = "Get your own info",
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "DTO Returned",
            content = *[(Content(schema = Schema(implementation = WhoAmIDTO::class)))]
        ), ApiResponse(
            responseCode = "404",
            description = "User not found",
        )
    )
    //endregion
    @PreAuthorize("authenticated")
    @GetMapping("/whoami")
    fun whoAmI(principal: Principal): ResponseEntity<WhoAmIDTO> =
        ok(authService.whoAmI(principal.name))

}
