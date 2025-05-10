package br.com.wferreiracosta.prontuario.writers;

import br.com.wferreiracosta.prontuario.models.entities.external.ProntuarioEntity;
import br.com.wferreiracosta.prontuario.repositories.external.ProntuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProntuarioWriter implements ItemWriter<ProntuarioEntity> {

    private final ProntuarioRepository prontuarioRepository;

    @Override
    public void write(Chunk<? extends ProntuarioEntity> chunk) throws Exception {
        prontuarioRepository.saveAll(chunk.getItems());
    }

}
