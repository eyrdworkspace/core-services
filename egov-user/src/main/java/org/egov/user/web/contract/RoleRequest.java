package org.egov.user.web.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.user.domain.model.Role;

import java.util.Date;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleRequest {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("createdBy")
    private Long createdBy;

    @JsonProperty("createdDate")
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date createdDate;

    @JsonProperty("lastModifiedBy")
    private Long lastModifiedBy;

    @JsonProperty("lastModifiedDate")
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date lastModifiedDate;

    public RoleRequest(org.egov.user.persistence.entity.Role roleEntity) {
        this.id = roleEntity.getId();
        this.name = roleEntity.getName();
        this.description = roleEntity.getDescription();
        this.createdBy = roleEntity.getCreatedBy()==null ? 0L : roleEntity.getCreatedBy();
        this.createdDate = roleEntity.getCreatedDate();
        this.lastModifiedBy = roleEntity.getLastModifiedBy()==null ? 0L : roleEntity.getLastModifiedBy();
        this.lastModifiedDate = roleEntity.getLastModifiedDate();
    }

    public RoleRequest(Role domainRole) {
        this.id = domainRole.getId();
        this.name = domainRole.getName();
        this.description = domainRole.getDescription();
        this.createdBy = domainRole.getCreatedBy()==null ? 0L : domainRole.getCreatedBy();
        this.createdDate = domainRole.getCreatedDate();
        this.lastModifiedBy = domainRole.getLastModifiedBy()==null ? 0L : domainRole.getLastModifiedBy();
        this.lastModifiedDate = domainRole.getLastModifiedDate();
    }
    
    public Role toDomain() {
        return Role.builder().id(id).name(name).build();
    }
}
