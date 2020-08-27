package com.agilent.cps.author;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.agilent.cps.components.BaseComponent;
import com.agilent.cps.core.DriverManager;
import com.agilent.cps.core.DriverManagerHelper;
import com.agilent.cps.utils.ReadExcel;
import com.agilent.cps.utils.ScreenShotUtility;
import com.agilent.cps.widgetactions.Button;
import com.agilent.cps.widgetactions.GUIWidget;
import com.agilent.cps.widgetactions.TextField;
import com.agilent.cps.widgets.WidgetInfo;

public class AuthorTests extends BaseAuthorTest{
	
	public static Map<String, List<Map<String, String>>> dataMap = new LinkedHashMap<String, List<Map<String, String>>>();
	BaseComponent componentObject = null;
	DriverManager DM = DriverManager.getInstance();
	
	public AuthorTests() {
		dataMap = (new ReadExcel()).getDataFromExcel("Authoring_TestData.xlsx");
	}
	
	@Test(groups= {"author"}, dataProvider = "getAuthorTestNames")
	public void runAuthorTests(String component, String testName, int rowCount) throws Exception {
		Map<String, String> rowData = dataMap.get(component).get(rowCount);
		rowData.remove("TestName");
		componentObject=screenMap.get(component);
		if(null != componentObject)
		{
			String componentName = componentObject.getComponentName();
			DM.getCurrentWebDriver().findElement(By.xpath("//div[@data-text='Drag components here']")).click();
			DM.getCurrentWebDriver().findElements(By.xpath("//button[@icon='add']/coral-icon")).get(1).click();
			
			DM.textField(new WidgetInfo("xpath=//coral-search//input", TextField.class)).setDisplayValue(componentName);
			DriverManagerHelper.sleep(2);
			DM.getCurrentWebDriver().findElement(By.xpath("//coral-selectlist-item[text()='"+componentName+"']")).click();
			DriverManagerHelper.sleep(2);
			DM.getCurrentWebDriver().findElement(By.xpath("//div[@title='"+componentName+"']")).click();
			DM.getCurrentWebDriver().findElement(By.xpath("//button[@title='Configure']")).click();
			componentObject.populate(rowData);
			DM.getCurrentWebDriver().findElement(By.xpath("//button[@title='Done']")).click();
			
			DM.getCurrentWebDriver().findElement(By.xpath("//button/coral-button-label[text()='Preview']")).click();;
			ScreenShotUtility.getInstance().takeScreenshot();
			DM.getCurrentWebDriver().findElement(By.xpath("//button[text()='Edit']")).click();
			
			DriverManagerHelper.sleep(2);
			DM.getCurrentWebDriver().findElement(By.xpath("//div[@title='"+componentName+"']")).click();
			DM.getCurrentWebDriver().findElement(By.xpath("//button[@title='Delete']")).click();
			
			DM.getCurrentWebDriver().findElement(By.xpath("//button/coral-button-label[text()='Delete']")).click();
		}
		else
			throw new Exception("Unable to find screen object");
	}
	
	@Test(groups= {"author1"})
	public void sampleAuthorTest() throws InterruptedException {
		DriverManager DM = DriverManager.getInstance();
		browser.startBrowser("http://localhost:4502/");
		
		DM.textField(new WidgetInfo("id=username", TextField.class)).setDisplayValue("admin");
		DM.textField(new WidgetInfo("id=password", TextField.class)).setDisplayValue("admin");
		DM.button(new WidgetInfo("id=submit-button", Button.class)).click();
		DM.GUIWidget(new WidgetInfo("xpath=//div[text()='Sites']", GUIWidget.class)).click();
		
		String[] path = {"pathology-education","Language Masters","English","Testing"};
		
		for(int i=0; i<path.length; i++) {
			if(i != path.length-1) {
				DM.getCurrentWebDriver().findElement(By.xpath("//div[@title='"+path[i]+"']")).click();
			}else {
				DM.getCurrentWebDriver().findElement(By.xpath("//div[@title='"+path[i]+"']/../..//img")).click();
			}
		}
		
		DM.getCurrentWebDriver().findElement(By.xpath("//button/coral-button-label[contains(text(), 'Edit')]")).click();
		
		for(int i=0; i<=6; i++) {
			Thread.sleep(5000);
			if(DM.getCurrentWebDriver().getWindowHandles().size()==2)
				break;
		}
		for(String window : DM.getCurrentWebDriver().getWindowHandles())
			DM.getCurrentWebDriver().switchTo().window(window);
		
		DM.getCurrentWebDriver().findElement(By.xpath("//div[@data-text='Drag components here']")).click();
		DM.getCurrentWebDriver().findElements(By.xpath("//button[@icon='add']/coral-icon")).get(1).click();
		
		DM.textField(new WidgetInfo("xpath=//coral-search//input", TextField.class)).setDisplayValue("Headline");
		Thread.sleep(2000);
		DM.getCurrentWebDriver().findElement(By.xpath("//coral-selectlist-item[text()='Headline']")).click();
		
		DM.getCurrentWebDriver().findElement(By.xpath("//div[@title='Headline']")).click();
		DM.getCurrentWebDriver().findElement(By.xpath("//button[@title='Configure']")).click();
		
		DM.getCurrentWebDriver().findElement(By.xpath("//button[@title='Done']")).click();
		
		DM.getCurrentWebDriver().findElement(By.xpath("//div[@title='Headline']")).click();
		DM.getCurrentWebDriver().findElement(By.xpath("//button[@title='Delete']")).click();
		
		DM.getCurrentWebDriver().findElement(By.xpath("//button/coral-button-label[text()='Delete']")).click();
		
	}
	
	@DataProvider(name="getAuthorTestNames")
	public Object[][] getTestNames(){
		Map<String, String> testDetails = new LinkedHashMap<String, String>();
		for(Map<String, String> rowData : dataMap.get("TestCases")){
			String componentName = rowData.get("ComponentName");
			String fieldValue = rowData.get("tests");
			List<Map<String, String>> componentData = dataMap.get(componentName);
			if(fieldValue.contains("-")) {
				int start = Integer.parseInt(fieldValue.split("-")[0].trim())-1;
				int end = Integer.parseInt(fieldValue.split("-")[1].trim());
				int index = start;
				for(Map<String, String> componentRowData : componentData.subList(start, end)) {
					testDetails.put(componentRowData.get("TestName"), componentName+"#"+index);
					index++;
				}
			}else {
				int index = Integer.parseInt(fieldValue)-1;
				testDetails.put(componentData.get(index).get("TestName"), componentName+"#"+index);
			}
				
		}
		Object [][] array = new Object[testDetails.size()][3];
		int count = 0;
		for(String testName : testDetails.keySet()) {
			String value = testDetails.get(testName);
			array[count][0] = value.split("#")[0];
			array[count][1] = testName;
			array[count][2] = Integer.parseInt(value.split("#")[1]);
			count++;
		}
		
		return array;
	}
	
	public List<Map<String, String>> getDataMap(String iterations)
	{
		if(iterations.contains("("))
		{
			String num = iterations.replaceAll(".*\\(|\\).*", "").trim();
			String sheet = iterations.substring(0, iterations.lastIndexOf("("));
			int start = Integer.parseInt(num.split(",")[0].trim())-1;
			int end = Integer.parseInt(num.split(",")[1].trim());
			return dataMap.get(sheet).subList(start, end);
		}
		else
			return dataMap.get(iterations);			
	}
	
}
