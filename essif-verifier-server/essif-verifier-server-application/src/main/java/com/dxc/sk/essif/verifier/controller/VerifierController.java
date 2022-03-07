package com.dxc.sk.essif.verifier.controller;

import com.dxc.sk.essif.communication.messages.EbsiDidAuthRequestMessage;
import com.dxc.sk.essif.communication.messages.EbsiDidAuthResponseMessage;
import com.dxc.sk.essif.communication.messages.EbsiSignedMessage;
import com.dxc.sk.essif.communication.model.VerifiableCredentialWrapperJWT;
import com.dxc.sk.essif.communication.model.credential_subject.EidasMinimalDataSet;
import com.dxc.sk.essif.verifier.model.VerificationSessionModel;
import com.dxc.sk.essif.verifier.server.api.VerifierApi;
import com.dxc.sk.essif.verifier.server.api.model.*;
import com.dxc.sk.essif.verifier.service.QRCodeGeneratorService;
import com.dxc.sk.essif.verifier.service.VerifierService;
import com.dxc.sk.essif.verifier.service.VerifierSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@Slf4j
public class VerifierController implements VerifierApi {

    @Autowired
    private QRCodeGeneratorService qrCodeGeneratorService;

    @Autowired
    private VerifierService verifierService;

    @Autowired
    private VerifierSessionService sessionService;

    @Value("${holder.baseUrl}")
    private String holderBaseUrl;

    @Override
    public ResponseEntity<VerifierGenerateQRResponse> getVerifierGenerateQr() {

        EbsiSignedMessage<EbsiDidAuthRequestMessage> signedMessage = verifierService.generateRequest();
        String requestJWT = signedMessage.getJwt().serialize();
        sessionService.set(signedMessage.getMessage().getNonce(), VerificationSessionModel.builder()
                .did(signedMessage.getMessage().getSub())
                .state(0).build());
        return ResponseEntity.ok(
                new VerifierGenerateQRResponse()
                        .qr(qrCodeGeneratorService.generateBase64(requestJWT))
                        .request(requestJWT)
                        .holderUrl(holderBaseUrl)
                        .sessionId(signedMessage.getMessage().getNonce())
        );
    }

    @Override
    public ResponseEntity<Void> postResponse(String body) {

        EbsiSignedMessage<EbsiDidAuthResponseMessage> didAuthResponse = verifierService.parseResponse(body);
        VerificationSessionModel sessionModel = sessionService.get(didAuthResponse.getMessage().getNonce());
        log.info(didAuthResponse.getMessage().getClaims().getVerified_claims());
        VerifiableCredentialWrapperJWT<EidasMinimalDataSet> vc = verifierService.parseVC(didAuthResponse.getMessage().getClaims().getVerified_claims());
        sessionModel.setCredentials(vc.getVc().getCredentialSubject());
        sessionModel.setState(1);
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<VerifierGetCredentialsResponse> postVerifierGetCredentials(VerifierGetCredentialsRequest verifierGetCredentialsRequest) {
        VerificationSessionModel sessionModel = sessionService.get(verifierGetCredentialsRequest.getId());

        return ResponseEntity.ok(new VerifierGetCredentialsResponse()
                .id(sessionModel.getCredentials().getId())
                .identifier(sessionModel.getCredentials().getPersonalIdentifier())
                .name(sessionModel.getCredentials().getFirstName())
                .lastname(sessionModel.getCredentials().getFamilyName())
                .dateOfBirth(sessionModel.getCredentials().getDateOfBirth())

        );
    }

    @Override
    public ResponseEntity<VerifierStateResponse> postVerifierState(VerifierStateRequest verifierStateRequest) {
        VerificationSessionModel sessionModel = sessionService.get(verifierStateRequest.getId());
        return ResponseEntity.ok(new VerifierStateResponse().state(sessionModel.getState()));
    }
}
