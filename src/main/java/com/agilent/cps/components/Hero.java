package com.agilent.cps.components;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import com.agilent.cps.core.AutoPopulator;
import com.agilent.cps.widgetactions.Button;
import com.agilent.cps.widgetactions.ListBox;
import com.agilent.cps.widgetactions.RTE;
import com.agilent.cps.widgetactions.TextField;
import com.agilent.cps.widgets.WidgetInfo;

public class Hero extends BaseComponent {
	
	public static final String componentName = "herocomponent";
	
	public static class Widgets{
		public static final WidgetInfo heroHeadlineText = new WidgetInfo("name=./headline", TextField.class);
		public static final WidgetInfo heroText = new WidgetInfo("xpath=//input[@name='./herotext']", RTE.class);
		public static final WidgetInfo backgroungImage = new WidgetInfo("name=./file", TextField.class);
		public static final WidgetInfo addButton = new WidgetInfo("xpath=//coral-button-label[text()='Add']", Button.class);
		public static final WidgetInfo buttonLink = new WidgetInfo("name=./cta/item0/./link", TextField.class);
		public static final WidgetInfo buttonText = new WidgetInfo("name=./cta/item0/./text", TextField.class);
		public static final WidgetInfo buttonLinkOpens = new WidgetInfo("name=./cta/item0/./openpage", ListBox.class);
		public static final WidgetInfo buttonColor = new WidgetInfo("name=./cta/item0/./ctacolor", ListBox.class);
		
	}
	
	public void populate(Map<String, String> rowData) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		AutoPopulator.populate(this, rowData);
	}

	@Override
	public String getComponentName() {
		return componentName;
	}

}
