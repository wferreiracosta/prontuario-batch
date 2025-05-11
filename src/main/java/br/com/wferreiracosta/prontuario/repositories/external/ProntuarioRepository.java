package br.com.wferreiracosta.prontuario.repositories.external;

import br.com.wferreiracosta.prontuario.models.entities.external.ProntuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProntuarioRepository extends JpaRepository<ProntuarioEntity, Long> {

}
