package br.com.wferreiracosta.prontuario.listeners;

import br.com.wferreiracosta.prontuario.utils.VariaveisGlobaisUtil;
import com.opencsv.CSVReaderBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

import java.io.FileReader;
import java.io.IOException;

@Slf4j
public class ProntuarioStepListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        try {
            final var filePath = stepExecution.getJobParameters().getString("filePath");
            final var filereader = new FileReader(filePath);
            final var csvReader = new CSVReaderBuilder(filereader).build();
            VariaveisGlobaisUtil.header = csvReader.readNext()[0].split(";");
            csvReader.close();
            filereader.close();
        } catch (final IOException e) {
            log.error(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return StepExecutionListener.super.afterStep(stepExecution);
    }

}
