package br.com.rodrigo.dataintegration.service.rest;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

import java.util.*;

public final class RestApiUtils {

	private RestApiUtils() {
	}

    public static String createQueryParams(Map<String, String> params) {
        final StringBuilder queryParams = new StringBuilder();

        if (params != null && !params.isEmpty()) {
        	boolean first = true;
        	queryParams.append('?');
            for (final Map.Entry<String, String> param : params.entrySet()) {
            	if (!first) {
            		queryParams.append('&');
            	}
                queryParams.append(param.getKey());
                queryParams.append('=');
                queryParams.append(param.getValue());
                first = false;
            }
            return queryParams.toString();
        }

        return "";
    }

    public static String getPropertyFrom(String response, String node) throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        final JsonNode nodeData = objectMapper.readTree(response).get(node);
        return objectMapper.readValue(nodeData.toString(), String.class);
    }
}