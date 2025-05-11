package br.com.wferreiracosta.prontuario.readers;

import br.com.wferreiracosta.prontuario.models.ProntuarioData;
import br.com.wferreiracosta.prontuario.utils.VariaveisGlobaisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Slf4j
@StepScope
@Component
public class ProntuarioReader implements ItemStreamReader<ProntuarioData> {

    private final FlatFileItemReader<ProntuarioData> delegate;

    public ProntuarioReader(
            @Value("#{jobParameters['filePath']}") String filePath,
            @Value("#{jobParameters['delimitador']}") String delimitador
    ) {
        this.delegate = new FlatFileItemReader<>();

        this.delegate.setResource(new FileSystemResource(filePath));
        this.delegate.setLinesToSkip(1);

        var tokenizer = new DelimitedLineTokenizer(delimitador);
        tokenizer.setNames(VariaveisGlobaisUtil.HEADER);

        var fieldSetMapper = new BeanWrapperFieldSetMapper<ProntuarioData>();
        fieldSetMapper.setTargetType(ProntuarioData.class);

        var lineMapper = new DefaultLineMapper<ProntuarioData>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        this.delegate.setLineMapper(lineMapper);
    }

    @Override
    public ProntuarioData read() throws Exception {
        return delegate.read();
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        delegate.open(executionContext);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        delegate.update(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        delegate.close();
    }
}
