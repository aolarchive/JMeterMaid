package com.web_application;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Lob;

@Entity
@Table(name = "TESTRUNS")
public class RunEntity {

	private int ID;
	private int testNumber;
	private String testName;
	private String environment;
	private String source;
	private String passOrFail;
	private Timestamp date;
	private byte[] resultFiles;

	@Id
	@Column(name = "ID")
	@GeneratedValue
	public int getID() {
		return this.ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	@Column(name = "TestNumber")
	public int getTestNumber() {
		return this.testNumber;
	}

	public void setTestNumber(int testNum) {
		this.testNumber = testNum;
	}

	@Column(name = "TestName")
	public String getTestName() {
		return this.testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	@Column(name = "Environment")
	public String getEnvironment() {
		return this.environment;
	}

	public void setEnvironment(String enviro) {
		this.environment = enviro;
	}

	@Column(name = "Source")
	public String getSource() {
		return this.source;
	}

	public void setSource(String src) {
		this.source = src;
	}

	@Column(name = "PassOrFail")
	public String getPassOrFail() {
		return this.passOrFail;
	}

	public void setPassOrFail(String pOrF) {
		this.passOrFail = pOrF;
	}

	@Column(name = "Date")
	public Timestamp getDate() {
		return this.date;
	}

	public void setDate(Timestamp dates) {
		this.date = dates;
	}

	@Lob
	@Column(name = "ResultFiles")
	public byte[] getResultFiles() {
		return this.resultFiles;
	}

	public void setResultFiles(byte[] file) {
		this.resultFiles = file;
	}

}
