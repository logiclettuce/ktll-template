package io.uvera.template.error.dto

import io.swagger.v3.oas.annotations.media.Schema
import io.uvera.template.util.extensions.EmptyObject
import io.uvera.template.util.extensions.compact
import org.springframework.validation.BindingResult
import java.time.LocalDateTime


class BindingError(
    val errors: Collection<ObjectErrorCompact>,
    @Schema(implementation = ObjectErrorCompact::class, nullable = true)
    val firstError: Any = errors.firstOrNull() ?: EmptyObject,
    @Schema(implementation = LocalDateTime::class)
    val timestamp: String = LocalDateTime.now().toString(),
) {
    constructor(oec: ObjectErrorCompact) : this(listOf(oec))
    constructor(ex: Exception) : this(
        ObjectErrorCompact(
            ex.localizedMessage ?: "Unknown error",
            ex::class.java.simpleName
        )
    )

    constructor(br: BindingResult) : this(br.allErrors.compact)
}
