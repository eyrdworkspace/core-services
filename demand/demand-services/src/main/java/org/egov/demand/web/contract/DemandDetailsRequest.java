package org.egov.demand.web.contract;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
public class DemandDetailsRequest {
	@JsonProperty("RequestInfo")
	RequestInfo requestInfo;

	@JsonProperty("DemandDetails")
	private List<DemandDetails> demandDetails = new ArrayList<DemandDetails>();
}