package com.web_application;

public class ImportantInformation {

	private static String localPath = "obi_ci_scripts";
	private String remotePath = "ssh://git@stash.ops.aol.com:2022/obi/obi_ci_scripts.git";

	public static String getLocalPath() {
		return localPath;
	}

	public String getRemotePath() {
		return remotePath;
	}
}