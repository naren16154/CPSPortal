package com.agilent.cps.components;

import java.util.Map;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;

import com.agilent.cps.core.Verify;
import com.agilent.cps.widgetactions.DropDown;
import com.agilent.cps.widgetactions.GUIWidget;
import com.agilent.cps.widgetactions.Link;
import com.agilent.cps.widgetactions.TextField;
import com.agilent.cps.widgets.WidgetInfo;

public class Headline extends BaseComponent{
	
	public static final String componentName = "Headline";
	
	public static class Widgets{
		public static final WidgetInfo headlineText = new WidgetInfo("name=./headline", TextField.class);
		public static final WidgetInfo headlineLink = new WidgetInfo("name=./headlinelink", TextField.class);
		public static final WidgetInfo headlineLinkOption = new WidgetInfo("name=./headlinelinkoptions", DropDown.class);
	}
	
	public void verifyPreview(Map<String, String> rowData) {
		if(rowData.containsKey("headlineLink")) {
			WebElement headlineLink = DMHelper.getWebElement(new WidgetInfo("linktext="+rowData.get("headlineText"), Link.class));
			verifyLinkOrbutton(headlineLink, rowData.getOrDefault("headlineLinkOption", "Existing window/tab"), rowData.get("headlineLink") , "Testing | Agilent");
			Verify.verifyEquals("Verifying headline text color", "#0085d5", Color.fromString(headlineLink.getCssValue("color")).asHex());
			Verify.verifyEquals("Verifying headline text font size", "", headlineLink.getCssValue("font-size"));
		}else {
			WidgetInfo headlineText = new WidgetInfo("xpath=//div[contains(text(),'"+rowData.get("headlineText")+"')]", GUIWidget.class);
			Verify.verifyEquals("Headline text visible", DM.widgetVisible(headlineText, 1, .5));
			Verify.verifyEquals("Verifying button text color", "#000000", Color.fromString(DMHelper.getWebElement(headlineText).getCssValue("color")).asHex());
			Verify.verifyEquals("Verifying headline text font size", "", DMHelper.getWebElement(headlineText).getCssValue("font-size"));
		}
	}

	@Override
	public String getComponentName() {
		return componentName;
	}

}
