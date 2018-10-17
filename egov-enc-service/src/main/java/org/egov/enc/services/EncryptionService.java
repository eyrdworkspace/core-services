package org.egov.enc.services;

import lombok.extern.slf4j.Slf4j;
import org.egov.enc.KeyManagementApplication;
import org.egov.enc.models.Ciphertext;
import org.egov.enc.models.MethodEnum;
import org.egov.enc.models.ModeEnum;
import org.egov.enc.models.Plaintext;
import org.egov.enc.utils.ProcessJSONUtil;
import org.egov.enc.web.models.EncryptReqObject;
import org.egov.enc.web.models.EncryptionRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.awt.image.ImageWatched;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;


@Slf4j
@Service
public class EncryptionService {


    private ProcessJSONUtil processJSONUtil;
    private KeyManagementApplication keyManagementApplication;

    @Autowired
    public EncryptionService(ProcessJSONUtil processJSONUtil, KeyManagementApplication keyManagementApplication) {
        this.processJSONUtil = processJSONUtil;
        this.keyManagementApplication = keyManagementApplication;
    }

    public Object encrypt(EncryptionRequest encryptionRequest) throws Exception {

        LinkedList<Object> outputList = new LinkedList<>();
        for(EncryptReqObject encryptReqObject: encryptionRequest.getEncryptReqObjects()) {
            if(!keyManagementApplication.checkTenant(encryptReqObject.getTenantId())) {
                throw new CustomException("Tenant Does Not Exist", "Tenant Does Not Exist");
            }
            outputList.add(processJSONUtil.processJSON(encryptReqObject.getValue(), ModeEnum.ENCRYPT, encryptReqObject.getMethod(), encryptReqObject.getTenantId()));
        }
        return outputList;
    }

    public Object decrypt(Object decryptReq) throws Exception {
        return processJSONUtil.processJSON(decryptReq, ModeEnum.DECRYPT, null, null);
    }
}
