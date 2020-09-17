package com.agilent.cps.components;

import java.util.Map;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;

import com.agilent.cps.core.Verify;
import com.agilent.cps.widgetactions.DropDown;
import com.agilent.cps.widgetactions.GUIWidget;
import com.agilent.cps.widgetactions.Link;
import com.agilent.cps.widgetactions.ListBox;
import com.agilent.cps.widgetactions.TextField;
import com.agilent.cps.widgets.WidgetInfo;

public class Headline extends BaseComponent{
	
	public static final String componentName = "Headline";
	
	public static class Widgets{
		public static final WidgetInfo headlineText = new WidgetInfo("name=./headline", TextField.class);
		public static final WidgetInfo headlineLink = new WidgetInfo("name=./headlinelink", TextField.class);
		public static final WidgetInfo headlineLinkOption = new WidgetInfo("name=./headlinelinkoptions", DropDown.class);
		public static final WidgetInfo textStyle = new WidgetInfo("name=./textStyle", ListBox.class);
	}
	
	public void verifyPreview(Map<String, String> rowData) {
		if(rowData.containsKey("headlineLink")) {
			WebElement headlineLink = DMHelper.getWebElement(new WidgetInfo("linktext="+rowData.get("headlineText"), Link.class));
			Verify.verifyEquals("Verifying headline link color", "Blue", Color.fromString(headlineLink.getCssValue("color")).asHex());
			verifyTextFontSize("Verifying headline link font size", headlineLink, rowData.getOrDefault("textStyle", "H0"));
			verifyLinkOrbutton(headlineLink, rowData.getOrDefault("headlineLinkOption", "Existing window/tab"), rowData.get("headlineLink") , "Testing | Agilent");
		}else {
			WebElement headlineText = DMHelper.getWebElement(new WidgetInfo("xpath=//*[contains(text(),'"+rowData.get("headlineText")+"')]", GUIWidget.class));
			Verify.verifyEquals("Verifying headline text color", rowData.getOrDefault("selectStyle", "Black"), Color.fromString(headlineText.getCssValue("color")).asHex());
			verifyTextFontSize("Verifying headline text font size", headlineText, rowData.getOrDefault("textStyle", "H0"));
		}
	}

	@Override
	public String getComponentName() {
		return componentName;
	}

}
