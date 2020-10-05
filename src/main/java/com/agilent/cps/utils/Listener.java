package com.agilent.cps.utils;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.asserts.SoftAssert;

import com.agilent.cps.core.DriverManager;
import com.agilent.cps.core.DriverManagerHelper;
import com.agilent.cps.core.Verify;

public class Listener implements ITestListener{

	@Override
	public void onTestStart(ITestResult result) {
		Logger.getInstance().info("################## Test Started : "+result.getName()+" ##############");
		Verify.softAssert = new SoftAssert();
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		
	}

	@Override
	public void onTestFailure(ITestResult result) {
		if(! (result.getThrowable() instanceof AssertionError || result.getName().equalsIgnoreCase("createPageForAuthorTests"))) {
			Logger.getInstance().error(result.getThrowable());
			try {
				DriverManager DM = DriverManager.getInstance();
				WebDriver driver = DM.getCurrentWebDriver();
				Set<String> windows = driver.getWindowHandles();
				if(2 <= windows.size()) {
					for(String window : windows) {
						driver.switchTo().window(window);
						if(!"AEM Sites".equalsIgnoreCase(driver.getTitle()))
							driver.close();
					}
				}
				DriverManagerHelper.getInstance().switchWindow("AEM Sites");
				String[] path = BaseTest.configProperties.getProperty("authorPagePath").split("/");
	
				for (int i = 0; i < path.length; i++) {
					if (i != path.length - 1)
						DM.getCurrentWebDriver().findElement(By.xpath("//div[@title='" + path[i] + "']")).click();
					else
						DM.getCurrentWebDriver().findElement(By.xpath("//div[@title='" + path[i] + "']/../..//img")).click();
					DriverManagerHelper.sleep(1);
				}
	
				DM.getCurrentWebDriver().findElement(By.xpath("//button/coral-button-label[contains(text(), 'Edit')]")).click();
	
				for (int i = 0; i <= 6; i++) {
					DriverManagerHelper.sleep(5);
					if (DM.getCurrentWebDriver().getWindowHandles().size() == 2)
						break;
				}
				for (String window : DM.getCurrentWebDriver().getWindowHandles())
					DM.getCurrentWebDriver().switchTo().window(window);
				
				List<WebElement> components = driver.findElements(By.xpath("//div[@data-type='Editable']/span[text()='Layout Container [Root]']/../div"));
				for(WebElement component : components) {
					String componentName = component.getAttribute("title");
					if(!"Layout Container [Root]".equalsIgnoreCase(componentName)) {
						WebElement componentElement = DM.getCurrentWebDriver().findElement(By.xpath("//div[@title='"+componentName+"']"));
						if(componentName.equalsIgnoreCase("Carousel") || componentName.equalsIgnoreCase("Accordion")) {
							componentElement.click();
							DM.getCurrentWebDriver().findElement(By.xpath("//button[@title='Parent']/coral-icon")).click();
							DM.getCurrentWebDriver().findElement(By.xpath("//coral-list-item-content[text()='"+componentName+"']")).click();
						}else
							componentElement.click();
						DM.getCurrentWebDriver().findElement(By.xpath("//button[@title='Delete']")).click();
						
						DM.getCurrentWebDriver().findElement(By.xpath("//button/coral-button-label[text()='Delete']")).click();
					}
				}
			}catch(Exception e) {
				Logger.getInstance().error("On test failure, unable to delete component");
			}
		}
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		result.getThrowable().printStackTrace();
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		
	}

	@Override
	public void onStart(ITestContext context) {
		
	}

	@Override
	public void onFinish(ITestContext context) {
		
	}

}
