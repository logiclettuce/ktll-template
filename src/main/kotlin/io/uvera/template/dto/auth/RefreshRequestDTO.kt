package io.uvera.template.dto.auth

import javax.validation.constraints.NotBlank

class RefreshRequestDTO(
    @field:NotBlank(message = "(Refresh) token field cannot be blank")
    val token: String
)
