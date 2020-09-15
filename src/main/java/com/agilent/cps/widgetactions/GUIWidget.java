package com.agilent.cps.widgetactions;

import org.openqa.selenium.WebElement;

import com.agilent.cps.widgets.WidgetInfo;

public class GUIWidget implements IGUIWidget{
	
	public static WidgetInfo widgetInfo = null;
	
	public GUIWidget(WidgetInfo widgetInfo)
	{
		GUIWidget.widgetInfo = widgetInfo;
	}
	
	public void setDisplayValue(String value)
	{
		WebElement element = managerHelper.getWebElement(widgetInfo);
		element.click();
	}
	
	public String getDisplayValue()
	{
		WebElement element = managerHelper.getWebElement(widgetInfo);
		return element.getText().trim();
	}
	
	public void click()
	{
		WebElement element = managerHelper.getWebElement(widgetInfo);
		element.click();
	}
	
	public void jsClick()
	{
		WebElement element = managerHelper.getWebElement(widgetInfo);
		manager.getJSExecutor().executeScript("arguments[0].click();", element);
	}
	
	public void clickJS(WebElement element)
	{
		manager.getJSExecutor().executeScript("arguments[0].click();", element);
	}
	
	public void click(WebElement element)
	{
		element.click();
	}
	
	public String getText()
	{
		WebElement element = managerHelper.getWebElement(widgetInfo);
		return element.getText();
	}
	public String getAttribute(String attributeName)
	{
		WebElement element = managerHelper.getWebElement(widgetInfo);
		return element.getAttribute(attributeName);
	}
	public String getCSSValue(String propertyName)
	{
		WebElement element = managerHelper.getWebElement(widgetInfo);
		return element.getCssValue(propertyName);
	}
}
