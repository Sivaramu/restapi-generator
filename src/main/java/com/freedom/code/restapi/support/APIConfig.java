package com.freedom.code.restapi.support;

import java.io.Serializable;
import java.util.List;

import org.springframework.core.style.ToStringCreator;

public class APIConfig implements Serializable {

	private static final long serialVersionUID = 4550429230623696402L;

	private String name;

	private String description;

	private String urlContext;

	private String basePackage;

	private String baseFolder;

	private String model;

	private QueryConfig queryConfig;

	private List<OperationConfig> operations;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrlContext() {
		return urlContext;
	}

	public void setUrlContext(String urlContext) {
		this.urlContext = urlContext;
	}

	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public QueryConfig getQueryConfig() {
		return queryConfig;
	}

	public void setQueryConfig(QueryConfig queryConfig) {
		this.queryConfig = queryConfig;
	}

	@Override
	public String toString() {
		return new ToStringCreator(this).append("name", name).append("description", description)
				.append("urlContext", urlContext).append("basePackage", basePackage).append("queryConfig", queryConfig)
				.toString();
	}

	public List<OperationConfig> getOperations() {
		return operations;
	}

	public void setOperations(List<OperationConfig> operations) {
		this.operations = operations;
	}

	public String getBaseFolder() {
		return baseFolder;
	}

	public void setBaseFolder(String baseFolder) {
		this.baseFolder = baseFolder;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

}
