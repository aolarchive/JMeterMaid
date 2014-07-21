package com.web_application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AllTestsAndPaths {

	public List<TestAndPath> testsAndPaths() throws IOException {
		List<TestAndPath> testsAndPaths = new ArrayList<TestAndPath>();
		String localPath = ImportantInformation.getLocalPath();

		List<String> allPaths = new ArrayList<String>();
		recursiveAllDirectories(localPath, allPaths, localPath.length());
		
		testsAndPaths = getTestersWithDirectoryPath(allPaths, localPath);

		
		return testsAndPaths;
	}

	public void recursiveAllDirectories(String path, List<String> paths,
			int pathLength) {
		File file = null;
		file = new File(path);

		String[] files = file.list();

		for (String fileTemp : files) {
			String newPath = path + "/" + fileTemp;
			File newFile = new File(newPath);
			if (newFile.isDirectory()) {
				if(arrayContainsJMX(newFile.list()))
				{
					paths.add(newPath.substring(pathLength + 1));
				}
				recursiveAllDirectories(newPath, paths, pathLength);
			}
		}

	}

	public boolean arrayContainsJMX(String[] array) {
		boolean containsJMX = false;
		for (int i = 0; i < array.length; i++) {
			if (array[i].length() > 3) {
				if ((array[i].endsWith("jmx"))) {
					containsJMX = true;
				}
			}
		}

		return containsJMX;
	}
	
	
	public List<TestAndPath> getTestersWithDirectoryPath(
			List<String> pathsWithJMX, String localPath)
			throws IOException {
		List<TestAndPath> testerArrayList = new ArrayList<TestAndPath>();

		for (int i = 0; i < pathsWithJMX.size(); i++) {
			File file = new File(localPath + "/" + pathsWithJMX.get(i));

			String[] fileList = file.list();
			for (int j = 0; j < fileList.length; j++) {
				if ((fileList[j].endsWith("jmx"))) {
					TestAndPath tempTester = new TestAndPath();
					tempTester.setName(fileList[j]);
					tempTester.setPath(pathsWithJMX.get(i));
					testerArrayList.add(tempTester);
				}
			}

		}

		return testerArrayList;

	}

}
