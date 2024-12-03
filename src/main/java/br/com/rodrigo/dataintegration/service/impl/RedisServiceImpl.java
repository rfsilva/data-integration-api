package br.com.rodrigo.dataintegration.service.impl;

import br.com.rodrigo.dataintegration.constants.GeneralConstants;
import br.com.rodrigo.dataintegration.service.RedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private final ObjectMapper objectMapper;

    @Autowired
    public RedisServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public <R> R obter(String field, String value, Class<R> type) {
        String key = field + GeneralConstants.REDIS_SEPARATOR + value;
        String result = (String) redisTemplate.opsForValue().get(key);
        if (result != null && !result.isEmpty()) {
            try {
                return objectMapper.readValue(result.getBytes(), type);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @Override
    public <R> void registrar(String field, String value, R obj, Class<R> type) {
        String key = field + GeneralConstants.REDIS_SEPARATOR + value;
        try {
            redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(obj), 60000, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
