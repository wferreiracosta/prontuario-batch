package br.com.wferreiracosta.prontuario.processors;

import br.com.wferreiracosta.prontuario.models.entities.external.ProntuarioEntity;
import br.com.wferreiracosta.prontuario.repositories.external.ProntuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Configuration;

import static java.lang.String.format;

@Configuration
@RequiredArgsConstructor
public class ProntuarioProcessorConfig implements ItemProcessor<ProntuarioEntity, ProntuarioEntity> {

    private final ProntuarioRepository repository;

    @Override
    public ProntuarioEntity process(ProntuarioEntity item) throws Exception {
        final var entity = repository.save(item);
        final var nomeCompleto = format("%s %s %s %s",
                entity.getPrimeiroNome(),
                entity.getSegundoNome(),
                entity.getPrimeiroSobrenome(),
                entity.getSegundoSobrenome()
        ).replaceAll("\\s+", " ");
        entity.setNomeCompleto(nomeCompleto);
        return entity;
    }

}
