package br.com.rodrigo.dataintegration.service.impl;

import br.com.rodrigo.dataintegration.config.*;
import br.com.rodrigo.dataintegration.dto.*;
import br.com.rodrigo.dataintegration.dto.acl.*;
import br.com.rodrigo.dataintegration.exception.*;
import br.com.rodrigo.dataintegration.service.*;
import br.com.rodrigo.dataintegration.service.rest.*;
import com.google.common.collect.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.reactive.function.client.*;

import java.util.*;

@Service
@Slf4j
public class ACLServiceImpl implements ACLService {

    @Autowired
    private HttpProvider provider;

    @Autowired
    @Getter
    private TokenGenerator tokenGenerator;

    @Autowired
    private SSOConfig ssoConfig;

    @Override
    public MainObjectDTO getOperationData(Long contextSequential, Long operationSequential) {
        log.info("Obtendo dados da operação {} do ciclo {}", operationSequential, contextSequential);
        String token = tokenGenerator.getAccessToken(ssoConfig.getAclClientId(), ssoConfig.getAclClientSecret());
        final Map<String, String> headers = ImmutableMap.<String, String>builder()
                .put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .put(HttpHeaders.AUTHORIZATION, token)
                .build();
        String host = ssoConfig.getAclHost() + "/" + contextSequential + "/" + operationSequential;
        MainObjectDTO operacao;
        try {
            operacao = provider.get(host, null, headers, MainObjectDTO.class);
        } catch (WebClientRequestException e) {
            log.info("Conexão com ACL recusada. Motivo: {}", e.getMessage());
            throw new ApiException(e, "acl", ErrorDTO.builder().code("500").description(e.getMessage()).build());
        }
        log.info("Dados da operacao {} obtidos da API do ACL", operacao.toString());
        return operacao;
    }

    @Override
    public Long insert(MainObjectDTO operacao) {
        log.info("Inserindo operacao {} no ACL", operacao.toString());

        String token = tokenGenerator.getAccessToken(ssoConfig.getAclClientId(), ssoConfig.getAclClientSecret());
        final Map<String, String> headers = ImmutableMap.<String, String>builder()
                .put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .put(HttpHeaders.AUTHORIZATION, token)
                .build();
        String host = ssoConfig.getAclHost();
        Long resultado;
        try {
            resultado = provider.post(host, headers, operacao, Long.class);
        } catch (WebClientRequestException e) {
            log.info("Conexão com ACL recusada. Motivo: {}", e.getMessage());
            throw new ApiException(e, "acl", ErrorDTO.builder().code("500").description(e.getMessage()).build());
        }
        log.info("Operação inserida com sucesso. Sequencial gerado: {}", resultado);

        return resultado;
    }
}
