package com.freedom.code.restapi.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SourceCodeTemplate implements Serializable {

	private static final long serialVersionUID = 398240204330380568L;

	private final List<String> variables = new ArrayList<>();

	private final List<String> classes = new ArrayList<>();

	public List<String> getClasses() {
		return classes;
	}

	public SourceCodeTemplate setClasses(List<String> classes) {
		if (classes != null)
			this.classes.addAll(classes);
		
		return this;
	}

	public SourceCodeTemplate addClass(String classCode) {
		getClasses().add(classCode);
		return this;
	}

	public List<String> getVariables() {
		return variables;
	}

	public SourceCodeTemplate setVariables(List<String> variables) {
		if (variables != null)
			this.classes.addAll(variables);

		return this;
	}

	public SourceCodeTemplate addVariable(String variable) {
		getVariables().add(variable);
		return this;
	}
}
