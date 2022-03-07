package com.dxc.sk.essif.verifier.service;

import com.dxc.sk.essif.verifier.model.VerificationSessionModel;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class VerifierSessionService {

    private Map<String, VerificationSessionModel> store = new HashMap<>();

    public void set(String id, VerificationSessionModel sessionModel){
        store.put(id, sessionModel);
    }

    public VerificationSessionModel get(String id){
        return store.get(id);
    }
}
