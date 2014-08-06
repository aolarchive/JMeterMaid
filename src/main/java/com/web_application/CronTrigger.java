package com.web_application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ThreadPoolExec;

@Component
public class CronTrigger {

	@Autowired
	AllEnvironmentsFromFile enviros;

	List<Environment> environments = new ArrayList<Environment>();
	Scheduler scheduler;

	public CronTrigger() throws IOException, SchedulerException {
		scheduler = ThreadPoolExec.getScheduler();
		scheduler.start();
	}

	public void triggerCron() throws SchedulerException, IOException {

		scheduler.clear();
		environments = enviros.getValidEnvironments();
		
		for (Environment enviro : environments) {
			try{
			JobKey jobKey = new JobKey("Scheduled", enviro.getName());

			JobDetail runTestJob = JobBuilder.newJob(ScheduledJob.class)
					.withIdentity(jobKey).build();

			Trigger runTrigger = TriggerBuilder
					.newTrigger()
					.withIdentity("cronTrigger", enviro.getName())
					.withSchedule(
							CronScheduleBuilder.cronSchedule(enviro.getCron()))
					.build();

			scheduler.scheduleJob(runTestJob, runTrigger);

		}
			catch (org.quartz.SchedulerException e)
			{
				System.out.println("Cron is invalid");
			}
		}
		

	}
}