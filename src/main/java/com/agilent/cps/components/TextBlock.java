package com.agilent.cps.components;

import java.util.Map;

import org.openqa.selenium.support.Color;

import com.agilent.cps.core.Verify;
import com.agilent.cps.widgetactions.Label;
import com.agilent.cps.widgetactions.RTE;
import com.agilent.cps.widgets.WidgetInfo;

public class TextBlock extends BaseComponent{

	public static final String componentName = "Text Block";
	
	public static class Widgets{
		public static final WidgetInfo rteText = new WidgetInfo("xpath=//div[@name='./text']/p", RTE.class);
	}
	
	@Override
	public void verifyPreview(Map<String, String> rowData) {
		WidgetInfo rteTextPublish = new WidgetInfo("xpath=//div[contains(@class, 'textblock')]", Label.class);
		
		Verify.verifyEquals("Verifying Textblock Content", rowData.get("rteText"), DM.label(rteTextPublish).getDisplayValue());
		
		if(rowData.containsKey("selectStyle")) {
			String expectedColor = "#000000";
			if(rowData.get("selectStyle").contains("White"))
				expectedColor="#ffffff";
			if(rowData.get("selectStyle").contains("Gray"))
				expectedColor="#535557";
			Verify.verifyEquals("Verifying Textblock Text Color", expectedColor, Color.fromString(DMHelper.getWebElement(rteTextPublish).getCssValue("color")).asHex());
		}else
			Verify.verifyEquals("Verifying Textblock Text Color", "#000000", Color.fromString(DMHelper.getWebElement(rteTextPublish).getCssValue("color")).asHex());
	}

	@Override
	public String getComponentName() {
		return componentName;
	}

}
