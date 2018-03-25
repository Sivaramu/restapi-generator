package com.freedom.code.restapi.support;

import java.io.Serializable;
import java.util.Map;

import org.springframework.core.style.ToStringCreator;

public class QueryConfig implements Serializable {

	private static final long serialVersionUID = -4316721247158160165L;

	private DatabaseConfig database;

	private String schema;

	private String table;

	private String key;

	private String query;

	private Map<String, String> columnNameOverrides;

	public DatabaseConfig getDatabase() {
		return database;
	}

	public void setDatabase(DatabaseConfig database) {
		this.database = database;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Map<String, String> getColumnNameOverrides() {
		return columnNameOverrides;
	}

	public void setColumnNameOverrides(Map<String, String> columnNameOverrides) {
		this.columnNameOverrides = columnNameOverrides;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return new ToStringCreator(this).toString();
	}

}
