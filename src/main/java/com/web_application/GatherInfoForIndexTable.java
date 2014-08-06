package com.web_application;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GatherInfoForIndexTable {

	@Autowired
	RunDao rDao;
	
	@Autowired
	AllEnvironmentsFromFile environments;
	
	AllTestsAndPaths tests = new AllTestsAndPaths();
	
	
	public List<TestAndPath> infoForTable() throws IOException
	{
		List<Environment> enviroList = environments.getEnvironments();
		List<TestAndPath> testList = tests.testsAndPaths();
		List<TestAndPath> listToReturn = new ArrayList<TestAndPath>();
		
		for(TestAndPath test : testList)
		{
			TestAndPath tempTest = new TestAndPath();
			tempTest.setName(test.getName());
			tempTest.setPath(test.getPath());
			IDAndPassOrFail[] passFailIDTemp = new IDAndPassOrFail[enviroList.size()];

			
			for(int i = 0; i < enviroList.size(); i++)
			{
				RunEntity runTemp = new RunEntity();
				int testNum = rDao.findLatestTestNumber(enviroList.get(i).getName(), "Scheduled");
				List<RunEntity> tempDaoList = new ArrayList<RunEntity>();
				IDAndPassOrFail passID = new IDAndPassOrFail();

				tempDaoList = rDao.findByNameNumberEnviroSource(tempTest.getName(), testNum, enviroList.get(i).getName(), "Scheduled");
				if(!tempDaoList.isEmpty())
				{
					runTemp = tempDaoList.get(0);
					passID.setPassFailOrNA(runTemp.getPassOrFail());
					passID.setRunID(runTemp.getID());
				}
				else
				{
					passID.setPassFailOrNA("N/A");
					passID.setRunID(0);
				}
				passFailIDTemp[i] = passID;
			}
			
			tempTest.setPassFailOrNA(passFailIDTemp);
			listToReturn.add(tempTest);
			
			
		}
		
		return listToReturn;
		
	}
	
}
