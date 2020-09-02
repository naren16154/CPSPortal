package com.agilent.cps.components;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.agilent.cps.core.DriverManagerHelper;
import com.agilent.cps.widgetactions.Button;
import com.agilent.cps.widgetactions.CheckBox;
import com.agilent.cps.widgetactions.GUIWidget;
import com.agilent.cps.widgetactions.TextField;
import com.agilent.cps.widgets.WidgetInfo;

public class Carousel extends BaseComponent{

	public static final String componentName = "Carousel";
	
	public static class Widgets{
		public static final WidgetInfo addButton = new WidgetInfo("xpath=//button/coral-button-label[text()='Add']", Button.class);
		public static final WidgetInfo autoSlide = new WidgetInfo("name=./autoplay", CheckBox.class);
		public static final WidgetInfo propertiesTab = new WidgetInfo("xpath=//coral-tab/coral-tab-label[text()='Properties']", GUIWidget.class);
	}
	
	public void addHeroComponents(String heroComponents) {
		String[] carouselTabs = heroComponents.split("\n");
		for(int i=0; i<carouselTabs.length; i++) {
			DM.button(Widgets.addButton).click();
			DM.textField(new WidgetInfo("//coral-search//input", TextField.class)).setDisplayValue("herocomponent");
			DM.GUIWidget(new WidgetInfo("xpath=//coral-selectlist-item[text()='herocomponent']", GUIWidget.class)).click();
			DriverManagerHelper.sleep(2);
		}
		List<WebElement> inputs = DM.getCurrentWebDriver().findElements(By.xpath("//input[contains(@name,'cq:panelTitle')]"));
		for(int i=0; i<inputs.size(); i++)
			inputs.get(i).sendKeys(carouselTabs[i]);
	}
	
	public void autoTransitionSlide(String autoSlide) {
		DM.GUIWidget(Widgets.propertiesTab).click();
		DM.checkBox(Widgets.autoSlide).setDisplayValue(autoSlide);
	}
	
	public void authorHerocomponents(String heroData) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		DriverManagerHelper.sleep(2);
		DM.getCurrentWebDriver().findElement(By.xpath("//button[@title='Done']")).click();
		for(Map<String, String> rowData : getDataMap(heroData)) {
			DM.getCurrentWebDriver().findElement(By.xpath("//div[@title=herocomponent']")).click();
			DM.getCurrentWebDriver().findElement(By.xpath("//button[@title='Configure']")).click();
			DriverManagerHelper.sleep(2);
			(new Hero()).populate(rowData);
			DriverManagerHelper.sleep(2);
			DM.getCurrentWebDriver().findElement(By.xpath("//button[@title='Done']")).click();
			DriverManagerHelper.sleep(2);
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
