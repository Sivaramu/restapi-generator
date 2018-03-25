package com.freedom.code.restapi.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freedom.code.restapi.CodeGenerator;
import com.freedom.code.restapi.dao.CodeGeneratorDAO;
import com.freedom.code.restapi.support.APIConfig;
import com.freedom.code.restapi.support.Column;
import com.freedom.code.restapi.support.QueryConfig;

@Service
public class CodeGeneratorService {

	private static final Logger logger = LoggerFactory.getLogger(CodeGeneratorService.class);

	public static final String DEFAULT_API_DEFINITION = "api-definition.json";

	public static final String DEFAULT_SOURCECODE_TEMPLATE = "sourcecode.template";

	public static final String DEFAULT_BASE_FOLDER = System.getProperty("user.dir") + "/target";

	@Autowired
	private CodeGeneratorDAO codeGeneratorDAO;

	@Autowired
	private ClassGenerator beanGenerator;

	public List<Column> findColumns(QueryConfig config) {
		try {
			return this.codeGeneratorDAO.findColumns(config);
		} catch (SQLException | ClassNotFoundException e) {
			logger.error("SQL Exception " + e.getMessage(), e);
		}
		return null;
	}

	public void generateCode(final String apiDefinition, final String sourceCodeTemplate)
			throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
		Objects.requireNonNull(apiDefinition, "apiDefinition is null");
		Objects.requireNonNull(sourceCodeTemplate, "sourceCodeTemplate is null");

		// Load configuration
		final APIConfig config = getAPIConfig(apiDefinition);
		logger.debug("Configuration loaded :{}", config);

		final List<Column> columns = findColumns(config.getQueryConfig());
		logger.debug("Columns = {}", columns);
		
		beanGenerator.generateBean(config, columns);
	}

	

	APIConfig getAPIConfig(final String apiDefinition)
			throws IOException, JsonParseException, JsonMappingException, URISyntaxException {
		return new ObjectMapper().readValue(getFileAsString(apiDefinition), APIConfig.class);
	}

	String getFileAsString(String filePath) throws IOException, URISyntaxException {
		return new String(
				Files.readAllBytes(Paths.get(CodeGenerator.class.getClassLoader().getResource(filePath).toURI())));
	}

}
