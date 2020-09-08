package com.agilent.cps.components;

import java.util.Map;

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
			WidgetInfo headlineLink = new WidgetInfo("linktext="+rowData.get("headlineText"), Link.class);
			verifyLinkOrbutton(headlineLink, rowData.getOrDefault("headlineLinkOption", "Existing window/tab"), rowData.get("headlineLink") , "Testing");
		}else {
			Verify.verifyEquals("Headline text visible", DM.widgetVisible(new WidgetInfo("xpath=//div[contains(text(),'"+rowData.get("headlineText")+"')]", GUIWidget.class), 1, .5));
		}
	}

	@Override
	public String getComponentName() {
		return componentName;
	}

}
