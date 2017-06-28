package org.egov.demand.model;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bill {
	//TODO some of the fields are mandatory in yml, lets discuss billdetail and billaccountdetail also for more clarity
	private String id;

	private String payeeName;

	private String payeeAddress;

	private String payeeEmail;

	private Boolean isActive;

	private Boolean isCancelled;

	private List<BillDetail> billDetails = new ArrayList<>();

	private String tenantId;
	
	private AuditDetail auditDetail;

}