package com.controller;

import java.util.ArrayList;
import java.util.List;

import com.web_application.EnvironmentEntity;

public class Source {
	
	private String sourceName;
	private List<String> testList;
	private List<EnvironmentEntity> eEntity = new ArrayList<EnvironmentEntity>();
	private int enviroErrorNum;
	private String enviroErrorType;
	private List<Integer> envirosToRemove;

	public String getSourceName()
	{
		return sourceName;
	}
	
	public void setSourceName(String name)
	{
		this.sourceName = name;
	}
	
	public List<String> getTestList()
	{
		return testList;
	}
	
	public void setTestList(List<String> list)
	{
		this.testList = list;
	}
	
	public List<EnvironmentEntity> getEnviro()
	{
		return eEntity;
	}
	
	public void setEnviro(List<EnvironmentEntity> entity)
	{
		eEntity = entity;
	}
	
	public int getEnviroErrorNum()
	{
		return enviroErrorNum;
	}
	
	public void setEnviroErrorNum(int error)
	{
		enviroErrorNum = error;
	}
	
	public String getEnviroErrorType()
	{
		return enviroErrorType;
	}
	
	public void setEnviroErrorType(String error)
	{
		enviroErrorType = error;
	}
	
	public List<Integer> getEnvirosToRemove()
	{
		return envirosToRemove;
	}
	
	public void setEnvirosToRemove(List<Integer> thisList)
	{
		envirosToRemove = thisList;
	}
	
	
	

}
