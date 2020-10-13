package com.agilent.cps.components;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

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
		WidgetInfo buttonLink = new WidgetInfo("linktext="+rowData.get("buttonLabel"), Link.class);
		WebElement element = DMHelper.getWebElement(buttonLink);
		String buttonColor = rowData.getOrDefault("buttonColor", "Blue/Solid");
		if(buttonColor.equalsIgnoreCase("Transparent")) {
			verifyColor("Verifying button text color", "Blue", element.getCssValue("color"));
			verifyColor("Verifying button BG color", "DefaultColor", element.getCssValue("background-color"));
			Verify.verifyEquals("Verifying that button has border", element.getCssValue("border").contains("1px solid"));
		}else if(buttonColor.equalsIgnoreCase("Text")) {
			verifyColor("Verifying button text color", "Blue", element.getCssValue("color"));
			verifyColor("Verifying button BG color", "DefaultColor", element.getCssValue("background-color"));
			Verify.verifyEquals("Verifying button does not has border", element.getCssValue("border").contains("0px none"));
		}else {
			verifyColor("Verifying button text color", "White", element.getCssValue("color"));
			verifyColor("Verifying button BG color", "Blue", element.getCssValue("background-color"));
			Verify.verifyEquals("Verifying button does not has border", element.getCssValue("border").contains("1px solid"));
		}
		verifyButtonStyle(element, rowData);
		verifyLinkOrbutton(element, rowData.getOrDefault("linkActions", "Existing window/tab"), rowData.get("buttonLink"), "AutomationTestingPage");
	}
	
	private void verifyButtonStyle(WebElement element, Map<String, String> rowData) {
		String buttonTextSize = "15px";
		String bottomPadding = "15px";
		String topPadding = "15px";
		String leftPadding = "30px";
		String rightPadding = "30px";
		String buttonColor = rowData.getOrDefault("buttonColor", "Blue/Solid");
		if(buttonColor.equalsIgnoreCase("Text")) {
			buttonTextSize = "18px";
			bottomPadding = "15px";
			topPadding = "15px";
			leftPadding = "10px";
			rightPadding = "10px";
		}
		if(rowData.containsKey("selectStyle")) {
			if("Small".equalsIgnoreCase(rowData.get("selectStyle"))) {
				buttonTextSize = "11px";
				bottomPadding = "7px";
				topPadding = "6px";
				leftPadding = "15px";
				rightPadding = "15px";
				if(buttonColor.equalsIgnoreCase("Text")) {
					buttonTextSize = "13px";
					bottomPadding = "7px";
					topPadding = "8px";
					leftPadding = "8px";
					rightPadding = "8px";
				}
			}
		}
		Verify.verifyEquals("Verifying button text size", buttonTextSize, element.findElement(By.tagName("span")).getCssValue("font-size"));
		Verify.verifyEquals("Verifying button bottom padding", bottomPadding, element.getCssValue("padding-bottom"));
		Verify.verifyEquals("Verifying button top padding", topPadding, element.getCssValue("padding-top"));
		Verify.verifyEquals("Verifying button left padding", leftPadding, element.getCssValue("padding-left"));
		Verify.verifyEquals("Verifying button right padding", rightPadding, element.getCssValue("padding-right"));
	}

	@Override
	public String getComponentName() {
		return componentName;
	}

}
