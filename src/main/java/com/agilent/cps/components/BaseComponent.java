package com.agilent.cps.components;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;

import com.agilent.cps.core.AutoPopulator;
import com.agilent.cps.core.DriverManager;
import com.agilent.cps.core.DriverManagerHelper;
import com.agilent.cps.core.Verify;
import com.agilent.cps.utils.BaseTest;
import com.agilent.cps.utils.Logger;
import com.agilent.cps.utils.ReadExcel;
import com.agilent.cps.widgetactions.Link;
import com.agilent.cps.widgetactions.TextField;
import com.agilent.cps.widgets.WidgetInfo;

enum ColorCode{
	black("#000000"),
	blue("#0085d5"),
	white("#ffffff"),
	gray("#535557"),
	primaryblue("#00426a"),
	defaultcolor("#000000");
	
	private String colorCode;
	ColorCode(String color) {
        this.colorCode = color;
    }
 
    public String getColorCode() {
        return colorCode;
    }
}

public abstract class BaseComponent {

	DriverManager DM = DriverManager.getInstance();
	DriverManagerHelper DMHelper = DriverManagerHelper.getInstance();
	
	
	public void populate(Map<String, String> rowData) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		AutoPopulator.populate(this, rowData);
	}
	
	public abstract void verifyPreview(Map<String, String> rowData) throws ParseException;
	
	public abstract String getComponentName();
	
	public void selectStyle(String styles) {
		DM.getCurrentWebDriver().findElement(By.xpath("//button[@title='Done']")).click();
		DriverManagerHelper.sleep(2);
		
		List<WebElement> components = DM.getCurrentWebDriver().findElements(By.xpath("//div[@title='"+this.getComponentName()+"']"));
		components.get(components.size()-1).click();
		DM.getCurrentWebDriver().findElement(By.xpath("//button[@title='Styles']")).click();
		
		List<String> actualStyles = Arrays.asList(styles.split(","));
		
		List<WebElement> stylesList = DM.getCurrentWebDriver().findElements(By.xpath("//form[@id='editor-styleselector-form']//coral-selectlist-item"));
		
		for(int i=0; i<stylesList.size(); i++) {
			boolean selected = Boolean.parseBoolean(stylesList.get(i).getAttribute("aria-selected"));
			String styleName = stylesList.get(i).getText(); 
			if(selected ^ actualStyles.contains(styleName))
				stylesList.get(i).click();
			if(actualStyles.contains(styleName))
				Logger.getInstance().info("Selected Style : "+styleName);
			
			stylesList = DM.getCurrentWebDriver().findElements(By.xpath("//form[@id='editor-styleselector-form']//coral-selectlist-item"));
		}
		
		DM.getCurrentWebDriver().findElements(By.xpath("//button[@icon='check']")).get(1).click();
		DriverManagerHelper.sleep(2);
	}
	
	public void verifyLinkOrbutton(WebElement element, String linkOption, String linkUrl, String newWindowTitle) {
		WebDriver driver = DM.getCurrentWebDriver();
		Verify.verifyEquals("Verfying Href text", element.getAttribute("href").contains(linkUrl));
		int windowsCountBefore = driver.getWindowHandles().size();
		String windowBefore = driver.getWindowHandle();
		String target = element.getAttribute("target");
		String onClick = element.getAttribute("onclick");
		element.click();
		DriverManagerHelper.sleep(5);
		Set<String> windows = driver.getWindowHandles();
		Boolean isWindowCountMatched =  false;
		Boolean isWindowTargetMatched = false;
		if("New tab".equalsIgnoreCase(linkOption) || "New window".equalsIgnoreCase(linkOption)) {
			isWindowCountMatched =  windowsCountBefore+1 == windows.size();
			isWindowTargetMatched = false;
			if("New tab".equalsIgnoreCase(linkOption))
				isWindowTargetMatched = "_blank".equals(target);
			else
				isWindowTargetMatched = "".contentEquals(target) && onClick.contains("window.open");
			Verify.verifyEquals("Verifying new tab/window opened", isWindowCountMatched && isWindowTargetMatched);
			for(String window : windows)
				driver.switchTo().window(window);
			verifyWindowTitle("Verifying window title", newWindowTitle.equals("")?getExpectedTitle(linkUrl):newWindowTitle, driver.getTitle());
			driver.close();
			driver.switchTo().window(windowBefore);
		}else {
			isWindowCountMatched = windowsCountBefore==windows.size();
			isWindowTargetMatched = "_self".equals(target) || "".equals(target);
			Verify.verifyEquals("Verifying link opened in same tab", isWindowCountMatched && isWindowTargetMatched);
			verifyWindowTitle("Verifying window title", newWindowTitle.equals("")?getExpectedTitle(linkUrl):newWindowTitle, driver.getTitle());
			driver.navigate().back();
		}
	}
	
	public void verifyColor(String message, String expectedColor, String actualColorCode) {
		String expectedColorCode = ColorCode.valueOf(expectedColor.toLowerCase()).getColorCode();
		Verify.verifyEquals(message, expectedColorCode, Color.fromString(expectedColorCode).asHex());
	}
	
	public void verifyImage(String message, String expectedImageSrc, String actualImageSrc, boolean isImgTag) {
		if(isImgTag) {
			expectedImageSrc = BaseTest.url.charAt(BaseTest.url.length()-1)=='/'?BaseTest.url.substring(0,BaseTest.url.length()-1)+expectedImageSrc:BaseTest.url+expectedImageSrc;
			Verify.verifyEquals(message, expectedImageSrc, actualImageSrc.replaceAll("%20", " "));
		}else {
			actualImageSrc = actualImageSrc.replaceAll(".*\\(\"", "").replaceAll("\"\\).*", "");
			Verify.verifyEquals(message, expectedImageSrc, actualImageSrc);
		}
	}
	
	public void verifyWindowTitle(String message, String expectedTitle, String actualTitle) {
		expectedTitle = actualTitle.endsWith(" | Agilent")?(expectedTitle.length()>55?expectedTitle.substring(0,55).trim():expectedTitle)+" | Agilent":expectedTitle;
		Verify.verifyEquals(message, expectedTitle, actualTitle);
	}
	
	public String getExpectedTitle(String page) {
		WebDriver driver = DM.getCurrentWebDriver();
		String title = "";
		if(page.contains("http")) {
			for(Map<String, String> rowData : ReadExcel.workbookData.get("WindowTitles")) {
				if(rowData.containsValue(page)) {
					title = rowData.get("Title");
					break;
				}
			}
		}else {
			String[] path = page.split("/");
			navigateToPage(path);
			List<WebElement> elements = driver.findElements(By.xpath("//coral-columnview-item-content/div[contains(text(),'"+path[path.length-1]+"')]"));
			elements.get(0).findElement(By.xpath("../..//img")).click();
			driver.findElement(By.xpath("//button/coral-button-label[contains(text(), 'Properties')]")).click();
			
			WidgetInfo pageTitle = new WidgetInfo("name=./pageTitle", TextField.class);
			WidgetInfo browserTitle = new WidgetInfo("name=./browserTitle", TextField.class);
			
			String pageTitleContent = DM.textField(pageTitle).getDisplayValue();
			String browserTitleContent = DM.textField(browserTitle).getDisplayValue();
			title = browserTitleContent.equals("")?pageTitleContent:browserTitleContent;
			
			DM.link(new WidgetInfo("linktext=Cancel", Link.class)).click();
			
			Set<String> windows = driver.getWindowHandles();
			for(String window : windows)
				driver.switchTo().window(window);
		}
		
		return title;
	}
	
	public void navigateToPage(String[] path) {
		DMHelper.switchWindow("AEM Sites");
		for(int i=2; i<path.length; i++) {
			List<WebElement> elements = DM.getCurrentWebDriver().findElements(By.xpath("//div[contains(text(),'"+path[i]+"')]"));
			elements.get(elements.size()-1).click();
			DriverManagerHelper.sleep(1);
		}
	}
	
	public void verifyTextFontSize(String string, WebElement element, String fontStyle) {
		String actualFontSize = element.getCssValue("font-size");
		String expectedFontSize = null;
		String actualTagName = "";
		if(element.getTagName().equalsIgnoreCase("a"))
			actualTagName = element.findElement(By.xpath("parent::*")).getTagName();
		else
			actualTagName = element.getTagName();
		
		
		switch (fontStyle.toLowerCase()) {
		case "h0":
			expectedFontSize = "36px";
			break;
		case "h1":
			expectedFontSize = "36px";
			break;
		case "h2":
			expectedFontSize = "28px";
			break;
		case "h3":
			expectedFontSize = "22px";
			break;
		case "h4":
			expectedFontSize = "18px";
			break;
		case "h5":
			expectedFontSize = "15px";
			break;
		case "h6":
			expectedFontSize = "13px";
			break;
		default:
			expectedFontSize = "NA";
			break;
		}
		Verify.verifyEquals("Verifying headline link font size", expectedFontSize, actualFontSize);
		Verify.verifyEquals("Verifying font tag name", "h0".equalsIgnoreCase(fontStyle.toLowerCase())?"div":fontStyle.toLowerCase(), actualTagName);
	}
	
	public static List<Map<String, String>> getDataMap(String iterations)
	{
		if(iterations.contains("("))
		{
			String num = iterations.replaceAll(".*\\(|\\).*", "").trim();
			String sheet = iterations.substring(0, iterations.lastIndexOf("("));
			int start = Integer.parseInt(num.split(",")[0].trim())-1;
			int end = Integer.parseInt(num.split(",")[1].trim());
			return ReadExcel.workbookData.get(sheet).subList(start, end);
		}
		else
			return ReadExcel.workbookData.get(iterations);			
	}
	
}
