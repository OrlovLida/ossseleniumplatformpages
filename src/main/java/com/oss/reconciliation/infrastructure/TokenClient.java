package com.oss.reconciliation.infrastructure;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.oss.transport.infrastructure.Environment;

class TokenClient {

    private static TokenClient INSTANCE;

    private static final Integer CURRENT_TOKEN_IDENTIFIER = 1;

    private static final Logger LOGGER = LoggerFactory.getLogger("TOOLTIP_" + Environment.class);

    private final Environment environment;

    private LoadingCache<Integer, String> keycloakTokenCache = CacheBuilder.newBuilder()
            .maximumSize(1)
            .refreshAfterWrite(15, TimeUnit.MINUTES)
            .build(
                    new CacheLoader<Integer, String>() {
                        @Override
                        public String load(Integer tokenNumber) { // no checked exception
                            return createNewToken();
                        }
                    });

    private TokenClient(Environment environment) {
        this.environment = environment;
    }

    public static TokenClient getInstance(Environment environment) {
        if (Objects.isNull(INSTANCE)) {
            INSTANCE = new TokenClient(environment);
        }

        return INSTANCE;
    }

    String getKeycloackToken() {
        try {
            return keycloakTokenCache.get(CURRENT_TOKEN_IDENTIFIER);
        } catch (ExecutionException e) {
            return createNewToken();
        }
    }

    private String createNewToken() {
        Response response = RestAssured.given()
                .baseUri(environment.getEnvironmentUrl())
                .port(environment.getEnvironmentPort())
                .contentType(ContentType.URLENC)
                .content("username=" + environment.getUser().getUsername() + "&password=" + environment.getUser().getPassword()
                        + "&client_id=JBossCore&grant_type=password")
                .post("auth/realms/OSS/protocol/openid-connect/token");
        if (response.getStatusCode() != 200) {
            LOGGER.error("Cannot get token for user {}", environment.getUser().getUsername());
        }
        String token = response.body()
                .jsonPath()
                .getString("access_token");

        LOGGER.info("Created session: {}", token);
        return token;
    }
}
