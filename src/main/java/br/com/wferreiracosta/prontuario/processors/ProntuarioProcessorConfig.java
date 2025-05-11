package br.com.wferreiracosta.prontuario.processors;

import br.com.wferreiracosta.prontuario.models.ProntuarioData;
import br.com.wferreiracosta.prontuario.models.entities.external.EnderecoEntity;
import br.com.wferreiracosta.prontuario.models.entities.external.ProntuarioEntity;
import br.com.wferreiracosta.prontuario.repositories.external.ProntuarioRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Configuration;

import static br.com.wferreiracosta.prontuario.utils.VariaveisGlobaisUtil.TEM_ENDERECO;
import static java.lang.String.format;

@Configuration
@RequiredArgsConstructor
public class ProntuarioProcessorConfig implements ItemProcessor<ProntuarioData, ProntuarioEntity> {

    private final ProntuarioRepository repository;

    @Override
    public ProntuarioEntity process(ProntuarioData item) throws Exception {
        final var modelMapper = new ModelMapper();

        final var entity = modelMapper.map(item, ProntuarioEntity.class);

        if (TEM_ENDERECO) {
            final var entityEndereco = modelMapper.map(item, EnderecoEntity.class);
            entityEndereco.setProntuario(entity);
            entityEndereco.setProntuario2(entity);

            entity.setEndereco(entityEndereco);
            entity.setEndereco2(entityEndereco);
        }

        repository.save(entity);

        if (TEM_ENDERECO) {
            final var enderecoCompleto = format("%s - %s, %s - %s",
                    entity.getId(),
                    capitalizeFirstLetter(entity.getEndereco().getCidade()),
                    capitalizeFirstLetter(entity.getEndereco().getEstado()),
                    capitalizeFirstLetter(entity.getEndereco().getPais())
            ).replaceAll("\\s+", " ");
            entity.setEnderecoCompleto(enderecoCompleto);
        }

        return entity;
    }

    private String capitalizeFirstLetter(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        return value;
    }

}
