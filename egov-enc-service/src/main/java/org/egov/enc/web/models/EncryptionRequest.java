package org.egov.enc.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.LinkedList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EncryptionRequest {

    @JsonProperty("encryptionRequestObjects")
    private LinkedList<EncryptionRequestObject> encryptionRequestObjects;

}
