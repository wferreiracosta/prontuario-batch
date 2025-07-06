package br.com.wferreiracosta.prontuario.listeners;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProntuarioBatchListener implements ApplicationRunner {

    private final JobLauncher jobLauncher;
    private final JobExplorer jobExplorer;
    private final Job prontuarioJobConfig;
    private final JobRepository jobRepository;

    @PreDestroy
    public void preDestroy() throws NoSuchJobException {
        final var token = "123";
        final var jobName = prontuarioJobConfig.getName();
        final var countJobInstances = (int) jobExplorer.getJobInstanceCount(jobName);
        final var instances = jobExplorer.getJobInstances(jobName, 0, countJobInstances);

        for (JobInstance instance : instances) {
            final var executions = jobExplorer.getJobExecutions(instance);
            for (JobExecution execution : executions) {
                final var jobStatus = execution.getStatus();
                var updateJob = false;
                if (jobStatus == BatchStatus.UNKNOWN || jobStatus == BatchStatus.STARTED || jobStatus == BatchStatus.STARTING) {
                    for (StepExecution step : execution.getStepExecutions()) {
                        final var stepToken = step.getJobParameters().getParameter("xTaskToken").getValue();
                        if (token.equalsIgnoreCase((String) stepToken)) {
                            log.info("Encerrando execução, trocando status do job de STARTED para FAILED");
                            step.setStatus(BatchStatus.FAILED);
                            step.setExitStatus(ExitStatus.FAILED);
                            step.setLastUpdated(LocalDateTime.now());
                            step.setEndTime(LocalDateTime.now());
                            jobRepository.update(step);
                            updateJob = true;
                        }
                    }

                    if (updateJob) {
                        execution.setStatus(BatchStatus.FAILED);
                        execution.setExitStatus(ExitStatus.FAILED);
                        execution.setEndTime(LocalDateTime.now());
                        jobRepository.update(execution); // <- MUITO IMPORTANTE
                    }
                }
            }
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        final var jobName = prontuarioJobConfig.getName();
        final var instances = jobExplorer.getJobInstances(jobName, 0, 10);
        final var filteredInstances = getJobInstances(instances);
        log.info("Quantidade de jobs que seram reiniciados: {}", filteredInstances.size());

        for (JobInstance instance : filteredInstances) {

            final var executions = jobExplorer.getJobExecutions(instance);
            for (JobExecution execution : executions) {
                final var jobStatus = execution.getStatus();

                if (jobStatus == BatchStatus.COMPLETED) {
                    continue;
                }

                if (jobStatus == BatchStatus.UNKNOWN || jobStatus == BatchStatus.STARTED || jobStatus == BatchStatus.STARTING) {
                    log.info("Corrigindo status da execucao: {} com status: {} para FAILED", execution.getJobId(), jobStatus);

                    for (StepExecution step : execution.getStepExecutions()) {
                        final var stepStatus = step.getStatus();
                        if (stepStatus == BatchStatus.UNKNOWN || stepStatus == BatchStatus.STARTED || stepStatus == BatchStatus.STARTING) {
                            step.setStatus(BatchStatus.FAILED);
                            step.setExitStatus(ExitStatus.FAILED);
                            jobRepository.update(step);
                        }
                    }
                    execution.setStatus(BatchStatus.FAILED);
                    execution.setExitStatus(ExitStatus.FAILED);
                    execution.setEndTime(LocalDateTime.now());
                    jobRepository.update(execution); // <- MUITO IMPORTANTE
                }

                try {
                    log.info("Reiniciando execucao: {}", execution.getJobId());
                    jobLauncher.run(prontuarioJobConfig, execution.getJobParameters());
                } catch (JobInstanceAlreadyCompleteException e) {
                    log.error("JobInstanceAlreadyCompleteException: A instância do job {} já foi concluída. Ignorando reinício. Parameter: "
                            , execution.getJobId(), execution.getJobParameters());
                }
            }
        }
        log.info("Finalizado o reinício de jobs pendentes...");
    }

    private List<JobInstance> getJobInstances(final List<JobInstance> instances) {
        final var filteredInstances = new ArrayList<JobInstance>();

        for (JobInstance instance : instances) {
            List<JobExecution> executions = jobExplorer.getJobExecutions(instance);

            boolean hasCompletedExecution = executions.stream()
                    .anyMatch(exec -> exec.getStatus() == BatchStatus.COMPLETED);

            if (!hasCompletedExecution) {
                filteredInstances.add(instance);
            }
        }
        return filteredInstances;
    }
}
