package org.egov.user.domain.service;


import org.egov.user.domain.exception.InvalidAccessTokenException;
import org.egov.user.domain.exception.UserDetailsException;
import org.egov.user.persistence.repository.ActionRestRepository;
import org.egov.user.web.contract.ActionResponse;
import org.egov.user.web.contract.auth.Action;
import org.egov.user.web.contract.auth.CustomUserDetails;
import org.egov.user.web.contract.auth.Role;
import org.egov.user.web.contract.auth.SecureUser;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TokenService {

    private TokenStore tokenStore;

    private ActionRestRepository actionRestRepository;

    private TokenService(TokenStore tokenStore,ActionRestRepository actionRestRepository) {
        this.tokenStore = tokenStore;
        this.actionRestRepository=actionRestRepository;
    }


    public CustomUserDetails getUser(String accessToken) {

        if (accessToken.isEmpty()) {
            throw new InvalidAccessTokenException();
        }
        OAuth2Authentication authentication = tokenStore.readAuthentication(accessToken);
        if (authentication == null) {
            throw new UserDetailsException();
        }
        SecureUser secureUser = ((SecureUser) authentication.getPrincipal());

         List<String> roleCodes = secureUser.getUser().getRoles().stream().map(Role::getCode).collect(Collectors.toList());
        String tenantId= secureUser.getUser().getTenantId();
        if(!roleCodes.isEmpty()) {
            ActionResponse actions = actionRestRepository.getActionByRoleCodes(roleCodes,tenantId);
            if (!actions.isActions()) {
                return new CustomUserDetails(secureUser, actions.getActions());
            }
        }
        return new CustomUserDetails(secureUser, new ArrayList<Action>());
    }

}
