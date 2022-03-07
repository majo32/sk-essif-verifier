package com.dxc.sk.essif.verifier.service;

import com.dxc.sk.essif.communication.EbsiDidAuthLib;
import com.dxc.sk.essif.communication.did.EbsiPublicKeyResolverService;
import com.dxc.sk.essif.communication.messages.EbsiDidAuthRequestMessage;
import com.dxc.sk.essif.communication.messages.EbsiDidAuthResponseMessage;
import com.dxc.sk.essif.communication.messages.EbsiSignedMessage;
import com.dxc.sk.essif.communication.model.VerifiableCredential;
import com.dxc.sk.essif.communication.model.VerifiableCredentialWrapperJWT;
import com.dxc.sk.essif.communication.model.credential_subject.EidasMinimalDataSet;
import com.dxc.sk.essif.communication.service.JWTSignerService;
import com.dxc.sk.essif.communication.service.JWTVerifierService;
import com.dxc.sk.essif.communication.service.VerifiableCredentialService;
import com.dxc.sk.essif.communication.service.VerifiableCredentialTransformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.UUID;

@Service
public class VerifierService {

    @Autowired
    private VerifierSessionService sessionService;

    @Autowired
    private VerifierWalletService walletService;

    @Autowired
    private VerifiableCredentialService verifiableCredentialService;

    @Autowired
    private JWTSignerService signerService;

    @Autowired
    private JWTVerifierService verifierService;

    @Autowired
    private VerifiableCredentialTransformationService transformationService;

    @Autowired
    private EbsiPublicKeyResolverService ebsiPublicKeyResolverService;

    @Value("${verifier.baseUrl}")
    private String baseUrl;

    private RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    private final EbsiDidAuthLib lib = new EbsiDidAuthLib();

    public EbsiSignedMessage<EbsiDidAuthRequestMessage> generateRequest() {
        String nonce = UUID.randomUUID().toString();
        EbsiSignedMessage<EbsiDidAuthRequestMessage> message =
                lib.createDidAuthRequest(walletService.getWallet(),
                        nonce, baseUrl + "/api/response","EssifVerifiableID");
        return message;
    }

    public EbsiSignedMessage<EbsiDidAuthResponseMessage> parseResponse(String response){
        EbsiSignedMessage<EbsiDidAuthResponseMessage> didAuthResponseMessage = lib.parseDidAuthResponse(response);
        return didAuthResponseMessage;
    }

    public VerifiableCredentialWrapperJWT<EidasMinimalDataSet> parseVC(String vc){
        VerifiableCredentialWrapperJWT<EidasMinimalDataSet> wrapperJWTParsed =
                verifierService.parseAndVerifySignature(ebsiPublicKeyResolverService, vc, EidasMinimalDataSet.class);

        return wrapperJWTParsed;
    }

}
