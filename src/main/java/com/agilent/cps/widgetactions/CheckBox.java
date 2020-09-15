package com.agilent.cps.widgetactions;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.agilent.cps.widgets.WidgetInfo;

import static com.agilent.cps.utils.Constants.*;

public class CheckBox extends GUIWidget implements IGUIWidget{

	public CheckBox(WidgetInfo widgetInfo) {
		super(widgetInfo);
	}

	@Override
	public void setDisplayValue(String value) {
		value = value.toUpperCase();
		WebElement element = managerHelper.getWebElement(widgetInfo);
		List<WebElement> elements = element.findElements(By.xpath("input"));
		for (WebElement ele : elements)
			element = ele;
		boolean selected = element.isSelected();
		if(selected ^ (!key_Uncheck.equals(value)))
			element.click();
	}

	@Override
	public String getDisplayValue() {
		WebElement element = managerHelper.getWebElement(widgetInfo);
		return String.valueOf(element.isSelected());
	}
	
	public void check()
	{
		setDisplayValue("check");
		logger.widgetAction("Widget : "+widgetInfo.getName()+" is checked");
	}
	
	public void unCheck()
	{
		setDisplayValue("uncheck");
		logger.widgetAction("Widget : "+widgetInfo.getName()+" is unchecked");
	}

}
