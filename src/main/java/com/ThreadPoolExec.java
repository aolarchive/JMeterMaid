package com;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.Scheduler;


import com.web_application.RunDao;


@Configuration
public class ThreadPoolExec {

	static int corePoolSize = 1;
	static int maxPoolSize = 1;
	static long keepAliveTime = 5000;
	static ThreadPoolExec thread;

	static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
			corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>());
	
	static Scheduler scheduler;
	
	public ThreadPoolExec() throws SchedulerException
	{
		scheduler = new StdSchedulerFactory().getScheduler();
	}

	public static ThreadPoolExecutor getExecutor()
	{
		return threadPoolExecutor;
	}
	
	public static Scheduler getScheduler() throws SchedulerException
	{
		scheduler = new StdSchedulerFactory().getScheduler();
		return scheduler;
	}
	
	public static ThreadPoolExec getThreadPoolExec()
	{
		return thread;
	}
	
	
}
