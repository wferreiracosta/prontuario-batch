package br.com.wferreiracosta.prontuario.models.parameters;

public record ProntuarioParameter(
        String filePath,
        String delimitador,
        Long gridSize
) {
}
