package com.agilent.cps.utils;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.asserts.SoftAssert;

import com.agilent.cps.core.Verify;

public class Listener implements ITestListener{

	@Override
	public void onTestStart(ITestResult result) {
		Logger.getInstance().info("################## Test Started : "+result.getName()+" ##############");
		Verify.softAssert = new SoftAssert();
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		
	}

	@Override
	public void onTestFailure(ITestResult result) {
		if(! (result.getThrowable() instanceof AssertionError))
			Logger.getInstance().error(result.getThrowable());
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		result.getThrowable().printStackTrace();
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		
	}

	@Override
	public void onStart(ITestContext context) {
		
	}

	@Override
	public void onFinish(ITestContext context) {
		
	}

}
