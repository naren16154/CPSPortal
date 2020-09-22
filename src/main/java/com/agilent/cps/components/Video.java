package com.agilent.cps.components;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;

import com.agilent.cps.core.AutoPopulator;
import com.agilent.cps.core.DriverManagerHelper;
import com.agilent.cps.widgetactions.Button;
import com.agilent.cps.widgetactions.DropDown;
import com.agilent.cps.widgetactions.GUIWidget;
import com.agilent.cps.widgetactions.TextField;
import com.agilent.cps.widgets.WidgetInfo;

public class Video extends BaseComponent{

	public static final String componentName = "Video";
	
	@Override
	public void populate(Map<String, String> rowData) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		AutoPopulator.populate(this, rowData);
		DM.getCurrentWebDriver().findElement(By.xpath("//button[@title='Done']")).click();
		DriverManagerHelper.sleep(1);
		DM.button(new WidgetInfo("xpath=//button[@title='Toggle Side Panel']", Button.class)).click();
	}
	
	public static class Widgets {
		public static final WidgetInfo dropTarget = new WidgetInfo("xpath=//coral-fileupload[@name='./file']", GUIWidget.class);
		public static final WidgetInfo videoDrag = new WidgetInfo("xpath=//coral-card", GUIWidget.class);
		public static final WidgetInfo assetFilter = new WidgetInfo("xpath=//foundation-autocomplete[@name='assetfilter_image_path']//input", TextField.class);
		public static final WidgetInfo assetType = new WidgetInfo("name=assetfilter_type_selector", DropDown.class);
	}
	
	public void dropVideo(String data) {
		DM.getCurrentWebDriver().findElement(By.xpath("//button[@title='Done']")).click();
		DriverManagerHelper.sleep(1);
		DM.button(new WidgetInfo("xpath=//button[@title='Toggle Side Panel']", Button.class)).click();
		DM.textField(Widgets.assetFilter).setDisplayValue(data);
		DM.dropDown(Widgets.assetType).setDisplayValue("Videos");
		DM.getCurrentWebDriver().findElement(By.xpath("//div[@title='"+componentName+"']")).click();;
		DM.getCurrentWebDriver().findElement(By.xpath("//button[@title='Configure']")).click();
		Actions actions = new Actions(DM.getCurrentWebDriver());
		actions.dragAndDrop(DMHelper.getWebElement(Widgets.videoDrag), DMHelper.getWebElement(Widgets.dropTarget)).perform();
	}
	
	public void dropThumbnail(String data) {
		DM.getCurrentWebDriver().findElement(By.xpath("//button[@title='Done']")).click();
		DriverManagerHelper.sleep(1);
		DM.button(new WidgetInfo("xpath=//button[@title='Toggle Side Panel']", Button.class)).click();
		DM.getCurrentWebDriver().findElement(By.xpath("//div[@title='"+componentName+"']")).click();;
		DM.getCurrentWebDriver().findElement(By.xpath("//button[@title='Configure']")).click();
		DM.textField(Widgets.assetFilter).setDisplayValue(data);
		DM.dropDown(Widgets.assetType).setDisplayValue("Images");
		Actions actions = new Actions(DM.getCurrentWebDriver());
		actions.dragAndDrop(DMHelper.getWebElement(Widgets.videoDrag), DMHelper.getWebElements(Widgets.dropTarget).get(1)).perform();
	}
	
	@Override
	public void verifyPreview(Map<String, String> rowData) {
	}

	@Override
	public String getComponentName() {
		return componentName;
	}

}
