package com.agilent.cps.screens;

public class AgilentUniversity extends BaseScreen{
	
	public void accessElearningCourse(String course) {
		seleniumActions.clickLink("Access online e-learning");
		seleniumActions.clickLinkWithParialText(course);
	}

}
