package br.com.wferreiracosta.prontuario.listeners;

import com.opencsv.CSVReaderBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

import java.io.FileReader;
import java.io.IOException;

import static br.com.wferreiracosta.prontuario.utils.VariaveisGlobaisUtil.*;

@Slf4j
public class ProntuarioStepListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        try {
            final var filePath = stepExecution.getJobParameters().getString("filePath");

            log.info("Começando step: {}", filePath);

            final var filereader = new FileReader(filePath);
            final var csvReader = new CSVReaderBuilder(filereader).build();

            HEADER = csvReader.readNext()[0].split(
                    stepExecution.getJobParameters().getString("delimitador")
            );

            for (String header : HEADER) {
                if (COLUNAS_ENDERECO.contains(header.toUpperCase())) {
                    TEM_ENDERECO = true;
                    break;
                }
            }

            csvReader.close();
            filereader.close();
        } catch (final IOException e) {
            log.error(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("Finalizando step: {}", stepExecution.getJobParameters().getString("filePath"));
        return StepExecutionListener.super.afterStep(stepExecution);
    }

}
