package com.agilent.cps.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.reflections.Reflections;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.agilent.cps.components.BaseComponent;
import com.agilent.cps.core.DriverManager;

public class BaseTest{
	
	public Browser browser = new Browser();
	public Map<String, BaseComponent> screenMap = new HashMap<String, BaseComponent>();
	
	public Logger logger = Logger.getInstance();
	public DriverManager DM = DriverManager.getInstance();
	public static Date startTime;
	public static Properties configProperties = new Properties();
	public static String url;
	
	@BeforeSuite(alwaysRun = true)
	public void setup() throws IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, FileNotFoundException, IOException {
		startTime = Calendar.getInstance().getTime();
		configurationSetup();
		logSetupInformation();
		Reflections ref = new Reflections("com.agilent.cps.components");
		Set<Class<? extends BaseComponent>> components = ref.getSubTypesOf(BaseComponent.class);
		for(Class<?> screen : components)
		{
			BaseComponent obj;
			try {
				obj = (BaseComponent) screen.getDeclaredConstructor().newInstance();
				screenMap.put(obj.getClass().getSimpleName(), obj );
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		configProperties.load(new FileInputStream("config.properties"));
	}
	
	@AfterSuite(alwaysRun = true)
	public void generateReport() {
		logger.verificationPoint("result=true Total number of verification points: "+(Logger.success+Logger.fail)+" Passed verification points: "+Logger.success+" Failed verification points: "+Logger.fail, true);
		logger.info("Total time elapsed : "+getTotalTimeLapsed());
		if(Logger.error == 0 && Logger.fail == 0)
			logger.status("Passed");
		else
			logger.status("Failed");
		try {
			writeXML();
			generateHtml();
		} catch (TransformerException | FileNotFoundException e) {
			logger.error(e);
		}
	}
	
	private static String getTotalTimeLapsed() {
		long diff = System.currentTimeMillis() - startTime.getTime();
		long diffSeconds = diff / 1000 % 60;
		long diffMinutes = diff / (60 * 1000) % 60;
		long diffHours = diff / (60 * 60 * 1000);
		String time = "";
		for(String s : (diffHours+":"+diffMinutes+":"+diffSeconds).split(":"))
		{
			if(s.length()<=1)
				s="0"+s;
			time += s+":";
		}
		return time.substring(0, time.length()-1);
	}

	private void logSetupInformation() {
		logger.info("Execution Time Stamp : "+startTime.toString());
		logger.info("Launching in Browser  : "+Constants.browser);
	}

	public static void writeXML () throws TransformerException
	{
		TransformerFactory transFactory = TransformerFactory.newInstance();
		Transformer transformer = transFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		DOMSource source = new DOMSource(Logger.doc);
		StreamResult target = new StreamResult(Logger.logFile);
		
		transformer.transform(source, target);
	}
	
	public static void generateHtml() throws FileNotFoundException, TransformerException
	{
		TransformerFactory tFactory=TransformerFactory.newInstance();

        Source xslDoc=new StreamSource("lib/NewStylesheet.xsl");
        Source xmlDoc=new StreamSource(Logger.logFile);

        OutputStream htmlFile=new FileOutputStream(Logger.logFile.replace(".xml", ".html"));
        Transformer trasform=tFactory.newTransformer(xslDoc);
        trasform.transform(xmlDoc, new StreamResult(htmlFile));
	}
	
	public void configurationSetup() {
		String browser = System.getProperty("browser");
		String isRCServer = System.getProperty("isRCServer");
		String port = System.getProperty("port");
		
		if(!("".equals(browser) || null == browser))
			Constants.browser = browser.toUpperCase();
		
		if(!("".equals(isRCServer) || null == isRCServer))
			Constants.isrcserver = isRCServer;
		
		if(!("".equals(port) || null == port))
			Constants.port = port;
		String osName = System.getProperty("os.name");
		if(osName.contains("Windows")) {
			Constants.chromeDriver = String.format(Constants.chromeDriver, "windows", ".exe");
			Constants.firefoxDriver = String.format(Constants.firefoxDriver, "windows", ".exe");
			Constants.ieDriver = String.format(Constants.ieDriver, "windows", ".exe");
		}else {
			Constants.chromeDriver = String.format(Constants.chromeDriver, "linux", "");
			Constants.firefoxDriver = String.format(Constants.firefoxDriver, "linux", "");
			Constants.ieDriver = String.format(Constants.ieDriver, "linux", "");
		}
	}
	
}

