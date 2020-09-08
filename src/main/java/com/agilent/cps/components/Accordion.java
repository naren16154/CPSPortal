package com.agilent.cps.components;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;

import com.agilent.cps.core.DriverManagerHelper;
import com.agilent.cps.core.Verify;
import com.agilent.cps.widgetactions.Button;
import com.agilent.cps.widgetactions.GUIWidget;
import com.agilent.cps.widgetactions.Link;
import com.agilent.cps.widgetactions.ListBox;
import com.agilent.cps.widgetactions.RadioGroup;
import com.agilent.cps.widgetactions.TextField;
import com.agilent.cps.widgets.WidgetInfo;

public class Accordion extends BaseComponent{

	public static final String componentName = "Accordion";
	
	public static class Widgets{
		public static final WidgetInfo expansionType = new WidgetInfo("xpath=//h3[text()='Expansion Type']/parent::div", RadioGroup.class);
		public static final WidgetInfo addButton = new WidgetInfo("xpath=//button/coral-button-label[text()='Add']", Button.class);
		public static final WidgetInfo headline1 = new WidgetInfo("name=./accordion/item0/./textbox", TextField.class);
		public static final WidgetInfo headline2 = new WidgetInfo("name=./accordion/item1/./textbox", TextField.class);
		public static final WidgetInfo headlineStyle = new WidgetInfo("name=./headingstyle", ListBox.class);
	}
	
	public void headlines(String data) {
		String[] headlines = data.split("\n");
		for(int i=0; i<headlines.length; i++) {
			DM.button(Widgets.addButton).click();
			WidgetInfo headlineTextField = new WidgetInfo("name=./accordion/item"+i+"/./textbox", TextField.class);
			DM.textField(headlineTextField).setDisplayValue(headlines[i]);
		}
	}
	
	public void addLinkList(String data) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		DM.getCurrentWebDriver().findElement(By.xpath("//button[@title='Done']")).click();
		DM.getCurrentWebDriver().navigate().refresh();
		List<Map<String, String>> linksData = getDataMap(data);
		LinkList linkList = new LinkList();
		for(int i=0; i< linksData.size(); i++) {
			List<WebElement> dragComponents = DM.getCurrentWebDriver().findElements(By.xpath("//div[@data-text='Drag components here']"));
			dragComponents.get(i).click();
			DM.getCurrentWebDriver().findElements(By.xpath("//button[@icon='add']/coral-icon")).get(1).click();
			
			DM.textField(new WidgetInfo("xpath=//coral-search//input", TextField.class)).setDisplayValue(linkList.getComponentName());
			DriverManagerHelper.sleep(2);
			DM.getCurrentWebDriver().findElement(By.xpath("//coral-selectlist-item[text()='"+linkList.getComponentName()+"']")).click();
			DriverManagerHelper.sleep(2);
			List<WebElement> linkListComponents = DM.getCurrentWebDriver().findElements(By.xpath("//div[@title='"+linkList.getComponentName()+"']"));
			linkListComponents.get(i).click();
			DM.getCurrentWebDriver().findElement(By.xpath("//button[@title='Configure']")).click();
			linkList.populate(linksData.get(i));
			DM.getCurrentWebDriver().findElement(By.xpath("//button[@title='Done']")).click();
			DriverManagerHelper.sleep(2);
		}
	}
	
	@Override
	public void verifyPreview(Map<String, String> rowData) {
		verifyAccordionHeadlinesTextAndStyle(rowData.get("headlines"), rowData.getOrDefault("headlineStyle", "H1"));
		verifyAccordionExpansionFeature(rowData.getOrDefault("expansionType", "Default State Open On Page Load"));
		verifyLinkLists(rowData.get("addLinkList"));
	}
	
	public void verifyAccordionExpansionFeature(String expansionType) {
		List<WebElement> accordionCards = DMHelper.getWebElements(new WidgetInfo("xpath=//div[@id='accordionBlock']/section/div", GUIWidget.class));
		if("Default State Open On Page Load".equals(expansionType)) {
			for(WebElement card : accordionCards) {
				Verify.verifyEquals("Verifying Accordion section is expanded", "card active".equals(card.getAttribute("class")));
			}
		}else {
			for(int i=0; i<accordionCards.size(); i++) {
				if(i != 0) {
					accordionCards.get(i).findElement(By.tagName("a")).click();
					DriverManagerHelper.sleep(2);
				}
				for(int j=0; j<accordionCards.size(); j++) {
					if(i == j)
						Verify.verifyEquals("Verifying that Accordion section is expanded", "card active".equals(accordionCards.get(j).getAttribute("class")));
					else
						Verify.verifyEquals("Verifying that Accordion section is collapsed", "card".equals(accordionCards.get(j).getAttribute("class")));
				}
			}
		}
	}
	
	public void verifyAccordionHeadlinesTextAndStyle(String headlines, String headlineStyle) {
		List<WebElement> accordionCards = DMHelper.getWebElements(new WidgetInfo("xpath=//div[@id='accordionBlock']/section/div", GUIWidget.class));
		String[] headlineArray = headlines.split("\n");
		for(int i=0; i<accordionCards.size(); i++) {
			Verify.verifyEquals("Verifying Headline Text & Style", headlineArray[i], accordionCards.get(i).findElement(By.id("heading"+i)).findElement(By.tagName(headlineStyle.toLowerCase())).getText());
		}
	}
	
	public void verifyLinkLists(String linkIterations) {
		List<WebElement> accordionCards = DMHelper.getWebElements(new WidgetInfo("xpath=//div[@id='accordionBlock']/section/div", GUIWidget.class));
		List<Map<String, String>> linksData = getDataMap(linkIterations);
		int iteration=0;
		for(WebElement card : accordionCards) {
			String iconType = linksData.get(iteration).getOrDefault("bulletIcon", "Standard Bullet");
			String textColor = linksData.get(iteration).getOrDefault("textColor", "Gray");
			String linkIterationDetails = linksData.get(iteration).get("addLinks");
			List<Map<String, String>> linksDetails = getDataMap(linkIterationDetails);
			if("card".equals(card.getAttribute("class")))
				card.click();
			WebElement linkList = card.findElement(By.tagName("ul"));
			List<WebElement> linkIcons = linkList.findElements(By.tagName("i"));
			for(int i=0; i<linkIcons.size(); i++) {
				Verify.verifyEquals("Verifying bullet list type", "Standard Bullet".equals(iconType)? "fa fa-circle": "fa fa-check", linkIcons.get(i).getAttribute("class"));
				verifyLinkFunctionality(textColor, linkIcons.get(i), linksDetails.get(i));
			}
			
			iteration++;
		}
	}

	private void verifyLinkFunctionality(String textColor, WebElement webElement, Map<String, String> linkDetails) {
		if(linkDetails.containsKey("headlineLink")) {
			WidgetInfo link = new WidgetInfo("linktext="+linkDetails.get("linkText"), Link.class);
			Verify.verifyEquals("Verfying Link Color", "", Color.fromString(DMHelper.getWebElement(link).getCssValue("color")).asHex());
			verifyLinkOrbutton(link, linkDetails.getOrDefault("linkAction", "Existing window/tab"), linkDetails.get("headlineLink"), "Testing");
		}else {
			WidgetInfo link = new WidgetInfo("xpath=//ul[contains(text(),'"+linkDetails.get("linkText")+"')]", GUIWidget.class);
			Verify.verifyEquals("Verfying Link Color", textColor, Color.fromString(DMHelper.getWebElement(link).getCssValue("color")).asHex());
		}
	}

	@Override
	public String getComponentName() {
		return componentName;
	}

}
