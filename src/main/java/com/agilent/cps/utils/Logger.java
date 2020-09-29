package com.agilent.cps.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Logger extends Constants{
	
	public static final Logger logger = new Logger();
	public static ScreenShotUtility screenshotUtility = ScreenShotUtility.getInstance();
	public static int success = 0;
	public static int fail = 0;
	public static int error = 0;
	SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss,sss");
	public static String logFile;
	public static Document doc;
	Element root;
	public Logger()
	{
		String logPath = "logs";
		String logName = "report";
		if(null != logPath)
		{
			File file = new File(logPath);
			if(!file.exists())
				file.mkdirs();
			logFile = logPath+"/"+logName+".xml";
			FileOutputStream fileOut = null;
			try {
				fileOut = new FileOutputStream(logFile);
			} catch (FileNotFoundException e) {
				logger.error(e);
			}
			finally{
				if(null != fileOut)
					try {
						fileOut.close();
					} catch (IOException e) {
						logger.error(e);
					}
			}
		}
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			doc = docBuilder.newDocument();
			
			root = doc.createElement("Adflog"); 
			doc.appendChild(root);
		} catch (ParserConfigurationException e) {
			logger.error(e);
		}
	}
	
	public static Logger getInstance()
	{
		return logger;
	}
	
	public void info(String message)
	{
		appendLogChild("INFO", message);
		System.out.println(message);
	}
	
	public void status(String message)
	{
		appendLogChild("STATUS", message);
		System.out.println(message);
	}
	
	public void debug(String message)
	{
		appendLogChild("DEBUG", message);
	}
	
	public void widgetAction(String message)
	{
		appendLogChild("ACTION", message);
		System.out.println(message);
	}
	
	public void verificationPoint(String message, boolean result)
	{
		if(result)
		{
			if(Boolean.parseBoolean(success_robot) || Boolean.parseBoolean(success_scroll))
			{
				if(Boolean.parseBoolean(success_scroll))
					screenshotUtility.takeScreenshotWithMessage("VERIFICATION_POINT", message, true);
				if(Boolean.parseBoolean(success_robot))
					screenshotUtility.takeScreenshotWithMessage("VERIFICATION_POINT", message, false);
			}
			else
				appendLogChild("VERIFICATION_POINT", message);
		}
		else
		{
			if(Boolean.parseBoolean(failure_robot) || Boolean.parseBoolean(failure_scroll))
			{
				if(Boolean.parseBoolean(failure_scroll))
					screenshotUtility.takeScreenshotWithMessage("VERIFICATION_POINT", message, true);
				if(Boolean.parseBoolean(failure_robot))
					screenshotUtility.takeScreenshotWithMessage("VERIFICATION_POINT", message, false);
			}
			else
				appendLogChild("VERIFICATION_POINT", message);
		}
			System.out.println(message);
	}
	
	public void fatal(String message)
	{
		appendLogChild("FATAL", message);
		System.out.println(message);
	}
	
	public void warning(String message)
	{
		appendLogChild("WARNINGS", message);
		System.out.println(message);
	}
	
	public void error(Throwable e)
	{
		StringBuilder strBuilder1 = new StringBuilder();
		String error = "";
		if(null != e.getCause())
		{
		for(StackTraceElement element : e.getCause().getStackTrace())
		{
			strBuilder1.append(element.toString());
			strBuilder1.append("\n");
		}
		error = e.getCause().toString()+"\n"+strBuilder1.toString();
		}
		StringBuilder strBuilder2 = new StringBuilder();
		for(StackTraceElement element : e.getStackTrace())
		{
			strBuilder2.append(element.toString());
			strBuilder2.append("\n");
		}
		error+=e.toString()+"\n"+strBuilder2.toString();
		if(Boolean.parseBoolean(error_robot) || Boolean.parseBoolean(error_scroll))
		{
			if(Boolean.parseBoolean(error_scroll))
				screenshotUtility.takeScreenshotWithMessage("ERROR", error, true);
			if(Boolean.parseBoolean(error_robot))
				screenshotUtility.takeScreenshotWithMessage("ERROR", error, false);
		}
		else
			appendLogChild("ERROR", error);
		e.printStackTrace();
		Logger.error++;
	}
	
	public void error(String message)
	{
		if(Boolean.parseBoolean(error_robot) || Boolean.parseBoolean(error_scroll))
		{
			if(Boolean.parseBoolean(error_scroll))
				screenshotUtility.takeScreenshotWithMessage("ERROR", message, true);
			if(Boolean.parseBoolean(error_robot))
				screenshotUtility.takeScreenshotWithMessage("ERROR", message, false);
		}
		else
			appendLogChild("ERROR", message);
			System.out.println(message);
			Logger.error++;
	}
	
	public void error(String message, boolean screenshot)
	{
		if(!screenshot)
		{
			appendLogChild("ERROR", message);
			System.out.println(message);
			Logger.error++;
		}
	}
	
	public void method(String message)
	{
		appendLogChild("METHOD", message);
		System.out.println(message);
	}
	
	public void infoWithScreenshot(String message, String screenShotFile)
	{
		appendLogChild("INFO", message, screenShotFile);
		System.out.println(message);
	}

	private void appendLogChild(String level, String message,
			String screenShotFile) {
		Element log = doc.createElement("log");
		log.setAttribute("level", level);
		log.setAttribute("timestamp", dateFormatter.format(System.currentTimeMillis()));
		log.setAttribute("message", message);
		log.setAttribute("file", screenShotFile);
		root.appendChild(log);
	}

	private void appendLogChild(String level, String message) {
		Element log = doc.createElement("log");
		log.setAttribute("level", level);
		log.setAttribute("timestamp", dateFormatter.format(System.currentTimeMillis()));
		log.setAttribute("message", message);
		root.appendChild(log);
	}

	public void Screenshot(String loggerType, String message, String screenShotFile) {
		appendLogChild(loggerType, message, screenShotFile);
	}

}
