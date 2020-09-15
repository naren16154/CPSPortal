package com.agilent.cps.components;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.assertj.core.util.Arrays;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.agilent.cps.core.AutoPopulator;
import com.agilent.cps.core.DriverManager;
import com.agilent.cps.core.DriverManagerHelper;
import com.agilent.cps.core.Verify;
import com.agilent.cps.utils.ReadExcel;

public abstract class BaseComponent {

	DriverManager DM = DriverManager.getInstance();
	DriverManagerHelper DMHelper = DriverManagerHelper.getInstance();
	
	
	public void populate(Map<String, String> rowData) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		AutoPopulator.populate(this, rowData);
	}
	
	public abstract void verifyPreview(Map<String, String> rowData);
	
	public abstract String getComponentName();
	
	public void selectStyle(String styles) {
		DM.getCurrentWebDriver().findElement(By.xpath("//button[@title='Done']")).click();
		DriverManagerHelper.sleep(2);
		
		List<WebElement> components = DM.getCurrentWebDriver().findElements(By.xpath("//div[@title='"+this.getComponentName()+"']"));
		components.get(components.size()-1).click();
		DM.getCurrentWebDriver().findElement(By.xpath("//button[@title='Styles']")).click();
		
		List<Object> actualStyles = Arrays.asList(styles.split(","));
		
		List<WebElement> stylesList = DM.getCurrentWebDriver().findElements(By.xpath("//form[@id='editor-styleselector-form']//coral-selectlist-item[@class='coral3-SelectList-item']"));
		
		for(WebElement style : stylesList) {
			boolean selected = Boolean.parseBoolean(style.getAttribute("aria-selected"));
			
			if(selected ^ actualStyles.contains(style.getText()))
				style.click();
		}
		
		DM.getCurrentWebDriver().findElements(By.xpath("//button[@icon='check']")).get(1).click();
		DriverManagerHelper.sleep(2);
	}
	
	public void verifyLinkOrbutton(WebElement element, String linkOption, String linkUrl, String newWindowTitle) {
		WebDriver driver = DM.getCurrentWebDriver();
		String expectedTarget = "New tab".equalsIgnoreCase(linkOption)?"_blank":("New window".equalsIgnoreCase(linkOption)?"":"_self");
		Verify.verifyEquals("Verfying Href text", element.getAttribute("href").contains(linkUrl));
		Verify.verifyEquals("Verfying Link Target Window", expectedTarget, element.getAttribute("target"));
		int windowsCountBefore = driver.getWindowHandles().size();
		String windowBefore = driver.getWindowHandle();
		element.click();
		Set<String> windows = driver.getWindowHandles();
		if("New tab".equalsIgnoreCase(linkOption) || "New window".equalsIgnoreCase(linkOption)) {
			Verify.verifyEquals("Opening in same tab", windowsCountBefore+1+"", windows.size()+"");
			for(String window : windows)
				driver.switchTo().window(window);
			Verify.verifyEquals("Verifying window title", newWindowTitle, driver.getTitle());
			driver.close();
			driver.switchTo().window(windowBefore);
		}else {
			Verify.verifyEquals("Opening in same tab", windowsCountBefore+"", windows.size()+"");
			Verify.verifyEquals("Verifying window title", newWindowTitle, driver.getTitle());
			driver.navigate().back();
		}
	}
	
	public static List<Map<String, String>> getDataMap(String iterations)
	{
		if(iterations.contains("("))
		{
			String num = iterations.replaceAll(".*\\(|\\).*", "").trim();
			String sheet = iterations.substring(0, iterations.lastIndexOf("("));
			int start = Integer.parseInt(num.split(",")[0].trim())-1;
			int end = Integer.parseInt(num.split(",")[1].trim());
			return ReadExcel.workbookData.get(sheet).subList(start, end);
		}
		else
			return ReadExcel.workbookData.get(iterations);			
	}

}
