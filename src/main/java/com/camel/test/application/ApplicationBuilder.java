package com.camel.test.application;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.camel.test.hazelcast.TestCamelHazelCast;

public class ApplicationBuilder extends RouteBuilder {
	
	private static final Logger LOG = LoggerFactory.getLogger(ApplicationBuilder.class);
	
	@Override
	public void configure() throws Exception {
		
		
		TestCamelHazelCast hazelcastIdempotentRepo = new TestCamelHazelCast("files");

		LOG.info("Configure the Routes for the context!");
		
		from("file:C:/Users/coelhro/camel-test/application/pending?idempotentKey=${file:name}") 
		    .idempotentConsumer(fileName(), hazelcastIdempotentRepo).eager(false)
		    .to("file:C:/Users/coelhro/camel-test/application/processed");
		}
	
	private static Expression fileName() {
		return new Expression()  {
			@SuppressWarnings("unchecked")
			public <T> T evaluate(Exchange exchange, Class<T> type) {
				String fileName = (String) exchange.getIn().getHeader("CamelFileName");
				return (T) fileName;
			}
		};
	}
}
