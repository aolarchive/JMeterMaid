package com.web_application;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;

public class RunGenerator implements Runnable {

	static String environment;
	RunDao rDao = ApplicationContextHolder.getContext().getBean(RunDao.class);
	int testNumber;
	List<TestAndPath> testList;
	GitManipulator git = new GitManipulator();
	static String localPath = ImportantInformation.getLocalPath();
	static String pathToODirectory;
	String source;

	public RunGenerator(String enviro, String sources) throws IOException {
		environment = enviro;
		testNumber = getLatestTestNumber() + 1;
		
		AllTestsAndPaths all = new AllTestsAndPaths();
		testList = all.testsAndPaths();
		pathToODirectory = "test-results";
		source = sources;

	}
	
	public RunGenerator(int testNum, String enviro, String sources, List<TestAndPath> testLists) throws IOException {
		environment = enviro;
		testNumber = testNum;
		System.out.println("The environment string is now " + enviro);
		testList = testLists;
		pathToODirectory = "test-results";
		source = sources;

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

		for (TestAndPath test : testList) {
			System.out.println("Running " + test.getName() + " in "
					+ environment + " environment!");
			RunEntity run = new RunEntity();
			run.setTestName(test.getName());
			run.setEnvironment(environment);
			run.setTestNumber(testNumber);
			java.util.Date date = new java.util.Date();
			run.setDate(new Timestamp(date.getTime()));
			run.setSource(source);
			try {
				run.setPassOrFail(testPassed(test.getPath(), test.getName()));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				run.setPassOrFail("Failed");
				break;
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

	public int getLatestTestNumber() {
		int testNum = rDao.findLatestTestNumber(environment, source);
		return testNum;
	}

	public static String testPassed(String directory, String fileName)
			throws InterruptedException {
		int passOrFail = 2;
		File file = new File(localPath);

		String returnSH = "./report.sh -e " + environment + " -t " + directory
				+ "/" + fileName + " -o " + pathToODirectory;

		try {
			Process process = Runtime.getRuntime().exec(returnSH, null, file);
			process.waitFor();
			// System.out.println("Input Stream: " + process.getInputStream());
			// System.out.println("Output Stream: " +
			// process.getOutputStream());
			// System.out.println("Errors: " + process.getErrorStream());

			passOrFail = process.exitValue();

		} catch (IOException e) {
			// TODO Auto-generated catch bloc
			e.printStackTrace();
		}

		if (passOrFail == 0) {
			return "Passed";
		} else {
			return "Failed";
		}
	}

	// ____________________________________________________________________________________________________________________________________
	public RunGenerator() {

	}
}

// Generate all values for run and return run

// ID INTEGER IDENTITY,
// Source VARCHAR(50),
// RESULTFILES BLOB);