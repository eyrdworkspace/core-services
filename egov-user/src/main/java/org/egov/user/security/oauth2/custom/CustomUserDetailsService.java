package org.egov.user.security.oauth2.custom;

import java.util.ArrayList;
import java.util.List;

import org.egov.user.domain.model.SecureUser;
import org.egov.user.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserService userService;

	@Autowired
	public CustomUserDetailsService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		if (null == CustomAuthenticationProvider.getTenantId())
			throw new OAuth2Exception("User Session not availble (There was a server re-start.) .Please login again");

		org.egov.user.domain.model.User user = userService.getUserByUsername(username,
				CustomAuthenticationProvider.getTenantId());

		if (null == user)
			user = userService.getUserByUsernameAndTenantId(username, CustomAuthenticationProvider.getTenantId());

		if (null == user)
			throw new OAuth2Exception("User Not Found.");

		List<org.egov.user.domain.model.Role> roles = user.getRoles();

		List<org.egov.user.web.contract.auth.Role> roleList = new ArrayList<org.egov.user.web.contract.auth.Role>();

		for (org.egov.user.domain.model.Role role : roles) {
			org.egov.user.web.contract.auth.Role role1 = new org.egov.user.web.contract.auth.Role(role);
			roleList.add(role1);
		}
		org.egov.user.web.contract.auth.User authuser = org.egov.user.web.contract.auth.User.builder().id(user.getId())
				.userName(user.getUsername()).name(user.getName()).mobileNumber(user.getMobileNumber())
				.emailId(user.getEmailId()).locale(user.getLocale()).active(user.getActive()).roles(roleList)
				.tenantId(user.getTenantId()).type(user.getType().toString()).build();

		SecureUser secureUser = new SecureUser(authuser);

		return secureUser;
	}

}
