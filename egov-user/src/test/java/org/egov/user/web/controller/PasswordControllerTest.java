package org.egov.user.web.controller;

import org.egov.user.Resources;
import org.egov.user.TestConfiguration;
import org.egov.user.domain.model.UpdatePassword;
import org.egov.user.domain.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PasswordController.class)
@Import(TestConfiguration.class)
public class PasswordControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	private Resources resources = new Resources();

	@Test
	@WithMockUser
	public void test_should_update_password_for_logged_in_user() throws Exception {
		mockMvc.perform(post("/password/_update")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(resources.getFileContents("updatePasswordRequest.json")))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(resources.getFileContents("updatePasswordResponse.json")));

		final UpdatePassword expectedRequest = UpdatePassword.builder()
				.existingPassword("oldPassword")
				.newPassword("newPassword")
				.userId(123L)
				.build();

		verify(userService).updatePasswordForLoggedInUser(expectedRequest);
	}

}