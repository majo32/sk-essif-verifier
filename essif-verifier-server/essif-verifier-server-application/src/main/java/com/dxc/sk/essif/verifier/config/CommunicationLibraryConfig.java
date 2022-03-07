package com.dxc.sk.essif.verifier.config;

import com.dxc.sk.essif.communication.service.JWTSignerService;
import com.dxc.sk.essif.communication.service.JWTVerifierService;
import com.dxc.sk.essif.communication.service.VerifiableCredentialService;
import com.dxc.sk.essif.communication.service.VerifiableCredentialTransformationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommunicationLibraryConfig {

    @Bean
    public VerifiableCredentialService verifiableCredentialService(){
        return new VerifiableCredentialService();
    }

    @Bean
    public JWTSignerService jwtSignerService(VerifiableCredentialTransformationService transformationService){
        return new JWTSignerService(transformationService);
    }

    @Bean
    public JWTVerifierService jwtVerifierService(VerifiableCredentialTransformationService transformationService){
        return new JWTVerifierService(transformationService);
    }

    @Bean
    public VerifiableCredentialTransformationService verifiableCredentialTransformationService(){
        return new VerifiableCredentialTransformationService();
    }
}
