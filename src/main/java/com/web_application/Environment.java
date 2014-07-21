package com.web_application;

public class Environment {
	String environmentName;
	String environmentCronValue;

	public String getName() {
		return environmentName;
	}

	public void setName(String name) {
		environmentName = name;
	}

	public String getCron() {
		return environmentCronValue;
	}

	public void setCron(String cron) {
		environmentCronValue = cron;
	}
}
