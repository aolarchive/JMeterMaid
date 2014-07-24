package com.controller;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
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
	
	@RequestMapping("/index")	
	public View index(Model model) throws Exception {
		//environments.addEnvironment("OBI02", "0 * * * * ?");
		String[] environmentList = new String[environments.getEnvironments().size()];
		for(int i = 0; i < environments.getEnvironments().size(); i++)
		{
			environmentList[i] = environments.getEnvironments().get(i).getName();
		}
		List<TestAndPath> info = gatherInfo.infoForTable();
		TestAndPath[] information = new TestAndPath[info.size()];
		for(int i = 0; i < info.size(); i++)
		{
			//System.out.println("Test " + info.get(i).getName());
			information[i] = info.get(i);
		}
		
		
		
        model.addAttribute("info", information);
        model.addAttribute("environmentList", environmentList);

		return resolver.resolveViewName("index", Locale.US);
	}

	@RequestMapping(value="/results", method=RequestMethod.GET)
	public View showResults(@RequestParam("id")int ID, Model model) throws Exception{
	
	model.addAttribute("ID", ID);
	
	if(ID != 0)
	{
		RunEntity run = new RunEntity();
		
		run = rDao.findById(ID);
		
	    model.addAttribute("run", run);
	}

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