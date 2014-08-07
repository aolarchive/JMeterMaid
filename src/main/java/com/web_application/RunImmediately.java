package com.web_application;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.ThreadPoolExec;

@Component
public class RunImmediately {

	@Autowired
	CronTrigger trigger;

	@Autowired
	ZipManipulator zipper;
	
	@Autowired
	AllEnvironmentsFromFile allE;

	GitManipulator git = new GitManipulator();
	
	ThreadPoolExecutor tpe = ThreadPoolExec.getThreadPoolExec().getExecutor();

	@PostConstruct
	public void go() throws IOException, SchedulerException,
			InvalidRemoteException, TransportException, GitAPIException {
		
		
		
		EnvironmentEntity e = new EnvironmentEntity();
		e.setName("OBI01");
		e.setCron("0 50 10 * * ?");
		List<Environment> z = allE.getEnvironments();
		Boolean exists = false;
		for(Environment er : z)
		{
			if(er.getName().equals(e.getName()))
			{
				exists = true;
			}
		}
		if(!exists)
		{
			allE.create(e);
		}
		git.cloneOrPull();
		trigger.triggerCron();
	}
	
	@PreDestroy
	public void kill() throws InterruptedException
	{
		while(tpe.getActiveCount() > 0)
		{
			Thread.sleep(5000);
		}
	}

}
