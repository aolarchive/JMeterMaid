package com;

import java.io.IOException;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.web_application.RunImmediately;

@Configuration
@ComponentScan
@EnableTransactionManagement
@EnableAutoConfiguration
public class Application {
	
	public static void main(String[] args) throws Exception {
		
		Object[] sources = { Application.class, RunImmediately.class };
		SpringApplication.run(sources, args);
	}
}