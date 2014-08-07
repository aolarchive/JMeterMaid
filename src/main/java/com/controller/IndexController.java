package com.controller;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.View;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import com.web_application.AllEnvironmentsFromFile;
import com.web_application.Environment;
import com.web_application.GatherInfoForIndexTable;
import com.web_application.RunDao;
import com.web_application.RunEntity;
import com.web_application.TestAndPath;
import com.web_application.ZipManipulator;

@RestController
public class IndexController {
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
	
	 @ResponseStatus(value=HttpStatus.NOT_FOUND, reason="ID is null")  // 404
	  @ExceptionHandler(IDIsNullException.class)
	  public void conflict() {
	    // Nothing to do
	  }
	
	@RequestMapping("/index")	
	public View index(Model model) throws Exception {
		//environments.addEnvironment("OBI02", "0 * * * * ?");
		List<Environment> enviroList = environments.getEnvironments();
		for(Environment env : enviroList)
		{
			env.setLastTestNum(rDao.findLatestTestNumber(env.getName(), "Scheduled"));
		}
		List<TestAndPath> info = gatherInfo.infoForTable();
		TestAndPath[] information = new TestAndPath[info.size()];
		for(int i = 0; i < info.size(); i++)
		{
			//System.out.println("Test " + info.get(i).getName());
			information[i] = info.get(i);

		}
		
        model.addAttribute("info", information);
        model.addAttribute("environmentList", enviroList);
        model.addAttribute("manualTestNum", rDao.findLatestSourceTestNumber("manual"));

		return resolver.resolveViewName("index", Locale.US);
	}

	@RequestMapping(value="/results", method=RequestMethod.GET)
	public View showResults(@RequestParam("id")int ID, Model model) throws Exception{
	
	model.addAttribute("ID", ID);
	
	if(ID != 0)
	{
		RunEntity run = new RunEntity();
		
		run = rDao.findById(ID);
		if(run == null)
		{
			throw new IDIsNullException();
		}
		else
		{
			model.addAttribute("run", run);
		}
	}
	int x = rDao.findLatestSourceTestNumber("manual");
	model.addAttribute("manualTestNum", x);
	return resolver.resolveViewName("results", Locale.US);
	}
	
	@RequestMapping(value="/resultDisplay", method=RequestMethod.GET)
	public void showResultDisplay(@RequestParam("id")int ID, Model model, HttpServletResponse res) throws Exception{
		
		RunEntity run = new RunEntity();
		
		run = rDao.findById(ID);
		res.setContentType("text/html");

		if(ID != 0 && run != null)
		{
			byte[] b = 	ZipManipulator.writeByteToFile(run.getResultFiles());
			
			IOUtils.write(b, res.getOutputStream());
		}
		
	}
}