package com.agilent.cps.components;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;

import com.agilent.cps.core.AutoPopulator;
import com.agilent.cps.core.DriverManagerHelper;
import com.agilent.cps.core.Verify;
import com.agilent.cps.widgetactions.Button;
import com.agilent.cps.widgetactions.CheckBox;
import com.agilent.cps.widgetactions.GUIWidget;
import com.agilent.cps.widgetactions.TextField;
import com.agilent.cps.widgets.WidgetInfo;

public class Image extends BaseComponent{

	public static final String componentName = "Image";
	
	@Override
	public void populate(Map<String, String> rowData) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		AutoPopulator.populate(this, rowData);
		DM.getCurrentWebDriver().findElement(By.xpath("//button[@title='Done']")).click();
		DriverManagerHelper.sleep(1);
		DM.button(new WidgetInfo("xpath=//button[@title='Toggle Side Panel']", Button.class)).click();
	}
	
	public static class Widgets {
		public static final WidgetInfo imageDrop = new WidgetInfo("name=./file", GUIWidget.class);
		public static final WidgetInfo imagedrag = new WidgetInfo("xpath=//coral-card", GUIWidget.class);
//		public static final WidgetInfo altText = new WidgetInfo("name=./alt", TextField.class);
		public static final WidgetInfo imageCaption = new WidgetInfo("xpath=//label[contains(text(),'Caption')]/../input[@name='./jcr:title']", TextField.class);
		public static final WidgetInfo captionAsPopup = new WidgetInfo("name=./displayPopupTitle", CheckBox.class);
	}
	
	public void dropImage(String data) {
		DM.getCurrentWebDriver().findElement(By.xpath("//button[@title='Done']")).click();
		DriverManagerHelper.sleep(1);
		DM.button(new WidgetInfo("xpath=//button[@title='Toggle Side Panel']", Button.class)).click();
		DM.textField(new WidgetInfo("xpath=//foundation-autocomplete[@name='assetfilter_image_path']//input", TextField.class)).setDisplayValue(data);
		DM.getCurrentWebDriver().findElement(By.xpath("//div[@title='"+componentName+"']")).click();;
		DM.getCurrentWebDriver().findElement(By.xpath("//button[@title='Configure']")).click();
		Actions actions = new Actions(DM.getCurrentWebDriver());
		actions.dragAndDrop(DMHelper.getWebElement(Widgets.imagedrag), DMHelper.getWebElement(Widgets.imageDrop)).perform();
	}
	
	@Override
	public void verifyPreview(Map<String, String> rowData) {
		WidgetInfo img = new WidgetInfo("xpath=//img[@data-mode='smartcrop']", GUIWidget.class);
		Verify.verifyEquals("Verifying presence of Image", DM.widgetVisible(img, 1, .5));
//		Verify.verifyEquals("Verifying Alt Text", rowData.get("altText"), DM.GUIWidget(img).getAttribute("alt"));
//		verifyImage("Verifying Image src", rowData.get("dropImage"), DM.GUIWidget(img).getAttribute("src"), true);
		if(rowData.getOrDefault("captionAsPopup", "CHECK").equalsIgnoreCase("CHECK"))
			Verify.verifyEquals("Verifying Image Caption", rowData.get("imageCaption"), DM.GUIWidget(new WidgetInfo("xpath=//meta[@itemprop='caption']", GUIWidget.class)).getAttribute("content"));
		else
			Verify.verifyEquals("Verifying Image Caption", rowData.get("imageCaption"), DM.GUIWidget(new WidgetInfo("xpath=//span[@itemprop='caption']", GUIWidget.class)).getText());
	}

	@Override
	public String getComponentName() {
		return componentName;
	}

}
