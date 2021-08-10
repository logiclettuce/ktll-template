package io.uvera.template.configuration.jdbi

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.postgres.PostgresPlugin
import org.jdbi.v3.sqlobject.SqlObjectPlugin
import org.jdbi.v3.sqlobject.kotlin.KotlinSqlObjectPlugin
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import javax.sql.DataSource

@Configuration
class JdbiConfiguration {

    @Bean
    fun jdbi(dataSource: DataSource, rowMappers: List<RowMapper<*>>): Jdbi {
        val proxy = TransactionAwareDataSourceProxy(dataSource)
        val jdbi = Jdbi.create(proxy)
            .installPlugin(PostgresPlugin())
            .installPlugin(KotlinPlugin())
            .installPlugin(SqlObjectPlugin())
            .installPlugin(KotlinSqlObjectPlugin())
            .installPlugins()
        rowMappers.forEach { jdbi.registerRowMapper(it) }
        return jdbi
    }
}