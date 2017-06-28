package org.egov.property.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.egov.models.Address;
import org.egov.models.AuditDetails;
import org.egov.models.Document;
import org.egov.models.Floor;
import org.egov.models.Property;
import org.egov.models.PropertyDetail;
import org.egov.models.PropertyLocation;
import org.egov.models.PropertyResponse;
import org.egov.models.RequestInfo;
import org.egov.models.ResponseInfo;
import org.egov.models.ResponseInfoFactory;
import org.egov.models.User;
import org.egov.models.VacantLandDetail;
import org.egov.property.exception.PropertySearchException;
import org.egov.property.model.PropertyUser;
import org.egov.property.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * 
 * @author Prasad This class will have the search APIs for RDBMS/Elastic search
 *
 */
@Service
public class PropertySearchService {

	@Autowired
	Environment environment;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	ResponseInfoFactory responseInfoFactory;

	@Autowired
	PropertyRepository propertyRepository;

	/**
	 * <p>
	 * This method will search the documents in Database(Postgres) with the
	 * given parameters
	 * </p>
	 * 
	 * @author Prasad
	 * @param requestInfo
	 * @param tenantId
	 * @param active
	 * @param upicNo
	 * @param pageSize
	 * @param pageNumber
	 * @param sort
	 * @param oldUpicNo
	 * @param mobileNumber
	 * @param aadhaarNumber
	 * @param houseNoBldgApt
	 * @param revenueZone
	 * @param revenueWard
	 * @param locality
	 * @param ownerName
	 * @param demandFrom
	 * @param demandTo
	 * @return Property Object if search is successful or Error Object if search
	 *         will fail
	 */

	@SuppressWarnings("unchecked")
	public PropertyResponse searchProperty(RequestInfo requestInfo, String tenantId, Boolean active, String upicNo,
			int pageSize, int pageNumber, String[] sort, String oldUpicNo, String mobileNumber, String aadhaarNumber,
			String houseNoBldgApt, int revenueZone, int revenueWard, int locality, String ownerName, int demandFrom,
			int demandTo) {

		List<Property> updatedPropety = null;

		try {

			Map<String, Object> map = propertyRepository.searchProperty(requestInfo, tenantId, active, upicNo, pageSize,
					pageNumber, sort, oldUpicNo, mobileNumber, aadhaarNumber, houseNoBldgApt, revenueZone, revenueWard,
					locality, ownerName, demandFrom, demandTo);

			List<Property> property = (List<Property>) map.get("properties");
			List<User> users = (List<User>) map.get("users");
			updatedPropety = addAllPropertyDetails(property, requestInfo, users);
		} catch (Exception e) {
			throw new PropertySearchException(environment.getProperty("invalid.input"), requestInfo);
		}

		PropertyResponse propertyResponse = new PropertyResponse();
		propertyResponse.setProperties(updatedPropety);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		propertyResponse.setResponseInfo(responseInfo);

		return propertyResponse;

	}

	/**
	 * <p>
	 * This method will add the property details to the given list of property
	 * objects ,such as floors,owners etc
	 * </p>
	 * 
	 * @author Prasad
	 * @param properties
	 * @return List of property Object's
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @see ArrayList
	 * 
	 */

	private List<Property> addAllPropertyDetails(List<Property> properties, RequestInfo requestInfo, List<User> users)
			throws JsonParseException, JsonMappingException, IOException {

		List<Property> updatedPropertyDetails = new ArrayList<>();

		for (Property property : properties) {

			Long propertyId = property.getId();

			Address address = propertyRepository.getAddressByProperty(propertyId);
			property.setAddress(address);

			List<User> ownerInfos = new ArrayList<>();

			List<PropertyUser> propertyUsers = propertyRepository.getPropertyUserByProperty(propertyId);
			List<Integer> userIds = new ArrayList<>();

			for (PropertyUser propertyUser : propertyUsers) {

				userIds.add(propertyUser.getOwner());
			}

			List<User> userOfProperty = getUserObjectForUserIds(userIds, users);

			// get owner info for property

			for (User propertyUser : userOfProperty) {
				ownerInfos.add(propertyUser);
			}

			property.setOwners(ownerInfos);

			PropertyDetail propertyDetail = propertyRepository.getPropertyDetailsByProperty(propertyId);
			property.setPropertyDetail(propertyDetail);

			VacantLandDetail vacantLandDetail = propertyRepository.getVacantLandByProperty(propertyId);
			property.setVacantLand(vacantLandDetail);

			PropertyLocation propertyLocation = propertyRepository.getPropertyLocationByproperty(propertyId);
			property.setBoundary(propertyLocation);

			Long propertyDetailId = property.getPropertyDetail().getId();

			List<Floor> floors = propertyRepository.getFloorsByPropertyDetails(propertyDetailId);
			property.getPropertyDetail().setFloors(floors);

			List<Document> documents = propertyRepository.getDocumentByPropertyDetails(propertyDetailId);
			property.getPropertyDetail().setDocuments(documents);

			AuditDetails auditDetails = propertyRepository.getAuditForPropertyDetails(propertyId);
			property.getPropertyDetail().setAuditDetails(auditDetails);

			updatedPropertyDetails.add(property);

		}
		return updatedPropertyDetails;

	}

	/**
	 * <p>
	 * This method will give you the user Objects which has the given userIds
	 * <p>
	 * 
	 * @author Prasad
	 * @param userIds
	 * @param users
	 * @return List Of user Object
	 */

	private List<User> getUserObjectForUserIds(List<Integer> userIds, List<User> users) {

		List<User> userList = new ArrayList<User>();
		if (users != null) {
			for (User user : users) {
				Long userId = user.getId();
				if (userIds.contains(userId.intValue())) {
					userList.add(user);
				}
			}

		}
		return userList;
	}

}