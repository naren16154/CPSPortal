package com.agilent.cps.components;

import java.util.Map;

public class Section extends BaseComponent{

	public static final String componentName = "Section";
	
	public static class Widgets{
		
	}
	
	@Override
	public void verifyPreview(Map<String, String> rowData) {
		
	}

	@Override
	public String getComponentName() {
		return componentName;
	}

}
