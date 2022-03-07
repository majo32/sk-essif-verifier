package com.dxc.sk.essif.verifier.service;

import com.dxc.sk.essif.communication.common.EbsiWallet;
import com.dxc.sk.essif.communication.common.EbsiWalletImpl;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.Security;

@Service
@Slf4j
public class VerifierWalletService {

    private EbsiWallet wallet;
    @Value("${verifier.private-key}")
    private String privateKey ;
    @Value("${verifier.did}")
    private String did ;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @PostConstruct
    public void init(){
        wallet = EbsiWalletImpl.fromPrivateKeyHex(privateKey, did);
        log.info("PUB Key: {}", wallet.getEbsiPublicKey().toJSON());
    }

    public EbsiWallet getWallet() {
        return wallet;
    }

}
