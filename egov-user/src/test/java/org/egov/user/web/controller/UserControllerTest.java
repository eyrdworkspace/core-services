package org.egov.user.web.controller;

import org.apache.commons.io.IOUtils;
import org.egov.user.TestConfiguration;
import org.egov.user.domain.model.Action;
import org.egov.user.domain.model.SecureUser;
import org.egov.user.domain.model.UserDetail;
import org.egov.user.domain.model.UserSearch;
import org.egov.user.domain.model.enums.*;
import org.egov.user.domain.service.TokenService;
import org.egov.user.domain.service.UserService;
import org.egov.user.web.contract.auth.Role;
import org.egov.user.web.contract.auth.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@Import(TestConfiguration.class)
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@MockBean
	private TokenService tokenService;

	@Test
	@WithMockUser
	public void testUserSearch() throws Exception {
		when(userService.searchUsers(argThat(new UserSearchMatcher(getUserSearch())))).thenReturn(getUserModels());

		mockMvc.perform(post("/_search/").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(getFileContents("getUserByIdRequest.json"))).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("userSearchResponse.json")));
	}

	@Test
	@WithMockUser
	public void test_should_partially_update_user() throws Exception {
		when(userService.partialUpdate(any())).thenReturn(org.egov.user.domain.model.User.builder().build());

		mockMvc.perform(post("/user/_patch")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(getFileContents("patchUserRequest.json")))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("patchUserResponse.json")));
	}

	@Test
	@WithMockUser
	public void test_should_create_citizen() throws Exception {
		final org.egov.user.domain.model.User user = org.egov.user.domain.model.User.builder()
				.username("userName")
				.name("foo")
				.build();
		when(userService.createCitizen(any())).thenReturn(user);

		mockMvc.perform(post("/citizen/_create")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(getFileContents("userCreateRequest.json")))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("userCreateSuccessResponse.json")));
	}

	@Test
	@WithMockUser
	public void test_should_create_user() throws Exception {
		final org.egov.user.domain.model.User user = org.egov.user.domain.model.User.builder()
				.username("userName")
				.name("foo")
				.build();
		when(userService.save(any())).thenReturn(user);

		mockMvc.perform(post("/users/_create")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(getFileContents("userCreateRequest.json")))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("userCreateSuccessResponse.json")));
	}

	@Test
	@WithMockUser
	public void testUserDetails() throws Exception {
		OAuth2Authentication oAuth2Authentication = mock(OAuth2Authentication.class);
		SecureUser secureUser = new SecureUser(getUser());
		when(oAuth2Authentication.getPrincipal()).thenReturn(secureUser);
		when(tokenService.getUser("c80e0ade-f48d-4077-b0d2-4e58526a6bfd"))
				.thenReturn(getCustomUserDetails());

		mockMvc.perform(post("/_details?access_token=c80e0ade-f48d-4077-b0d2-4e58526a6bfd"))
				.andExpect(status().isOk())
				.andExpect(content().json(getFileContents("userDetailsResponse.json")));
	}

	private UserSearch getUserSearch() {
		return UserSearch.builder()
				.id(asList(1L, 2L))
				.userName("userName")
				.name("name")
				.mobileNumber("mobileNumber")
				.aadhaarNumber("aadhaarNumber")
				.pan("pan")
				.emailId("emailId")
				.fuzzyLogic(true)
				.active(true)
				.pageSize(20)
				.pageNumber(0)
				.sort(singletonList("name"))
				.type("CITIZEN")
				.build();
	}

	private List<org.egov.user.domain.model.User> getUserModels() {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("IST"));
		calendar.set(1990, Calendar.JULY, 1, 16, 41, 11);
		Date date = calendar.getTime();

		org.egov.user.domain.model.User user = org.egov.user.domain.model.User.builder()
				.id(1L)
				.tenantId("")
				.username("userName")
				.title("title")
				.password("password")
				.salutation("salutation")
				.guardian("name of relative")
				.guardianRelation(GuardianRelation.Father)
				.name("name")
				.gender(Gender.FEMALE)
				.mobileNumber("mobileNumber1")
				.emailId("email")
				.altContactNumber("mobileNumber2")
				.pan("pan")
				.aadhaarNumber("aadhaarNumber")
				.address(getAddressList())
				.active(true)
				.roles(getListOfRoles())
				.dob(date)
				.pwdExpiryDate(date)
				.locale("en_IN")
				.type(UserType.CITIZEN)
				.bloodGroup(BloodGroup.A_POSITIVE)
				.identificationMark("identification mark")
				.signature("7a9d7f12-bdcb-4487-9d43-709838a0ad39")
				.photo("3b26fb49-e43d-401b-899a-f8f0a1572de0")
				.accountLocked(false)
				.createdDate(date)
				.lastModifiedDate(date)
				.createdBy(1L)
				.lastModifiedBy(1L).build();

		return Collections.singletonList(user);
	}

	private List<org.egov.user.domain.model.Address> getAddressList() {
		return asList(org.egov.user.domain.model.Address.builder().id(1L).type(AddressType.PERMANENT).houseNoBldgApt
						("house number 1")
						.areaLocalitySector("area/locality/sector").streetRoadLine("street/road/line").landmark
								("landmark")
						.cityTownVillage("city/town/village 1").postOffice("post office").subDistrict("sub district")
						.district("district").state("state").country("country").pinCode("pincode 1").build(),

				org.egov.user.domain.model.Address.builder().id(1L).type(AddressType.CORRESPONDENCE).houseNoBldgApt
						("house number 2")
						.areaLocalitySector("area/locality/sector").streetRoadLine("street/road/line")
						.landmark("landmark").cityTownVillage("city/town/village 2").postOffice("post office")
						.subDistrict("sub district").district("district").state("state").country("country")
						.pinCode("pincode 2").build());
	}

	private List<org.egov.user.domain.model.Role> getListOfRoles() {
		User user = User.builder().id(0L).build();
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("IST"));
		calendar.set(1990, Calendar.JULY, 1, 16, 41, 11);

		org.egov.user.domain.model.Role role1 = org.egov.user.domain.model.Role.builder()
				.id(1L)
				.name("name of the role 1")
				.description("description")
				.createdBy(0L)
				.lastModifiedBy(0L)
				.createdDate(calendar.getTime())
				.lastModifiedDate(calendar.getTime())
				.build();

		return Collections.singletonList(role1);
	}

	private String getFileContents(String fileName) {
		try {
			return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	class UserSearchMatcher extends ArgumentMatcher<UserSearch> {

		private UserSearch expectedUserSearch;

		public UserSearchMatcher(UserSearch expectedUserSearch) {
			this.expectedUserSearch = expectedUserSearch;
		}

		@Override
		public boolean matches(Object o) {
			UserSearch userSearch = (UserSearch) o;
			return userSearch.getId().equals(expectedUserSearch.getId()) &&
					userSearch.getUserName().equals(expectedUserSearch.getUserName()) &&
					userSearch.getName().equals(expectedUserSearch.getName()) &&
					userSearch.getMobileNumber().equals(expectedUserSearch.getMobileNumber()) &&
					userSearch.getAadhaarNumber().equals(expectedUserSearch.getAadhaarNumber()) &&
					userSearch.getPan().equals(expectedUserSearch.getPan()) &&
					userSearch.getEmailId().equals(expectedUserSearch.getEmailId()) &&
					userSearch.isFuzzyLogic() == expectedUserSearch.isFuzzyLogic() &&
					userSearch.isActive() == expectedUserSearch.isActive() &&
					userSearch.getPageSize() == expectedUserSearch.getPageSize() &&
					userSearch.getPageNumber() == expectedUserSearch.getPageNumber() &&
					userSearch.getSort().equals(expectedUserSearch.getSort()) &&
					userSearch.getType().equals(expectedUserSearch.getType());
		}
	}

	private User getUser() {
		return User.builder()
				.id(18L)
				.userName("narasappa")
				.name("narasappa")
				.mobileNumber("123456789")
				.emailId("abc@gmail.com")
				.locale("en_IN")
				.type("EMPLOYEE")
				.active(Boolean.TRUE)
				.roles(getRoles())
				.build();
	}

	private List<Role> getRoles() {
		List<Role> roles = new ArrayList<Role>();
		org.egov.user.domain.model.Role roleModel = org.egov.user.domain.model.Role.builder()
				.id(15L)
				.name("Employee")
				.build();

		Role role = new Role(roleModel);
		roles.add(role);

		return roles;
	}

	private UserDetail getCustomUserDetails() {
		SecureUser secureUser = new SecureUser(getUser());
		List<Action> actions = new ArrayList<Action>();
		Action action = Action.builder()
				.url("/pgr/receivingmode")
				.name("ReceivingMode")
				.displayName("ReceivingMode")
				.orderNumber(0)
				.queryParams("tenantId=")
				.parentModule("1")
				.serviceCode("PGR")
				.build();
		actions.add(action);

		return new UserDetail(secureUser, actions);
	}


}