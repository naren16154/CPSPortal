package com.agilent.cps.author;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.agilent.cps.components.BaseComponent;
import com.agilent.cps.core.DriverManager;
import com.agilent.cps.core.DriverManagerHelper;
import com.agilent.cps.core.Verify;
import com.agilent.cps.utils.ReadExcel;
import com.agilent.cps.utils.ScreenShotUtility;
import com.agilent.cps.widgetactions.Button;
import com.agilent.cps.widgetactions.GUIWidget;
import com.agilent.cps.widgetactions.Link;
import com.agilent.cps.widgetactions.TextField;
import com.agilent.cps.widgets.WidgetInfo;

public class AuthorTests extends BaseAuthorTest{
	
	public static Map<String, List<Map<String, String>>> dataMap = new LinkedHashMap<String, List<Map<String, String>>>();
	BaseComponent componentObject = null;
	DriverManager DM = DriverManager.getInstance();
	
	public AuthorTests() {
		dataMap = (new ReadExcel()).getDataFromExcel("Authoring_TestData.xlsx");
	}
	
	@Test(groups= {"author"}, dataProvider = "getAuthorTestNames", dependsOnMethods = {"createPageForAuthorTests"})
	public void runAuthorTests(String component, String testName, int rowCount) throws Exception {
		Map<String, String> rowData = dataMap.get(component).get(rowCount);
		rowData.remove("TestName");
		componentObject=screenMap.get(component);
		if(null != componentObject)
		{
			String componentName = componentObject.getComponentName();
			DriverManagerHelper.sleep(1);
			DM.getCurrentWebDriver().findElement(By.xpath("//div[@data-text='Drag components here']")).click();
			DM.getCurrentWebDriver().findElements(By.xpath("//button[@icon='add']/coral-icon")).get(1).click();
			
			DM.textField(new WidgetInfo("xpath=//coral-search//input", TextField.class)).setDisplayValue(componentName);
			DriverManagerHelper.sleep(2);
			DM.getCurrentWebDriver().findElement(By.xpath("//coral-selectlist-group[@label='next-gen-acom-components.Content']//coral-selectlist-item[text()='"+componentName+"']")).click();
			DriverManagerHelper.sleep(2);
			WebElement componentElement = DM.getCurrentWebDriver().findElement(By.xpath("//div[@title='"+componentName+"']"));
			if(componentName.equalsIgnoreCase("Carousel")) {
				componentElement.click();
				DM.getCurrentWebDriver().findElement(By.xpath("//button[@title='Parent']/coral-icon")).click();
				DM.getCurrentWebDriver().findElement(By.xpath("//coral-list-item-content[text()='Carousel']")).click();
			}else
				componentElement.click();
			DM.getCurrentWebDriver().findElement(By.xpath("//button[@title='Configure']")).click();
			DriverManagerHelper.sleep(2);
			componentObject.populate(rowData);
			if(DM.widgetEnabled(new WidgetInfo("xpath=//button[@title='Done']", Button.class), 1, .5)) {
				DM.getCurrentWebDriver().findElement(By.xpath("//button[@title='Done']")).click();
				DriverManagerHelper.sleep(2);
			}
//			DM.getCurrentWebDriver().findElement(By.xpath("//button/coral-button-label[text()='Preview']")).click();
//			DriverManagerHelper.sleep(2);
//			DriverManagerHelper.getInstance().switchFrame(new WidgetInfo("id=ContentFrame", GUIWidget.class));
//			componentObject.verifyPreview(rowData);
//			ScreenShotUtility.getInstance().takeScreenshot();
//			DM.getCurrentWebDriver().switchTo().defaultContent();
//			DM.getCurrentWebDriver().findElement(By.xpath("//button[text()='Edit']")).click();
			
			String authoringWindow = DM.getCurrentWebDriver().getTitle();
			DM.getCurrentWebDriver().findElement(By.id("pageinfo-trigger")).click();
			DriverManagerHelper.sleep(2);
			DM.getCurrentWebDriver().findElement(By.xpath("//button[@title='View as Published']")).click();
			
			for(String window : DM.getCurrentWebDriver().getWindowHandles())
				DM.getCurrentWebDriver().switchTo().window(window);
			
			componentObject.verifyPreview(rowData);
			ScreenShotUtility.getInstance().takeScreenshot();
			
			DM.getCurrentWebDriver().close();
			
			DriverManagerHelper.getInstance().switchWindow(authoringWindow);
			
			DriverManagerHelper.sleep(2);
			componentElement = DM.getCurrentWebDriver().findElement(By.xpath("//div[@title='"+componentName+"']"));
			if(componentName.equalsIgnoreCase("Carousel") || componentName.equalsIgnoreCase("Accordion")) {
				componentElement.click();
				DM.getCurrentWebDriver().findElement(By.xpath("//button[@title='Parent']/coral-icon")).click();
				DM.getCurrentWebDriver().findElement(By.xpath("//coral-list-item-content[text()='"+componentName+"']")).click();
			}else
				componentElement.click();
			DM.getCurrentWebDriver().findElement(By.xpath("//button[@title='Delete']")).click();
			
			DM.getCurrentWebDriver().findElement(By.xpath("//button/coral-button-label[text()='Delete']")).click();
			Verify.softAssert.assertAll();
			DriverManagerHelper.sleep(2);
		}
		else
			throw new Exception("Unable to find screen object");
	}
	
	@Test(groups= {"author"}, priority = 0)
	public void createPageForAuthorTests() throws Exception {
		DM.GUIWidget(new WidgetInfo("xpath=//div[text()='Sites']", GUIWidget.class)).click();
		
		String pagePath = configProperties.getProperty("authorPagePath");
		String[] path = pagePath.split("/");
		
		for(int i=0; i<path.length; i++) {
			if(i != path.length-1) {
				DM.getCurrentWebDriver().findElement(By.xpath("//div[@title='"+path[i]+"']")).click();
			}else {
				DM.GUIWidget(new WidgetInfo("xpath=//button/coral-button-label[text()='Create']", GUIWidget.class)).click();
				DM.link(new WidgetInfo("linktext=Page", Link.class)).click();
				DM.GUIWidget(new WidgetInfo("xpath=//coral-card-title[text()='PEP Core Template']", GUIWidget.class)).click();
				DM.GUIWidget(new WidgetInfo("xpath=//button/coral-button-label[text()='Next']", GUIWidget.class)).click();
				DM.textField(new WidgetInfo("name=./jcr:title", TextField.class)).setDisplayValue(path[i]);
				DM.GUIWidget(new WidgetInfo("xpath=//button/coral-button-label[text()='Create']", GUIWidget.class)).click();
				DM.GUIWidget(new WidgetInfo("xpath=//button/coral-button-label[text()='Open']", GUIWidget.class)).click();
			}
		}
		
		for(int i=0; i<=6; i++) {
			DriverManagerHelper.sleep(5);
			if(DM.getCurrentWebDriver().getWindowHandles().size()==2)
				break;
		}
		for(String window : DM.getCurrentWebDriver().getWindowHandles())
			DM.getCurrentWebDriver().switchTo().window(window);
		
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
	
}
