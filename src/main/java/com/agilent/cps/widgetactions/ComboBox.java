package com.agilent.cps.widgetactions;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.agilent.cps.core.DriverManagerHelper;
import com.agilent.cps.widgets.WidgetInfo;

public class ComboBox extends GUIWidget implements IGUIWidget {

	public ComboBox(WidgetInfo widgetInfo) {
		super(widgetInfo);
	}
	
	@Override
	public void setDisplayValue(String value) {
		WebElement element = managerHelper.getWebElement(widgetInfo);
		element.findElement(By.tagName("button")).click();
		String[] path = value.split("/");
		for(int i=1; i<path.length; i++) {
			if(i != path.length-1)
				manager.getCurrentWebDriver().findElement(By.xpath("//div[text()='"+path[i]+"']")).click();
			else
				manager.getCurrentWebDriver().findElement(By.xpath("//div[text()='"+path[i]+"']/../..//img")).click();
			DriverManagerHelper.sleep(1);
		}
		
		manager.getCurrentWebDriver().findElement(By.xpath("//button/coral-button-label[contains(text(), 'Select')]")).click();
	}

	@Override
	public String getDisplayValue() {
		List<WebElement> elements = managerHelper.getWebElements(widgetInfo);
		for(WebElement element : elements) {
			if(element.getTagName().equals("input"))
				return element.getAttribute("value").trim();
		}
		return null;
	}

}
