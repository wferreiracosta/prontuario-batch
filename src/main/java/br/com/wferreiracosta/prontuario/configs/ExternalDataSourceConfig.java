package br.com.wferreiracosta.prontuario.configs;

import br.com.wferreiracosta.prontuario.configs.properties.SecretsManagerProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "br.com.wferreiracosta.prontuario.repositories.external",
        entityManagerFactoryRef = "externalEntityManagerFactory",
        transactionManagerRef = "externalTransactionManager"
)
public class ExternalDataSourceConfig {


    private final SecretsManagerProperties properties;

    @Bean
    @Qualifier("externalDataSource")
    public DriverManagerDataSource externalDataSource() {
        final var dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(properties.externalDriverClassName());
        dataSource.setUrl(properties.externalUrl());
        dataSource.setUsername(properties.externalUsername());
        dataSource.setPassword(properties.externalPassword());
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean externalEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(externalDataSource())
                .packages("br.com.wferreiracosta.prontuario.models.entities.external")
                .persistenceUnit("external")
                .build();
    }

    @Bean
    @Qualifier("externalTransactionManager")
    public PlatformTransactionManager externalTransactionManager(
            final @Qualifier("externalEntityManagerFactory") LocalContainerEntityManagerFactoryBean emf) {
        return new JpaTransactionManager(emf.getObject());
    }

}
