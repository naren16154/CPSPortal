package com.agilent.cps.widgetactions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.agilent.cps.widgets.WidgetInfo;

public class DropDown extends GUIWidget implements IGUIWidget {

	public DropDown(WidgetInfo widgetInfo) {
		super(widgetInfo);
	}
	
	@Override
	public void setDisplayValue(String value) {
		WebElement element = managerHelper.getWebElement(widgetInfo);
		if(!value.equalsIgnoreCase(element.getText())){
			element.click();
			element.findElement(By.xpath("//coral-selectlist-item[text()='"+value+"']")).click();
		}
	}

	@Override
	public String getDisplayValue() {
		WebElement element = managerHelper.getWebElement(widgetInfo);
		return element.getText().trim();
	}

}
