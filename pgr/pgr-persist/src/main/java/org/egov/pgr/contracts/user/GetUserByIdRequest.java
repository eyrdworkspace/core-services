package org.egov.pgr.contracts.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.egov.pgr.contracts.grievance.RequestInfo;

import java.util.List;

@Setter
@Getter
@Builder
public class GetUserByIdRequest {
    @JsonProperty("requestInfo")
    private RequestInfo requestInfo;

    @JsonProperty("id")
    private List<Long> id;
}