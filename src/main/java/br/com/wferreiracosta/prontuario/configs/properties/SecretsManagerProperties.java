package br.com.wferreiracosta.prontuario.configs.properties;

public record SecretsManagerProperties(

        String externalUrl,
        String externalDriverClassName,
        String externalUsername,
        String externalPassword,
        String username,
        String password,
        String engine,
        String host,
        Integer port,
        String dbInstanceIdentifier,
        String driverClassName,
        String database

) {
}
