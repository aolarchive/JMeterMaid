package com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.web_application.RunEntity;

public interface RunRepository extends JpaRepository<RunEntity, Integer> {

	@Query("SELECT r FROM RunEntity r WHERE r.environment = :environment AND r.source = :source")
	public List<RunEntity> findByEnvironmentAndSource(
			@Param("environment") String environment,
			@Param("source") String source);

	@Query("SELECT r FROM RunEntity r WHERE r.testName = :testname AND r.testNumber = :testnumber AND r.environment = :environment AND r.source = :source")
	public List<RunEntity> findByNameNumberEnvironmentSource(
			@Param("testname") String testname,
			@Param("testnumber") int testnumber,
			@Param("environment") String environment,
			@Param("source") String source);

}