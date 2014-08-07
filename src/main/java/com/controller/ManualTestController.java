package com.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import com.ThreadPoolExec;
import com.web_application.AllEnvironmentsFromFile;
import com.web_application.AllTestsAndPaths;
import com.web_application.Environment;
import com.web_application.RunDao;
import com.web_application.RunEntity;
import com.web_application.RunGenerator;
import com.web_application.TestAndPath;

@RestController
public class ManualTestController {
	@Autowired
	private ThymeleafViewResolver resolver;

	@Autowired
	RunDao rDao;

	@Autowired
	AllEnvironmentsFromFile environments;

	@RequestMapping(value = "/manualTest", method = RequestMethod.GET)
	public View greetingForm(Model model) throws Exception {

		AllTestsAndPaths a = new AllTestsAndPaths();
		List<TestAndPath> testList = a.testsAndPaths();
		String[] environmentList = new String[environments.getEnvironments()
				.size()];
		for (int i = 0; i < environments.getEnvironments().size(); i++) {
			environmentList[i] = environments.getEnvironments().get(i)
					.getName();
		}

		int x = rDao.findLatestSourceTestNumber("manual");
		model.addAttribute("manualTestNum", x);
		model.addAttribute("testList", testList);
		model.addAttribute("source", new Source());
		model.addAttribute("environmentList", environmentList);
		return resolver.resolveViewName("manualTest", Locale.US);
	}

	@RequestMapping(value = "/manualTest", method = RequestMethod.POST)
	public ModelAndView sourceSubmit(@ModelAttribute Source source,
			ModelAndView mav) throws IOException, InterruptedException {
		source.setSourceName("manual");
		int testNum = rDao.findLatestSourceTestNumber(source.getSourceName());
		System.out.println(source.getSourceName());
		List<TestAndPath> tests = new ArrayList<TestAndPath>();
		for (String hello : source.getTestList()) {
			System.out.println(hello + " this is what i am now looking for");
			String[] testAndEnviro = hello.split("-");

			TestAndPath tAndP = new TestAndPath();
			tAndP.setName(testAndEnviro[0]);
			tAndP.setEnvironment(testAndEnviro[1]);
			tests.add(tAndP);

		}
		AllTestsAndPaths a = new AllTestsAndPaths();
		List<TestAndPath> testsToRun = a.specificTestsAndPaths(tests);

		for (TestAndPath t : testsToRun) {
			System.out.println(t.getEnvironment() + " name: " + t.getName());
		}

		ThreadPoolExecutor threadPoolExecutor = ThreadPoolExec
				.getThreadPoolExec().getExecutor();
		for (Environment enviro : environments.getEnvironments()) {
			List<TestAndPath> withEnvironment = new ArrayList<TestAndPath>();
			for (TestAndPath testers : testsToRun) {

				if (testers.getEnvironment().equals(enviro.getName())) {
					withEnvironment.add(testers);
				}

			}
			if (!withEnvironment.isEmpty()) {
				System.out.println("blahbabob");
				for (TestAndPath lala : withEnvironment) {
					System.out.println(lala.getEnvironment());
					System.out.println(lala.getName());

				}
				System.out.println(enviro.getName());
				RunGenerator run = new RunGenerator(testNum + 1,
						enviro.getName(), source.getSourceName(),
						withEnvironment);
				System.out.println(threadPoolExecutor.getTaskCount());
				System.out.println("Complete task count "
						+ threadPoolExecutor.getCompletedTaskCount());
				long count = threadPoolExecutor.getCompletedTaskCount();

				threadPoolExecutor.execute(run);

				System.out.println("taskCount "
						+ threadPoolExecutor.getTaskCount());
				System.out.println("Complete task count "
						+ threadPoolExecutor.getCompletedTaskCount());
			}
		}
		int x = rDao.findLatestSourceTestNumber("manual");
		mav.setViewName("redirect:/manualResults?source="
				+ source.getSourceName() + "&testNum=" + x);
		return mav;
	}

	@RequestMapping(value = "/manualResults", method = RequestMethod.GET)
	public View manualResults(@RequestParam("source") String source,@RequestParam("testNum") int testNum, Model model)
			throws Exception {
		List<RunEntity> runs = new ArrayList<RunEntity>();
		runs = rDao.findByTestNumberAndSource(testNum, source);
		int newest = rDao.findLatestSourceTestNumber("manual");
		int max =rDao.findLatestSourceTestNumber("manual") + 1; 
		int left = testNum + 1;
		int right = testNum - 1;
		String date = new SimpleDateFormat("MM/dd/yyyy").format(runs.get(0).getDate());

		model.addAttribute("testNumber", testNum);
		model.addAttribute("runs", runs);
		model.addAttribute("newest", newest);
		model.addAttribute("max", max);
		model.addAttribute("left", left);
		model.addAttribute("right", right);
		model.addAttribute("date", date);
		model.addAttribute("testNum", testNum);
		return resolver.resolveViewName("manualResults", Locale.US);

	}

}
