package org.egov.domain.model;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
@Builder
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Message {
 
    private String code;
    private String message;
    private Tenant tenant;
    private String locale;
    private String module;

    public boolean isMoreSpecificComparedTo(Message otherMessage) {
        return code.equals(otherMessage.getCode())
            && locale.equals(otherMessage.getLocale())
            && tenant.isMoreSpecificComparedTo(otherMessage.getTenant());
    }
}