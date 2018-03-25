package com.freedom.code.restapi;

import java.io.IOException;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

import com.freedom.code.restapi.config.CodeGeneratorConfig;
import com.freedom.code.restapi.service.CodeGeneratorService;

@Configuration
public class CodeGenerator {

	private static Logger logger = LoggerFactory.getLogger(CodeGenerator.class);

	public static void main(String[] args) {
		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				CodeGeneratorConfig.class);) {
			CodeGeneratorService service = context.getBean(CodeGeneratorService.class);
			service.generateCode(args.length > 0 ? args[0] : CodeGeneratorService.DEFAULT_API_DEFINITION,
					args.length > 1 ? args[1] : CodeGeneratorService.DEFAULT_SOURCECODE_TEMPLATE);
		} catch (IOException | URISyntaxException e) {
			logger.error("Error while generating API code: " + e.getMessage(), e);
		}
	}

}
