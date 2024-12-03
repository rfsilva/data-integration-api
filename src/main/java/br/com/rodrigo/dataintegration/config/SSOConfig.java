package br.com.rodrigo.dataintegration.config;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

@Configuration
@Data
public class SSOConfig {

    @Value("${service.token-path}")
    private String tokenPath;

    @Value("${service.company.host}")
    private String companyHost;

    @Value("${service.company.client-id}")
    private String companyClientId;

    @Value("${service.company.client-secret}")
    private String companyClientSecret;

    @Value("${service.person.host}")
    private String personHost;

    @Value("${service.person.client-id}")
    private String personClientId;

    @Value("${service.person.client-secret}")
    private String personClientSecret;

    @Value("${service.acl.host}")
    private String aclHost;

    @Value("${service.acl.client-id}")
    private String aclClientId;

    @Value("${service.acl.client-secret}")
    private String aclClientSecret;
}
