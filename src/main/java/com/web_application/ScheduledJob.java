package com.web_application;

import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Controller;

import com.ThreadPoolExec;

@Controller
public class ScheduledJob extends QuartzJobBean {

	String environment;

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {

		ThreadPoolExecutor threadPoolExecutor = ThreadPoolExec.getThreadPoolExec().getExecutor();
		System.out.println(threadPoolExecutor.getPoolSize());
		JobKey key = context.getJobDetail().getKey();
		String enviro = key.getGroup();

		RunGenerator run = null;
		try {
			run = new RunGenerator(enviro, "Scheduled");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		threadPoolExecutor.execute(run);

	}

}
