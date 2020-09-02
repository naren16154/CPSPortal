package com.agilent.cps.components;

import java.util.Map;
import java.util.Set;

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
			Verify.verifyEquals("Headline link visible", DM.widgetVisible(headlineLink, 1, .5));
			Verify.verifyEquals("Verfying Href text", DM.link(headlineLink).getAttribute("href").contains(rowData.get("headlineLink")));
			int windowsCountBefore = DM.getCurrentWebDriver().getWindowHandles().size();
			DM.link(headlineLink).click();
			Set<String> windows = DM.getCurrentWebDriver().getWindowHandles();
			if(rowData.containsKey("headlineLinkOption")) {
				if("New tab".equalsIgnoreCase(rowData.get("headlineLinkOption"))) {
					Verify.verifyEquals("Verfying Link Target Window", "_blank", DM.link(headlineLink).getAttribute("target"));
					Verify.verifyEquals("Opening in same tab", windowsCountBefore+1+"", windows.size()+"");
				}
				else if("New window".equalsIgnoreCase(rowData.get("headlineLinkOption"))) {
					Verify.verifyEquals("Verfying Link Target Window", "", DM.link(headlineLink).getAttribute("target"));
					Verify.verifyEquals("Opening in same tab", windowsCountBefore+1+"", windows.size()+"");
				}
				else {
					Verify.verifyEquals("Verfying Link Target Window", "_self", DM.link(headlineLink).getAttribute("target"));
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
				Verify.verifyEquals("Verfying Link Target Window", "_self", DM.link(headlineLink).getAttribute("target"));
				Verify.verifyEquals("Opening in same tab", windowsCountBefore+"", windows.size()+"");
				Verify.verifyEquals("Verifying window title", "Testing", DM.getCurrentWebDriver().getTitle());
				DMHelper.getWebDriver().navigate().back();
			}
			
		}else {
			Verify.verifyEquals("Headline text visible", DM.widgetVisible(new WidgetInfo("xpath=//div[contains(text(),'"+rowData.get("headlineText")+"')]", GUIWidget.class), 1, .5));
		}
	}

	@Override
	public String getComponentName() {
		return componentName;
	}

}
