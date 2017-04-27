package org.egov.user.web.contract;

import lombok.*;
import org.omg.PortableInterceptor.RequestInfo;

/*
	Update password request by non logged in user
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NonLoggedInUserUpdatePasswordRequest {
	private RequestInfo requestInfo;
	private String otpReference;
	private String mobileNumber;
	private String existingPassword;
	private String newPassword;
	private String tenantId;

	public org.egov.user.domain.model.NonLoggedInUserUpdatePasswordRequest toDomain() {
		return org.egov.user.domain.model.NonLoggedInUserUpdatePasswordRequest.builder()
				.otpReference(otpReference)
				.mobileNumber(mobileNumber)
				.existingPassword(existingPassword)
				.newPassword(newPassword)
				.tenantId(tenantId)
				.build();
	}
}
