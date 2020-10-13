package com.agilent.cps.components;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.agilent.cps.core.DriverManagerHelper;
import com.agilent.cps.core.Verify;
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
		public static final WidgetInfo transitionDelay = new WidgetInfo("name=./delay", TextField.class);
		public static final WidgetInfo pauseOnHoverDisable = new WidgetInfo("name=./autopauseDisabled", CheckBox.class); 
	}
	
	public void addHeroComponents(String heroComponents) {
		String[] carouselTabs = heroComponents.split("\n");
		for(int i=0; i<carouselTabs.length; i++) {
			DM.button(Widgets.addButton).click();
			DriverManagerHelper.sleep(2);
			DM.textField(new WidgetInfo("xpath=//coral-search//input", TextField.class)).setDisplayValue("herocomponent");
			DriverManagerHelper.sleep(2);
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
		List<Map<String, String>> dataIterations = getDataMap(heroData);
		List<WebElement> heroComponents = DM.getCurrentWebDriver().findElements(By.xpath("//div[@title='herocomponent']"));
		for(int i=0; i<heroComponents.size(); i++) {
			DM.clickJS(heroComponents.get(0));
			DriverManagerHelper.sleep(.5);
			DM.getCurrentWebDriver().findElement(By.xpath("//button[@title='Configure']")).click();
			DriverManagerHelper.sleep(2);
			Map<String, String> rowData = dataIterations.get(i);
			rowData.remove("TestName");
			(new Hero()).populate(rowData);
			DriverManagerHelper.sleep(2);
			if(DM.widgetEnabled(new WidgetInfo("xpath=//button[@title='Done']", Button.class), 1, .5)) {
				DM.getCurrentWebDriver().findElement(By.xpath("//button[@title='Done']")).click();
				DriverManagerHelper.sleep(2);
			}
			heroComponents = DM.getCurrentWebDriver().findElements(By.xpath("//div[@title='herocomponent']"));
		}
	}
	
	@Override
	public void verifyPreview(Map<String, String> rowData) {
		verifyAutoSlidefeature(rowData);
		verifyCarousalList(rowData.get("addHeroComponents"));
		verifyHeroComponents(rowData);
	}
	
	public void verifyHeroComponents(Map<String, String> rowData) {
		List<Map<String, String>> dataMap = getDataMap(rowData.get("authorHerocomponents"));
		String[] heros = rowData.get("addHeroComponents").split("\n");
		for(int i=0; i<heros.length; i++) {
			DM.getJSExecutor().executeScript("window.scrollTo(0,0);");
			DM.getCurrentWebDriver().findElement(By.xpath("//li[contains(text(), '"+heros[i]+"')]")).click();
			DriverManagerHelper.sleep(2);
			Map<String, String> verifyData = dataMap.get(i);
			verifyData.remove("TestName");
			(new Hero()).verifyPreview(verifyData);
		}
	}

	public void verifyCarousalList(String carouselTabs) {
		WidgetInfo carousalHeroList = new WidgetInfo("xpath=//div[@id='carousel--hero']/ol[@class='carousel-indicators']/li", GUIWidget.class);
		List<WebElement> elementList = DMHelper.getWebElements(carousalHeroList);
		String[] heros = carouselTabs.split("\n");
		for(int i=0; i<heros.length; i++)
			Verify.verifyEquals("Verifying Carousel tab"+(i+1), heros[i], elementList.get(i).getText());
	}
	
	public void verifyAutoSlidefeature(Map<String, String> rowData) {
		String autoSlide =  rowData.get("autoTransitionSlide");
		if("check".equalsIgnoreCase(autoSlide)) {
			String transitionDelay = rowData.get("transitionDelay");
			String disableTransitionOnHover = rowData.getOrDefault("pauseOnHoverDisable", "uncheck");
			WidgetInfo carousalHeroList = new WidgetInfo("xpath=//div[@id='carousel--hero']/ol[@class='carousel-indicators']/li", GUIWidget.class);
			List<WebElement> elementList = DMHelper.getWebElements(carousalHeroList);
			WidgetInfo carousalActiveTab = new WidgetInfo("xpath=//div[@id='carousel--hero']/ol[@class='carousel-indicators']/li[@class='active']", GUIWidget.class);
			Actions actions = new Actions(DM.getCurrentWebDriver());
			actions.moveToElement(elementList.get(0)).perform();
			actions.moveToElement(DM.getCurrentWebDriver().findElement(By.linkText("AutomationTestingPage"))).perform();
			if("uncheck".equalsIgnoreCase(disableTransitionOnHover)) {
				String carouselTabBefore = DM.GUIWidget(carousalActiveTab).getText();
				DriverManagerHelper.sleep((Integer.parseInt(transitionDelay)/1000)+1);
				String carouselTabAfter = DM.GUIWidget(carousalActiveTab).getText();
				
				Verify.verifyEquals("Verifying Auto Transition Enabled without hover", !carouselTabBefore.equalsIgnoreCase(carouselTabAfter));
				
				actions.moveToElement(elementList.get(0)).perform();
				
				carouselTabBefore = DM.GUIWidget(carousalActiveTab).getText();
				DriverManagerHelper.sleep((Integer.parseInt(transitionDelay)/1000)+1);
				carouselTabAfter = DM.GUIWidget(carousalActiveTab).getText();
				
				Verify.verifyEquals("Verifying Auto Transition Disabled with hover", carouselTabBefore.equalsIgnoreCase(carouselTabAfter));
				
			}else {
				actions.moveToElement(elementList.get(0)).perform();
				
				String carouselTabBefore = DM.GUIWidget(carousalActiveTab).getText();
				DriverManagerHelper.sleep((Integer.parseInt(transitionDelay)/1000)+1);
				String carouselTabAfter = DM.GUIWidget(carousalActiveTab).getText();
				
				Verify.verifyEquals("Verifying Auto Transition Enabled Though We Hover", !carouselTabBefore.equalsIgnoreCase(carouselTabAfter));
			}
			
		}else {
			WidgetInfo carousalActiveTab = new WidgetInfo("xpath=//div[@id='carousel--hero']/ol[@class='carousel-indicators']/li[@class='active']", GUIWidget.class);
			String carouselTabBefore = DM.GUIWidget(carousalActiveTab).getText();
			DriverManagerHelper.sleep(6);
			String carouselTabAfter = DM.GUIWidget(carousalActiveTab).getText();
			
			Verify.verifyEquals("Verifying Auto Transition Disabled", carouselTabBefore.equalsIgnoreCase(carouselTabAfter));
		}
	}

	@Override
	public String getComponentName() {
		return componentName;
	}

}
