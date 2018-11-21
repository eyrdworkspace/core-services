package org.egov.infra.indexer.controller;

import javax.validation.Valid;

import org.egov.IndexerApplicationRunnerImpl;
import org.egov.infra.indexer.consumer.KafkaConsumerConfig;
import org.egov.infra.indexer.service.IndexerService;
import org.egov.infra.indexer.testproducer.IndexerProducer;
import org.egov.infra.indexer.util.ResponseInfoFactory;
import org.egov.infra.indexer.validator.Validator;
import org.egov.infra.indexer.web.contract.ESResponseWrapper;
import org.egov.infra.indexer.web.contract.ESSearchCriteria;
import org.egov.infra.indexer.web.contract.ReindexRequest;
import org.egov.infra.indexer.web.contract.ReindexResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/index-operations")
public class IndexerController {

	public static final Logger logger = LoggerFactory.getLogger(IndexerController.class);
	
	@Autowired
	private IndexerProducer indexerProducer;
	
	@Autowired
	private KafkaConsumerConfig kafkaConsumerConfig;
	
	@Autowired
	private IndexerApplicationRunnerImpl runner;
	
	@Autowired
	private IndexerService service;
	
	@Autowired
	private ResponseInfoFactory factory;
	
	@Autowired
	private Validator validator;
	
	//This is testing API 
    @PostMapping("/_index")
    @ResponseBody
    private ResponseEntity<?> produceIndexJson(@RequestParam(name = "topic") String topic, @RequestBody Object indexJson){
    	try{
    		indexerProducer.producer(topic, indexJson);
    	}catch(Exception e){
    		return new ResponseEntity<>(indexJson ,HttpStatus.INTERNAL_SERVER_ERROR);
    	}
		return new ResponseEntity<>(indexJson ,HttpStatus.OK);

    }

    @GetMapping("/_reload")
    @ResponseBody
    private ResponseEntity<?> reload(){
    	Object response = null;
    	try{
    		runner.readFiles();
    		kafkaConsumerConfig.startContainer();
    	}catch(Exception e){
    		response = "Reload FAILED";
    		return new ResponseEntity<>(response ,HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	response = "Reload Successful";
		return new ResponseEntity<>(response ,HttpStatus.OK);

    }
    
    @PostMapping("/_reindex")
    @ResponseBody
    private ResponseEntity<?> getIndexedData(@Valid @RequestBody ReindexRequest reindexRequest){
    	validator.validaterReindexRequest(reindexRequest);
    	ReindexResponse response = service.createReindexJob(reindexRequest);
		return new ResponseEntity<>(response ,HttpStatus.OK);

    }
}
