package org.egov.infra.indexer.testproducer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class IndexerProducer {
	
	public static final Logger logger = LoggerFactory.getLogger(IndexerProducer.class);

	@Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
	
    public void producer(String topicName, Object value) {
    	kafkaTemplate.send(topicName, value);
    }

}
