package org.egov.report.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.domain.model.RequestInfoWrapper;
import org.egov.swagger.model.ColumnDetail;
import org.egov.swagger.model.ColumnDetail.TypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.swagger.model.MetadataResponse;
import org.egov.swagger.model.ReportDefinition;
import org.egov.swagger.model.SearchColumn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;


@Service
public class IntegrationService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	public static final Logger LOGGER = LoggerFactory.getLogger(IntegrationService.class);
	
	public MetadataResponse getData(ReportDefinition reportDefinition, MetadataResponse metadataResponse, RequestInfo requestInfo,String moduleName){
		
		
		List<SearchColumn>  searchColumns = reportDefinition.getSearchParams();
		List<ColumnDetail> columnDetails = metadataResponse.getReportDetails().getSearchParams();
		Map<String, ColumnDetail> colNameMap = columnDetails.stream().collect(Collectors.toMap(ColumnDetail::getName, Function.identity()));
		
		LOGGER.info("colNameMap:"+colNameMap);
		for(SearchColumn searchColumn : searchColumns){
			LOGGER.info("searchColumn:"+searchColumn);
			if(searchColumn.getType().equals(TypeEnum.SINGLEVALUELIST) || searchColumn.getType().equals(TypeEnum.MULTIVALUELIST)){
				LOGGER.info("if searchColumn:"+searchColumn);
				String[] patterns= searchColumn.getPattern().split("\\|");
				LOGGER.info("patterns:"+patterns.toString());
				String url = patterns[0];
				url = url.replaceAll("\\$tenantid",metadataResponse.getTenantId());
				LOGGER.info("url:"+url);
				ColumnDetail columnDetail = colNameMap.get(searchColumn.getName());
				
				if(url != null && url.startsWith("list://")){
					//consider this as fixed value and send this after removing list://
					url = url.substring(7);
					Map<Object, Object> map = new HashMap<>();
					String[] pairs = url.split(",");
					for(String str: pairs){
						String[] keyValue = str.split(":");
						map.put(keyValue[0].replace('_', ','), keyValue[1]);
						//map.put(keyValue[0],keyValue[1]);
					}
					columnDetail.setDefaultValue(map);
				}else{
					String res = "";
					try{
					if(searchColumn.getWrapper()){
						RequestInfoWrapper riw = generateRequestInfoWrapper(requestInfo);
					 res = restTemplate.postForObject(url,riw, String.class);
					} else {
						res = restTemplate.postForObject(url,requestInfo, String.class);
					}
					Object document = Configuration.defaultConfiguration().jsonProvider().parse(res);
					LOGGER.info("document:"+document);
					List<Object> keys = JsonPath.read(document, patterns[1]);
					LOGGER.info("keys:"+keys);
					List<Object> values = JsonPath.read(document, patterns[2]);
					LOGGER.info("values:"+values);
					Map<Object, Object> map = new HashMap<>();
					for(int i=0;i<keys.size();i++){
						map.put(keys.get(i), values.get(i));
					}
					LOGGER.info("value map-->"+map);
					columnDetail.setDefaultValue(map);
					} catch(Exception e) {
						e.printStackTrace();
					} 
					
				}
		}
	}
		return metadataResponse;
	}
	private RequestInfoWrapper generateRequestInfoWrapper(RequestInfo requestInfo) {
		RequestInfoWrapper riw = new RequestInfoWrapper();
		org.egov.swagger.model.RequestInfo ri = new org.egov.swagger.model.RequestInfo();
		ri.setAction(requestInfo.getAction());
		ri.setAuthToken(requestInfo.getAuthToken());
		ri.apiId(requestInfo.getApiId());
		ri.setVer(requestInfo.getVer());
		ri.setTs(1L);
		ri.setDid(requestInfo.getDid());
		ri.setKey(requestInfo.getKey());
		ri.setMsgId(requestInfo.getMsgId());
		ri.setRequesterId(requestInfo.getRequesterId());
		riw.setRequestInfo(ri);
		return riw;
	}
	
	

}
