package com.agilent.cps.core;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.agilent.cps.widgetactions.Button;
import com.agilent.cps.widgetactions.CheckBox;
import com.agilent.cps.widgetactions.DropDown;
import com.agilent.cps.widgetactions.GUIWidget;
import com.agilent.cps.widgetactions.Label;
import com.agilent.cps.widgetactions.Link;
import com.agilent.cps.widgetactions.ListBox;
import com.agilent.cps.widgetactions.RTE;
import com.agilent.cps.widgetactions.RadioButton;
import com.agilent.cps.widgetactions.RadioGroup;
import com.agilent.cps.widgetactions.TextArea;
import com.agilent.cps.widgetactions.TextField;
import com.agilent.cps.widgets.WidgetInfo;
import com.google.common.base.Stopwatch;

public class DriverManager {
	
	public WebDriver driver = null;
	public Map<String, WebDriver> driverMap = new HashMap<String, WebDriver>();
	public static final DriverManager adfManager = new DriverManager();
	public static DriverManagerHelper adfManagerHelper = DriverManagerHelper.getInstance();
	
	public static DriverManager getInstance()
	{
		return adfManager;
	}
	
	public void setActiveDriver (WebDriver driver)
	{
		this.driver = driver;
	}
	
	public WebDriver getCurrentWebDriver()
	{
		return this.driver;
	}
	
	public JavascriptExecutor getJSExecutor()
	{
		return (JavascriptExecutor)this.driver;
	}
	
	public Map<String, WebDriver> getDriverMap()
	{
		return this.driverMap;
	}
	
	public void waitForWidget(WidgetInfo widgetInfo, long timeOutInSeconds, double d)
	{
		By locator = adfManagerHelper.getByLocator(widgetInfo);
		Stopwatch sw = Stopwatch.createStarted();
		do
		{
			if(driver.findElements(locator).size()>0)
				return;
			try {
				Thread.sleep((long) (d*1000));
			} catch (InterruptedException e) {
			}
		}while(sw.elapsed().toMillis()<(timeOutInSeconds*1000));
		DriverManagerHelper.sleep(.5);
		sw.stop();
	}
	
	public void waitForWidget(By locator, long timeOutInSeconds, double d)
	{
		Stopwatch sw = Stopwatch.createStarted();
		do
		{
			if(driver.findElements(locator).size()>0)
				return;
			try {
				Thread.sleep((long) (d*1000));
			} catch (InterruptedException e) {
			}
		}while(sw.elapsed().toMillis()<(timeOutInSeconds*1000));
		DriverManagerHelper.sleep(.5);
		sw.stop();
	}
	
	public boolean widgetExists(WidgetInfo widgetInfo, long timeOutInSeconds, double intervalInSeconds)
	{
		By locator = adfManagerHelper.getByLocator(widgetInfo);
		Stopwatch sw = Stopwatch.createStarted();
		do
		{
			if(driver.findElements(locator).size()>0)
				return true;
			try {
				Thread.sleep((long) (intervalInSeconds*1000));
			} catch (InterruptedException e) {
			}
		}while(sw.elapsed().toMillis()<(timeOutInSeconds*1000));
		DriverManagerHelper.sleep(.5);
		sw.stop();
		return false;
	}
	
	public boolean widgetNotExists(WidgetInfo widgetInfo, long timeOutInSeconds, double intervalInSeconds)
	{
		By locator = adfManagerHelper.getByLocator(widgetInfo);
		Stopwatch sw = Stopwatch.createStarted();
		do
		{
			if(driver.findElements(locator).size()==0)
				return true;
			try {
				Thread.sleep((long) (intervalInSeconds*1000));
			} catch (InterruptedException e) {
			}
		}while(sw.elapsed().toMillis()<(timeOutInSeconds*1000));
		DriverManagerHelper.sleep(.5);
		sw.stop();
		return false;
	}
	
	public boolean widgetVisible(WidgetInfo widgetInfo, long timeOutInSeconds, double interval)
	{
		By locator = adfManagerHelper.getByLocator(widgetInfo);
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds, (long) (interval*1000));
		try{
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			DriverManagerHelper.sleep(.5);
			return true;
		}catch(Exception e)
		{
			return false;
		}
	}
	
	public boolean widgetEnabled(WidgetInfo widgetInfo, long timeOutInSeconds, double interval)
	{
		By locator = adfManagerHelper.getByLocator(widgetInfo);
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds, (long) (interval*1000));
		try{
			wait.until(ExpectedConditions.elementToBeClickable(locator));
			DriverManagerHelper.sleep(.5);
			return true;
		}catch(Exception e)
		{
			return false;
		}
	}
	
	public void waitForAjaxToLoad()
	{
		Stopwatch sw = Stopwatch.createStarted();
		String style = null;
		do
		{
			DriverManagerHelper.sleep(.5);
			WebElement element = driver.findElement(By.xpath("html/body"));
			style = element.getAttribute("style");
			if(null != style && (!"cursor: wait;".equals(style)))
			{
				DriverManagerHelper.sleep(1);
				return;
			}
		}while(sw.elapsed().toMillis()<(30*1000));
		sw.stop();
	}
	
	public void waitForPageToLoad()
	{
		By locator = By.xpath("//*[@id='pt1:_UIScil1u' or @id='_FOpt1:_UIScil1u']");
		WebDriverWait wait = new WebDriverWait(driver, 60, 2000);
		try{
			DriverManagerHelper.sleep(2);
			wait.until(ExpectedConditions.elementToBeClickable(locator));
			waitForAjaxToLoad();
			return;
		}catch(Exception e)
		{
		}
	}
	
	public Button button(WidgetInfo widgetInfo)
	{
		return new Button(widgetInfo);
	}
	
	public CheckBox checkBox(WidgetInfo widgetInfo)
	{
		return new CheckBox(widgetInfo);
	}
	
	public RTE rte(WidgetInfo widgetInfo)
	{
		return new RTE(widgetInfo);
	}
	
	public DropDown dropDown(WidgetInfo widgetInfo)
	{
		return new DropDown(widgetInfo);
	}
	
	public GUIWidget widget(WidgetInfo widgetInfo)
	{
		return new GUIWidget(widgetInfo);
	}
	
	public Label label(WidgetInfo widgetInfo)
	{
		return new Label(widgetInfo);
	}
	
	public Link link(WidgetInfo widgetInfo)
	{
		return new Link(widgetInfo);
	}
	
	public ListBox listBox(WidgetInfo widgetInfo)
	{
		return new ListBox(widgetInfo);
	}
	
	public RadioButton radioButton(WidgetInfo widgetInfo)
	{
		return new RadioButton(widgetInfo);
	}
	
	public RadioGroup radioGroup(WidgetInfo widgetInfo)
	{
		return new RadioGroup(widgetInfo);
	}
	
	public TextArea testArea(WidgetInfo widgetInfo)
	{
		return new TextArea(widgetInfo);
	}
	
	public TextField textField(WidgetInfo widgetInfo)
	{
		return new TextField(widgetInfo);
	}
	
	public GUIWidget GUIWidget(WidgetInfo widgetInfo)
	{
		return new GUIWidget(widgetInfo);
	}

}
