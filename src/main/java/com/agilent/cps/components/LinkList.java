package com.agilent.cps.components;

import java.util.List;
import java.util.Map;

import com.agilent.cps.core.AutoPopulator;
import com.agilent.cps.widgetactions.Button;
import com.agilent.cps.widgetactions.DropDown;
import com.agilent.cps.widgetactions.ListBox;
import com.agilent.cps.widgetactions.TextField;
import com.agilent.cps.widgets.WidgetInfo;

public class LinkList extends BaseComponent {

	public static final String componentName = "Link list";
	
	public static class Widgets{
		public static final WidgetInfo addButton = new WidgetInfo("xpath=//button/coral-button-label[text()='Add']", Button.class);
		public static final WidgetInfo textColor = new WidgetInfo("name=./textColor", ListBox.class);
		public static final WidgetInfo bulletIcon = new WidgetInfo("name=./bulletIcon", ListBox.class);
		public static final WidgetInfo textStyle = new WidgetInfo("name=./textStyle", ListBox.class);
	}
	
	public void addLinks(String data) {
		List<Map<String, String>> linksData = getDataMap(data);
		for(int i=0; i< linksData.size(); i++) {
			DM.button(Widgets.addButton).click();
			WidgetInfo linkText = new WidgetInfo("name=./linklist/item"+i+"/./linkText", TextField.class);
			WidgetInfo headlineLink = new WidgetInfo("name=./linklist/item"+i+"/./headlineLink", TextField.class);
			WidgetInfo linkAction = new WidgetInfo("name=./linklist/item"+i+"/./linkAction", DropDown.class);
			Map<String, String> dataToPopulate = linksData.get(i);
			AutoPopulator.populateWidget(linkText, "linkText", dataToPopulate.get("linkText"));
			if(dataToPopulate.containsKey("headlineLink"))
				AutoPopulator.populateWidget(headlineLink, "headlineLink", dataToPopulate.get("headlineLink"));
			if(dataToPopulate.containsKey("linkAction"))
				AutoPopulator.populateWidget(linkAction, "linkAction", dataToPopulate.get("linkAction"));
		}
	}
	
	@Override
	public void verifyPreview(Map<String, String> rowData) {
		
	}

	@Override
	public String getComponentName() {
		return componentName;
	}

}