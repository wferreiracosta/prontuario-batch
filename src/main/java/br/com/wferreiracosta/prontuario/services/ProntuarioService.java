package br.com.wferreiracosta.prontuario.services;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProntuarioService {

    private final JobLauncher jobLauncher;

    private final Job prontuarioJobConfig;

    @Async
    public void execute(
            final JobParameters jobParameters,
            final String xTaskToken
    ) {
        try {
            jobLauncher.run(prontuarioJobConfig, jobParameters);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

}
