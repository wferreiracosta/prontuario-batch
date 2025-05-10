package br.com.wferreiracosta.prontuario.configs.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws")
public record AwsProperties(

        String region,
        String secretsManagerName,
        String url,
        String accessKeyid,
        String secretAccessKeyid

) {
}
