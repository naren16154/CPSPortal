package com.agilent.cps.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.testng.asserts.SoftAssert;

import com.agilent.cps.utils.Logger;

public class Verify {

	public static Logger logger = Logger.getInstance();
	public static SoftAssert softAssert;

	public static void verifyEquals(String message, String expectedValue,
			String actualValue) {
		logger.debug("Expected Value : "+expectedValue);
		logger.debug("Actual Value : "+actualValue);
		Pattern pattern = Pattern.compile(expectedValue, Pattern.LITERAL);
		Matcher matcher = pattern.matcher(actualValue);
		boolean matchFound = matcher.matches();
		logger.verificationPoint(message+" result="+matchFound+" expected="+expectedValue+" actual="+actualValue, matchFound);
		if(matchFound)
			Logger.success++;
		else
			Logger.fail++;
		softAssert.assertEquals(actualValue, expectedValue, message);
	}

	public static void verifyEquals(String expectedValue, String actualValue) {
		logger.debug("Expected Value : "+expectedValue);
		logger.debug("Actual Value : "+actualValue);
		Pattern pattern = Pattern.compile(expectedValue);
		Matcher matcher = pattern.matcher(actualValue);
		boolean matchFound = matcher.matches();
		logger.verificationPoint("result="+matchFound+" expected="+expectedValue+" actual="+actualValue, matchFound);
		if(matchFound)
			Logger.success++;
		else
			Logger.fail++;
		softAssert.assertEquals(actualValue, expectedValue);
	}
	
	public static void verifyEquals(String message, Boolean result) {
		logger.verificationPoint("result="+result+" "+message, result);
		if(result)
			Logger.success++;
		else
			Logger.fail++;
		softAssert.assertTrue(result, message);
	}

}
