package com.ifood.backend.ifoodbackend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import javax.annotation.PostConstruct;
import java.util.Objects;

@EnableOAuth2Client
@Configuration
public class OAuth2Config {

    private static final String CLIENT_ID_NAME = "clientId";
    private static String CLIENT_ID = "";

    private static final String CLIENT_SECRET_NAME = "clientSecret";
    private static String CLIENT_SECRET = "";

    @Value("${tokenUrl}")
    private String tokenUrl;

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2Config.class);

    @PostConstruct
    private void validateEnvVars() {

        CLIENT_ID = System.getenv(CLIENT_ID_NAME);

        if ( Objects.isNull(CLIENT_ID) || "".equals(CLIENT_ID) ) {
            throw new IllegalStateException(String.format("%s env var must be provided", CLIENT_ID_NAME));
        }
        CLIENT_SECRET = System.getenv(CLIENT_SECRET_NAME);
        if ( Objects.isNull(CLIENT_SECRET) || "".equals(CLIENT_SECRET) ) {
            throw new IllegalStateException(String.format("%s env var must be provided", CLIENT_SECRET_NAME));
        }

    }

    @Bean
    public OAuth2RestTemplate oAuth2RestTemplate() {
        ClientCredentialsResourceDetails resourceDetails = new ClientCredentialsResourceDetails();
        resourceDetails.setClientId(CLIENT_ID);
        resourceDetails.setClientSecret(CLIENT_SECRET);
        resourceDetails.setAccessTokenUri(tokenUrl);

        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resourceDetails);

        return restTemplate;
    }

}