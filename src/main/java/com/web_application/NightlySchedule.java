package com.web_application;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
public class NightlySchedule {

	GitManipulator git = new GitManipulator();
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"HH:mm:ss");
	@Autowired
	JdbcTemplate jdbcTemplate;


	@Scheduled(cron = "55 39 15 * * *")
	public void reportCurrentTime() throws InvalidRemoteException,
			TransportException, GitAPIException, IOException {
		System.out.println("The time is now " + dateFormat.format(new Date()));
		
		AllEnvironmentsFromFile test = new AllEnvironmentsFromFile();
		test.getEnvironments();
	}

}
