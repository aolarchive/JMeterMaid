package com.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.web_application.EnvironmentEntity;

public interface EnvironmentRepository extends JpaRepository<EnvironmentEntity, Integer> {

}