package com.freedom.code.restapi.support;

import java.io.Serializable;

import org.springframework.core.style.ToStringCreator;

public class Column implements Serializable {

	private static final long serialVersionUID = -8802774438576697307L;

	private String name;

	private Class dbType;

	private Class javaType;

	private boolean required;

	public String getName() {
		return name;
	}

	public Column setName(String name) {
		this.name = name;
		return this;
	}

	public Class getDbType() {
		return dbType;
	}

	public Column setDbType(Class dbType) {
		this.dbType = dbType;
		return this;
	}

	public boolean isRequired() {
		return required;
	}

	public Column setRequired(boolean required) {
		this.required = required;
		return this;
	}

	public Class getJavaType() {
		return javaType;
	}

	public Column setJavaType(Class javaType) {
		this.javaType = javaType;
		return this;
	}

	@Override
	public String toString() {
		return new ToStringCreator(this).append("name", name).append("dbType", dbType).append("javaType", javaType)
				.append("required", required).toString();
	}
}
