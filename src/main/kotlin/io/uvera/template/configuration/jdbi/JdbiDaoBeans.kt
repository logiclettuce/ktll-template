package io.uvera.template.configuration.jdbi

import io.uvera.template.common.dao.user.UserDao
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.kotlin.onDemand
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JdbiDaoBeans(private val jdbi: Jdbi) {
    private inline fun <reified T : Any> define() = jdbi.onDemand<T>()

    @Bean
    fun userDao(): UserDao = define()
}