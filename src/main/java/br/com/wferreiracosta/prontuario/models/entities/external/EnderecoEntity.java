package br.com.wferreiracosta.prontuario.models.entities.external;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.SEQUENCE;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "FUNDACAO", name = "ENDERECO")
@SequenceGenerator(
        name = "ENDERECO_SEQ",
        sequenceName = "ENDERECO_SEQ",
        schema = "FUNDACAO",
        allocationSize = 1
)
public class EnderecoEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "ENDERECO_SEQ", strategy = SEQUENCE)
    private Long id;

    @Column(name = "PAIS")
    private String pais;

    @Column(name = "ESTADO")
    private String estado;

    @Column(name = "CIDADE")
    private String cidade;

    @OneToOne
    @JoinColumn(name = "PRONTUARIO_ID", referencedColumnName = "ID")
    private ProntuarioEntity prontuario;

    @OneToOne(mappedBy = "endereco2", cascade = ALL)
    private ProntuarioEntity prontuario2;

}
