package br.com.wferreiracosta.prontuario.controllers;

import br.com.wferreiracosta.prontuario.models.parameters.ProntuarioParameter;
import br.com.wferreiracosta.prontuario.services.ProntuarioService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class ProntuarioController {

    private final ProntuarioService service;

    @ResponseStatus(CREATED)
    @Operation(summary = "", description = "")
    @PostMapping("/register")
    public void execute(
            @RequestBody ProntuarioParameter parameter,
            @RequestHeader(value = "X-Task-Token") final String xTaskToken
    ) {
        final var jobParameters = new JobParametersBuilder()
                .addString("filePath", parameter.filePath(), true)
                .addString("delimitador", parameter.delimitador(), false)
                .addLong("gridSize", parameter.gridSize(), false)
                .addString("xTaskToken", xTaskToken, false)
                .toJobParameters();
        service.execute(jobParameters, xTaskToken);
    }

}
