package br.com.rodrigo.dataintegration.service.rest;

import br.com.rodrigo.dataintegration.config.*;
import br.com.rodrigo.dataintegration.dto.*;
import br.com.rodrigo.dataintegration.exception.*;
import com.fasterxml.jackson.core.*;
import lombok.extern.log4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.util.*;
import org.springframework.web.reactive.function.client.*;

import java.util.*;
import java.util.concurrent.*;

@Service
@Log4j2
public class TokenGenerator {

    @Autowired
    private SSOConfig ssoConfig;

    @Autowired
    private HttpProvider provider;

    private static final String ERRO_MSG = "Falha ao obter o token.";

    public String getAccessToken(String clientId, String clientSecret) {
        if (!Token.getInstance().isValido()) {
            try {
                log.info("Obtendo o token de {}", clientId);
                final MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
                body.add("grant_type", "client_credentials");
                body.add("client_id", clientId);
                body.add("client_secret", clientSecret);

                final Map<String, String> headers = new ConcurrentHashMap<>();
                headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
                final String response = provider.post(ssoConfig.getTokenPath(), headers, body, String.class);
                Token.getInstance().setToken(response);
                log.info("Token obtido com sucesso!");

            } catch (WebClientResponseException ex) {
                log.info(ex.getResponseBodyAsString());
                log.error("Erro ao Obter Token para a requisição");
                throw new ApiException(ex, ERRO_MSG, ErrorDTO.builder().code("500").description(ex.getMessage()).build());
            } catch(JsonProcessingException ex){
                log.error("Erro ao Obter Token para a requisição");
                throw new ApiException(ex, ERRO_MSG, ErrorDTO.builder().code("500").description(ex.getMessage()).build());
            }
        }
        return Token.getInstance().getAccessToken();
    }
}