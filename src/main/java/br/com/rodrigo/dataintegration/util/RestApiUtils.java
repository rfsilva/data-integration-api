package br.com.rodrigo.dataintegration.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public final class RestApiUtils {

	private RestApiUtils() {
	}

    public static String createQueryParams(Map<String, String[]> params) {
        final StringBuilder queryParams = new StringBuilder();

        if (params == null || params.isEmpty()) {
            return "";
        }

        boolean first = true;
        queryParams.append('?');
        for (final Map.Entry<String, String[]> param : params.entrySet()) {
            for (int i = 0; i < param.getValue().length; i++) {
                if (!first) {
                    queryParams.append('&');
                }
                queryParams.append(param.getKey());
                queryParams.append('=');
                queryParams.append(param.getValue()[i]);
                first = false;

            }
        }
        return queryParams.toString();

    }

    public static String getPropertyFrom(String response, String node) throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        final JsonNode nodeData = objectMapper.readTree(response).get(node);
        return objectMapper.readValue(nodeData.toString(), String.class);
    }
}