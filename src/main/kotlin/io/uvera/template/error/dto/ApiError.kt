package io.uvera.template.error.dto

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema
import io.uvera.template.util.extensions.EmptyObject
import io.uvera.template.util.extensions.compact
import org.springframework.validation.BindingResult
import java.time.LocalDateTime


class ApiError(
    @ArraySchema(schema = Schema(implementation = ObjectErrorCompact::class, nullable = false))
    val errors: Collection<ObjectErrorCompact>,
    @Schema(implementation = ObjectErrorCompact::class, nullable = true)
    val firstError: Any = errors.firstOrNull() ?: EmptyObject,
    @Schema(implementation = LocalDateTime::class)
    val timestamp: String = LocalDateTime.now().toString(),
) {
    companion object {
        fun fromException(ex: Exception) = ApiError(
            errors = listOf(
                ObjectErrorCompact(
                    defaultMessage = ex.localizedMessage ?: "Unknown error",
                    code = ex::class.simpleName ?: "UnknownException"
                )
            )
        )

        fun fromBindingResult(br: BindingResult) = ApiError(
            errors = br.allErrors.compact
        )
    }
}