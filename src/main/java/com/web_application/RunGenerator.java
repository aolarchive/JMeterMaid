package com.web_application;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;

public class RunGenerator implements Runnable {

	String environment;
	RunDao rDao = ApplicationContextHolder.getContext().getBean(RunDao.class);
	int testNumber;
	List<TestAndPath> testList;
	GitManipulator git = new GitManipulator();
	final static String localPath = ImportantInformation.getLocalPath();
	String pathToODirectory;
	String source;
	List<RunEntity> runList = new ArrayList<RunEntity>();

	public RunGenerator(String enviro, String sources) throws IOException {
		environment = enviro;
		testNumber = getLatestTestNumber(enviro, sources) + 1;
		
		AllTestsAndPaths all = new AllTestsAndPaths();
		testList = all.testsAndPaths();
		pathToODirectory = "test-results";
		source = sources;
		
		createRuns(enviro, testNumber, testList);

	}
	
	public RunGenerator(int testNum, String enviro, String sources, List<TestAndPath> testLists) throws IOException {
		environment = enviro;
		testNumber = testNum;
		System.out.println("The environment string is now " + enviro);
		testList = testLists;
		pathToODirectory = "test-results";
		source = sources;
		createRuns(enviro, testNumber, testList);
	}
	
	public void createRuns(String enviro, int testNum, List<TestAndPath> testLists)
	{
		for (TestAndPath test : testList) {
			
			RunEntity run = new RunEntity();
			run.setTestName(test.getName());
			run.setEnvironment(environment);
			run.setTestPath(test.getPath());
			run.setTestNumber(testNumber);
			java.util.Date date = new java.util.Date();
			run.setDate(new Timestamp(date.getTime()));
			run.setSource(source);
			run.setPassOrFail("Pending");
			rDao.create(run);
			runList.add(run);
		}
	}

	@Override
	public void run() {
		try {
			git.cloneOrPull();
		} catch (InvalidRemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (RunEntity run : runList) {
			System.out.println("Running " + run.getTestName() + " in "
					+ environment + " environment!");
			run.setPassOrFail("In Progress");
			rDao.create(run);
			try {
				run.setPassOrFail(testPassed(run.getTestPath(), run.getTestName(), run));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				run.setPassOrFail("Failed");
				break;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			try {
				run.setResultFiles(ZipManipulator.compressZipFile(localPath
						+ "/test-results/"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			rDao.create(run);
		}
	}

	public int getLatestTestNumber(String enviro, String sources) {
		int testNum = rDao.findLatestTestNumber(enviro, sources);
		System.out.println("Latest test number " + testNum);
		System.out.println("Environment: " + enviro);
		System.out.println("Source: " + sources);
		return testNum;
	}

	private String testPassed(String directory, String fileName, RunEntity run)
			throws InterruptedException, IOException, TimeoutException {
		String toReturn;
		File file = new File(localPath);

		String returnSH = "./report.sh -e " + environment + " -t " + directory
				+ "/" + fileName + " -o " + pathToODirectory;

		
		final Process process = Runtime.getRuntime().exec(returnSH, null, file);
		
		BufferedReader error = new BufferedReader(new InputStreamReader(process.getInputStream()));
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Thread t1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					IOUtils.copy(process.getInputStream(), baos);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		t1.start();
		Thread t2 = new Thread(new Runnable()
		{

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					IOUtils.copy(process.getErrorStream(), baos);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		t2.start();
		
		
		ThreadWorker worker = new ThreadWorker(process);
		int exitCode = worker.waitForProcess(3600000);

		if (exitCode == Integer.MIN_VALUE)
		{
		    // Timeout
			toReturn = "Abort";
		}
		else
		{
		    // No timeout !
		      if (exitCode == 0) 
		      {
					toReturn = "Passed";
				} else {
					toReturn = "Failed";
				}
		}
		t1.join();
		t2.join();
		
		byte[] bytes = baos.toByteArray();
		
		run.setConsoleOutput(bytes);
		
	    return toReturn;

	}

	// ____________________________________________________________________________________________________________________________________
	
}

// Generate all values for run and return run

// ID INTEGER IDENTITY,
// Source VARCHAR(50),
// RESULTFILES BLOB);