package br.com.wferreiracosta.prontuario.steps;

import br.com.wferreiracosta.prontuario.models.entities.external.ProntuarioEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ProntuarioStepConfig {

    @JobScope
    @Bean(name = "prontuarioStep")
    public Step prontuarioStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ItemReader<ProntuarioEntity> reader,
            ItemProcessor<ProntuarioEntity, ProntuarioEntity> processor,
            ItemWriter<ProntuarioEntity> writer,
            @Value("#{jobParameters['gridSize']}") String gridSize
    ) {
        final var chunkSize = Integer.parseInt(gridSize); // valor default 10
        return new StepBuilder("prontuarioStep", jobRepository)
                .<ProntuarioEntity, ProntuarioEntity>chunk(chunkSize, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

}
