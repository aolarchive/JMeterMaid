package com.web_application;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.errors.CanceledException;
import org.eclipse.jgit.api.errors.DetachedHeadException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidConfigurationException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;

public class GitManipulator {

	static ImportantInformation info = new ImportantInformation();
	private static String localPath = info.getLocalPath();
	private String remotePath = info.getRemotePath();
	private Git git;
	private Repository localRepo;

	public GitManipulator(){
		try {
			localRepo = new FileRepository(localPath + "/.git");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		git = new Git(localRepo);
	}

	public void cloneOrPull() throws InvalidRemoteException, TransportException, GitAPIException
	{
		if(!repositoryExists())
		{
			cloneGit();
		}
		else
		{
			pullAndResetGit();
		}
	}
	
	
	public void cloneGit() throws InvalidRemoteException, TransportException,
			GitAPIException {
		System.out.println("Cloning from obi_ci_scripts");
		Git.cloneRepository().setURI(remotePath)
				.setDirectory(new File(localPath)).call();
	}

	public void pullAndResetGit() throws WrongRepositoryStateException,
			InvalidConfigurationException, DetachedHeadException,
			InvalidRemoteException, CanceledException, RefNotFoundException,
			NoHeadException, TransportException, GitAPIException {
		System.out.println("Pulling from obi_ci_scripts and Reseting");
		git.pull().call();
		git.reset().setMode(ResetType.HARD).call();
	}

	public static boolean repositoryExists() {
		File f = new File(localPath);
		if (f.exists()) {
			return true;
		} else {
			return false;
		}
	}
}
