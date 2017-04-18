package org.egov.user.web.contract;

import org.egov.user.domain.model.Address;
import org.egov.user.domain.model.Role;
import org.egov.user.domain.model.User;
import org.egov.user.domain.model.enums.*;
import org.junit.Test;

import java.util.*;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.*;

public class UserRequestTest {

    @Test
    public void test_domain_to_contract_conversion() {
        User domainUser = getUser();

        UserRequest userRequestContract = new UserRequest(domainUser);

        assertThat(userRequestContract.getId()).isEqualTo(domainUser.getId());
        assertThat(userRequestContract.getUserName()).isEqualTo(domainUser.getUsername());
        assertThat(userRequestContract.getSalutation()).isEqualTo(domainUser.getSalutation());
        assertThat(userRequestContract.getName()).isEqualTo(domainUser.getName());
        assertThat(userRequestContract.getGender()).isEqualTo(domainUser.getGender().toString());
        assertThat(userRequestContract.getMobileNumber()).isEqualTo(domainUser.getMobileNumber());
        assertThat(userRequestContract.getEmailId()).isEqualTo(domainUser.getEmailId());
        assertThat(userRequestContract.getAltContactNumber()).isEqualTo(domainUser.getAltContactNumber());
        assertThat(userRequestContract.getPan()).isEqualTo(domainUser.getPan());
        assertThat(userRequestContract.getAadhaarNumber()).isEqualTo(domainUser.getAadhaarNumber());
        assertThat(userRequestContract.getPermanentAddress()).isEqualTo("house number 1, area/locality/sector, " +
                "street/road/line, landmark, city/town/village 1, post office, sub district, " +
                "district, state, country, PIN: pincode 1");
        assertThat(userRequestContract.getPermanentCity()).isEqualTo("city/town/village 1");
        assertThat(userRequestContract.getPermanentPinCode()).isEqualTo("pincode 1");
        assertThat(userRequestContract.getCorrespondenceAddress()).isEqualTo("house number 2, area/locality/sector, " +
                "street/road/line, landmark, city/town/village 2, post office, sub district, " +
                "district, state, country, PIN: pincode 2");
        assertThat(userRequestContract.getCorrespondenceCity()).isEqualTo("city/town/village 2");
        assertThat(userRequestContract.getCorrespondencePinCode()).isEqualTo("pincode 2");
        assertThat(userRequestContract.getActive()).isEqualTo(domainUser.getActive());
        assertThat(userRequestContract.getDob()).isEqualTo(domainUser.getDob());
        assertThat(userRequestContract.getPwdExpiryDate()).isEqualTo(domainUser.getPwdExpiryDate());
        assertThat(userRequestContract.getLocale()).isEqualTo(domainUser.getLocale());
        assertThat(userRequestContract.getType()).isEqualTo(domainUser.getType());
        assertThat(userRequestContract.getAccountLocked()).isEqualTo(domainUser.getAccountLocked());
        assertThat(userRequestContract.getFatherOrHusbandName()).isEqualTo(domainUser.getGuardian());
        assertThat(userRequestContract.getSignature()).isEqualTo(domainUser.getSignature());
        assertThat(userRequestContract.getBloodGroup()).isEqualTo(domainUser.getBloodGroup().getValue());
        assertThat(userRequestContract.getPhoto()).isEqualTo(domainUser.getPhoto());
        assertThat(userRequestContract.getIdentificationMark()).isEqualTo(domainUser.getIdentificationMark());
        assertThat(userRequestContract.getRoles().get(0).getName()).isEqualTo("name of the role 1");
        assertThat(userRequestContract.getRoles().get(1).getName()).isEqualTo("name of the role 2");
        assertThat(userRequestContract.getCreatedBy()).isEqualTo(1L);
        assertThat(userRequestContract.getCreatedDate()).isEqualTo(domainUser.getCreatedDate());
        assertThat(userRequestContract.getLastModifiedBy()).isEqualTo(2L);
        assertThat(userRequestContract.getLastModifiedDate()).isEqualTo(domainUser.getLastModifiedDate());
    }

    @Test
    public void test_contract_to_domain_conversion() {
        UserRequest userRequest = buildUserRequest();

        User userForCreate = userRequest.toDomain();

        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        c.set(2017, 1, 1, 1, 1, 1);
        String expectedDate = c.getTime().toString();

        assertEquals("Kroorveer", userForCreate.getName());
        assertEquals("yakku", userForCreate.getUsername());
        assertEquals("Dr.", userForCreate.getSalutation());
        assertEquals("8967452310", userForCreate.getMobileNumber());
        assertEquals("kroorkool@maildrop.cc", userForCreate.getEmailId());
        assertEquals("0987654321", userForCreate.getAltContactNumber());
        assertEquals("KR12345J", userForCreate.getPan());
        assertEquals("qwerty-1234567", userForCreate.getAadhaarNumber());
        assertTrue(userForCreate.getActive());
        assertEquals(expectedDate, userForCreate.getDob().toString());
        assertEquals(expectedDate, userForCreate.getPwdExpiryDate().toString());
        assertEquals("en_IN", userForCreate.getLocale());
        assertEquals(UserType.CITIZEN, userForCreate.getType());
        assertFalse(userForCreate.getAccountLocked());
        assertEquals("signature", userForCreate.getSignature());
        assertEquals("myPhoto", userForCreate.getPhoto());
        assertEquals("hole in the mole", userForCreate.getIdentificationMark());
        assertEquals(Gender.MALE, userForCreate.getGender());
        assertEquals(BloodGroup.O_POSITIVE, userForCreate.getBloodGroup());
        assertNotNull(userForCreate.getLastModifiedDate());
        assertNotNull(userForCreate.getCreatedDate());
        assertNotEquals(expectedDate, userForCreate.getLastModifiedDate().toString());
        assertNotEquals(expectedDate, userForCreate.getCreatedDate().toString());
		final List<Role> roles = userForCreate.getRoles();
		assertEquals(1, roles.size());
		assertEquals("CITIZEN", roles.get(0).getCode());
        assertEquals("ap.public", userForCreate.getTenantId());
        assertEquals("otpreference1", userForCreate.getOtpReference());
        assertEquals("!abcd1234", userForCreate.getPassword());
    }

