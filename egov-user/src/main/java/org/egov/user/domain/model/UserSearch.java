package org.egov.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class UserSearch {

    private List<Long> id;

    private String userName;

    private String name;

    private String mobileNumber;

    private String aadhaarNumber;

    private String pan;

    private String emailId;

    private boolean fuzzyLogic;

    private boolean active;

    private int pageSize;

    private int pageNumber;

    private List<String> sort;

    private String type;

    private String tenantId;
}