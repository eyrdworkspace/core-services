package org.egov.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.egov.user.domain.exception.InvalidUserException;
import org.egov.user.domain.model.enums.BloodGroup;
import org.egov.user.domain.model.enums.Gender;
import org.egov.user.domain.model.enums.GuardianRelation;
import org.egov.user.domain.model.enums.UserType;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static org.springframework.util.ObjectUtils.isEmpty;

@AllArgsConstructor
@Getter
@Builder
public class User {

	private Long id;
	private String tenantId;
	private String username;
	private String title;
	private String password;
	private String salutation;
	private String guardian;
	private GuardianRelation guardianRelation;
	private String name;
	private Gender gender;
	private String mobileNumber;
	private String emailId;
	private String altContactNumber;
	private String pan;
	private String aadhaarNumber;
	private List<Address> address = new ArrayList<>();
	private Boolean active;
	private List<Role> roles = new ArrayList<>();
	private Date dob;
	private Date pwdExpiryDate = new Date();
	private String locale = "en_IN";
	private UserType type;
	private BloodGroup bloodGroup;
	private String identificationMark;
	private String signature;
	private String photo;
	private Boolean accountLocked;
	private Date lastModifiedDate;
	private Date createdDate;
	private String otpReference;
	private Long createdBy;
	private Long lastModifiedBy;
	private Long loggedInUserId;
	@Setter
	private boolean otpValidationMandatory;

	public void validate() {
		if (isUsernameAbsent()
				|| isNameAbsent()
				|| isGenderAbsent()
				|| isMobileNumberAbsent()
				|| isActiveIndicatorAbsent()
				|| isTypeAbsent()
				|| isRolesAbsent()
				|| isOtpReferenceAbsent()
				|| isTenantIdAbsent()) {
			throw new InvalidUserException(this);
		}
	}

	public boolean isOtpReferenceAbsent() {
		return otpValidationMandatory && isEmpty(otpReference);
	}

	public boolean isTypeAbsent() {
		return isEmpty(type);
	}

	public boolean isActiveIndicatorAbsent() {
		return isEmpty(active);
	}

	public boolean isGenderAbsent() {
		return isEmpty(gender);
	}

	public boolean isMobileNumberAbsent() {
		return isEmpty(mobileNumber);
	}

	public boolean isNameAbsent() {
		return isEmpty(name);
	}

	public boolean isUsernameAbsent() {
		return isEmpty(username);
	}

	public boolean isTenantIdAbsent() {
		return isEmpty(tenantId);
	}

	public boolean isRolesAbsent() {
		return CollectionUtils.isEmpty(roles) || roles.stream().anyMatch(r -> isEmpty(r.getCode()));
	}

	public boolean isIdAbsent() {
		return id == null;
	}

	public void nullifySensitiveFields() {
		username = null;
		mobileNumber = null;
		password = null;
		pwdExpiryDate = null;
		roles = null;
	}

	public boolean isLoggedInUserDifferentFromUpdatedUser() {
		return !id.equals(loggedInUserId);
	}

	public User setRoleToCitizen() {
		type = UserType.CITIZEN;
		roles = Collections.singletonList(Role.getCitizenRole());
		return this;
	}
}