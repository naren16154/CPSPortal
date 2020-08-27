package com.agilent.cps.components;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import com.agilent.cps.core.AutoPopulator;
import com.agilent.cps.core.DriverManager;

public abstract class BaseComponent {

	DriverManager DM = DriverManager.getInstance();
	
	
	public void populate(Map<String, String> rowData) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		AutoPopulator.populate(this, rowData);
	}
	
	public abstract String getComponentName();

}
