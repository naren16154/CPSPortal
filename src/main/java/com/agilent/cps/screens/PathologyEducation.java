package com.agilent.cps.screens;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.agilent.cps.widgetactions.GUIWidget;
import com.agilent.cps.widgetactions.Label;
import com.agilent.cps.widgets.WidgetInfo;

public class PathologyEducation extends BaseScreen{
	
	public static class Widgets{
		
		//Hero Component
		public static final WidgetInfo carousalHeroList = new WidgetInfo("xpath=//div[@id='carousel--hero']/ol[@class='carousel-indicators']/li", GUIWidget.class);
		public static final WidgetInfo heroTitle = new WidgetInfo("xpath=//div[@id='carousel--hero']//div[@class='carousel-item active']//div[@class='text-section__title']", Label.class);
		public static final WidgetInfo heroDescription = new WidgetInfo("xpath=//div[@id='carousel--hero']//div[@class='carousel-item active']//div[@class='text-section__description']/p", Label.class);
		
		//Content Card Component
		public static final WidgetInfo contentCardsList = new WidgetInfo("xpath=//div[@class='cards-container featured']//div[contains(@class, 'mdc-card card-component')]", GUIWidget.class);
		
		//accordian
		public static final WidgetInfo accordianBlock = new WidgetInfo("id=accordionBlock", GUIWidget.class);
	}
	
	public String getCarousalList() {
		List<WebElement> elementList = DMHelper.getWebElements(Widgets.carousalHeroList);
		String heros = "";
		for(WebElement element : elementList)
			heros = heros+","+element.getText();
		
		heros = heros.equals("")?null:heros.substring(1);
		
		return heros;
	}
	
	public void verifyCarousalHerosContentDetails(String heros) {
		String[] heroComponents = heros.split(",");
		for(String hero : heroComponents) {
			System.out.println("******************* "+hero+" *******************");
			DM.getCurrentWebDriver().findElement(By.xpath("//li[contains(text(), '"+hero+"')]")).click();
			System.out.println(DM.GUIWidget(Widgets.heroTitle).getText());
			System.out.println(DM.GUIWidget(Widgets.heroDescription).getText());
		}
	}
	
	public void verifyContentCardsDetails() {
		List<WebElement> contentCardsList = DMHelper.getWebElements(Widgets.contentCardsList);
		Map<String, String> cardDetails = new LinkedHashMap<String, String>();
		
		for(WebElement contentCard : contentCardsList) {
			String tagName = contentCard.findElement(By.className("category-label")).getText();
			String textContent = contentCard.findElement(By.className("card__supporting-text")).getText();
			String title = null;
			if(contentCard.findElements(By.className("card__title")).size()>0)
				title = contentCard.findElement(By.className("card__title")).getText();
			cardDetails.put(tagName, title==null?textContent:title+"----"+textContent);
		}
		
		System.out.println(cardDetails);
	}
	
	public void verifyAccordianBlock() {
		List<String> contentList = new ArrayList<String>();
		List<WebElement> cards = DMHelper.getWebElement(Widgets.accordianBlock).findElements(By.xpath("//div[@class='card' or @class='card active']"));
		String classname0 = cards.get(0).findElement(By.id("collapse0")).getAttribute("class");
		String classname1 = cards.get(1).findElement(By.id("collapse1")).getAttribute("class");
		Assert.assertEquals(classname0, "collapse show", "Verifying is section expanded : ");
		Assert.assertEquals(classname1, "collapse", "Verifying is section collapsed : ");
		List<WebElement> list = cards.get(0).findElements(By.tagName("li"));
		for(WebElement listItem : list)
			contentList.add(listItem.getText());
		cards.get(1).findElement(By.id("heading1")).findElement(By.tagName("a")).click();
		classname0 = cards.get(0).findElement(By.id("collapse0")).getAttribute("class");
		classname1 = cards.get(1).findElement(By.id("collapse1")).getAttribute("class");
		Assert.assertEquals(classname1, "collapse show", "Verifying is section expanded : ");
		Assert.assertEquals(classname0, "collapse", "Verifying is section collapsed : ");
		
		list = cards.get(1).findElements(By.tagName("li"));
		for(WebElement listItem : list)
			contentList.add(listItem.getText());
		
		for(String individual : contentList)
			System.out.println(individual);
	}

}
