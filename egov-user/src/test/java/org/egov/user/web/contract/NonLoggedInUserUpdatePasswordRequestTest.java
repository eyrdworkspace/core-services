package org.egov.user.web.contract;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class NonLoggedInUserUpdatePasswordRequestTest {

	@Test
	public void test_should_map_from_contract_to_domain() {
		final NonLoggedInUserUpdatePasswordRequest request = NonLoggedInUserUpdatePasswordRequest.builder()
				.existingPassword("existingPassword")
				.newPassword("newPassword")
				.mobileNumber("mobileNumber")
				.otpReference("otpReference")
				.tenantId("tenant")
				.build();

		final org.egov.user.domain.model.NonLoggedInUserUpdatePasswordRequest domain = request.toDomain();

		assertNotNull(domain);
		assertEquals("existingPassword", domain.getExistingPassword());
		assertEquals("mobileNumber", domain.getMobileNumber());
		assertEquals("newPassword", domain.getNewPassword());
		assertEquals("otpReference", domain.getOtpReference());
		assertEquals("tenant", domain.getTenantId());
	}

}