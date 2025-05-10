package br.com.wferreiracosta.prontuario.models.entities.external;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "FUNDACAO", name = "PRONTUARIO")
@SequenceGenerator(
        name = "PRONTUARIO_SEQ",
        sequenceName = "PRONTUARIO_SEQ",
        schema = "FUNDACAO",
        allocationSize = 1
)
public class ProntuarioEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "PRONTUARIO_SEQ", strategy = SEQUENCE)
    private Long id;

    @Column(name = "PRIMEIRO_NOME")
    private String primeiroNome;

    @Column(name = "SEGUNDO_NOME")
    private String segundoNome;

    @Column(name = "PRIMEIRO_SOBRENOME")
    private String primeiroSobrenome;

    @Column(name = "SEGUNDO_SOBRENOME")
    private String segundoSobrenome;

    @Column(name = "NOME_COMPLETO")
    private String nomeCompleto;

}
