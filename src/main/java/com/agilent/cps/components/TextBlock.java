package com.agilent.cps.components;

import java.util.Map;

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
		String textColor = "Black";
		String textSize = "13px";
		if(rowData.containsKey("selectStyle")) {
			if(rowData.get("selectStyle").contains("White"))
				textColor="White";
			if(rowData.get("selectStyle").contains("Gray"))
				textColor="Gray";
			if(rowData.get("selectStyle").contains("para"))
				textSize="15px";
			if(rowData.get("selectStyle").contains("Small"))
				textSize="13px";
			if(rowData.get("selectStyle").contains("Label"))
				textSize="11px";
		}
		verifyColor("Verifying Textblock Text Color", textColor, DMHelper.getWebElement(rteTextPublish).getCssValue("color"));
		Verify.verifyEquals("Verifying Textblock Text size", textSize, DMHelper.getWebElement(rteTextPublish).getCssValue("font-size"));
	}

	@Override
	public String getComponentName() {
		return componentName;
	}

}
