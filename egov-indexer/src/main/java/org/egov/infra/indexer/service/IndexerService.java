package org.egov.infra.indexer.service;

import java.util.Date;
import java.util.Map;

import org.egov.IndexerApplicationRunnerImpl;
import org.egov.infra.indexer.bulkindexer.BulkIndexer;
import org.egov.infra.indexer.util.IndexerUtils;
import org.egov.infra.indexer.web.contract.CustomJsonMapping;
import org.egov.infra.indexer.web.contract.FieldMapping;
import org.egov.infra.indexer.web.contract.Index;
import org.egov.infra.indexer.web.contract.Mapping;
import org.egov.infra.indexer.web.contract.UriMapping;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;


@Service
public class IndexerService {

	public static final Logger logger = LoggerFactory.getLogger(IndexerService.class);

		
	@Autowired
	private BulkIndexer bulkIndexer;
	
	@Autowired
	private IndexerApplicationRunnerImpl runner;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private IndexerUtils indexerUtils;
	
	
	@Value("${egov.infra.indexer.host}")
	private String esHostUrl;
		
	public void elasticIndexer(String topic, String kafkaJson) throws Exception{
		Map<String, Mapping> mappingsMap = runner.getMappingMaps();
		if(null != mappingsMap.get(topic)){
			Mapping mapping = mappingsMap.get(topic);
			logger.info("Mapping to be used: "+mapping);
			try{
				for(Index index: mapping.getIndexes()){
					indexProccessor(index, kafkaJson,
							(index.getIsBulk() == null || index.getIsBulk() == false) ? false : true);
				}
			}catch(Exception e){
				logger.error("Exception while indexing, Uncaught at the indexer level: ", e);
			}
		}else{
			logger.error("No mappings found for the service to which the following topic belongs: "+topic);
		}
	}
	
	public void indexProccessor(Index index, String kafkaJson, boolean isBulk) throws Exception {
        Long startTime = null;
        Long endTime = null;
		StringBuilder url = new StringBuilder();
		url.append(esHostUrl).append(index.getName()).append("/").append(index.getType()).append("/")
		   .append("_bulk");	
        startTime = new Date().getTime();
	    if (index.getJsonPath() != null) {
				indexerUtils.validateAndIndex(buildIndexJsonWithJsonpath(index, kafkaJson, isBulk), 
						url.toString(), index);
		} else if(null != index.getCustomJsonMapping()){
			    StringBuilder urlForMap = new StringBuilder();
			    urlForMap.append(esHostUrl).append(index.getName()).append("/").append("_mapping").append("/").append(index.getType());	
			    indexerUtils.validateAndIndex(buildCustomJsonForBulk(index, kafkaJson, urlForMap.toString(), isBulk), 
						url.toString(), index);		
		}else {
				indexerUtils.validateAndIndex(buildIndexJsonWithoutJsonpath(index, kafkaJson, isBulk), 
						url.toString(), index);
		}
	    endTime = new Date().getTime();
		logger.info("TOTAL TIME TAKEN FOR INDEXING: "+(endTime - startTime)+"ms");
	}
	
	public String buildIndexJsonWithJsonpath(Index index, String kafkaJson, boolean isBulk) throws Exception{
        Long startTime = null;
        Long endTime = null;
        startTime = new Date().getTime();
        StringBuilder jsonTobeIndexed = new StringBuilder();
        String result = null;
        JSONArray kafkaJsonArray = null;
        final String format = "{ \"index\" : {\"_id\" : \"%s\" } }%n ";
        try {
        	kafkaJsonArray = indexerUtils.validateAndConstructJsonArray(kafkaJson, index, isBulk);
			for(int i = 0; i < kafkaJsonArray.length() ; i++){
				String stringifiedObject = indexerUtils.buildString(kafkaJsonArray.get(i));
				if(null != index.getId()){
					String id = indexerUtils.buildIndexId(index, stringifiedObject);
		            final String actionMetaData = String.format(format, "" + id);
		            jsonTobeIndexed.append(actionMetaData)
     			                   .append(stringifiedObject)
		            			   .append("\n");
				}else{
					logger.info("Index id not provided for the document, Allowing ES to generate the id.");
					jsonTobeIndexed.append(stringifiedObject);
				}
			}
			result = jsonTobeIndexed.toString();
	    }catch(Exception e){
	    	logger.error("Error while building jsonstring for indexing", e);
	    }
        endTime = new Date().getTime();
		logger.info("TIME TAKEN for building data to be indexed: "+(endTime - startTime)+"ms");

		return result;
  }
	
