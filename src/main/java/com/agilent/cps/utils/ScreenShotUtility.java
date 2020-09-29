package com.agilent.cps.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.agilent.cps.core.DriverManager;

public class ScreenShotUtility {
	
	private static final ScreenShotUtility screenShotUtility = new ScreenShotUtility();
	private String imagePath = "";
	DriverManager adfManager = DriverManager.getInstance();
	Logger logger = Logger.getInstance();
	public ScreenShotUtility()
	{
		String basePath = "logs";
		imagePath = basePath+"/images/";
		File file = new File(imagePath); 
		if(!file.exists())
			file.mkdirs();
	}
	
	public static ScreenShotUtility getInstance()
	{
		return screenShotUtility;
	}
	
	public void takeWindowScreenshot(String imageName, WebDriver driver)
	{
		try {
			File source = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(source, new File(imagePath+imageName));
		} catch (IOException e) {
			logger.error(e);
		}
	}
	
	public void takeScrollingScreenshot(String imageName, WebDriver driver)
	{
		try {
			JavascriptExecutor js = (JavascriptExecutor)driver;
			
			long bodyscrollheight = (long) js.executeScript("return document.body.scrollHeight");
			
			long bodyheight = (long) js.executeScript("return document.body.clientHeight");

			long docscrollheight = (long) js.executeScript("return document.documentElement.scrollHeight");
			
			long docheight = (long) js.executeScript("return document.documentElement.clientHeight");
			long windowWidth = (long) js.executeScript("return document.body.clientWidth");
			
			long windowheight =(long) Math.min(Math.min(bodyheight, bodyscrollheight),Math.min(docscrollheight,docheight)) ;
			long scrollheight = (long) Math.max(Math.max(bodyheight, bodyscrollheight),Math.max(docscrollheight,docheight));
			int iterations = (int) (scrollheight/windowheight);
			if(scrollheight/windowheight>0)
				iterations++;
			BufferedImage result = new BufferedImage((int)windowWidth,(int)scrollheight,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g = result.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			int y=0;
			String command = "window.scrollTo(0,0);";
			for(int i=0; i<=iterations; i++)
			{
				js.executeScript(command);
				File source = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				sleep(.5);
				Image bi;
				
					bi = ImageIO.read(source);
					if(scrollheight-y<windowheight)
					{
						g.drawImage(bi, 0,(int)(scrollheight-windowheight), (int)windowWidth, (int)windowheight, null);
						break;
					}
					g.drawImage(bi, 0,y, (int)windowWidth, (int)windowheight , null);
					y+=windowheight;
					command = "window.scrollTo(0,"+(windowheight+(windowheight*i))+");";
			}
			ImageIO.write(result, "png", new File(imagePath+imageName));
			command = "window.scrollTo(0,0);";
			sleep(.5);
			js.executeScript(command);
		} catch (Exception e) {
			logger.error("Unable to Capture Screenshot", false);
		}
	}
	
	public void takeScreenshot()
	{
		takeScreenshot(true);
	}
	
	public void takeScreenshot(Boolean flag)
	{
		WebDriver driver = adfManager.getCurrentWebDriver();
		String imageName = genarateScreenshotPath();
		if(flag)
		{
			screenShotUtility.takeScrollingScreenshot(imageName, driver);
			logger.infoWithScreenshot("Captured Scrolling Screenshot : "+imagePath+imageName, imageName);
		}
		else
		{
			screenShotUtility.takeWindowScreenshot(imageName, driver);
			logger.infoWithScreenshot("Captured Window Screenshot : "+imagePath+imageName, imageName);
		}
	}
	
	public void takeScreenshotWithMessage(String loggerType, String message, Boolean flag)
	{
		WebDriver driver = adfManager.getCurrentWebDriver();
		if(null!=driver)
		{
		String imageName = genarateScreenshotPath();
		if(flag)
		{
			screenShotUtility.takeScrollingScreenshot(imageName, driver);
			logger.Screenshot(loggerType, message, imageName);
		}
		else
		{
			screenShotUtility.takeWindowScreenshot(imageName, driver);
			logger.Screenshot(loggerType, message, imageName);
		}
		}
		else
			logger.error(message, false);
	}

	private String genarateScreenshotPath() {
		return "img_"+System.currentTimeMillis()+".png";
	}
	
	public void sleep(double sec) {
		try {
			Thread.sleep((long) (sec*1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


}
