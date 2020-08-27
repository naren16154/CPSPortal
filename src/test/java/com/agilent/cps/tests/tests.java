package com.agilent.cps.tests;

import java.lang.reflect.InvocationTargetException;

import org.testng.annotations.Test;

import com.agilent.cps.screens.AgilentUniversity;
import com.agilent.cps.screens.PathologyEducation;
import com.agilent.cps.screens.Webinars;

public class tests extends BaseTest{
	
	@Test(groups= {"testdemo"})
	public void registerAWebinor() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Webinars webinarPage = new Webinars();
		webinarPage.navigatToWebinarsPage();
		webinarPage.selectWebinar("Solutions for Light Hydrocarbons and Gases");
		webinarPage.registerForWebinar();
	}
	
	@Test(groups= {"testdemo"})
	public void registerAElearningCourse() {
		AgilentUniversity agilentUniversity = new AgilentUniversity();
		agilentUniversity.navigatToAgilentUniversityPage();
		agilentUniversity.accessElearningCourse("Agilent University ePass - Unlimited Self-paced Online Course Access");
	}
	
	@Test(groups= {"test"})
	public void verifyCarousalList() {
		PathologyEducation pe = new PathologyEducation();
		String herosList = pe.getCarousalList();
		System.out.println(herosList);
		pe.verifyCarousalHerosContentDetails(herosList);
	}
	
	@Test(groups= {"test"})
	public void verifyContentCardsListDetails() {
		PathologyEducation pe = new PathologyEducation();
		pe.verifyContentCardsDetails();
	}
	
	@Test(groups= {"test"})
	public void verifyAccordianDetails() {
		PathologyEducation pe = new PathologyEducation();
		pe.verifyAccordianBlock();
	}
	
}
