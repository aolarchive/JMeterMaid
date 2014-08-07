package com.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
public class EnvironmentHistoryController {
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
	
	@RequestMapping(value = "/environmentHistory", method = RequestMethod.GET)
	public View manualResults(@RequestParam("testNum") int testNum,@RequestParam("environment") String enviro, Model model) throws Exception {
		int max = rDao.findLatestTestNumber(enviro, "Scheduled") + 1;
		int left = testNum + 1;
		int right = testNum -1;
		List<RunEntity> runs = rDao.findByTestNumberAndSource(testNum, "Scheduled");
		List<RunEntity> runsTwo = new ArrayList<RunEntity>();
		for(RunEntity run : runs)
		{
			if(!run.getPassOrFail().equals("Pending"))
			{
				runsTwo.add(run);
			}
		}
		String date = new SimpleDateFormat("MM/dd/yyyy").format(runs.get(0).getDate());
		int x = rDao.findLatestSourceTestNumber("manual");
		model.addAttribute("manualTestNum", x);
		model.addAttribute("date", date);
		model.addAttribute("runs", runsTwo);
		model.addAttribute("max", max);
		model.addAttribute("left", left);
		model.addAttribute("right", right);
		model.addAttribute("enviro", enviro);

		return resolver.resolveViewName("environmentHistory", Locale.US);
	}

	
}