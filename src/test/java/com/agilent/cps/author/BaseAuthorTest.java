package com.agilent.cps.author;

import java.lang.reflect.Method;

import org.openqa.selenium.By;
import org.testng.ITest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.agilent.cps.core.DriverManagerHelper;
import com.agilent.cps.utils.BaseTest;
import com.agilent.cps.widgetactions.Button;
import com.agilent.cps.widgetactions.TextField;
import com.agilent.cps.widgets.WidgetInfo;

public class BaseAuthorTest extends BaseTest implements ITest{
	
	String testname;
	
//	@BeforeClass(alwaysRun = true)
	public void launchBrowser() {
		String username;
		String password;
		String envType = System.getProperty("env");
		if("dev".equalsIgnoreCase(envType)) {
			logger.info("Executing in DEV environment");
			url = configProperties.getProperty("dev_url");
			username = configProperties.getProperty("dev_username");
			password = configProperties.getProperty("dev_password");
		}else if("stage".equalsIgnoreCase(envType)) {
			logger.info("Executing in STAGE environment");
			url = configProperties.getProperty("stage_url");
			username = configProperties.getProperty("stage_username");
			password = configProperties.getProperty("stage_password");
		}else {
			logger.info("Executing in LOCAL instance");
			url = configProperties.getProperty("local_url");
			username = configProperties.getProperty("local_username");
			password = configProperties.getProperty("local_password");
		}
		browser.startBrowser(url);
		
		DM.textField(new WidgetInfo("id=username", TextField.class)).setDisplayValue(username);
		DM.textField(new WidgetInfo("id=password", TextField.class)).setDisplayValue(password);
		DM.button(new WidgetInfo("id=submit-button", Button.class)).click();
	}
	
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method, Object[] args) {
		if(args.length>0)
			testname=(String) args[1];
		else
			testname = method.getName();
	}
	
	@AfterMethod(alwaysRun = true)
	public void afterMethod() {
	}
	
	@AfterClass(alwaysRun = true)
	public void teardown() {
		try {
			DriverManagerHelper.getInstance().switchWindow("AEM Sites");
			
			String[] path = configProperties.getProperty("authorPagePath").split("/");
	
			for (int i = 0; i < path.length; i++) {
				if (i != path.length - 1)
					DM.getCurrentWebDriver().findElement(By.xpath("//div[@title='" + path[i] + "']")).click();
				else
					DM.getCurrentWebDriver().findElement(By.xpath("//div[@title='" + path[i] + "']/../..//img")).click();
				DriverManagerHelper.sleep(1);
			}
			
			DM.button(new WidgetInfo("xpath=//button[@icon='more']", Button.class)).click();
			
			DM.getCurrentWebDriver().findElement(By.xpath("//button/coral-button-label[contains(text(),'Delete')]")).click();
			
			DM.getCurrentWebDriver().findElements(By.xpath("//button/coral-button-label[text()='Delete']")).get(1).click();
			
			DriverManagerHelper.sleep(2);
		}finally {
			DriverManagerHelper.getInstance().tearDown();
		}
	}

	@Override
	public String getTestName() {
		return testname;
	}
	
}
