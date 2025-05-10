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
        final var nomeCompleto = format("%s - %s %s %s %s",
                entity.getId(),
                capitalizeFirstLetter(entity.getPrimeiroNome()),
                capitalizeFirstLetter(entity.getSegundoNome()),
                capitalizeFirstLetter(entity.getPrimeiroSobrenome()),
                capitalizeFirstLetter(entity.getSegundoSobrenome())
        ).replaceAll("\\s+", " ");
        entity.setNomeCompleto(nomeCompleto);
        return entity;
    }

    private String capitalizeFirstLetter(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
    }

}
