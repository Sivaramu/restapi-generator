package com.freedom.code.restapi;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import com.freedom.code.restapi.service.CodeGeneratorService;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { CodeGeneratorTestConfig.class })
public class CodeGeneratorTest {

	private static final Logger logger = LoggerFactory.getLogger(CodeGeneratorTest.class);

	@Autowired
	private CodeGeneratorService generator;

	@Before
	@Sql(scripts= {"schema.sql"})
	public void createSchema() {
		
	}
	
	@Test
	public void whenAPIDefinitionIsPassedThenAPIConfigIsLoaded() {
		try {
			generator.generateCode("api-definition.json", "sourcecode.template");
			logger.debug("code generation complete");
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
			Assert.fail(" Code generation failed with error " + e.getMessage());
		}
	}

}
