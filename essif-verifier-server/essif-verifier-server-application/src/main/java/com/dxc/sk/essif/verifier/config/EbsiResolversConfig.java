package com.dxc.sk.essif.verifier.config;

import com.dxc.sk.essif.communication.did.EbsiDidResolverService;
import com.dxc.sk.essif.communication.did.EbsiPublicKeyResolverService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EbsiResolversConfig {

    @Bean
    public EbsiDidResolverService didResolverService(){
        return new EbsiDidResolverService();
    }

    @Bean
    public EbsiPublicKeyResolverService publicKeyResolverService(EbsiDidResolverService didResolverService){
        return new EbsiPublicKeyResolverService(didResolverService);
    }
}
