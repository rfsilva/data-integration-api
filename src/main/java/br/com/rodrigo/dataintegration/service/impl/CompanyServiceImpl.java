package br.com.rodrigo.dataintegration.service.impl;

import br.com.rodrigo.dataintegration.config.*;
import br.com.rodrigo.dataintegration.constants.*;
import br.com.rodrigo.dataintegration.dto.*;
import br.com.rodrigo.dataintegration.dto.company.*;
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
public class CompanyServiceImpl implements CompanyService {

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
    public CompanyDTO getCompanyData(String companyId) {
        log.info("Obtendo dados da conta {} no cache", companyId);
        CompanyDTO conta = redisService.obter(GeneralConstants.COMPANY_KEY, companyId, CompanyDTO.class);
        if (conta == null) {
            log.info("Dados da conta {} não encontrados no cache. Obtendo da API de CC", companyId);
            String token = tokenGenerator.getAccessToken(ssoConfig.getCompanyClientId(), ssoConfig.getCompanyClientSecret());
            final Map<String, String> headers = ImmutableMap.<String, String>builder()
                    .put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .put(HttpHeaders.AUTHORIZATION, token)
                    .build();
            String host = ssoConfig.getCompanyHost() + "/id/" + companyId;
            try {
                conta = provider.get(host, null, headers, CompanyDTO.class);
            } catch (WebClientRequestException e) {
                log.info("Conexão com CC recusada. Motivo: {}", e.getMessage());
                throw new ApiException(e, "cc", ErrorDTO.builder().code("500").description(e.getMessage()).build());
            }

            log.info("Dados da conta obtidos da API. Salvando os dados da conta {} no cache", companyId);
            redisService.registrar(GeneralConstants.COMPANY_KEY, companyId, conta, CompanyDTO.class);
            log.info("Dados da conta {} salvos no cache", companyId);
        }
        return conta;
    }
}
