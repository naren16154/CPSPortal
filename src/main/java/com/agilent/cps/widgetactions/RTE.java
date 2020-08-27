package com.agilent.cps.widgetactions;

import org.openqa.selenium.WebElement;

import com.agilent.cps.widgets.WidgetInfo;

public class RTE extends GUIWidget implements IGUIWidget {

	public RTE(WidgetInfo widgetInfo) {
		super(widgetInfo);
	}
	
	@Override
	public void setDisplayValue(String value) {
		WebElement element = managerHelper.getWebElement(widgetInfo);
//		WebElement elementP = elements.get(elements.size()-1).findElement(By.tagName("p"));
		manager.getJSExecutor().executeScript("arguments[0].value = arguments[1]", element, value);
	}

	@Override
	public String getDisplayValue() {
		WebElement element = managerHelper.getWebElement(widgetInfo);
		return element.getText().trim();
	}

}
