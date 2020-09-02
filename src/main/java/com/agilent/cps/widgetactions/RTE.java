package com.agilent.cps.widgetactions;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.agilent.cps.widgets.WidgetInfo;

public class RTE extends GUIWidget implements IGUIWidget {

	public RTE(WidgetInfo widgetInfo) {
		super(widgetInfo);
	}
	
	@Override
	public void setDisplayValue(String value) {
		WebElement element = managerHelper.getWebElement(widgetInfo);
		Actions actions = new Actions(manager.getCurrentWebDriver());
		actions.click(element).sendKeys(value).build().perform();
	}

	@Override
	public String getDisplayValue() {
		WebElement element = managerHelper.getWebElement(widgetInfo);
		return element.getText().trim();
	}

}
