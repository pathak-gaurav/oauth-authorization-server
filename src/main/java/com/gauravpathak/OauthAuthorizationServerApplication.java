package com.gauravpathak;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@SpringBootApplication
@EnableAuthorizationServer
public class OauthAuthorizationServerApplication implements CommandLineRunner {

    @Autowired
    private AccountRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(OauthAuthorizationServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        repository.save(new Account("zack","zack"));
    }
}

