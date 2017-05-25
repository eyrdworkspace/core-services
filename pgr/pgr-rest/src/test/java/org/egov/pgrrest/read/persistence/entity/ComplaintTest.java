package org.egov.pgrrest.read.persistence.entity;

import org.egov.pgrrest.common.entity.*;
import org.egov.pgrrest.common.model.AuthenticatedUser;
import org.egov.pgrrest.common.model.Requester;
import org.egov.pgrrest.read.domain.model.ServiceRequestLocation;
import org.egov.pgrrest.read.domain.model.Coordinates;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.egov.pgrrest.read.domain.model.ServiceRequestType;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.*;

public class ComplaintTest {

    private static final String IST = "Asia/Calcutta";

    @Test
    public void test_should_map_from_entity_to_domain() {
        final ServiceType complaintType = new ServiceType();
        complaintType.setName("complaintName");
        complaintType.setCode("complaintCode");
        complaintType.setTenantId("tenantId");

        final Complainant complainant = Complainant.builder().id(2L).name("firstName").mobile("mobileNumber")
                .email("email@email.com").address("address").build();
        final LocalDateTime lastAccessedDateTime = LocalDateTime.of(2016, 1, 2, 3, 4, 5);
        final ReceivingCenter receivingCenter = ReceivingCenter.builder().id(4L).build();
        final ReceivingMode receivingMode = new ReceivingMode();
        receivingMode.setCode("EMAIL");
        final Complaint entityComplaint = Complaint.builder().crn("crn").details("complaint description")
                .complaintType(complaintType).receivingMode(receivingMode).status("FORWARDED")
                .complainant(complainant).latitude(1.0).longitude(2.0).crossHierarchyId(4L).location(3L).department(3L)
                .lastAccessedTime(toDate(lastAccessedDateTime)).landmarkDetails("landMark").receivingMode(receivingMode)
                .receivingCenter(receivingCenter).childLocation(5L).assignee(6L).stateId(7L).tenantId("tenantId").build();
        final LocalDateTime lastModifiedDateTime = LocalDateTime.of(2016, 2, 3, 3, 3, 3);
        final LocalDateTime createdDateTime = LocalDateTime.of(2016, 2, 1, 3, 3, 3);
        entityComplaint.setLastModifiedDate(toDate(lastModifiedDateTime));
        entityComplaint.setCreatedDate(toDate(createdDateTime));

        final ServiceRequest domainComplaint = entityComplaint.toDomain();

        assertNotNull(domainComplaint);
        final ServiceRequestLocation serviceRequestLocation = domainComplaint.getServiceRequestLocation();
        assertNotNull(serviceRequestLocation);
        assertEquals("3", serviceRequestLocation.getLocationId());
        assertEquals("4", serviceRequestLocation.getCrossHierarchyId());
        assertEquals(new Coordinates(1.0, 2.0), serviceRequestLocation.getCoordinates());
        assertEquals(toDate(lastModifiedDateTime), domainComplaint.getLastModifiedDate());
        assertEquals(toDate(createdDateTime), domainComplaint.getCreatedDate());
        assertEquals(Long.valueOf(3), domainComplaint.getDepartment());
        assertFalse(domainComplaint.isClosed());
        assertEquals(Collections.emptyList(), domainComplaint.getMediaUrls());
        assertEquals("crn", domainComplaint.getCrn());
        assertEquals("complaint description", domainComplaint.getDescription());
        final Requester domainComplainant = domainComplaint.getRequester();
        assertNotNull(domainComplainant);
        assertEquals("firstName", domainComplainant.getFirstName());
        assertEquals("mobileNumber", domainComplainant.getMobile());
        assertEquals("email@email.com", domainComplainant.getEmail());
        assertEquals("address", domainComplainant.getAddress());
        final AuthenticatedUser authenticatedUser = domainComplaint.getAuthenticatedUser();
        assertNotNull(authenticatedUser);
        assertTrue(authenticatedUser.isAnonymousUser());
        final ServiceRequestType expectedComplaintType = new ServiceRequestType(
                "complaintName", "complaintCode", "tenantId");
        assertEquals(expectedComplaintType, domainComplaint.getServiceRequestType());
        assertEquals("EMAIL", domainComplaint.getReceivingMode());
        assertEquals("4", domainComplaint.getReceivingCenter());
        assertEquals("5", domainComplaint.getChildLocation());
        assertEquals(Long.valueOf(6), domainComplaint.getAssignee());
        assertEquals("7", domainComplaint.getState());
    }

    private Date toDate(LocalDateTime dateTime) {
        final ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneId.of(IST));
        return Date.from(zonedDateTime.toInstant());
    }
}