package org.egov.infra.mdms.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

@Component
public class MDMSUtils {
	
	/*public static final Logger logger = LoggerFactory.getLogger(MDMSUtils.class);

	public List<Object> filter(List<Object> list, String key, Object value) throws JsonProcessingException{
		List<Object> filteredList = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		try{
			filteredList = list.parallelStream()
					.filter(obj -> true == ((JsonPath.read(obj.toString(), key.toString())).equals(value)))
					.collect(Collectors.toList());
		}catch(Exception e){
			filteredList = list.parallelStream()
					.map(obj -> {
						try{
							return mapper.writeValueAsString(obj);
						}catch(Exception ex){
							logger.error("Parsing error inside the stream: ",ex);
						}
						return null;
					})
					.filter(obj -> true == ((JsonPath.read(obj, key.toString())).equals(value)))
					.collect(Collectors.toList());	
		}
		return filteredList;
	}*/

}
