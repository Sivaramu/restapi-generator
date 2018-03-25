package com.freedom.code.restapi.support;

import java.io.Serializable;

import org.springframework.core.style.ToStringCreator;

public class DatabaseConfig implements Serializable {

	private static final long serialVersionUID = -4306315782005751173L;
	private Database type;
	private String connectionString;
	private String driverClass;
	private String userName;
	private String password;
	private String dataSource;

	public Database getType() {
		return type;
	}

	public void setType(Database type) {
		this.type = type;
	}

	public String getConnectionString() {
		return connectionString;
	}

	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public String toString() {
		return new ToStringCreator(this).append("type", type).append("dataSource", dataSource)
				.append("connectionString", connectionString).append("userName", userName).append("password", password)
				.append("driverClass", driverClass).toString();
	}
}