package com.mateusz.jakuszko.roomforyou

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification

@Testcontainers
class TestcontainersSpecification extends Specification {
    @Shared
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres")
            .withDatabaseName("database")
            .withUsername("maciek")
            .withPassword("password")

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        postgreSQLContainer.start()
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl)
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword)
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername)
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver")
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.PostgreSQLDialect")
    }
}
