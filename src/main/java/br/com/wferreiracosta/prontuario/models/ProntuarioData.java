package br.com.wferreiracosta.prontuario.models;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProntuarioData {

    private String primeiroNome;
    private String segundoNome;
    private String primeiroSobrenome;
    private String segundoSobrenome;
    private String pais;
    private String estado;
    private String cidade;

}
