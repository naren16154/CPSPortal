package com.agilent.cps.components;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.assertj.core.util.Arrays;
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
import com.agilent.cps.widgetactions.Link;
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
		public static final WidgetInfo buildList = new WidgetInfo("name=./listFrom", DropDown.class);
		public static final WidgetInfo showFilters = new WidgetInfo("name=./filter", CheckBox.class);
		
//		Child Pages Options
		public static final WidgetInfo parentPage = new WidgetInfo("name=./parentPage", ComboBox.class);
		public static final WidgetInfo childDeapth = new WidgetInfo("name=./childDepth", TextField.class);
		public static final WidgetInfo orderByPublishDate = new WidgetInfo("name=./orderby", CheckBox.class);
		public static final WidgetInfo hideImage = new WidgetInfo("name=./hideimage", CheckBox.class);
		public static final WidgetInfo hidePEPDescription = new WidgetInfo("name=./hidedescription", CheckBox.class);
		public static final WidgetInfo hideCategoryTags = new WidgetInfo("name=./hidecategorytags", CheckBox.class);
		public static final WidgetInfo hideEventDate = new WidgetInfo("name=./hideeventdate", CheckBox.class);
		public static final WidgetInfo hideEventPresenter = new WidgetInfo("name=./hideeventpresenter", CheckBox.class);
		
