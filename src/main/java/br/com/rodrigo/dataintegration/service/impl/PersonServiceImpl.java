package br.com.rodrigo.dataintegration.service.impl;

import br.com.rodrigo.dataintegration.config.*;
import br.com.rodrigo.dataintegration.constants.*;
import br.com.rodrigo.dataintegration.dto.*;
import br.com.rodrigo.dataintegration.dto.person.*;
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
public class PersonServiceImpl implements PersonService {

    @Autowired
    private HttpProvider provider;

    @Autowired
    private RedisService redisService;

    @Autowired
    @Getter
    private TokenGenerator tokenGenerator;

    @Autowired
    private SSOConfig ssoConfig;

    @Override
    public PersonDTO getPersonData(String personId) {
        log.info("Obtendo dados da pessoa {} no cache", personId);
        PersonDTO pessoa = redisService.obter(GeneralConstants.PERSON_KEY, personId, PersonDTO.class);
        if (pessoa == null) {
            log.info("Dados da pessoa {} não encontrados no cache. Obtendo da API de EQ3", personId);
            String token = tokenGenerator.getAccessToken(ssoConfig.getPersonClientId(), ssoConfig.getPersonClientSecret());
            final Map<String, String> headers = ImmutableMap.<String, String>builder()
                    .put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .put(HttpHeaders.AUTHORIZATION, token)
                    .build();
            String host = ssoConfig.getPersonHost() + "/id/" + personId;
            try {
                pessoa = provider.get(host, null, headers, PersonDTO.class);
            } catch (WebClientRequestException e) {
                log.info("Conexão com EQ3 recusada. Motivo: {}", e.getMessage());
                throw new ApiException(e, "eq3", ErrorDTO.builder().code("500").description(e.getMessage()).build());
            }

                log.info("Dados da pessoa obtidos da API. Salvando os dados da pessoa {} no cache", personId);
            redisService.registrar(GeneralConstants.PERSON_KEY, personId, pessoa, PersonDTO.class);
            log.info("Dados da pessoa {} salvos no cache", personId);
        }
        return pessoa;
    }
}
