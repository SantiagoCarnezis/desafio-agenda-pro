package com.example.desafio_agenda_pro.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import java.util.Map;
import java.util.function.Consumer;

@Configuration
public class DataSourceConfig {

    @Value("${aws.accessKeyId}")
    private String accessKeyId;

    @Value("${aws.secretKey}")
    private String secretKey;

    @Value("${aws.region}")
    private String region;

    @Value("${aws.secretName}")
    private String secretName;

    //@Bean
    public AwsSecretManagerDatabaseSecret dbCredentials() throws Exception {
//        AWSSecretsManager client = AWSSecretsManagerClientBuilder.standard()
//                .withRegion(Regions.fromName(region))
//                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKeyId, secretKey)))
//                .build();

//        AWSSecretsManager client = AWSSecretsManagerClientBuilder.standard()
//                .withRegion(Regions.fromName(region))
//                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
//                .build();
//
//        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
//                .withSecretId(secretName);
//        GetSecretValueResult getSecretValueResult = client.getSecretValue(getSecretValueRequest);
//
//        String secret = getSecretValueResult.getSecretString();
//        ObjectMapper objectMapper = new ObjectMapper();


        // Crear el cliente de Secrets Manager
        SecretsManagerClient client = SecretsManagerClient.builder()
                .region(Region.US_EAST_2)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();

        // Crear la solicitud para obtener el secreto
        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();

        // Obtener el valor del secreto
        GetSecretValueResponse getSecretValueResponse = client.getSecretValue(getSecretValueRequest);

        String secretString = getSecretValueResponse.secretString();

        // Parsear el SecretString a un objeto de la clase Secret
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            AwsSecretManagerDatabaseSecret secret = objectMapper.readValue(secretString, AwsSecretManagerDatabaseSecret.class);
            System.out.println("Username: " + secret.getUsername());
            System.out.println("Password: " + secret.getPassword());
            System.out.println("Engine: " + secret.getEngine());
            System.out.println("Host: " + secret.getHost());
            System.out.println("Port: " + secret.getPort());
            System.out.println("DB Instance Identifier: " + secret.getDbInstanceIdentifier());

            return secret;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("No se pudo recuperar las credenciales de: " + secretString);
            return  null;
        }
    }

    @Bean
    public DataSource dataSource() throws Exception {

        AwsSecretManagerDatabaseSecret credenciales = this.dbCredentials();

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("mysql://" + credenciales.getHost() + ":3306/desafio-agenda-pro");
        dataSource.setUsername(credenciales.getUsername());
        dataSource.setPassword(credenciales.getPassword());
        return dataSource;
    }
}