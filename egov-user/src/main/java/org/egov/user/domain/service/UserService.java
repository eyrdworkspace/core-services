package org.egov.user.domain.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.user.domain.exception.AtleastOneRoleCodeException;
import org.egov.user.domain.exception.DuplicateUserNameException;
import org.egov.user.domain.exception.InvalidOtpException;
import org.egov.user.domain.exception.InvalidUpdatePasswordRequestException;
import org.egov.user.domain.exception.OtpValidationPendingException;
import org.egov.user.domain.exception.PasswordMismatchException;
import org.egov.user.domain.exception.UserIdMandatoryException;
import org.egov.user.domain.exception.UserNameNotValidException;
import org.egov.user.domain.exception.UserNotFoundException;
import org.egov.user.domain.exception.UserProfileUpdateDeniedException;
import org.egov.user.domain.model.LoggedInUserUpdatePasswordRequest;
import org.egov.user.domain.model.NonLoggedInUserUpdatePasswordRequest;
import org.egov.user.domain.model.OtpValidationRequest;
import org.egov.user.domain.model.User;
import org.egov.user.domain.model.UserSearchCriteria;
import org.egov.user.domain.model.enums.UserType;
import org.egov.user.persistence.repository.FileStoreRepository;
import org.egov.user.persistence.repository.OtpRepository;
import org.egov.user.persistence.repository.UserRepository;
import org.egov.user.web.contract.Otp;
import org.egov.user.web.contract.OtpValidateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

	private UserRepository userRepository;
	private OtpRepository otpRepository;
	private PasswordEncoder passwordEncoder;
	private int defaultPasswordExpiryInDays;
	private boolean isCitizenLoginOtpBased;
	private boolean isEmployeeLoginOtpBased;
	private FileStoreRepository fileRepository;

	@Autowired
	private RestTemplate restTemplate;

	public UserService(UserRepository userRepository, OtpRepository otpRepository, FileStoreRepository fileRepository,
			PasswordEncoder passwordEncoder,
			@Value("${default.password.expiry.in.days}") int defaultPasswordExpiryInDays,
			@Value("${citizen.login.password.otp.enabled}") boolean isCitizenLoginOtpBased,
			@Value("${employee.login.password.otp.enabled}") boolean isEmployeeLoginOtpBased) {
		this.userRepository = userRepository;
		this.otpRepository = otpRepository;
		this.passwordEncoder = passwordEncoder;
		this.defaultPasswordExpiryInDays = defaultPasswordExpiryInDays;
		this.isCitizenLoginOtpBased = isCitizenLoginOtpBased;
		this.isEmployeeLoginOtpBased = isEmployeeLoginOtpBased;
		this.fileRepository = fileRepository;
	}

	/**
	 * get user By UserName And TenantId
	 * 
	 * @param userName
	 * @param tenantId
	 * @return
	 */
	public User getUserByUsername(final String userName, String tenantId) {
		return userRepository.findByUsername(userName, tenantId);
	}

	public User getUserByUsernameAndTenantId(String userName, String tenantid) {
		// TODO Auto-generated method stub
		return userRepository.findByUsernameAndTenantId(userName, tenantid);
	}

	/**
	 * Get User By EmailId And TenantId
	 * 
	 * @param emailId
	 * @param tenantId
	 * @return
	 */
	public User getUserByEmailId(final String emailId, String tenantId) {
		return userRepository.findByEmailId(emailId, tenantId);
	}

	/**
	 * api will create the user based on some validations
	 * 
	 * @param user
	 * @return
	 */
	public User createUser(User user) {
		user.setUuid(UUID.randomUUID().toString());
		user.validateNewUser();
		conditionallyValidateOtp(user);
		validateDuplicateUserName(user);
		user.setDefaultPasswordExpiry(defaultPasswordExpiryInDays);
		return persistNewUser(user);
	}

	/**
	 * api will create the citizen with otp
	 * 
	 * @param user
	 * @return
	 */
	public User createCitizen(User user) {
		user.setUuid(UUID.randomUUID().toString());
		if (isCitizenLoginOtpBased && !StringUtils.isNumeric(user.getUsername()))
			throw new UserNameNotValidException();
		else if (isCitizenLoginOtpBased)
			user.setMobileNumber(user.getUsername());

		user.setRoleToCitizen();
		validateDuplicateUserName(user);
		user.validateNewUser();
		// validateOtp(user.getOtpValidationRequest());
		String tenantId = null;
		if (user.getTenantId().contains("."))
			tenantId = user.getTenantId().split("\\.")[0];
		else
			tenantId = user.getTenantId();

		Otp otp = Otp.builder().otp(user.getOtpReference()).identity(user.getUsername()).tenantId(tenantId).build();
		try {
			validateOtp(otp);
		} catch (Exception e) {
			String errorMessage = JsonPath.read(e.getMessage(), "$.error.message");
			System.out.println("message " + errorMessage);
			throw new InvalidOtpException(errorMessage);
		}
		user.setDefaultPasswordExpiry(defaultPasswordExpiryInDays);
		user.setActive(true);

		return persistNewUser(user);
	}

	/**
	 * api will create the citizen with otp
	 * 
	 * @param user
	 * @return
	 */
	public Object regosterWithLogin(User user) {
		log.info("Into register with login method......");
		user.setUuid(UUID.randomUUID().toString());
		if (isCitizenLoginOtpBased && !StringUtils.isNumeric(user.getUsername()))
			throw new UserNameNotValidException();
		else if (isCitizenLoginOtpBased)
			user.setMobileNumber(user.getUsername());

		user.setRoleToCitizen();
		validateDuplicateUserName(user);
		user.validateNewUser();
		// validateOtp(user.getOtpValidationRequest());
		log.info("User validated successfully");
		String tenantId = null;
		if (user.getTenantId().contains("."))
			tenantId = user.getTenantId().split("\\.")[0];
		else
			tenantId = user.getTenantId();

		Otp otp = Otp.builder().otp(user.getOtpReference()).identity(user.getUsername()).tenantId(tenantId).build();
		try {
			validateOtp(otp);
		} catch (Exception e) {
			log.error("Exception while validating otp: " + e);
			String errorMessage = JsonPath.read(e.getMessage(), "$.error.message");
			System.out.println("message " + errorMessage);
			throw new InvalidOtpException(errorMessage);
		}
		user.setDefaultPasswordExpiry(defaultPasswordExpiryInDays);
		user.setActive(true);
		User registrationResult = null;
		try {
			registrationResult = persistNewUser(user);
			StringBuilder uri = new StringBuilder();
			uri.append("http://egov-micro-dev.egovernments.org").append("/user/oauth/token")
					.append("?username=" + user.getUsername()).append("&password=" + otp.getOtp())
					.append("&grant_type=password&scope=read").append("&tenantId=" + user.getTenantId());

			return restTemplate.postForObject(uri.toString(), "", UsernamePasswordAuthenticationToken.class);
		} catch (Exception e) {
			log.info("Exception while fecting authtoken: " + e);
			return registrationResult;
		}
	}

	/**
	 * get the users based on on userSearch criteria
	 * 
	 * @param searchCriteria
	 * @return
	 */
	public List<org.egov.user.domain.model.User> searchUsers(UserSearchCriteria searchCriteria) {
		searchCriteria.validate();
		if (null != searchCriteria.getUserName() && null != searchCriteria.getTenantId()
				&& null == searchCriteria.getName() && null == searchCriteria.getAadhaarNumber()
				&& null == searchCriteria.getEmailId() && null == searchCriteria.getMobileNumber()
				&& null == searchCriteria.getPan() && null == searchCriteria.getRoleCodes()
				&& null == searchCriteria.getId() && 0 == searchCriteria.getPageNumber()
				&& null == searchCriteria.getType()) {
			User user = null;
			user = userRepository.findByUsername(searchCriteria.getUserName(), searchCriteria.getTenantId());
			if (user == null) {
				String tenant = null;
				if (searchCriteria.getTenantId() != null && searchCriteria.getTenantId().contains("."))
					tenant = searchCriteria.getTenantId().split("\\.")[0];
				else
					tenant = searchCriteria.getTenantId();
				user = userRepository.findByUsernameAndTenantId(searchCriteria.getUserName(), tenant);
				List<org.egov.user.domain.model.User> list = new ArrayList<org.egov.user.domain.model.User>();
				if (user != null) {
					list.add(user);
					setFileStoreUrlsByFileStoreIds(list);
				}
				return list;
			}
		}

		List<org.egov.user.domain.model.User> list = userRepository.findAll(searchCriteria);
		setFileStoreUrlsByFileStoreIds(list);
		return list;
	}

	/**
	 * api will update user details without otp
	 * 
	 * @param id
	 * @param user
	 * @return
	 */
	public User updateWithoutOtpValidation(final Long id, final User user) {
		user.validateUserModification();
		validateUser(id, user);
		validateUserRoles(user);
		return updateExistingUser(user);
	}

	/**
	 * this api will validate whether user roles exist in Database or not
	 * 
	 * @param user
	 */
	private void validateUserRoles(User user) {
		if (user.getRoles() == null || user.getRoles() != null && user.getRoles().isEmpty()) {
			throw new AtleastOneRoleCodeException();
		}
	}

	/**
	 * this api will update user profile data except these fields userName ,
	 * mobileNumber , password ,pwsExpiryData, roles
	 * 
	 * @param user
	 * @return
	 */
	public User partialUpdate(final User user) {
		validateUserId(user);
		validateProfileUpdateIsDoneByTheSameLoggedInUser(user);
		user.nullifySensitiveFields();
		User updatedUser = updateExistingUser(user);
		List<User> list = new ArrayList<User>();
		list.add(updatedUser);
		setFileStoreUrlsByFileStoreIds(list);
		return updatedUser;
	}

	/**
	 * This api will update the password for logged-in user
	 * 
	 * @param updatePasswordRequest
	 */
	public void updatePasswordForLoggedInUser(LoggedInUserUpdatePasswordRequest updatePasswordRequest) {
		updatePasswordRequest.validate();
		final User user = userRepository.getUserById(updatePasswordRequest.getUserId(),
				updatePasswordRequest.getTenantId());
		validateUserPresent(user);
		if (user.getType().toString().equals(UserType.CITIZEN.toString()) && isCitizenLoginOtpBased)
			throw new InvalidUpdatePasswordRequestException();
		if (user.getType().toString().equals(UserType.EMPLOYEE.toString()) && isEmployeeLoginOtpBased)
			throw new InvalidUpdatePasswordRequestException();

		validateExistingPassword(user, updatePasswordRequest.getExistingPassword());
		user.updatePassword(updatePasswordRequest.getNewPassword());
		userRepository.update(user);
	}

	/**
	 * This Api will update the password for non logged-in user
	 * 
	 * @param request
	 */
	public void updatePasswordForNonLoggedInUser(NonLoggedInUserUpdatePasswordRequest request) {
		request.validate();
		// validateOtp(request.getOtpValidationRequest());
		final User user = getUserByUsername(request.getUserName(), request.getTenantId());
		validateUserPresent(user);
		if (user.getType().toString().equals(UserType.CITIZEN.toString()) && isCitizenLoginOtpBased)
			throw new InvalidUpdatePasswordRequestException();
		if (user.getType().toString().equals(UserType.EMPLOYEE.toString()) && isEmployeeLoginOtpBased)
			throw new InvalidUpdatePasswordRequestException();
		Otp otp = Otp.builder().otp(request.getOtpReference()).identity(user.getMobileNumber())
				.tenantId(request.getTenantId()).build();
		try {
			validateOtp(otp);
		} catch (Exception e) {
			String errorMessage = JsonPath.read(e.getMessage(), "$.error.message");
			System.out.println("message " + errorMessage);
			throw new InvalidOtpException(errorMessage);
		}
		user.updatePassword(request.getNewPassword());
		userRepository.update(user);
	}

	/**
	 * dependent on otpValidationMandatory filed,it will validate the otp.
	 * 
	 * @param user
	 */
	private void conditionallyValidateOtp(User user) {
		if (user.isOtpValidationMandatory()) {
			validateOtp(user.getOtpValidationRequest());
		}
	}

	/**
	 * This api will validate existing password and current password matching or
	 * not
	 * 
	 * @param user
	 * @param existingRawPassword
	 */
	private void validateExistingPassword(User user, String existingRawPassword) {
		if (!passwordEncoder.matches(existingRawPassword, user.getPassword())) {
			throw new PasswordMismatchException();
		}
	}

	/**
	 * this api will check user is exist or not, If not exist it will throw
	 * exception.
	 * 
	 * @param user
	 */
	private void validateUserPresent(User user) {
		if (user == null) {
			throw new UserNotFoundException(null);
		}
	}

	/**
	 * this api will validate, updating the profile for same logged-in user or
	 * not
	 * 
	 * @param user
	 */
	private void validateProfileUpdateIsDoneByTheSameLoggedInUser(User user) {
		if (user.isLoggedInUserDifferentFromUpdatedUser()) {
			throw new UserProfileUpdateDeniedException();
		}
	}

	/**
	 * this api will validate whether userId exist or not in user object
	 * 
	 * @param user
	 */
	private void validateUserId(User user) {
		if (user.isIdAbsent()) {
			throw new UserIdMandatoryException();
		}
	}

	/**
	 * This api will validate the userName is exist or not
	 * 
	 * @param id
	 * @param user
	 */
	private void validateDuplicateUserName(Long id, User user) {
		if (userRepository.isUserPresent(user.getUsername(), id, user.getTenantId())) {
			throw new DuplicateUserNameException(user);
		}
	}

	/**
	 * This api will validate the userName is exist or not
	 * 
	 * @param user
	 */
	private void validateDuplicateUserName(User user) {
		if (userRepository.isUserPresent(user.getUsername(), user.getTenantId())) {
			throw new DuplicateUserNameException(user);
		}
	}

	/**
	 * This api will persist the user
	 * 
	 * @param user
	 * @return
	 */
	private User persistNewUser(User user) {

		return userRepository.create(user);
	}

	/**
	 * This api will fetch the fileStoreUrl By fileStoreId
	 * 
	 * @param user
	 * @throws Exception
	 */
	private void setFileStoreUrlsByFileStoreIds(List<User> userList) {
		List<String> fileStoreIds = userList.parallelStream().map(p -> p.getPhoto()).collect(Collectors.toList());
		if (fileStoreIds != null && fileStoreIds.size() > 0) {
			Map<String, String> fileStoreUrlList = null;
			try {
				fileStoreUrlList = fileRepository.getUrlByFileStoreId(userList.get(0).getTenantId(), fileStoreIds);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (fileStoreUrlList != null && !fileStoreUrlList.isEmpty()) {
				for (User user : userList) {
					user.setPhoto(fileStoreUrlList.get(user.getPhoto()));
				}
			}
		}
	}

	/**
	 * api will check the otp whether validation complete or not.
	 * 
	 * @param otpValidationRequest
	 */
	private void validateOtp(OtpValidationRequest otpValidationRequest) {
		if (!otpRepository.isOtpValidationComplete(otpValidationRequest))
			throw new OtpValidationPendingException();
	}

	/**
	 * this api will validate the user by id and tenantId and also will check
	 * duplicate UserName
	 * 
	 * @param id
	 * @param user
	 */
	private void validateUser(final Long id, final User user) {
		// validateDuplicateUserName(id, user);
		if (userRepository.getUserById(id, user.getTenantId()) == null) {
			throw new UserNotFoundException(user);
		}
	}

	/**
	 * this api will update the user details.
	 * 
	 * @param user
	 * @return
	 */
	private User updateExistingUser(final User user) {
		return userRepository.update(user);
	}

	/**
	 * This api will validate the otp
	 * 
	 * @param otp
	 * @return
	 * @throws Exception
	 */
	public Boolean validateOtp(Otp otp) throws Exception {
		RequestInfo requestInfo = RequestInfo.builder().action("validate").ts(new Date()).build();
		OtpValidateRequest otpValidationRequest = OtpValidateRequest.builder().requestInfo(requestInfo).otp(otp)
				.build();
		return otpRepository.validateOtp(otpValidationRequest);

	}

}
