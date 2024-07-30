package com.example.desafio_agenda_pro.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

import java.util.Map;

@Configuration
public class DataSourceConfig {

    @Autowired
    private Map<String, String> dbCredentials;

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(dbCredentials.get("url"));
        dataSource.setUsername(dbCredentials.get("username"));
        dataSource.setPassword(dbCredentials.get("password"));
        return dataSource;
    }
}

