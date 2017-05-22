package org.egov.domain.service;

import org.egov.domain.model.SMSMessageContext;
import org.egov.domain.model.ServiceType;
import org.egov.domain.model.SevaRequest;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class UndefinedSMSMessageStrategy implements SMSMessageStrategy {

    @Override
    public boolean matches(SevaRequest sevaRequest, ServiceType serviceType) {
        return true;
    }

    @Override
    public SMSMessageContext getMessageContext(SevaRequest sevaRequest, ServiceType serviceType) {
        throw new NotImplementedException();
    }
}