//		Fixed List Options
		public static final WidgetInfo addButton = new WidgetInfo("xpath=//button/coral-button-label[text()='Add']", Button.class);
		public static final WidgetInfo page = new WidgetInfo("name=./fixedlist/item%s/./pages", ComboBox.class);
		public static final WidgetInfo hidePEPImage = new WidgetInfo("name=./fixedlist/item%s/./hideimage", CheckBox.class);
		public static final WidgetInfo image = new WidgetInfo("name=./fixedlist/item%s/./images", ComboBox.class);
		public static final WidgetInfo hidePEPDescriptionFixed = new WidgetInfo("name=./fixedlist/item%s/./hidedescription", CheckBox.class);
		public static final WidgetInfo description = new WidgetInfo("name=./fixedlist/item%s/./description", RTE.class);
		public static final WidgetInfo hidePEPTags = new WidgetInfo("name=./fixedlist/item%s/./hidecategorytags", CheckBox.class);
		public static final WidgetInfo tags = new WidgetInfo("name=./fixedlist/item%s/./cattag", ComboBox.class);
		public static final WidgetInfo featureFlag = new WidgetInfo("name=./fixedlist/item%s/./flag", CheckBox.class);
		public static final WidgetInfo buttonText = new WidgetInfo("name=./fixedlist/item%s/./ctatext", TextField.class);
		public static final WidgetInfo buttonLink = new WidgetInfo("name=./fixedlist/item%s/./ctalink", TextField.class);
		public static final WidgetInfo buttonLinkAction = new WidgetInfo("name=./fixedlist/item%s/./ctalinkaction", DropDown.class);
		public static final WidgetInfo buttonIconPosition = new WidgetInfo("name=./fixedlist/item%s/./ctaposition", DropDown.class);
		public static final WidgetInfo showWebinarProps = new WidgetInfo("name=./fixedlist/item%s/./webcheck", CheckBox.class);
		public static final WidgetInfo headlineText = new WidgetInfo("name=./fixedlist/item%s/./headlineText", TextField.class);
		public static final WidgetInfo eventDate = new WidgetInfo("xpath=//coral-datepicker[@name='./fixedlist/item0/./eventDate']//input", TextField.class);
		public static final WidgetInfo eventPresenterName = new WidgetInfo("name=./fixedlist/item0/./eventPresenterName", TextField.class);
		public static final WidgetInfo eventPresenterTitle = new WidgetInfo("name=./fixedlist/item0/./eventPresenterTitle", TextField.class);
		
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
	
	public void AddChildPages(String data) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		List<Map<String, String>> iterationsData = getDataMap(data);
		int iteration = 0;
		for(Map<String, String> rowdata : iterationsData) 
			AutoPopulator.populate(this, rowdata, iteration+"");
	}
	
	@Override
	public void verifyPreview(Map<String, String> rowData) {
		String buildList = rowData.getOrDefault("buildList", "Child pages");
		String layoutType = rowData.get("layoutType"); 
		String publishPage = DM.getCurrentWebDriver().getWindowHandle();
		DMHelper.switchWindow("AEM Sites");
		Map<String, Map<String, String>> propertiesData = new HashMap<String, Map<String,String>>();
		if("Child pages".equalsIgnoreCase(buildList)) {
			Map<String, String> listSettings = getDataMap(rowData.get("AddChildPages")).get(0);
			propertiesData.putAll(getChildpagesProperties(listSettings.get("parentPage"), layoutType));
		}else {
			List<Map<String, String>> iterationsData = getDataMap(rowData.get("AddFixedList"));
			for(Map<String, String> singlePageData : iterationsData)
				propertiesData.putAll(getPageProperties(singlePageData.get("page"), layoutType));
		}
		System.out.println(propertiesData);
		
		DM.getCurrentWebDriver().switchTo().window(publishPage);
		
		verifyAllCardsProperties(layoutType, buildList, rowData, propertiesData);
		
	}

	private void verifyAllCardsProperties(String layoutType, String buildList, Map<String, String> rowData, Map<String, Map<String, String>> propertiesData) {
		int expectedCardsCount = propertiesData.size();
		int actualCardsCount = 0;
		WebDriver driver = DM.getCurrentWebDriver();
		
		List<Map<String, String>> overrideCardsData = "Child pages".equalsIgnoreCase(buildList)?getDataMap(rowData.get("AddChildPages")):getDataMap(rowData.get("AddFixedList"));
		
		List<WebElement> cards = driver.findElements(By.xpath("//div[contains(@class,'cards-container')]/div[@class='col left']/div"));
		for(int i=0; i< cards.size(); i++) {
			Map<String, String> overrideCardData = "Child pages".equalsIgnoreCase(buildList)?overrideCardsData.get(0):overrideCardsData.get(actualCardsCount);
			verifyCardProperties(cards.get(i), layoutType, buildList, overrideCardData, propertiesData, actualCardsCount, false);
			if("Grid Text".equalsIgnoreCase(layoutType) || "Grid text with image".equalsIgnoreCase(layoutType))
				verifyPresenceOfBrandBar(cards.get(i), rowData.getOrDefault("showBrandLines", "uncheck"));
			verifyCTAButtonsFunctionality(cards.get(i), layoutType, buildList, overrideCardData, false);
			actualCardsCount++;
			cards = driver.findElements(By.xpath("//div[contains(@class,'cards-container')]/div[@class='col left']/div"));
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
		if(!"Featured Card".equalsIgnoreCase(layoutType))
			verifyFilterFunctionality(propertiesData, rowData);
		Verify.verifyEquals("Total Cards Count", expectedCardsCount+"", actualCardsCount+"");
	}

	private void verifyFilterFunctionality(Map<String, Map<String, String>> propertiesData, Map<String, String> rowData) {
		WebDriver driver = DM.getCurrentWebDriver();
		Set<String> expectedTagsList = new HashSet<String>();
		Set<String> actualTagsList = new HashSet<String>();
		Map<String, List<Object>> cardTagsMap = new HashMap<String, List<Object>>();
		
		DM.getJSExecutor().executeScript("window.scrollTo(0,0);");
		
		for(String cardTitle : propertiesData.keySet()) {
			String[] tags = propertiesData.get(cardTitle).get("tags").split(",");
			for(String tag : tags)
				expectedTagsList.add(tag);
			cardTagsMap.put(cardTitle, Arrays.asList(tags));
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
		
		List<WebElement> cards = DM.getCurrentWebDriver().findElements(By.xpath("//div[contains(@class,'cards-container')]/div[@class='col left']/div"));
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
		if("Featured Card".equalsIgnoreCase(layoutType) && !isFeatured)
			title = cardElement.findElement(By.className("card__supporting-text")).getText();
		else
			title = cardElement.findElement(By.className("card__title")).getText();
		if("Child pages".equalsIgnoreCase(buildList)) {
			cardElement.findElement(By.linkText("View More")).click();
			Verify.verifyEquals("Button Link Title", title, driver.getTitle());
			driver.navigate().back();
		}else {
			if(overrideCardData.containsKey("buttonLink"))
				verifyLinkOrbutton(cardElement.findElement(By.linkText(overrideCardData.get("buttonText"))), overrideCardData.get("buttonLinkAction"), overrideCardData.get("buttonLink"), title);
		}
	}

	private void verifyPresenceOfBrandBar(WebElement card, String brandBar) {
		List<WebElement> brandBarElements = card.findElements(By.className("brand-lines"));
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
			Map<String, Map<String, String>> propertiesData, int cardIndex, boolean isFeaturedCard) {
		
		Boolean hideImage = "check".equalsIgnoreCase(overrideCardData.getOrDefault("hideImage", "uncheck"))?true:false;
		Boolean hideDescription = "check".equalsIgnoreCase(overrideCardData.getOrDefault("hideDescription", "uncheck"))?true:false;
		Boolean hideTags = "check".equalsIgnoreCase(overrideCardData.getOrDefault("hideTags", "uncheck"))?true:false;
		
		String overrideImage = "";
		String overrideDescription = "";
		String overrideTags = "";
		
		if("Fixed list".equalsIgnoreCase(buildList)) {
			overrideImage = overrideCardData.getOrDefault("image", "");
			overrideDescription = overrideCardData.getOrDefault("description", "");
			overrideTags = overrideCardData.getOrDefault("tags", "");
		}
		
		Map<String, String> actualCardDetails = new HashMap<String, String>();
		
		String cardXpathText = null;
		if(isFeaturedCard)
			cardXpathText = "//div[contains(@class,'cards-container')]/div[@class='col right']/div[1]";
		else
			cardXpathText = "//div[contains(@class,'cards-container')]/div[@class='col left']/div["+(cardIndex+1)+"]";
		
		WidgetInfo image = new WidgetInfo("xpath="+cardXpathText+"//img", GUIWidget.class);
		WidgetInfo pageTitle = new WidgetInfo("xpath="+cardXpathText+"//div[@class='card__title']", GUIWidget.class);
		WidgetInfo description = new WidgetInfo("xpath="+cardXpathText+"//div[@class='card__supporting-text']", GUIWidget.class);
		WidgetInfo tags = new WidgetInfo("xpath="+cardXpathText+"//div[@class='tag']", GUIWidget.class);
		WidgetInfo overlayText = new WidgetInfo("xpath="+cardXpathText+"//div[@class='category-label']", GUIWidget.class);
		
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
		
		Map<String, String> cardProperties = propertiesData.get(actualCardDetails.get("pageTitle"));
		
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
		
		String pageTitleText = DM.textField(pageTitle).getDisplayValue();
		propertiesMap.put("title", DM.textField(title).getDisplayValue());
		propertiesMap.put("pageTitle", pageTitleText);
		DM.link(pepPropertiesLink).click();
		propertiesMap.put("image", DM.textField(pepImage).getDisplayValue());
		propertiesMap.put("description", DM.textField(description).getDisplayValue().replaceAll("(<.*?>)", ""));
		propertiesMap.put("featuredFlag", DM.checkBox(featuresFlag).getDisplayValue());
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
		
		propertiesMap.put("tags", tagsList.substring(1));
		if("Featured Card".equalsIgnoreCase(layoutType))
			propertiesMap.put("tags", tagsList.substring(1).split(",")[0].toUpperCase());
		
		propertiesData.put(pageTitleText, propertiesMap);
		
		DM.link(new WidgetInfo("linktext=Cancel", Link.class)).click();
		
		return propertiesData;
	}
	
	private void navigateToPage(String[] path) {
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
	
	public static void main(String[] args) {
		/*
		 * String text =
		 * "<li>Understand the core principles of PD-L1 pathology·        </li>";
		 * System.out.println(text.replaceAll("[(<[p(li)(ul))]>)][(</[p(li)(ul)]>)]",
		 * "")); System.out.println(text.replaceAll("(<.*?>)", ""));
		 */
//		System.out.println(String.valueOf(false));
		String expectedValue = "false";
		String actualValue = "false";
		Pattern pattern = Pattern.compile(expectedValue, Pattern.LITERAL);
		Matcher matcher = pattern.matcher(actualValue);
		boolean matchFound = matcher.matches();
		System.out.println(matchFound);
	}

}