	public String buildIndexJsonWithoutJsonpath(Index index, String kafkaJson, boolean isBulk) throws Exception{
        Long startTime = null;
        Long endTime = null;
        startTime = new Date().getTime();
		StringBuilder jsonTobeIndexed = new StringBuilder();
        String result = null;
        JSONArray kafkaJsonArray = null;
        final String format = "{ \"index\" : {\"_id\" : \"%s\" } }%n ";
        try {
        	kafkaJsonArray = indexerUtils.validateAndConstructJsonArray(kafkaJson, index, isBulk);
			for(int i = 0; i < kafkaJsonArray.length() ; i++){
				String stringifiedObject = indexerUtils.buildString(kafkaJsonArray.get(i));
				if(null != index.getId()){
					String id = indexerUtils.buildIndexId(index, stringifiedObject);
					logger.info("Inserting id to the json being indexed, id = " + id);
		            final String actionMetaData = String.format(format, "" + id);
		            jsonTobeIndexed.append(actionMetaData)
     			                   .append(stringifiedObject)
		            			   .append("\n");
				}else{
					logger.info("Index id not provided for the document, Allowing ES to generate the id.");
					jsonTobeIndexed.append(stringifiedObject);
				}
			}
			result = jsonTobeIndexed.toString();
	    }catch(Exception e){
	    	logger.error("Error while building jsonstring for indexing", e);
	    }
        endTime = new Date().getTime();
		logger.info("TIME TAKEN for building data to be indexed: "+(endTime - startTime)+"ms");
		
		return result;
  }
	
	public String buildCustomJsonForBulk(Index index, String kafkaJson, String urlForMap, boolean isBulk) throws Exception{
        Long startTime = null;
        Long endTime = null;
        startTime = new Date().getTime();
		StringBuilder jsonTobeIndexed = new StringBuilder();
        String result = null;
        JSONArray kafkaJsonArray = null;
        final String format = "{ \"index\" : {\"_id\" : \"%s\" } }%n ";
        try {
        	kafkaJsonArray = indexerUtils.validateAndConstructJsonArray(kafkaJson, index, isBulk);
			for(int i = 0; i < kafkaJsonArray.length() ; i++){
				String stringifiedObject = indexerUtils.buildString(kafkaJsonArray.get(i));
				String customIndexJson = buildCustomJsonForIndex(index.getCustomJsonMapping(), stringifiedObject, urlForMap);
				if(null != index.getId()){
					String id = indexerUtils.buildIndexId(index, stringifiedObject);
					logger.info("Inserting id to the json being indexed, id = " + id);
		            final String actionMetaData = String.format(format, "" + id);
		            jsonTobeIndexed.append(actionMetaData)
     			                   .append(customIndexJson)
		            			   .append("\n");
				}else{
					logger.info("Index id not provided for the document, Allowing ES to generate the id.");
					jsonTobeIndexed.append(customIndexJson);
				}
			}
			result = jsonTobeIndexed.toString();
	    }catch(Exception e){
	    	logger.error("Error while building jsonstring for indexing", e);
	    }
        endTime = new Date().getTime();
		logger.info("TIME TAKEN for building data to be indexed: "+(endTime - startTime)+"ms");

		return result;
  }
	
	public String buildCustomJsonForIndex(CustomJsonMapping customJsonMappings, String kafkaJson, String urlForMap){
		Object indexMap = null;
		String customJson = null;
		ObjectMapper mapper = new ObjectMapper();
		if(null != customJsonMappings.getIndexMapping()){
			indexMap = customJsonMappings.getIndexMapping();
		}else{
			indexMap = bulkIndexer.getIndexMappingfromES(urlForMap);
		}
    	DocumentContext documentContext = JsonPath.parse(indexMap);
    	
    	if(!CollectionUtils.isEmpty(customJsonMappings.getFieldMapping())){
			for(FieldMapping fieldMapping: customJsonMappings.getFieldMapping()){
				String[] expressionArray = (fieldMapping.getOutJsonPath()).split("[.]");
				StringBuilder expression = new StringBuilder();
				for(int i = 0; i < (expressionArray.length - 1) ; i++ ){
					expression.append(expressionArray[i]);
					if(i != expressionArray.length - 2)
						expression.append(".");
				}
				try {
					documentContext.put(expression.toString(), expressionArray[expressionArray.length - 1],
							JsonPath.read(kafkaJson, fieldMapping.getInjsonpath()));
				}catch(Exception e) {
					continue;
				}
			
			}
    	}		
		if(!CollectionUtils.isEmpty(customJsonMappings.getUriMapping())){
			for(UriMapping uriMapping: customJsonMappings.getUriMapping()){
				Object response = null;
				String uri = null;
				try{
					uri = indexerUtils.buildUri(uriMapping, kafkaJson);
					response = restTemplate.postForObject(uri, uriMapping.getRequest(), Map.class);
				}catch(Exception e){
					logger.error("Exception while trying to hit: "+uri, e);
					continue;
				}
				if(null == response){
					continue;
				}
				logger.info("Response: "+response+" from the URI: "+uriMapping.getPath());
				for(FieldMapping fieldMapping: uriMapping.getUriResponseMapping()){
					String[] expressionArray = (fieldMapping.getOutJsonPath()).split("[.]");
					StringBuilder expression = new StringBuilder();
					for(int i = 0; i < (expressionArray.length - 1) ; i++ ){
						expression.append(expressionArray[i]);
						if(i != expressionArray.length - 2)
							expression.append(".");
					}
					try{
						documentContext.put(expression.toString(), expressionArray[expressionArray.length - 1],
								JsonPath.read(mapper.writeValueAsString(response), fieldMapping.getInjsonpath()));
					}catch(Exception e){
						logger.error("Value: "+fieldMapping.getInjsonpath()+" is not found in the uri: "+uriMapping.getPath()+" response", e);
					}
				}
					
			}
		}
		customJson = documentContext.jsonString(); 
		return customJson.toString(); //jsonString has to be converted to string
	}
}
