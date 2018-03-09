package org.egov.search.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.search.model.Definition;
import org.egov.search.model.Params;
import org.egov.search.model.Query;
import org.egov.search.model.SearchDefinition;
import org.egov.search.model.SearchParams;
import org.egov.search.model.SearchRequest;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import org.json.JSONArray;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

@Component
public class SearchUtils {
	
	public static final Logger logger = LoggerFactory.getLogger(SearchUtils.class);

	public String buildQuery(SearchRequest searchRequest, SearchParams searchParam, Query query){
		StringBuilder queryString = new StringBuilder();
		StringBuilder where = new StringBuilder();
		queryString.append(query.getBaseQuery());
		String whereClause = buildWhereClause(searchRequest, searchParam);
		if(null == whereClause){
			return whereClause;
		}
		where.append(" where ( ").append(whereClause.toString()+ " ) ");	
		if(null != query.getGroupBy()){
			queryString.append(" group by ").append(query.getGroupBy());
		}
		if(null != query.getOrderBy()){
			where.append(" order by ").append(query.getOrderBy().split(",")[0]).append(" ").append(query.getOrderBy().split(",")[1]);
		}
		if(null != query.getSort()){
			queryString.append(" "+query.getSort());
		}
		
		String finalQuery = queryString.toString().replace("$where", where.toString());
		logger.info("Final Query: "+finalQuery);
		
		return finalQuery;
	}
	
	public 	String buildWhereClause(SearchRequest searchRequest, SearchParams searchParam){
		StringBuilder whereClause = new StringBuilder();
		ObjectMapper mapper = new ObjectMapper();
		String condition = searchParam.getCondition();
		for(Params param: searchParam.getParams()){
				Object paramValue = null ;
				try {
					paramValue = JsonPath.read(mapper.writeValueAsString(searchRequest), param.getJsonPath());
					if(null == paramValue) {
						continue;
					}
				}catch(Exception e) {
					continue;
				}
                if(paramValue instanceof net.minidev.json.JSONArray){ //TODO: might need to add some more types
                	net.minidev.json.JSONArray array = (net.minidev.json.JSONArray)paramValue;
					StringBuilder paramBuilder = new StringBuilder();
					for (Object object : array) {
						paramBuilder.append("'"+object+"'");
						if(array.indexOf(object)!=array.size()-1)
							paramBuilder.append(",");
					}
					whereClause.append(param.getName()).append(" IN ").append("(")
					  .append(paramBuilder.toString()).append(")");
				}else{
					logger.info("param: "+param.getName());
					String operator = " = ";
					if(param.getJsonPath().contains("startDate") || param.getJsonPath().contains("fromDate")) {
						operator = " >= ";
					}else if(param.getJsonPath().contains("endDate") || param.getJsonPath().contains("toDate")) {
						operator = " <= ";
					}
					whereClause.append(param.getName()).append(operator).append("'"+paramValue+"'");
				}
                whereClause.append(" "+condition+" ");
		}
		Integer index = whereClause.toString().lastIndexOf(searchParam.getCondition());
		String where = whereClause.toString().substring(0, index);
		logger.info("WHERE clause: "+where);
		return where;
	}
	
	public Definition getSearchDefinition(Map<String, SearchDefinition> searchDefinitionMap,
			String moduleName, String searchName){
		logger.info("Fetching Definitions for module: "+moduleName+" and search feature: "+searchName);
		List<Definition> definitions = null;
		try{
			definitions = searchDefinitionMap.get(moduleName).getDefinitions().parallelStream()
											.filter(def -> (def.getName().equals(searchName)))
		                                 .collect(Collectors.toList());
		}catch(Exception e){
			logger.error("There's no Search Definition provided for this search feature");
			throw new CustomException(HttpStatus.BAD_REQUEST.toString(), 
					"There's no Search Definition provided for this search feature");
		}
		if(0 == definitions.size()){
			logger.error("There's no Search Definition provided for this search feature");
			throw new CustomException(HttpStatus.BAD_REQUEST.toString(), 
					"There's no Search Definition provided for this search feature");
		}
		return definitions.get(0);
		
	}
	
	public List<String> convertPGOBjects(List<PGobject> maps){
		List<String> result = new ArrayList<>();
		if(null != maps || !maps.isEmpty()) {
			for(PGobject obj: maps){
				if(null == obj.getValue())
					break;
				String tuple = obj.toString();
				if(tuple.startsWith("[") && tuple.endsWith("]")){
					JSONArray jsonArray = new JSONArray(tuple);
					for(int i = 0; i < jsonArray.length();  i++){
						result.add(jsonArray.get(i).toString());
					}
				}else{
					try{
						result.add(obj.getValue());
					}catch(Exception e){
						throw e;
					}
				}
			}
		}
		
		return result;
	}
	
	
}
