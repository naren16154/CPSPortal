package com.agilent.cps.widgetactions;

import java.util.List;

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
			List<WebElement> selectItems = element.findElements(By.tagName("coral-selectlist-item"));
			for(WebElement item : selectItems) {
				if(item.getText().equals(value)) {
					item.click();
					break;
				}
			}
		}
	}

	@Override
	public String getDisplayValue() {
		WebElement element = managerHelper.getWebElement(widgetInfo);
		return element.getText().trim();
	}

}
