package com.freedom.code.restapi.service;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import javax.activation.DataSource;
import javax.lang.model.element.Modifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.freedom.code.restapi.support.APIConfig;
import com.freedom.code.restapi.support.Column;
import com.freedom.code.restapi.support.HttpMethod;
import com.freedom.code.restapi.support.OperationConfig;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

@Component
public class ClassGenerator {

	private static final Logger logger = LoggerFactory.getLogger(ClassGenerator.class);

	public void generateBean(APIConfig config, List<Column> cols) throws IOException {
		TypeSpec.Builder pojoBuilder = TypeSpec.classBuilder(config.getModel()).addSuperinterface(Serializable.class)
				.addModifiers(Modifier.PUBLIC);
		pojoBuilder.addField(FieldSpec.builder(Long.class, "serialVersionUID", Modifier.PRIVATE, Modifier.STATIC)
				.initializer("$LL", System.nanoTime()).build());

		for (Column col : cols) {
			logger.debug("Settign column {}", col);
			pojoBuilder.addField(col.getDbType(), col.getName(), Modifier.PRIVATE);
			final String getterMethod = "get" + col.getName().substring(0, 1).toUpperCase()
					+ col.getName().substring(1, col.getName().length());
			pojoBuilder.addMethod(MethodSpec.methodBuilder(getterMethod).addModifiers(Modifier.PUBLIC)
					.returns(col.getDbType()).addStatement("return " + col.getName()).build());
			final String setterMethod = "set" + col.getName().substring(0, 1).toUpperCase()
					+ col.getName().substring(1, col.getName().length());

			pojoBuilder.addMethod(MethodSpec.methodBuilder(setterMethod).addModifiers(Modifier.PUBLIC)
					.returns(Void.class).addParameter(col.getDbType(), col.getName(), Modifier.FINAL)
					.addStatement("this." + col.getName() + "=" + col.getName()).build());

		}

		JavaFile pojoFile = JavaFile
				.builder(config.getBasePackage().concat("." + config.getModel().toLowerCase()), pojoBuilder.build())
				.build();

		Path basePackage = createBasePackage(
				config.getBaseFolder() == null ? CodeGeneratorService.DEFAULT_BASE_FOLDER : config.getBaseFolder(),
				config.getBasePackage());

		pojoFile.writeTo(Paths.get(basePackage.toAbsolutePath().toString()));

		TypeSpec.Builder daoConstantsBuilder = TypeSpec.classBuilder(config.getModel() + "DAOConstants")
				.addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);

		for (OperationConfig operation : config.getOperations()) {
			if (HttpMethod.GET.equals(operation.getMethod())) {
				final String fieldName = operation.getName().toUpperCase()
						+ ((operation.getQueryByColumns() != null && !operation.getQueryByColumns().isEmpty())
								? "BY_" + String.join("_", operation.getQueryByColumns())
								: "");
				String query = "SELECT * FROM "+config.getQueryConfig().getTable();
				if (operation.getQueryByColumns() == null || operation.getQueryByColumns().isEmpty()) {

				} else {
					query = query + " WHERE ";
					for (String queryCol : operation.getQueryByColumns()) {
						query = query + " " + queryCol + " = ? AND";
					}
					query = query.substring(0,query.lastIndexOf("AND"));
				}
				FieldSpec field = FieldSpec
						.builder(String.class, fieldName, Modifier.STATIC, Modifier.FINAL, Modifier.PUBLIC)
						.initializer("$S",query).build();
				daoConstantsBuilder.addField(field).build();
			}
		}

		JavaFile daoConstantsFile = JavaFile
				.builder(config.getBasePackage().concat("." + config.getModel().toLowerCase()) + ".dao",
						daoConstantsBuilder.build())
				.build();

		daoConstantsFile.writeTo(Paths.get(basePackage.toAbsolutePath().toString()));

		TypeSpec.Builder daoBuilder = TypeSpec.classBuilder(config.getModel() + "DAO").addModifiers(Modifier.PUBLIC)
				.addAnnotation(AnnotationSpec.builder(Repository.class).build());

		// logger
		FieldSpec loggerField = FieldSpec.builder(Logger.class, "logger", Modifier.PRIVATE, Modifier.STATIC)
				.initializer("$T.getLoggerFactory(" + config.getModel() + "DAO.class)", LoggerFactory.class).build();
		daoBuilder.addField(loggerField);

		// jdbcTemplate
		FieldSpec jdbcTemplateField = FieldSpec.builder(JdbcTemplate.class, "jdbcTemplate", Modifier.PRIVATE).build();
		daoBuilder.addField(jdbcTemplateField);

		MethodSpec daoConstructor = MethodSpec.constructorBuilder()
				.addParameter(ParameterSpec.builder(DataSource.class, "dataSource", Modifier.FINAL)
						.addAnnotation(AnnotationSpec.builder(Qualifier.class)
								.addMember("name", "$S", config.getQueryConfig().getDatabase().getDataSource()).build())
						.build())
				.addModifiers(Modifier.PUBLIC)
				.addStatement("this.jdbcTemplate = new $T(dataSource)", JdbcTemplate.class).build();
		daoBuilder.addMethod(daoConstructor);

		for (OperationConfig operation : config.getOperations()) {
			logger.debug("Operation : {}", operation);
			MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(operation.getName())
					.addModifiers(Modifier.PUBLIC);
			if (HttpMethod.GET.equals(operation.getMethod())) {
				ClassName pojoList = ClassName.get("java.util", "List");
				TypeName pojoType = ClassName.get(config.getBasePackage(), config.getModel());
				TypeName listOfPOJOs = ParameterizedTypeName.get(pojoList, pojoType);
				TypeName rowMapperType = ParameterizedTypeName.get(ClassName.get(BeanPropertyRowMapper.class),
						pojoType);

				methodBuilder.returns(listOfPOJOs).addStatement("return this.jdbcTemplate.query(sql, new $T($T.class))",
						rowMapperType, pojoType);
				List<String> queryCols = operation.getQueryByColumns();
				if (queryCols == null) {
					continue;
				}
				for (String queryCol : queryCols) {
					Column column = getColumn(cols, queryCol);
					if (column == null) {
						continue;
					}
					ParameterSpec parameter = ParameterSpec
							.builder(column.getDbType(), column.getName(), Modifier.FINAL).build();
					methodBuilder.addParameter(parameter);
				}
			}
			daoBuilder.addMethod(methodBuilder.build());
		}

		JavaFile daoFile = JavaFile
				.builder(config.getBasePackage().concat("." + config.getModel().toLowerCase()) + ".dao",
						daoBuilder.build())
				.build();

		daoFile.writeTo(Paths.get(basePackage.toAbsolutePath().toString()));
	}

	private Column getColumn(List<Column> cols, String queryCol) {
		for (Column col : cols) {
			if (col.getName().equalsIgnoreCase(queryCol)) {
				return col;
			}
		}
		return null;
	}

	private Path createBasePackage(final String baseFolder, final String basePackage) {
		// TODO: Test this. This may be grave.
		Objects.requireNonNull(baseFolder, "baseFolder is null");
		Objects.requireNonNull(basePackage, "basePackage is null");

		return Paths.get(baseFolder, basePackage.split("."));
	}
}
