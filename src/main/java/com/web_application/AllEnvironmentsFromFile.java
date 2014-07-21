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

@Component
public class AllEnvironmentsFromFile {

	@Autowired
	CronTrigger trigger;

	String environmentsFile = ImportantInformation.getPathToEnvironmentsInfo();

	public List<Environment> getEnvironments() throws IOException {
		List<Environment> environments = new ArrayList<Environment>();

		BufferedReader br = new BufferedReader(new FileReader(environmentsFile));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				System.out.println(line);
				String[] nameAndTime = line.split(",");
				Environment tempEnviro = new Environment();
				tempEnviro.setName(nameAndTime[0]);
				tempEnviro.setCron(nameAndTime[1]);
				environments.add(tempEnviro);
				line = br.readLine();
			}
			return environments;
		} finally {
			br.close();
		}

	}

	public void addEnvironment(String name, String cron) throws IOException,
			SchedulerException {
		Boolean environmentAlreadyExists = false;
		List<Environment> tempList = new ArrayList<Environment>();
		tempList = getEnvironments();
		File file = new File(environmentsFile);
		FileWriter writer = new FileWriter(file);
		for (Environment enviro : tempList) {
			if (enviro.getName().equals(name)) {
				environmentAlreadyExists = true;
			}
			writer.write(enviro.getName());
			writer.write(",");
			writer.write(enviro.getCron());
			writer.append("\n");
		}

		if (!environmentAlreadyExists) {
			writer.write(name);
			writer.write(",");
			writer.write(cron);
		}

		writer.flush();
		writer.close();

		trigger.triggerCron();

	}

	public void removeEnvironment(String environment) throws IOException,
			SchedulerException {
		List<Environment> tempList = new ArrayList<Environment>();
		tempList = getEnvironments();
		File file = new File(environmentsFile);
		FileWriter writer = new FileWriter(file);
		for (Environment enviro : tempList) {
			if (enviro.getName() != environment) {
				writer.write(enviro.getName());
				writer.write(",");
				writer.write(enviro.getCron());
				writer.append("\n");
			}
		}

		writer.flush();
		writer.close();

		trigger.triggerCron();
	}

}
