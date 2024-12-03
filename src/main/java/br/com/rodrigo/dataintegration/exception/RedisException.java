package br.com.rodrigo.dataintegration.exception;

import java.io.*;

public class RedisException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 9032390L;

    public RedisException(String message) {
        super(message);
    }
}
