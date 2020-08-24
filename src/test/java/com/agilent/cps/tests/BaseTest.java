package com.agilent.cps.tests;


import java.lang.reflect.Method;

import org.testng.ITest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.agilent.cps.utils.Browser;
import com.agilent.cps.utils.Constants;
import com.agilent.cps.utils.DriverManager;

public class BaseTest implements ITest{
	
	Browser browser = new Browser();
	String testname;
	
	@BeforeSuite
	public void configurationSetup() {
		String browser = System.getProperty("browser");
		String isRCServer = System.getProperty("isrcserver");
		String port = System.getProperty("port");
		
		if(!("".equals(browser) || null == browser))
			Constants.browser = browser.toUpperCase();
		
		if(!("".equals(isRCServer) || null == isRCServer))
			Constants.isrcserver = isRCServer;
		
		if(!("".equals(port) || null == port))
			Constants.port = port;
	}
	
	@BeforeMethod(alwaysRun = true)
	public void launchBrowser(Method method) {
		testname=method.getName();
		browser.startBrowser("https://www.agilent.com/");
	}
	
	@AfterMethod(alwaysRun = true)
	public void teardown() {
		DriverManager.getInstance().tearDown();
	}

	@Override
	public String getTestName() {
		return testname;
	}
}
