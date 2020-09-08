package com.agilent.cps.components;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;

import com.agilent.cps.core.AutoPopulator;
import com.agilent.cps.core.Verify;
import com.agilent.cps.widgetactions.Button;
import com.agilent.cps.widgetactions.DropDown;
import com.agilent.cps.widgetactions.Link;
import com.agilent.cps.widgetactions.ListBox;
import com.agilent.cps.widgetactions.TextField;
import com.agilent.cps.widgets.WidgetInfo;

public class CTA extends BaseComponent {

	public static final String componentName = "Button Component";
	
	public static class Widgets{
		public static final WidgetInfo addButton = new WidgetInfo("xpath=//button/coral-button-label[text()='Add']", Button.class);
		public static final WidgetInfo buttonLabel = new WidgetInfo("name=./cta/item0/./ctatext", TextField.class);
		public static final WidgetInfo buttonLink = new WidgetInfo("name=./cta/item0/./link", TextField.class);
		public static final WidgetInfo linkActions = new WidgetInfo("name=./cta/item0/./linkaction", DropDown.class);
		public static final WidgetInfo iconPosition = new WidgetInfo("name=./cta/item0/./position", DropDown.class);
		public static final WidgetInfo buttonColor = new WidgetInfo("name=./cta/item0/./ctacolor", ListBox.class);
		public static final WidgetInfo buttontextColor = new WidgetInfo("name=./cta/item0/./ctatextcolor", ListBox.class);
	}
	
	public void populate(Map<String, String> rowData) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		DM.button(Widgets.addButton).click();
		AutoPopulator.populate(this, rowData);
	}
	
	@Override
	public void verifyPreview(Map<String, String> rowData) {
		WidgetInfo buttonLink = new WidgetInfo("linktext="+rowData.get("buttonLabel").toUpperCase(), Link.class);
		Verify.verifyEquals("Headline link visible", DM.widgetVisible(buttonLink, 1, .5));
		Verify.verifyEquals("Verfying Href text", DM.link(buttonLink).getAttribute("href").contains(rowData.get("buttonLink")));
		WebElement element = DMHelper.getWebElement(buttonLink);
		String buttonColor = rowData.getOrDefault("buttonColor", "Blue/Solid");
		if(buttonColor.equalsIgnoreCase("Transparent")) {
			Verify.verifyEquals("Verifying button text color", "blue", Color.fromString(element.getCssValue("color")).asHex());
			Verify.verifyEquals("Verifying button BG color", "white", Color.fromString(element.getCssValue("background-color")).asHex());
		}else if(buttonColor.equalsIgnoreCase("Text")) {
			Verify.verifyEquals("Verifying button text color", "blue", Color.fromString(element.getCssValue("color")).asHex());
			Verify.verifyEquals("Verifying button BG color", "white", Color.fromString(element.getCssValue("background-color")).asHex());
		}else {
			Verify.verifyEquals("Verifying button text color", "white", Color.fromString(element.getCssValue("color")).asHex());
			Verify.verifyEquals("Verifying button BG color", "blue", Color.fromString(element.getCssValue("background-color")).asHex());
		}
		int windowsCountBefore = DM.getCurrentWebDriver().getWindowHandles().size();
		DM.link(buttonLink).click();
		Set<String> windows = DM.getCurrentWebDriver().getWindowHandles();
		if(rowData.containsKey("linkActions")) {
			if("New tab".equalsIgnoreCase(rowData.get("linkActions"))) {
				Verify.verifyEquals("Verfying Link Target Window", "_blank", DM.link(buttonLink).getAttribute("target"));
				Verify.verifyEquals("Opening in same tab", windowsCountBefore+1+"", windows.size()+"");
			}
			else if("New window".equalsIgnoreCase(rowData.get("linkActions"))) {
				Verify.verifyEquals("Verfying Link Target Window", "", DM.link(buttonLink).getAttribute("target"));
				Verify.verifyEquals("Opening in same tab", windowsCountBefore+1+"", windows.size()+"");
			}
			else {
				Verify.verifyEquals("Verfying Link Target Window", "_self", DM.link(buttonLink).getAttribute("target"));
				Verify.verifyEquals("Opening in same tab", windowsCountBefore+"", windows.size()+"");
			}
			
			for(String window : windows)
				DM.getCurrentWebDriver().switchTo().window(window);
			Verify.verifyEquals("Verifying window title", "Testing", DM.getCurrentWebDriver().getTitle());
			DMHelper.getWebDriver().close();
			windows = DM.getCurrentWebDriver().getWindowHandles();
			for(String window : windows)
				DM.getCurrentWebDriver().switchTo().window(window);
		}else {
			Verify.verifyEquals("Verfying Link Target Window", "_self", DM.link(buttonLink).getAttribute("target"));
			Verify.verifyEquals("Opening in same tab", windowsCountBefore+"", windows.size()+"");
			Verify.verifyEquals("Verifying window title", "Testing", DM.getCurrentWebDriver().getTitle());
			DMHelper.getWebDriver().navigate().back();
		}
	}
	
	@Override
	public String getComponentName() {
		return componentName;
	}

}
