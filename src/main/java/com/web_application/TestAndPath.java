package com.web_application;

public class TestAndPath {
	private String testName;
	private String testPath;
	private IDAndPassOrFail[] passFailOrNA;
	private String environment;

	public String getName() {
		return testName;
	}

	public void setName(String name) {
		testName = name;
	}

	public String getPath() {
		return testPath;
	}

	public void setPath(String path) {
		testPath = path;
	}

	public IDAndPassOrFail[] getPassFailOrNA() {
		return passFailOrNA;
	}

	public void setPassFailOrNA(IDAndPassOrFail[] passOr) {
		passFailOrNA = passOr;
	}
	
	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String enviro) {
		environment = enviro;
	}

}
