package com.web_application;

public class Environment {
	String name;
	String environmentCronValue;
	int lastTestNum;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCron() {
		return environmentCronValue;
	}

	public void setCron(String cron) {
		environmentCronValue = cron;
	}
	
	public int getLastTestNum() {
		return lastTestNum;
	}

	public void setLastTestNum(int lastTestNumber) {
		lastTestNum = lastTestNumber;
	}
}
