package io.uvera.template.service

import io.uvera.template.configuration.CacheConfiguration
import io.uvera.template.model.User
import io.uvera.template.repository.UserRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserCachingService(
    private val repo: UserRepository,
) {
    @Cacheable(cacheNames = [CacheConfiguration.USER_ENTITY_CACHE_NAME], key = "#email")
    fun findUserByEmail(email: String): User? = repo.findByIdOrNull(email)
}