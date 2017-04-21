package org.egov.user.domain.service;

import org.egov.user.domain.exception.*;
import org.egov.user.domain.model.Role;
import org.egov.user.domain.model.User;
import org.egov.user.domain.model.UserSearch;
import org.egov.user.domain.model.enums.Gender;
import org.egov.user.domain.model.enums.UserType;
import org.egov.user.persistence.repository.OtpRepository;
import org.egov.user.persistence.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private OtpRepository otpRepository;

	@InjectMocks
	private UserService userService;

	private final List<Long> ID = Arrays.asList(1L, 2L);
	private final String EMAIL = "email@gmail.com";
	private final String USER_NAME = "userName";

	@Test
	public void test_should_get_user_by_email() throws Exception {
		when(userRepository.findByEmailId(EMAIL)).thenReturn(getUserObject());

		User actualUser = userService.getUserByEmailId(EMAIL);

		assertThat(actualUser.getEmailId()).isEqualTo(EMAIL);
	}

	@Test
	public void test_should_get_user_by_username() throws Exception {
		when(userRepository.findByUsername(USER_NAME)).thenReturn(getUserObject());

		User actualUser = userService.getUserByUsername(USER_NAME);

		assertThat(actualUser.getUsername()).isEqualTo(USER_NAME);
	}

	@Test
	public void test_should_search_user() throws Exception {
		UserSearch userSearch = UserSearch.builder().build();
		List<org.egov.user.domain.model.User> expectedListOfUsers = mock(List.class);
		when(userRepository.findAll(userSearch)).thenReturn(expectedListOfUsers);

		List<org.egov.user.domain.model.User> actualResult = userService.searchUsers(userSearch);

		assertThat(expectedListOfUsers).isEqualTo(actualResult);
	}

	@Test
	public void test_should_save_a_valid_user() throws Exception {
		org.egov.user.domain.model.User domainUser = validDomainUser();
		when(otpRepository.isOtpValidationComplete(domainUser)).thenReturn(true);
		final User expectedEntityUser = User.builder().build();
		when(userRepository.save(domainUser)).thenReturn(expectedEntityUser);

		User returnedUser = userService.save(domainUser);

		assertEquals(expectedEntityUser, returnedUser);
	}

	@Test
	public void test_should_create_a_valid_citizen() {
		org.egov.user.domain.model.User domainUser = mock(User.class);
		when(otpRepository.isOtpValidationComplete(domainUser)).thenReturn(true);
		final User expectedEntityUser = User.builder().build();
		when(userRepository.save(domainUser)).thenReturn(expectedEntityUser);

		User returnedUser = userService.createCitizen(domainUser);

		assertEquals(expectedEntityUser, returnedUser);
	}

	@Test
	public void test_should_set_role_to_citizen_when_creating_a_citizen() {
		org.egov.user.domain.model.User domainUser = mock(User.class);
		when(otpRepository.isOtpValidationComplete(domainUser)).thenReturn(true);
		final User expectedEntityUser = User.builder().build();
		when(userRepository.save(domainUser)).thenReturn(expectedEntityUser);

		userService.createCitizen(domainUser);

		verify(domainUser).setRoleToCitizen();
	}

	@Test(expected = DuplicateUserNameException.class)
	public void test_should_raise_exception_when_duplicate_user_name_exists() throws Exception {
		org.egov.user.domain.model.User domainUser = validDomainUser();
		when(otpRepository.isOtpValidationComplete(domainUser)).thenReturn(true);
		when(userRepository.isUserPresent("supandi_rocks", "tenantId")).thenReturn(true);

		userService.save(domainUser);
	}

	@Test(expected = OtpValidationPendingException.class)
	public void test_exception_is_raised_when_otp_validation_fails() throws Exception {
		org.egov.user.domain.model.User domainUser = validDomainUserWithRole();
		domainUser.setOtpValidationMandatory(true);
		when(otpRepository.isOtpValidationComplete(domainUser)).thenReturn(false);

		userService.save(domainUser);
	}

	@Test
	public void test_otp_is_not_validated_when_validation_flag_is_false() throws Exception {
		org.egov.user.domain.model.User domainUser = validDomainUserWithRole();
		when(otpRepository.isOtpValidationComplete(domainUser)).thenReturn(false);

		userService.save(domainUser);

		verify(otpRepository, never()).isOtpValidationComplete(domainUser);
	}

	@Test(expected = InvalidUserException.class)
	public void test_should_raise_exception_when_user_is_invalid() throws Exception {
		org.egov.user.domain.model.User domainUser = org.egov.user.domain.model.User.builder().build();

		userService.save(domainUser);
		verify(userRepository, never()).save(any(org.egov.user.domain.model.User.class));
	}

	@Test
	public void test_should_update_a_valid_user() throws Exception {
		User domainUser = validDomainUser();
		org.egov.user.persistence.entity.User user = new org.egov.user.persistence.entity.User();
		final User expectedEntityUser = User.builder().build();
		when(userRepository.update(any(Long.class), any(org.egov.user.domain.model.User.class)))
				.thenReturn(expectedEntityUser);
		when(userRepository.getUserById(any(Long.class))).thenReturn(user);
		when(userRepository.isUserPresent(any(String.class), any(Long.class), any(String.class))).thenReturn(false);

		User returnedUser = userService.updateWithoutOtpValidation(1L, domainUser);

		assertEquals(expectedEntityUser, returnedUser);
	}

	@Test(expected = DuplicateUserNameException.class)
	public void test_should_throw_error_when_username_exists_while_updating() throws Exception {
		User domainUser = validDomainUser();
		when(userRepository.isUserPresent(any(String.class), any(Long.class), any(String.class))).thenReturn(true);

		userService.updateWithoutOtpValidation(1L, domainUser);
	}

	@Test(expected = UserNotFoundException.class)
	public void test_should_throw_error_when_user_not_exists_while_updating() throws Exception {
		User domainUser = validDomainUser();
		when(userRepository.isUserPresent(any(String.class), any(Long.class), any(String.class))).thenReturn(false);
		when(userRepository.getUserById(any(Long.class))).thenReturn(null);

		userService.updateWithoutOtpValidation(1L, domainUser);
	}

	@Test(expected = UserIdMandatoryException.class)
	public void test_should_throw_exception_on_partial_update_when_id_is_not_present() {
		final User user = User.builder()
				.id(null)
				.build();

		userService.partialUpdate(user);
	}

	@Test
	public void test_should_nullify_fields_that_are_not_allowed_to_be_updated() {
		final User user = mock(User.class);

		userService.partialUpdate(user);

		verify(user).nullifySensitiveFields();
	}

	@Test
	public void test_should_partially_update_user() {
		final User user = mock(User.class);
		final long userId = 123L;
		when(user.getId()).thenReturn(userId);

		userService.partialUpdate(user);

		verify(userRepository).update(userId, user);
	}

	@Test(expected = UserProfileUpdateDeniedException.class)
	public void test_should_throw_exception_when_logged_in_user_is_different_from_user_being_updated() {
		final User user = mock(User.class);
		when(user.isLoggedInUserDifferentFromUpdatedUser()).thenReturn(true);

		userService.partialUpdate(user);
	}

	private org.egov.user.domain.model.User validDomainUser() {
		return getUserBuilder().build();
	}

	private org.egov.user.domain.model.User validDomainUserWithRole() {
		return getUserBuilder().build();
	}

	private org.egov.user.domain.model.User.UserBuilder getUserBuilder() {
		return org.egov.user.domain.model.User.builder()
				.username("supandi_rocks")
				.name("Supandi")
				.gender(Gender.MALE)
				.type(UserType.CITIZEN)
				.active(Boolean.TRUE)
				.mobileNumber("9988776655")
				.tenantId("tenantId")
				.otpReference("12312")
				.roles(Collections.singletonList(Role.builder().code("roleCode1").build()))
				.accountLocked(false);
	}

	private User getUserObject() {
		return User.builder().id(ID.get(0)).emailId(EMAIL).username(USER_NAME).build();
	}
}