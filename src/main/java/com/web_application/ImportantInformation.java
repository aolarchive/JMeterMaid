package com.web_application;

public class ImportantInformation {
	
	private static String  localPath = "/Users/dansbacher14/Documents/workspace/obi_jmx_test_webapp/src/obi_ci_scripts";
	private String remotePath = "ssh://git@stash.ops.aol.com:2022/obi/obi_ci_scripts.git";
	private static String pathToEnvironmentsInfo = "/Users/dansbacher14/Documents/workspace/obi_jmx_test_webapp/src/main/java/com/web_application/EnvironmentInfo.txt";

	public static String getLocalPath()
	{
		return localPath;
	}
	
	public String getRemotePath()
	{
		return remotePath;
	}
	
	public static String getPathToEnvironmentsInfo()
	{
		return pathToEnvironmentsInfo;
	}
}
