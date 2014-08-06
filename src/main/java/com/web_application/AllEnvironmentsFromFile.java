package com.web_application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.repository.EnvironmentRepository;

@Component
@Service
public class AllEnvironmentsFromFile {

	@Autowired
	EnvironmentRepository enviroRepo;

	@Autowired
	CronTrigger trigger;

	@Transactional
	public EnvironmentEntity create(EnvironmentEntity environment) {

		EnvironmentEntity createdEnviro = environment;

		return enviroRepo.save(createdEnviro);
	}

	public List<Environment> getEnvironments() throws IOException {
		List<Environment> environments = new ArrayList<Environment>();
		List<EnvironmentEntity> entity = getAllEntities();
		for (EnvironmentEntity e : entity) {
			Environment z = new Environment();
			z.setName(e.getName());
			z.setCron(e.getCron());
			environments.add(z);
		}
		return environments;
	}

	@Transactional
	public List<EnvironmentEntity> getAllEntities() {
		System.out.println("Call Check");
		List<EnvironmentEntity> entity = enviroRepo.findAll();
		for (EnvironmentEntity e : entity) {
			if ((!org.quartz.CronExpression.isValidExpression(e.getCron()) || e.getCron().endsWith("31 2 ?")) && e.getCron() != "") {
				System.out.println("Setting cron to empty string");
				e.setCron("");
				create(e);
			}
		}
		return entity;
	}

	@Transactional
	public void addAll(List<EnvironmentEntity> env) throws SchedulerException,
			IOException {
		enviroRepo.deleteAll();
		for (EnvironmentEntity e : env) {
			System.out.println("going into database " + e.getName());
			enviroRepo.save(e);
		}

		trigger.triggerCron();

	}

	public List<Environment> getValidEnvironments() throws IOException {
		// TODO Auto-generated method stub
		List<Environment> enviroTemp = new ArrayList<Environment>();
		for (Environment e : getEnvironments()) {
			if (!e.getCron().equals("")) {
				System.out.println("Cron value" + e.getCron());
				enviroTemp.add(e);
			}
		}
		return enviroTemp;
	}

}
