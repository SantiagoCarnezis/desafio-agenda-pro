package com.example.desafio_agenda_pro.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;


@Configuration
//@Profile("jenkins")
public class DataSourceConfig {

    @Value("${aws.secretName}")
    private String secretName;

    public AwsSecretManagerDatabaseSecret dbCredentials() {

        SecretsManagerClient client = SecretsManagerClient.builder()
                .region(Region.US_EAST_2)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();

        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();

        GetSecretValueResponse getSecretValueResponse = client.getSecretValue(getSecretValueRequest);

        String secretString = getSecretValueResponse.secretString();

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(secretString, AwsSecretManagerDatabaseSecret.class);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("No se pudo recuperar las credenciales de: " + secretString);
            return  null;
        }
    }

    @Bean
    public DataSource dataSource() {

        AwsSecretManagerDatabaseSecret credenciales = this.dbCredentials();

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://" + credenciales.getHost() + ":3306/desafio-agenda-pro");
        dataSource.setUsername(credenciales.getUsername());
        dataSource.setPassword(credenciales.getPassword());

        return dataSource;
    }
}