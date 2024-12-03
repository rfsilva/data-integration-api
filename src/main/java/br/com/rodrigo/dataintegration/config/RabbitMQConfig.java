package br.com.rodrigo.dataintegration.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.*;

@Configuration
public class RabbitMQConfig {

    // Nome da fila
    public static final String QUEUE_NAME_INPUT = "QUEUE_INSERT_IN";

    public static final String QUEUE_NAME_OUTPUT = "QUEUE_INSERT_OUT";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME_INPUT, true);
    }
}