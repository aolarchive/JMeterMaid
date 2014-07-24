package com.web_application;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RunImmediately {

	@Autowired
	CronTrigger trigger;

	@Autowired
	ZipManipulator zipper;

	GitManipulator git = new GitManipulator();

	@PostConstruct
	public void go() throws IOException, SchedulerException,
			InvalidRemoteException, TransportException, GitAPIException {
		git.cloneOrPull();
		trigger.triggerCron();
	}

}
