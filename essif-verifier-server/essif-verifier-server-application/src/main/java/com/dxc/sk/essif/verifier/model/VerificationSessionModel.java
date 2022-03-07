package com.dxc.sk.essif.verifier.model;

import com.dxc.sk.essif.communication.model.credential_subject.EidasMinimalDataSet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerificationSessionModel {
    private EidasMinimalDataSet credentials;
    private int state;
    private String nonce;
    private String url;
    private String kid;
    private String did;
}