	private UserRequest buildUserRequest() {
        List<RoleRequest> roles = new ArrayList<>();
        roles.add(RoleRequest.builder().code("CITIZEN").build());
        roles.add(RoleRequest.builder().code("CITIZEN").build());
        return getUserBuilder(roles).build();
    }

    private UserRequest.UserRequestBuilder getUserBuilder(List<RoleRequest> roles) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        c.set(2017, 01, 01, 01, 01, 01);
        Date dateToTest = c.getTime();
        return UserRequest.builder()
                .name("Kroorveer")
                .userName("yakku")
                .salutation("Dr.")
                .mobileNumber("8967452310")
                .emailId("kroorkool@maildrop.cc")
                .altContactNumber("0987654321")
                .pan("KR12345J")
                .aadhaarNumber("qwerty-1234567")
                .active(Boolean.TRUE)
                .dob(dateToTest)
                .pwdExpiryDate(dateToTest)
                .locale("en_IN")
                .type(UserType.CITIZEN)
                .accountLocked(Boolean.FALSE)
                .signature("signature")
                .photo("myPhoto")
                .identificationMark("hole in the mole")
                .gender("Male")
                .bloodGroup("O_positive")
                .lastModifiedDate(dateToTest)
                .createdDate(dateToTest)
                .tenantId("ap.public")
                .otpReference("otpreference1")
                .password("!abcd1234")
                .roles(roles);
    }

    private User getUser() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1990, Calendar.JULY, 1);
        Date date = calendar.getTime();

        return User.builder()
                .id(1L)
                .username("userName")
                .salutation("salutation")
                .name("name")
                .gender(Gender.FEMALE)
                .mobileNumber("mobileNumber1")
                .emailId("email")
                .altContactNumber("mobileNumber2")
                .pan("pan")
                .aadhaarNumber("aadhaarNumber")
                .address(getAddressList())
                .active(true)
                .dob(date)
                .pwdExpiryDate(date)
                .locale("en_IN")
                .type(UserType.CITIZEN)
                .accountLocked(false)
                .roles(getListOfRoles())
                .guardian("name of relative")
                .guardianRelation(GuardianRelation.Father)
                .signature("7a9d7f12-bdcb-4487-9d43-709838a0ad39")
                .bloodGroup(BloodGroup.A_POSITIVE)
                .photo("3b26fb49-e43d-401b-899a-f8f0a1572de0")
                .identificationMark("identification mark")
                .createdBy(1L)
                .createdDate(date)
                .lastModifiedBy(2L)
                .lastModifiedDate(date)
                .build();
    }

    private List<Address> getAddressList() {
        return asList(Address.builder()
                        .id(1L)
                        .type(AddressType.PERMANENT)
                        .houseNoBldgApt("house number 1")
                        .areaLocalitySector("area/locality/sector")
                        .streetRoadLine("street/road/line")
                        .landmark("landmark")
                        .cityTownVillage("city/town/village 1")
                        .postOffice("post office")
                        .subDistrict("sub district")
                        .district("district")
                        .state("state")
                        .country("country")
                        .pinCode("pincode 1")
                        .build(),


                Address.builder()
                        .id(1L)
                        .type(AddressType.CORRESPONDENCE)
                        .houseNoBldgApt("house number 2")
                        .areaLocalitySector("area/locality/sector")
                        .streetRoadLine("street/road/line")
                        .landmark("landmark")
                        .cityTownVillage("city/town/village 2")
                        .postOffice("post office")
                        .subDistrict("sub district")
                        .district("district")
                        .state("state")
                        .country("country")
                        .pinCode("pincode 2")
                        .build()
        );
    }

    private List<Role> getListOfRoles() {
        User user = User.builder().id(0L).build();
        Calendar calendar = Calendar.getInstance();
        calendar.set(1990, Calendar.JULY, 1);

        Role role1 = Role.builder()
                .id(1L)
                .name("name of the role 1")
                .description("description")
                .createdBy(1L)
                .createdDate(calendar.getTime())
                .lastModifiedBy(1L)
                .lastModifiedDate(calendar.getTime())
                .build();

        Role role2 = Role.builder()
                .id(2L)
                .name("name of the role 2")
                .description("description")
                .createdBy(1L)
                .createdDate(calendar.getTime())
                .lastModifiedBy(1L)
                .lastModifiedDate(calendar.getTime())
                .build();

        return Arrays.asList(role1, role2);
    }
}