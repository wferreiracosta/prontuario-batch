package br.com.wferreiracosta.prontuario.configs;

import br.com.wferreiracosta.prontuario.configs.properties.SecretsManagerProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import static java.lang.String.format;

@Profile("!test")
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "br.com.wferreiracosta.prontuario.repositories.internal",
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager"
)
public class InternalDataSourceConfig {

    private final SecretsManagerProperties properties;

    @Bean
    @Primary
    @Qualifier("internalDataSource")
    public DriverManagerDataSource dataSource() {
        final var url = format("jdbc:%s://%s:%s/%s",
                properties.engine(), properties.host(),
                properties.port(), properties.database());

        final var dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(properties.driverClassName());
        dataSource.setUrl(url);
        dataSource.setUsername(properties.username());
        dataSource.setPassword(properties.password());

        return dataSource;
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(dataSource())
                .packages("br.com.wferreiracosta.prontuario.models.entities.internal")
                .persistenceUnit("internal")
                .build();
    }

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(
            final @Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean emf) {
        return new JpaTransactionManager(emf.getObject());
    }

}
