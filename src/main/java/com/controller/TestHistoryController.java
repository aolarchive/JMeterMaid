package com.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.View;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import com.web_application.AllEnvironmentsFromFile;
import com.web_application.GatherInfoForIndexTable;
import com.web_application.RunDao;
import com.web_application.RunEntity;

@RestController
public class TestHistoryController {
	@Autowired
	private ThymeleafViewResolver resolver;
	
	@Autowired
	GatherInfoForIndexTable gatherInfo;

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	RunDao rDao;
	
	@Autowired
	AllEnvironmentsFromFile environments;
	
	@RequestMapping(value = "/testHistory", method = RequestMethod.GET)
	public View manualResults(@RequestParam("test") String testName, Model model) throws Exception {
		
		List<RunEntity> runs = rDao.findByName(testName);
		int x = rDao.findLatestSourceTestNumber("manual");
		model.addAttribute("manualTestNum", x);
		model.addAttribute("testName", testName);
		model.addAttribute("runHistory", runs);
		return resolver.resolveViewName("testHistory", Locale.US);
	}
}
	