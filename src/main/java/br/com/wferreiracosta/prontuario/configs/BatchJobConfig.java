package br.com.wferreiracosta.prontuario.configs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.BatchConfigurationException;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Slf4j
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchJobConfig extends DefaultBatchConfiguration {

    private final DataSource dataSource;
    private final PlatformTransactionManager platformTransactionManager;
    private final Step prontuarioStep;

    @Override
    public JobRepository jobRepository() throws BatchConfigurationException {
        log.info("Criando JobRepository");
        try {
            final var factory = new JobRepositoryFactoryBean();
            factory.setDataSource(dataSource);
            factory.setTransactionManager(platformTransactionManager);
            factory.setIsolationLevelForCreate("ISOLATION_READ_COMMITTED");
            factory.setTablePrefix("controle.PRONTUARIO_");
            factory.setMaxVarCharLength(1000);
            factory.afterPropertiesSet();
            return factory.getObject();
        } catch (Exception e) {
            throw new BatchConfigurationException("Nao foi possivel configurar jobRepository", e);
        }
    }

    @Bean
    public JobExplorer jobExplorer() {
        try {
            JobExplorerFactoryBean jobExplorerFactoryBean = new JobExplorerFactoryBean();
            jobExplorerFactoryBean.setDataSource(dataSource);
            jobExplorerFactoryBean.setTransactionManager(platformTransactionManager);
            jobExplorerFactoryBean.setTablePrefix("controle.PRONTUARIO_"); // ✅ AGORA VEM ANTES
            jobExplorerFactoryBean.afterPropertiesSet(); // ✅ SOMENTE DEPOIS DE SETAR TUDO
            return jobExplorerFactoryBean.getObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public Job prontuarioJobConfig(
            JobRepository jobRepository
    ) {
        log.info("Criando Job: prontuarioJobConfig");
        return new JobBuilder("prontuarioJobConfig", jobRepository)
                .start(prontuarioStep)
                .build();
    }

}
