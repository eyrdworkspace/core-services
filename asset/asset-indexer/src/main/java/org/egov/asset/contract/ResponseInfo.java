package org.egov.asset.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class ResponseInfo {

	private String apiId;
	private String ver;
	private String ts;
	private String resMsgId;
	private String msgId;
	private String status;
	
	
	
	
}