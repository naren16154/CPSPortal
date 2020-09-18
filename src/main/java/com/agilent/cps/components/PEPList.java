package com.agilent.cps.components;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.agilent.cps.core.AutoPopulator;
import com.agilent.cps.core.DriverManagerHelper;
import com.agilent.cps.core.Verify;
import com.agilent.cps.utils.Logger;
import com.agilent.cps.widgetactions.Button;
import com.agilent.cps.widgetactions.CheckBox;
import com.agilent.cps.widgetactions.ComboBox;
import com.agilent.cps.widgetactions.DropDown;
import com.agilent.cps.widgetactions.GUIWidget;
import com.agilent.cps.widgetactions.Label;
import com.agilent.cps.widgetactions.Link;
import com.agilent.cps.widgetactions.ListBox;
import com.agilent.cps.widgetactions.RTE;
import com.agilent.cps.widgetactions.RadioGroup;
import com.agilent.cps.widgetactions.TextField;
import com.agilent.cps.widgets.WidgetInfo;

public class PEPList extends BaseComponent {

	public static final String componentName = "PEP List";
	
	Logger logger = Logger.getInstance();
	
	public static class Widgets{
		public static final WidgetInfo layoutType = new WidgetInfo("xpath=//h3[text()='Layout Type']/parent::div", RadioGroup.class);
		public static final WidgetInfo gridOption = new WidgetInfo("name=./gridoption", RadioGroup.class);
		public static final WidgetInfo showBrandLines = new WidgetInfo("name=./showBrandLines", CheckBox.class);
		public static final WidgetInfo listSettingsTab = new WidgetInfo("xpath=//coral-tab/coral-tab-label[text()='List Settings']", GUIWidget.class);
		public static final WidgetInfo webinarTab = new WidgetInfo("xpath=//coral-tab/coral-tab-label[text()='Webinar']", GUIWidget.class);
		public static final WidgetInfo buildList = new WidgetInfo("name=./listFrom", DropDown.class);
		public static final WidgetInfo showFilters = new WidgetInfo("name=./filter", CheckBox.class);
		
//		Child Pages Options
		public static final WidgetInfo parentPage = new WidgetInfo("name=./parentPage", ComboBox.class);
		public static final WidgetInfo childDeapth = new WidgetInfo("name=./childDepth", TextField.class);
		public static final WidgetInfo orderByPublishDate = new WidgetInfo("name=./orderby", CheckBox.class);
		public static final WidgetInfo hideImage = new WidgetInfo("name=./hideimage", CheckBox.class);
		public static final WidgetInfo hideDescription = new WidgetInfo("name=./hidedescription", CheckBox.class);
		public static final WidgetInfo hideTags = new WidgetInfo("name=./hidecategorytags", CheckBox.class);
		public static final WidgetInfo hideEventDate = new WidgetInfo("name=./hideeventdate", CheckBox.class);
		public static final WidgetInfo hideEventPresenter = new WidgetInfo("name=./hideeventpresenter", CheckBox.class);
		
//		Fixed List Options
		public static final WidgetInfo addButton = new WidgetInfo("xpath=//button/coral-button-label[text()='Add']", Button.class);
		public static final WidgetInfo page = new WidgetInfo("name=./fixedlist/item%s/./pages", ComboBox.class);
		public static final WidgetInfo hideImageFixed = new WidgetInfo("name=./fixedlist/item%s/./hideimage", CheckBox.class);
		public static final WidgetInfo image = new WidgetInfo("name=./fixedlist/item%s/./images", ComboBox.class);
		public static final WidgetInfo hideDescriptionFixed = new WidgetInfo("name=./fixedlist/item%s/./hidedescription", CheckBox.class);
		public static final WidgetInfo description = new WidgetInfo("name=./fixedlist/item%s/./description", RTE.class);
		public static final WidgetInfo hideTagsFixed = new WidgetInfo("name=./fixedlist/item%s/./hidecategorytags", CheckBox.class);
		public static final WidgetInfo tags = new WidgetInfo("name=./fixedlist/item%s/./cattag", ComboBox.class);
		public static final WidgetInfo featureFlag = new WidgetInfo("name=./fixedlist/item%s/./flag", CheckBox.class);
		public static final WidgetInfo buttonText = new WidgetInfo("name=./fixedlist/item%s/./ctatext", TextField.class);
		public static final WidgetInfo buttonLink = new WidgetInfo("name=./fixedlist/item%s/./ctalink", TextField.class);
		public static final WidgetInfo buttonLinkAction = new WidgetInfo("name=./fixedlist/item%s/./ctalinkaction", DropDown.class);
		public static final WidgetInfo buttonIconPosition = new WidgetInfo("name=./fixedlist/item%s/./ctaposition", DropDown.class);
		
//		Webinar Page
		public static final WidgetInfo webinarAddButton = new WidgetInfo("xpath=//coral-multifield[@data-granite-coral-multifield-name='./webinarList']/button/coral-button-label[text()='Add']", Button.class);
		public static final WidgetInfo webinarPageSource = new WidgetInfo("name=./webinarList/item%s/./webPageSource", ComboBox.class);
		public static final WidgetInfo webinarHeadline = new WidgetInfo("name=./webinarList/item%s/./webHeadlineText", TextField.class);
		public static final WidgetInfo webinarHideTextArea = new WidgetInfo("name=./webinarList/item%s/./webHideTextArea", CheckBox.class);
		public static final WidgetInfo webinarTextArea = new WidgetInfo("name=./webinarList/item%s/./webTextArea", RTE.class);
		public static final WidgetInfo webinarHideTags = new WidgetInfo("name=./webinarList/item%s/./webHideContentTags", CheckBox.class);
		public static final WidgetInfo webinarTags = new WidgetInfo("name=./webinarList/item%s/./webContentTag", ComboBox.class);
		public static final WidgetInfo eventDate = new WidgetInfo("xpath=//coral-datepicker[@name='./webinarList/item%s/./webEventDate']/input", TextField.class);
		public static final WidgetInfo eventPresenterName = new WidgetInfo("name=./webinarList/item%s/./webEventPresenterName", TextField.class);
		public static final WidgetInfo eventPresenterTitle = new WidgetInfo("name=./webinarList/item%s/./webEventPresenterTitle", TextField.class);
		public static final WidgetInfo eventPresenterHeadshot = new WidgetInfo("name=./webinarList/item%s/./webEventPresenterHeadshot", ComboBox.class);
		
