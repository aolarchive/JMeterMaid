package com.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import com.web_application.AllEnvironmentsFromFile;
import com.web_application.AllTestsAndPaths;
import com.web_application.EnvironmentEntity;
import com.web_application.GatherInfoForIndexTable;
import com.web_application.RunDao;

@RestController
public class ConfigureController {
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

	@RequestMapping(value = "/configure", method = RequestMethod.GET)
	public View configureForm(@RequestParam("error") String error, Model model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Integer errorNum = -1;
		String errorType = null;
		List<EnvironmentEntity> enviros = new ArrayList<EnvironmentEntity>();
		if (error.equals("true")) {
			Source sor = (Source) request.getSession().getAttribute("errors");
			errorNum = sor.getEnviroErrorNum();
			errorType = sor.getEnviroErrorType();
			enviros = sor.getEnviro();
			request.getSession().removeAttribute("errors");
		} else {
			enviros = environments.getAllEntities();
		}
		
		Source source = new Source();
		source.setEnviro(enviros);
		int x = rDao.findLatestSourceTestNumber("manual");
		model.addAttribute("manualTestNum", x);
		model.addAttribute("errorNum", errorNum);
		model.addAttribute("errorType", errorType);
		model.addAttribute("environments", enviros);
		model.addAttribute("source", source);
		return resolver.resolveViewName("configure", Locale.US);
	}

	@RequestMapping(value = "/configure", method = RequestMethod.POST)
	public ModelAndView configureSubmit(@ModelAttribute Source source,
			ModelAndView mav, HttpServletRequest request,
			HttpServletResponse response) throws SchedulerException,
			IOException, ServletException {
		List<EnvironmentEntity> env = source.getEnviro();

		boolean errorBool = false;
		String errorType = null;
		Integer errorNum = env.size() + 5;

		AllTestsAndPaths aTP = new AllTestsAndPaths();
		String[] enviroOptions = aTP.getAllEnviros();
		for (int i = 0; i < env.size(); i++) {
			if (!org.quartz.CronExpression.isValidExpression(env.get(i)
					.getCron())) {
				errorBool = true;
				errorType = "cron";
			}
			boolean notAnOption = true;
			for (String name : enviroOptions) {
				if (env.get(i).getName().equals(name)) {
					notAnOption = false;
				}
			}
			if (notAnOption) {
				errorBool = true;
				errorType = "environment";
			} else {
				if (errorBool && env.get(i).getCron().equals("")) {
					// Setting cron value that will never run
					errorBool = false;
				}
			}
			if (env.get(i).getName().equals("")
					&& env.get(i).getCron().equals("")) {
				errorBool = false;
			}

			if (errorBool) {
				errorNum = i;
				break;
			}
		}
		if (errorBool) {
			Source sor = new Source();
			sor.setEnviro(env);
			sor.setEnviroErrorType(errorType);
			sor.setEnviroErrorNum(errorNum);
			request.getSession().setAttribute("errors", sor);

			mav.setViewName("redirect:/configure?error=true");
			return mav;
		} else {
			List<Integer> intList = source.getEnvirosToRemove();
			List<EnvironmentEntity> envTwo = new ArrayList<EnvironmentEntity>();
			if (!(intList == null)) {
				for (int i = 0; i < env.size(); i++) {
					boolean add = true;
					for (Integer j : intList) {
						if (j == i) {
							add = false;
						}
					}
					if (env.get(i).getName().equals("")
							&& env.get(i).getCron().equals("")) {
						add = false;
					}
					if (add) {
						envTwo.add(env.get(i));
					}
				}

			} else {
				for (EnvironmentEntity e : env) {
					if (!(e.getName().equals("") && e.getCron().equals(""))) {
						envTwo.add(e);
					}
				}
			}
			environments.addAll(envTwo);
			mav.setViewName("redirect:/index");
			return mav;
		}

	}

}