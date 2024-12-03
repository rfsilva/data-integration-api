package br.com.rodrigo.dataintegration.service.rest;

import br.com.rodrigo.dataintegration.dto.*;
import br.com.rodrigo.dataintegration.exception.ApiException;
import br.com.rodrigo.dataintegration.util.RestApiUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.IOException;
import java.util.Map;

@Service
@Log4j2
public class HttpProvider {
	
	private final ObjectMapper objectMapper;
	
	@Autowired
	public HttpProvider(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
    
    public <B, R> R post(String baseUrl, Map<String, String> headers, B body,
                         Class<R> type) {

        try {
	        return WebClient.create(baseUrl)
	            .post()
	            .headers(h -> h.setAll(headers))
	            .bodyValue(body)
	            .retrieve()
	            .bodyToMono(type)
	            .block();
        } catch (WebClientResponseException e) {
            log.error("Erro na requisição", e);

            try {
                return objectMapper.readValue(e.getResponseBodyAsByteArray(), type);
            } catch (IOException ex) {
                log.error("Erro ao acessar api", ex);
                throw new ApiException(e, baseUrl, ErrorDTO.builder().code("500").description(e.getMessage()).build());
            }
        }
    }

    public <R> R get(String baseUrl, Map<String, String[]> queryParams, Map<String, String> headers, Class<R> type) {
        try{
            final String fullPath = baseUrl + RestApiUtils.createQueryParams(queryParams);
            return WebClient.create(fullPath).get().headers(h -> h.setAll(headers)).retrieve().bodyToMono(type).block();
        } catch (WebClientResponseException e) {
            log.error("Erro na requisição", e);

            try {
                return objectMapper.readValue(e.getResponseBodyAsByteArray(), type);
            } catch (IOException ex) {
                log.error("Erro ao acessar api", ex);
                throw new ApiException(e, baseUrl, ErrorDTO.builder().code("500").description(e.getMessage()).build());
            }
        }
    }
}