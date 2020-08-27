package com.agilent.cps.screens;

import com.agilent.cps.core.DriverManager;
import com.agilent.cps.core.DriverManagerHelper;
import com.agilent.cps.widgetactions.Link;
import com.agilent.cps.widgets.WidgetInfo;

public class BaseScreen {

	
	DriverManager DM = DriverManager.getInstance();
	DriverManagerHelper DMHelper = DriverManagerHelper.getInstance();
	
	public static class Widgets{
		public static final WidgetInfo traingEventsMenuLink = new WidgetInfo("id=MegaMenu-TrainingEvents", Link.class);
		public static final WidgetInfo webinarsLink = new WidgetInfo("id=MegaMenu-Webinars", Link.class);
		public static final WidgetInfo educationLink = new WidgetInfo("id=MegaMenu-Education", Link.class);
	}
	
	public void navigatToWebinarsPage() {
		
		DM.link(Widgets.traingEventsMenuLink).click();
		DM.link(Widgets.webinarsLink).click();
	}
	
	public void navigatToAgilentUniversityPage() {
		
		DM.link(Widgets.traingEventsMenuLink).click();
		DM.link(Widgets.educationLink).click();
	}
	
}