		public static final WidgetInfo ctaAddButton = new WidgetInfo("xpath=//label[text()='Button CTA']/..//button/coral-button-label[text()='Add']", Button.class);
		public static final WidgetInfo webinarButtonText = new WidgetInfo("name=./webinarList/item%s/./buttonCta/item%s/./buttonText", TextField.class);
		public static final WidgetInfo webinarButtonLink = new WidgetInfo("name=./webinarList/item%s/./buttonCta/item%s/./buttonSource", TextField.class);
		public static final WidgetInfo linkActions = new WidgetInfo("name=./webinarList/item%s/./buttonCta/item%s/./buttonSource", ListBox.class);
		public static final WidgetInfo buttonColor = new WidgetInfo("name=./webinarList/item%s/./buttonCta/item%s/./buttonColor", ListBox.class);
		public static final WidgetInfo webinarFilters = new WidgetInfo("name=./filterWebinar", CheckBox.class);
		
	}
	
	public void AddFixedList(String data) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		List<Map<String, String>> iterationsData = getDataMap(data);
		int iteration = 0;
		for(Map<String, String> rowdata : iterationsData) {
			DM.button(Widgets.addButton).click();
			DriverManagerHelper.sleep(1);
			AutoPopulator.populate(this, rowdata, iteration+"");
			DriverManagerHelper.sleep(1);
			iteration++;
		}
	}
	
	public void AddWebinars(String data) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		DM.GUIWidget(Widgets.webinarTab).click();
		DriverManagerHelper.sleep(1);
		List<Map<String, String>> iterationsData = getDataMap(data);
		int iteration = 0;
		for(Map<String, String> rowdata : iterationsData) {
			DM.button(Widgets.webinarAddButton).click();
			DriverManagerHelper.sleep(1);
			AutoPopulator.populate(this, rowdata, iteration+"");
			DriverManagerHelper.sleep(1);
			iteration++;
		}
	}
	
	public void AddChildPages(String data) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		List<Map<String, String>> iterationsData = getDataMap(data);
		int iteration = 0;
		for(Map<String, String> rowdata : iterationsData) 
			AutoPopulator.populate(this, rowdata, iteration+"");
	}
	
	public void addCTAButtons (String data) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		List<Map<String, String>> iterationsData = getDataMap(data);
		int webinarCardNumber = DMHelper.getWebElements(new WidgetInfo("xpath=//label[text()='Headline Text']", Label.class)).size();
		int iteration = 0;
		for(Map<String, String> rowdata : iterationsData) {
			List<WebElement> ctaAddButtons = DMHelper.getWebElements(Widgets.ctaAddButton);
			ctaAddButtons.get(ctaAddButtons.size()-1).click();
			DriverManagerHelper.sleep(1);
			AutoPopulator.populate(this, rowdata, webinarCardNumber-1+"" , iteration+"");
			DriverManagerHelper.sleep(1);
			iteration++;
		}
	}
	
	@Override
	public void verifyPreview(Map<String, String> rowData) throws ParseException {
		String buildList = rowData.getOrDefault("buildList", "Child pages");
		String layoutType = rowData.get("layoutType"); 
		String publishPage = DM.getCurrentWebDriver().getWindowHandle();
		DMHelper.switchWindow("AEM Sites");
		Map<String, Map<String, String>> propertiesData = new HashMap<String, Map<String,String>>();
		if("Child pages".equalsIgnoreCase(buildList)) {
			Map<String, String> listSettings = getDataMap(rowData.get("AddChildPages")).get(0);
			propertiesData.putAll(getChildpagesProperties(listSettings.get("parentPage"), layoutType));
		}else {
			List<Map<String, String>> iterationsData = new ArrayList<Map<String,String>>();
			String columnName;
			if(layoutType.contains("Webinar")) {
				iterationsData = getDataMap(rowData.get("AddWebinars"));
				columnName = "webinarPageSource";
			}else {
				iterationsData = getDataMap(rowData.get("AddFixedList"));
				columnName = "page";
			}
			for(Map<String, String> singlePageData : iterationsData)
				propertiesData.putAll(getPageProperties(singlePageData.get(columnName), layoutType));
		}
		System.out.println(propertiesData);
		
		DM.getCurrentWebDriver().switchTo().window(publishPage);
		
		verifyAllCardsProperties(layoutType, buildList, rowData, propertiesData);
		
	}

	private void verifyAllCardsProperties(String layoutType, String buildList, Map<String, String> rowData, Map<String, Map<String, String>> propertiesData) throws ParseException {
		int expectedCardsCount = propertiesData.size();
		int actualCardsCount = 0;
		WebDriver driver = DM.getCurrentWebDriver();
		
		List<Map<String, String>> overrideCardsData = "Child pages".equalsIgnoreCase(buildList)?getDataMap(rowData.get("AddChildPages")):(layoutType.contains("Webinar")?getDataMap(rowData.get("AddWebinars")):getDataMap(rowData.get("AddFixedList")));
		
		List<WebElement> cards = driver.findElements(By.xpath("//div[contains(@class,'cards-container')]//div[@class='mdc-card card-component']"));
		for(int i=0; i< cards.size(); i++) {
			Map<String, String> overrideCardData = "Child pages".equalsIgnoreCase(buildList)?overrideCardsData.get(0):overrideCardsData.get(actualCardsCount);
			verifyCardProperties(cards.get(i), layoutType, buildList, overrideCardData, propertiesData, actualCardsCount, false);
			if("Grid Text".equalsIgnoreCase(layoutType) || "Grid text with image".equalsIgnoreCase(layoutType))
				verifyPresenceOfBrandBar(cards.get(i), rowData.getOrDefault("showBrandLines", "uncheck"));
			verifyCTAButtonsFunctionality(cards.get(i), layoutType, buildList, overrideCardData, false);
			actualCardsCount++;
			cards = driver.findElements(By.xpath("//div[contains(@class,'cards-container')]//div[@class='mdc-card card-component']"));
		}
		
		if("Featured Card".equalsIgnoreCase(layoutType)) {
			List<WebElement> featuredCards = driver.findElements(By.xpath("//div[contains(@class,'cards-container')]/div[@class='col right']/div"));
			Verify.verifyEquals("Featured Cards Count", 1+"", featuredCards.size()+"");
			for(WebElement card : featuredCards) {
				Map<String, String> overrideCardData = "Child pages".equalsIgnoreCase(buildList)?overrideCardsData.get(0):overrideCardsData.get(actualCardsCount);
				verifyCardProperties(card, layoutType, buildList, overrideCardData, propertiesData, actualCardsCount, true);
				verifyCTAButtonsFunctionality(card,layoutType, buildList, overrideCardData, true);
				actualCardsCount++;
			}
		}
		if(!"Featured Card".equalsIgnoreCase(layoutType)) {
			String showFilters;
			if(layoutType.contains("Webinar"))
				showFilters = rowData.getOrDefault("webinarFilters", "uncheck");
			else
				showFilters = rowData.getOrDefault("showFilters", "uncheck");
			if("check".equalsIgnoreCase(showFilters))
				verifyFilterFunctionality(propertiesData, layoutType);
			else
				Verify.verifyEquals("Filters section should not display", DM.widgetNotExists(new WidgetInfo("xpath=//div[contains(@id,'mdc-chip')]", GUIWidget.class), 1, .5));
			
		}
		Verify.verifyEquals("Total Cards Count", expectedCardsCount+"", actualCardsCount+"");
	}

	private void verifyFilterFunctionality(Map<String, Map<String, String>> propertiesData, String layoutType) {
		WebDriver driver = DM.getCurrentWebDriver();
		Set<String> expectedTagsList = new HashSet<String>();
		Set<String> actualTagsList = new HashSet<String>();
		Map<String, List<Object>> cardTagsMap = new HashMap<String, List<Object>>();
		
		DM.getJSExecutor().executeScript("window.scrollTo(0,0);");
		
		for(String cardTitle : propertiesData.keySet()) {
			String[] tags = propertiesData.get(cardTitle).get("tags").split(",");
			for(String tag : tags)
				expectedTagsList.add(tag);
			if(layoutType.contains("Webinar"))
				cardTagsMap.put(propertiesData.get(cardTitle).get("headline"), Arrays.asList(tags));
			else
				cardTagsMap.put(propertiesData.get(cardTitle).get("pageTitle"), Arrays.asList(tags));
		}
		
		List<WebElement> filters = driver.findElements(By.xpath("//div[contains(@id,'mdc-chip')]"));
		for(WebElement filter : filters) {
			String tag = filter.getText().trim();
			if(actualTagsList.contains(tag))
				Verify.verifyEquals("Filter '"+tag+"' displayed more than one time in filter section", false);
			actualTagsList.add(tag);
		}
		verifyTagsList(expectedTagsList, actualTagsList);
		Set<String> selectedFilters = new HashSet<String>();
		for(int i=0; i< filters.size(); i++) {
			selectedFilters.add(filters.get(i).getText().trim());
			filters.get(i).click();
			
			verifyCardsAfterFilterApplied(selectedFilters, cardTagsMap);
		}
		
		for(int i=0; i< filters.size(); i++) {
			driver.findElement(By.xpath("//div[@id='"+filters.get(i).getAttribute("id")+"']/div[text()='close']")).click();
			selectedFilters.remove(filters.get(i).getText().trim());

			verifyCardsAfterFilterApplied(selectedFilters, cardTagsMap);
		}
		
	}

	private void verifyCardsAfterFilterApplied(Set<String> selectedFilters, Map<String, List<Object>> cardTagsMap) {
		Set<String> expectedCards = new HashSet<String>();
		Set<String> actualCards = new HashSet<String>();
		
		List<WebElement> cards = DM.getCurrentWebDriver().findElements(By.xpath("//div[contains(@class,'cards-container')]//div[@class='mdc-card card-component']/.."));
		for(WebElement card : cards) {
			if(!card.getCssValue("display").equalsIgnoreCase("none")) {
				String cardTitle = card.findElement(By.className("card__title")).getText();
				if(actualCards.contains(cardTitle))
					Verify.verifyEquals("With same title '"+cardTitle+"' more than one card displayed", false);
				actualCards.add(cardTitle);
			}
		}
		
		for(String cardTitle : cardTagsMap.keySet()) {
			List<Object> tags = cardTagsMap.get(cardTitle);
			for(String selectedFilter : selectedFilters) {
				if(tags.contains(selectedFilter)) {
					expectedCards.add(cardTitle);
					break;
				}
			}
			if(selectedFilters.size()==0)
				expectedCards.add(cardTitle);
		}
		logger.info("Filters Applied : "+selectedFilters);
		verifyFilteredCardsList(expectedCards, actualCards);
		
	}
	
	private void verifyFilteredCardsList(Set<String> expectedCards, Set<String> actualCards) {
		logger.debug("Expected Card Titles : "+expectedCards);
		logger.debug("Actual Card Titles : "+actualCards);
		boolean isSuccess = true; 
		for(String expectedCard : expectedCards) {
			if(actualCards.contains(expectedCard))
				actualCards.remove(expectedCard);
			else {
				Verify.verifyEquals("Card with title '"+expectedCard+"' does not diaplay after filter applied", false);
				isSuccess = false;
			}
		}
		for(String extraCard : actualCards) {
			Verify.verifyEquals("Extra Card with title '"+extraCard+"' diaplayed after filter applied", false);
			isSuccess = false;
		}
		
		if(isSuccess)
			Verify.verifyEquals("After filter applied cards are filtered as expected", true);
	}

	private void verifyTagsList(Set<String> expectedTagsList, Set<String> actualTagsList) {
		logger.info("Expected Filter Options : "+expectedTagsList);
		logger.info("Actual Filter Options : "+actualTagsList);
		boolean isSuccess = true; 
		for(String expectedTag : expectedTagsList) {
			if(actualTagsList.contains(expectedTag))
				actualTagsList.remove(expectedTag);
			else {
				Verify.verifyEquals("Tag '"+expectedTag+"' does not diaplay as filter option", false);
				isSuccess = false;
			}
		}
		for(String extraTag : actualTagsList) {
			Verify.verifyEquals("Extra Tag '"+extraTag+"' diaplayed as filter option", false);
			isSuccess = false;
		}
		if(isSuccess)
			Verify.verifyEquals("Expected & Actual Filters options are matching", true);
	}

	private void verifyCTAButtonsFunctionality(WebElement cardElement, String layoutType, String buildList, Map<String, String> overrideCardData, boolean isFeatured) {
		WebDriver driver = DM.getCurrentWebDriver();
		String title = "";
		
		if("Video Playlist".equalsIgnoreCase(layoutType)) {
			title = cardElement.findElement(By.className("card__title")).getText();
			WebElement link = cardElement.findElement(By.tagName("a"));
			Verify.verifyEquals("Play button should display", link.findElements(By.className("fa-play-circle")).size()==1);
			link.click();
			Verify.verifyEquals("Screen Title", title, driver.getTitle());
			driver.navigate().back();
		}else if("Child pages".equalsIgnoreCase(buildList)) {
			if("Featured Card".equalsIgnoreCase(layoutType) && !isFeatured)
				title = cardElement.findElement(By.className("card__supporting-text")).getText();
			else
				title = cardElement.findElement(By.className("card__title")).getText();
			cardElement.findElement(By.linkText("View More")).click();
			Verify.verifyEquals("Button Link Title", title, driver.getTitle());
			driver.navigate().back();
		}else {
			if(layoutType.contains("Webinar")) {
				List<Map<String, String>> iterationsData = new ArrayList<Map<String,String>>();
				if(overrideCardData.containsKey("addCTAButtons"))
					iterationsData = getDataMap(overrideCardData.get("addCTAButtons"));
				for(Map<String, String> rowData : iterationsData)
					verifyLinkOrbutton(cardElement.findElement(By.linkText(rowData.get("webinarButtonText"))), rowData.get("linkActions"), rowData.get("webinarButtonLink"), title);
			}else if(overrideCardData.containsKey("buttonLink"))
				verifyLinkOrbutton(cardElement.findElement(By.linkText(overrideCardData.get("buttonText"))), overrideCardData.get("buttonLinkAction"), overrideCardData.get("buttonLink"), title);
		}
	}

	private void verifyPresenceOfBrandBar(WebElement card, String brandBar) {
		List<WebElement> brandBarElements = card.findElements(By.xpath("parent::div/div[contains(@class,'brand-lines')]"));
		if("check".equalsIgnoreCase(brandBar)) {
			if(brandBarElements.size()==0)
				Verify.verifyEquals("Brandbar should display", false);
			else
				Verify.verifyEquals("Brandbar should display", true);
		}else {
			if(brandBarElements.size()==0)
				Verify.verifyEquals("Brandbar should not display", true);
			else
				Verify.verifyEquals("Brandbar should not display", false);
		}
	}

	private void verifyCardProperties(WebElement card, String layoutType, String buildList, Map<String, String> overrideCardData,
			Map<String, Map<String, String>> propertiesData, int cardIndex, boolean isFeaturedCard) throws ParseException {
		
		Boolean hideImage = false;
		Boolean hideDescription = false;
		Boolean hideTags = false;
		
		String overrideImage = "";
		String overrideDescription = "";
		String overrideTags = "";
		
		if("Fixed list".equalsIgnoreCase(buildList)) {
			if(layoutType.contains("Webinar")) {
				hideDescription = "check".equalsIgnoreCase(overrideCardData.getOrDefault("webinarHideTextArea", "uncheck"))?true:false;
				hideTags = "check".equalsIgnoreCase(overrideCardData.getOrDefault("webinarHideTags", "uncheck"))?true:false;
				overrideDescription = overrideCardData.getOrDefault("webinarTextArea", "");
				overrideTags = overrideCardData.getOrDefault("webinarTags", "");
			}else {
				hideImage = "check".equalsIgnoreCase(overrideCardData.getOrDefault("hideImageFixed", "uncheck"))?true:false;
				hideDescription = "check".equalsIgnoreCase(overrideCardData.getOrDefault("hideDescriptionFixed", "uncheck"))?true:false;
				hideTags = "check".equalsIgnoreCase(overrideCardData.getOrDefault("hideTagsFixed", "uncheck"))?true:false;
				overrideImage = overrideCardData.getOrDefault("image", "");
				overrideDescription = overrideCardData.getOrDefault("description", "");
				overrideTags = overrideCardData.getOrDefault("tags", "");
			}
		}else {
			hideImage = "check".equalsIgnoreCase(overrideCardData.getOrDefault("hideImage", "uncheck"))?true:false;
			hideDescription = "check".equalsIgnoreCase(overrideCardData.getOrDefault("hideDescription", "uncheck"))?true:false;
			hideTags = "check".equalsIgnoreCase(overrideCardData.getOrDefault("hideTags", "uncheck"))?true:false;
		}
		
		Map<String, String> actualCardDetails = new HashMap<String, String>();
		
		String cardXpathText = null;
		if(isFeaturedCard)
			cardXpathText = "//div[contains(@class,'cards-container')]/div[@class='col right']/div[1]";
		else
			cardXpathText = "(//div[contains(@class,'cards-container')]//div[@class='mdc-card card-component'])["+(cardIndex+1)+"]";
		
		WidgetInfo image = new WidgetInfo("xpath="+cardXpathText+"//img", GUIWidget.class);
		WidgetInfo pageTitle = new WidgetInfo("xpath="+cardXpathText+"//div[@class='card__title']", GUIWidget.class);
		WidgetInfo description = new WidgetInfo("xpath="+cardXpathText+"//div[@class='card__supporting-text']", GUIWidget.class);
		WidgetInfo tags = new WidgetInfo("xpath="+cardXpathText+"//div[starts-with(@class,'tag')]", GUIWidget.class);
		WidgetInfo overlayText = new WidgetInfo("xpath="+cardXpathText+"//div[@class='category-label']", GUIWidget.class);
		WidgetInfo eventDate = new WidgetInfo("xpath="+cardXpathText+"//div[@class='date']", GUIWidget.class);
		WidgetInfo eventPresenterName = new WidgetInfo("xpath="+cardXpathText+"//div[@class='author']", GUIWidget.class);
		WidgetInfo eventPresenterNameArchive = new WidgetInfo("xpath="+cardXpathText+"//div[@class='auther']", GUIWidget.class);
		WidgetInfo eventPresenterHeadshot = new WidgetInfo("xpath="+cardXpathText+"//img", GUIWidget.class);
		WidgetInfo eventPresenterTitle = new WidgetInfo("xpath="+cardXpathText+"//figcaption", GUIWidget.class);
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dfactual = new SimpleDateFormat("dd MMM yyyy");
		
		switch (layoutType) {
			case "Grid Text":
				actualCardDetails.put("pageTitle", DM.GUIWidget(pageTitle).getDisplayValue());
				if(hideImage)
					Verify.verifyEquals("Image should not visible", !DM.widgetVisible(image, 1, .5));
				else
					Verify.verifyEquals("Image should not display", DMHelper.getWebElement(image).findElement(By.xpath("..")).getCssValue("display").equalsIgnoreCase("none"));
				if(hideDescription)
					Verify.verifyEquals("Description should not visible", !DM.widgetVisible(description, 1, .5));
				else
					actualCardDetails.put("description", DM.GUIWidget(description).getDisplayValue());
				if(hideTags)
					Verify.verifyEquals("Tags should not visible", !DM.widgetVisible(tags, 1, .5));
				else
					actualCardDetails.put("tags", DM.GUIWidget(tags).getDisplayValue().replaceAll("\n", ","));
				break;
	
			case "Grid text with image":
				actualCardDetails.put("pageTitle", DM.GUIWidget(pageTitle).getDisplayValue());
				if(hideImage)
					Verify.verifyEquals("Image should not visible", !DM.widgetVisible(image, 1, .5));
				else
					actualCardDetails.put("image", DM.GUIWidget(image).getAttribute("src"));
				if(hideDescription)
					Verify.verifyEquals("Description should not visible", !DM.widgetVisible(description, 1, .5));
				else
					actualCardDetails.put("description", DM.GUIWidget(description).getDisplayValue());
				if(hideTags)
					Verify.verifyEquals("Tags should not visible", !DM.widgetVisible(tags, 1, .5));
				else
					actualCardDetails.put("tags", DM.GUIWidget(tags).getDisplayValue().replaceAll("\n", ","));
				break;
				
			case "Webinar Card 1 Column":
				actualCardDetails.put("headline", DM.GUIWidget(pageTitle).getDisplayValue());
				actualCardDetails.put("eventDate", df.format(dfactual.parse(DM.GUIWidget(eventDate).getDisplayValue().substring(6))));
				actualCardDetails.put("eventPresenterName", DM.GUIWidget(eventPresenterName).getDisplayValue().substring(11));
				actualCardDetails.put("eventPresenterTitle", DM.GUIWidget(eventPresenterTitle).getDisplayValue());
				actualCardDetails.put("eventPresenterHeadshot", DM.GUIWidget(eventPresenterHeadshot).getAttribute("src"));
				if(hideDescription)
					Verify.verifyEquals("Description should not visible", !DM.widgetVisible(description, 1, .5));
				else
					actualCardDetails.put("description", DM.GUIWidget(description).getDisplayValue());
				if(hideTags)
					Verify.verifyEquals("Tags should not visible", !DM.widgetVisible(tags, 1, .5));
				else {
					WebElement tagElement = DMHelper.getWebElement(tags);
					List<WebElement> spans = tagElement.findElements(By.tagName("span"));
					String tagsList = "";
					for(WebElement span : spans)
						tagsList += ","+span.getText().trim();
					actualCardDetails.put("tags", tagsList.substring(1));
				}
				break;
				
			case "Webinar Archive":
				actualCardDetails.put("headline", DM.GUIWidget(pageTitle).getDisplayValue());
				actualCardDetails.put("eventDate", df.format(dfactual.parse(DM.GUIWidget(eventDate).getDisplayValue().substring(6))));
				actualCardDetails.put("eventPresenterName", DM.GUIWidget(eventPresenterNameArchive).getDisplayValue().substring(11));
				break;
			
			case "Video Playlist":
				actualCardDetails.put("image", DM.GUIWidget(image).getAttribute("src"));
				actualCardDetails.put("pageTitle", DM.GUIWidget(pageTitle).getDisplayValue());
				if(hideDescription)
					Verify.verifyEquals("Description should not visible", !DM.widgetVisible(description, 1, .5));
				else
					actualCardDetails.put("description", DM.GUIWidget(description).getDisplayValue());
				break;
				
			default:
				if(isFeaturedCard) {
					actualCardDetails.put("pageTitle", DM.GUIWidget(pageTitle).getDisplayValue());
					actualCardDetails.put("featuredFlag", "true");
				}else
					actualCardDetails.put("featuredFlag", "false");
				if(hideImage)
					Verify.verifyEquals("Image should not visible", !DM.widgetVisible(image, 1, .5));
				else
					actualCardDetails.put("image", DM.GUIWidget(image).getAttribute("src"));
				if(hideDescription)
					Verify.verifyEquals("Description should not visible", !DM.widgetVisible(description, 1, .5));
				else if(isFeaturedCard)
					actualCardDetails.put("description", DM.GUIWidget(description).getDisplayValue());
				else
					actualCardDetails.put("pageTitle", DM.GUIWidget(description).getDisplayValue());
				if(hideTags)
					Verify.verifyEquals("Tags should not visible", !DM.widgetVisible(overlayText, 1, .5));
				else
					actualCardDetails.put("tags", DM.GUIWidget(overlayText).getDisplayValue().replaceAll("\n", ","));
				break;
		}
		
		Map<String, String> cardProperties = getCardProperties(propertiesData, actualCardDetails, layoutType);
		
		for(String attribute : actualCardDetails.keySet()){
			switch (attribute) {
			case "image":
				if(hideImage & !"".equals(overrideImage))
					Verify.verifyEquals("Verifying Card Content for "+attribute, cardProperties.get(attribute), overrideImage);
				else
					Verify.verifyEquals("Verifying Card Content for "+attribute, cardProperties.get(attribute), actualCardDetails.get(attribute));
				break;
			case "description":
				if(hideDescription & !"".equals(overrideDescription))
					Verify.verifyEquals("Verifying Card Content for "+attribute, cardProperties.get(attribute), overrideImage);
				else
					Verify.verifyEquals("Verifying Card Content for "+attribute, cardProperties.get(attribute), actualCardDetails.get(attribute));			
				break;
			case "tags":
				if(hideTags & !"".equals(overrideTags))
					Verify.verifyEquals("Verifying Card Content for "+attribute, cardProperties.get(attribute), overrideTags);
				else
					Verify.verifyEquals("Verifying Card Content for "+attribute, cardProperties.get(attribute), actualCardDetails.get(attribute));
				break;
			default:
				Verify.verifyEquals("Verifying Card Content for "+attribute, cardProperties.get(attribute), actualCardDetails.get(attribute));
				break;
			}
		}
		
	}

	private Map<String, String> getCardProperties(Map<String, Map<String, String>> propertiesData, Map<String, String> actualCardDetails, String layoutType) {
		Map<String, String> cardProperties = new HashMap<String, String>();
		String keyName = "pageTitle";
		if(layoutType.contains("Webinar"))
			keyName = "headline";
			
		for(String title : propertiesData.keySet()) {
			cardProperties = propertiesData.get(title);
			if(cardProperties.get(keyName).equalsIgnoreCase(actualCardDetails.get(keyName)))
				break;
		}
		return cardProperties;
	}

	private Map<String, Map<String, String>> getPageProperties(String page, String layoutType) {
		String[] path = page.split("/");
		navigateToPage(path);
		List<WebElement> elements = DM.getCurrentWebDriver().findElements(By.xpath("//coral-columnview-item-content/div[contains(text(),'"+path[path.length-1]+"')]"));
		elements.get(0).findElement(By.xpath("../..//img")).click();
		Map<String, Map<String, String>> propertiesData = new HashMap<String, Map<String,String>>();
		
		propertiesData.putAll(readProperties(layoutType));
		
		return propertiesData;
	}

	private Map<String, Map<String, String>> getChildpagesProperties(String parentPage, String layoutType) {
		Map<String, Map<String, String>> propertiesData = new HashMap<String, Map<String,String>>();
		String[] path = parentPage.split("/");
		navigateToPage(path);
		WebDriver driver = DM.getCurrentWebDriver();
		WebElement column = driver.findElement(By.xpath("//coral-columnview-column[@data-foundation-layout-columnview-columnid='"+parentPage+"']"));
		List<WebElement> pages = column.findElements(By.tagName("coral-columnview-item"));
		for(int i=0; i<pages.size(); i++) {
			pages.get(i).findElement(By.tagName("img")).click();
			propertiesData.putAll(readProperties(layoutType));
			column = driver.findElement(By.xpath("//coral-columnview-column[@data-foundation-layout-columnview-columnid='"+parentPage+"']"));
			pages = column.findElements(By.tagName("coral-columnview-item"));
		}
		return propertiesData;
	}
	
	private Map<String, Map<String, String>> readProperties(String layoutType) {
		DM.getCurrentWebDriver().findElement(By.xpath("//button/coral-button-label[contains(text(), 'Properties')]")).click();
		Map<String, Map<String, String>> propertiesData = new HashMap<String, Map<String,String>>();
		Map<String, String> propertiesMap = new HashMap<String, String>();
		WidgetInfo title = new WidgetInfo("name=./jcr:title", TextField.class);
		WidgetInfo pageTitle = new WidgetInfo("name=./pageTitle", TextField.class);
		
		WidgetInfo pepPropertiesLink = new WidgetInfo("xpath=//coral-tab/coral-tab-label[text()='PEP Properties']", Link.class);
		WidgetInfo pepImage = new WidgetInfo("name=./imagePath", TextField.class);
		WidgetInfo description = new WidgetInfo("name=./text", TextField.class);
		WidgetInfo tags = new WidgetInfo("name=./classificationTag", GUIWidget.class);
		WidgetInfo featuresFlag = new WidgetInfo("xpath=//input[@name='./featuredflag']", CheckBox.class);
		WidgetInfo mediaType = new WidgetInfo("name=./mediaType", DropDown.class);
		
		WidgetInfo webinarPropertiesLink = new WidgetInfo("xpath=//coral-tab/coral-tab-label[text()='Webinar Properties']", Link.class);
		WidgetInfo headline = new WidgetInfo("name=./headlineText", TextField.class);
		WidgetInfo eventDate = new WidgetInfo("xpath=//coral-datepicker[@name='./eventDate']/input", TextField.class);
		WidgetInfo eventPresenterName = new WidgetInfo("name=./eventPresenterName", TextField.class);
		WidgetInfo eventPresenterTitle = new WidgetInfo("name=./eventPresenterTitle", TextField.class);
		WidgetInfo eventPresenterHeadshot = new WidgetInfo("name=./eventPresenterHeadshot", ComboBox.class);
		
		
		String titleText = DM.textField(title).getDisplayValue();
		propertiesMap.put("pageTitle", DM.textField(pageTitle).getDisplayValue());
		DM.link(pepPropertiesLink).click();
		propertiesMap.put("description", DM.textField(description).getDisplayValue().replaceAll("<ul>\n", "").replaceAll("(<.*?>)", "").trim());
		propertiesMap.put("mediaType", DM.dropDown(mediaType).getDisplayValue());
		//Read Tags
		List<WebElement> tagElements = DMHelper.getWebElement(tags).findElements(By.tagName("coral-tag-label"));
		String tagsList = "";
		for(WebElement tag : tagElements) {
			String tagName = tag.getText();
			if(tagName.contains("/"))
				tagsList += ","+tagName.split("/")[1].trim();
			else
				tagsList += ","+tagName.split(":")[1].trim();
		}
		
		propertiesMap.put("tags", tagsList.length()>1?tagsList.substring(1):"");
		if("Featured Card".equalsIgnoreCase(layoutType))
			propertiesMap.put("tags", tagsList.substring(1).split(",")[0].toUpperCase());
		
		if(layoutType.contains("Webinar")) {
			DM.link(webinarPropertiesLink).click();
			propertiesMap.put("headline", DM.textField(headline).getDisplayValue());
			propertiesMap.put("eventDate", DM.textField(eventDate).getDisplayValue());
			propertiesMap.put("eventPresenterName", DM.textField(eventPresenterName).getDisplayValue());
			propertiesMap.put("eventPresenterTitle", DM.textField(eventPresenterTitle).getDisplayValue());
			propertiesMap.put("eventPresenterHeadshot", DM.comboBox(eventPresenterHeadshot).getDisplayValue());
		}else {
			propertiesMap.put("image", DM.textField(pepImage).getDisplayValue());
			propertiesMap.put("featuredFlag", DM.checkBox(featuresFlag).getDisplayValue());
		}
		
		propertiesData.put(titleText, propertiesMap);
		
		DM.link(new WidgetInfo("linktext=Cancel", Link.class)).click();
		
		return propertiesData;
	}
	
	private void navigateToPage(String[] path) {
		DriverManagerHelper.sleep(1);
		for(int i=2; i<path.length; i++) {
			List<WebElement> elements = DM.getCurrentWebDriver().findElements(By.xpath("//div[contains(text(),'"+path[i]+"')]"));
			elements.get(elements.size()-1).click();
			DriverManagerHelper.sleep(1);
		}
	}

	@Override
	public String getComponentName() {
		return componentName;
	}

}
