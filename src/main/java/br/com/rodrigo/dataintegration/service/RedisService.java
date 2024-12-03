package br.com.rodrigo.dataintegration.service;

public interface RedisService {

    <R> R obter(String field, String value, Class<R> type);

    <R> void registrar(String field, String value, R obj, Class<R> type);
}
