package com.web_application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.repository.RunRepository;

@Service
public class RunDao {

	@Autowired
	private RunRepository runRepository;

	@Transactional
	public RunEntity create(RunEntity run) {

		RunEntity createdRun = run;

		return runRepository.save(createdRun);
	}

	@Transactional
	public RunEntity findById(int id) {
		return runRepository.findOne(id);
	}

	@Transactional
	public List<RunEntity> findAll() {
		return runRepository.findAll();
	}

	@Transactional
	public List<RunEntity> findAllByEnvironment(String enviro, String source) {
		return runRepository.findByEnvironmentAndSource(enviro, source);
	}
	
	@Transactional
	public List<RunEntity> findByTestNumberAndSource(int testNum, String source) {
		return runRepository.findByTestNumberAndSource(testNum, source);
	}
	
	@Transactional
	public List<RunEntity> findByName(String name) {
		return runRepository.findByName(name);
	}

	public List<RunEntity> findByNameNumberEnviroSource(String testName,
			int testNum, String enviro, String source) {
		return runRepository.findByNameNumberEnvironmentSource(testName,
				testNum, enviro, source);
	}

	public int findLatestTestNumber(String enviro, String source) {
		int testNum = 0;
		List<RunEntity> runList = findAllByEnvironment(enviro, source);
		if (runList != null) {
			for (RunEntity run : runList) {
				if (run.getTestNumber() > testNum) {
					testNum = run.getTestNumber();
				}
			}
		}

		return testNum;
	}
	
	public int findLatestSourceTestNumber(String source) {
		int testNum = 0;
		List<RunEntity> runList = runRepository.findBySource(source);
		if (runList != null) {
			for (RunEntity run : runList) {
				if (run.getTestNumber() > testNum) {
					testNum = run.getTestNumber();
				}
			}
		}

		return testNum;
	}
	

}