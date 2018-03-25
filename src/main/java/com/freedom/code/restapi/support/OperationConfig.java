package com.freedom.code.restapi.support;

import java.io.Serializable;
import java.util.List;

import org.springframework.core.style.ToStringCreator;

public class OperationConfig implements Serializable {

	private static final long serialVersionUID = 1621832407383553751L;

	private String name;

	private HttpMethod method;

	private List<String> queryByColumns;

	private List<String> updateColumns;

	private List<String> timestampColumns;

	public HttpMethod getMethod() {
		return method;
	}

	public void setMethod(HttpMethod method) {
		this.method = method;
	}

	public List<String> getQueryByColumns() {
		return queryByColumns;
	}

	public void setQueryByColumns(List<String> queryByColumns) {
		this.queryByColumns = queryByColumns;
	}

	public List<String> getUpdateColumns() {
		return updateColumns;
	}

	public void setUpdateColumns(List<String> updateColumns) {
		this.updateColumns = updateColumns;
	}

	public List<String> getTimestampColumns() {
		return timestampColumns;
	}

	public void setTimestampColumns(List<String> timestampColumns) {
		this.timestampColumns = timestampColumns;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return new ToStringCreator(this).append("name", name).append("timestampColumns", timestampColumns)
				.append("updateColumns", updateColumns).append("queryByColumns", queryByColumns)
				.append("method", method).toString();
	}

}

