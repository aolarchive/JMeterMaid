package com.web_application;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Environments")
public class EnvironmentEntity {

	private int ID;
	private String name;
	private String cron;

	
	@Id
	@Column(name = "ID")
	@GeneratedValue
	public int getID() {
		return this.ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	@Column(name = "Name")
	public String getName() {
		return this.name;
	}

	public void setName(String names) {
		name = names;
	}
	
	@Column(name = "Cron")
	public String getCron() {
		return this.cron;
	}

	public void setCron(String crons) {
		cron = crons;
	}

}
