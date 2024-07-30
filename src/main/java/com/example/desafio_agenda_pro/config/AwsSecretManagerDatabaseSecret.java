package com.example.desafio_agenda_pro.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AwsSecretManagerDatabaseSecret {

    private String username;
    private String password;
    private String engine;
    private String host;
    private int port;

    @JsonProperty("dbInstanceIdentifier")
    private String dbInstanceIdentifier;
}
