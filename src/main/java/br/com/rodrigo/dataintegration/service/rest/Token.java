package br.com.rodrigo.dataintegration.service.rest;

import com.fasterxml.jackson.core.*;
import lombok.*;

import java.time.*;

@Data
public final class Token {

    private static Token instance = new Token();

    private Token() {
    }

    public static Token getInstance() {
        return instance;
    }

    public void setToken(String response) throws JsonProcessingException {
        accessToken = "Bearer " + RestApiUtils.getPropertyFrom(response, "access_token");
        expiresIn = Integer.valueOf(RestApiUtils.getPropertyFrom(response, "expires_in"));
        refreshExpiresIn = Integer.valueOf(RestApiUtils.getPropertyFrom(response, "refresh_expires_in"));
        tokenType = RestApiUtils.getPropertyFrom(response, "token_type");
        notBeforePolicy = Integer.valueOf(RestApiUtils.getPropertyFrom(response, "not-before-policy"));
        scope = RestApiUtils.getPropertyFrom(response, "scope");
        generationTime = LocalDateTime.now();
    }

    @Getter
    private LocalDateTime generationTime;
    @Getter
    private String accessToken;
    @Getter
    private Integer expiresIn;
    @Getter
    private Integer refreshExpiresIn;
    @Getter
    private String tokenType;
    @Getter
    private Integer notBeforePolicy;
    @Getter
    private String scope;

    public boolean isValido() {
        return generationTime != null && LocalDateTime.now().isBefore(generationTime.plusSeconds(expiresIn));
    }
}