package com.agilent.cps.components;

import java.util.Map;

import com.agilent.cps.core.Verify;
import com.agilent.cps.widgetactions.Button;
import com.agilent.cps.widgetactions.GUIWidget;
import com.agilent.cps.widgetactions.Label;
import com.agilent.cps.widgetactions.Link;
import com.agilent.cps.widgetactions.ListBox;
import com.agilent.cps.widgetactions.RTE;
import com.agilent.cps.widgetactions.TextField;
import com.agilent.cps.widgets.WidgetInfo;

public class Hero extends BaseComponent {
	
	public static final String componentName = "herocomponent";
	
	public static class Widgets{
		public static final WidgetInfo heroHeadlineText = new WidgetInfo("name=./headline", TextField.class);
		public static final WidgetInfo heroText = new WidgetInfo("xpath=//div[@name='./herotext']/p", RTE.class);
		public static final WidgetInfo backgroungImage = new WidgetInfo("name=./file", TextField.class);
		public static final WidgetInfo addButton = new WidgetInfo("xpath=//coral-button-label[text()='Add']", Button.class);
		public static final WidgetInfo buttonLink = new WidgetInfo("name=./cta/item0/./link", TextField.class);
		public static final WidgetInfo buttonText = new WidgetInfo("name=./cta/item0/./text", TextField.class);
		public static final WidgetInfo buttonLinkOpens = new WidgetInfo("name=./cta/item0/./openpage", ListBox.class);
		public static final WidgetInfo buttonColor = new WidgetInfo("name=./cta/item0/./ctacolor", ListBox.class);
		
	}
	
	@Override
	public String getComponentName() {
		return componentName;
	}

	@Override
	public void verifyPreview(Map<String, String> rowData) {
		String carouselHeroText = "";
		if(DM.widgetExists(new WidgetInfo("xpath=//div[@class='carousel-item active']", GUIWidget.class), 1, .5))
			carouselHeroText = "//div[@class='carousel-item active']";
		WidgetInfo heroText = new WidgetInfo("xpath="+carouselHeroText+"//div[@class='text-section__title']", Label.class);
		WidgetInfo heroDescription = new WidgetInfo("xpath="+carouselHeroText+"//div[@class='text-section__description']", Label.class);
		WidgetInfo brandBar = new WidgetInfo("xpath="+carouselHeroText+"//div[@class='brand-lines']", GUIWidget.class);
		WidgetInfo imageSection = new WidgetInfo("xpath="+carouselHeroText+"//div[@class='image-section']/img", GUIWidget.class);
		if(rowData.containsKey("heroHeadlineText"))
			Verify.verifyEquals("Verifying Hero Text", rowData.get("heroHeadlineText"), DM.label(heroText).getDisplayValue());
		
		if(rowData.containsKey("heroText"))
			Verify.verifyEquals("Verifying Hero Description", rowData.get("heroText"), DM.label(heroDescription).getDisplayValue());
		
		if(rowData.containsKey("backgroungImage")) {
			Verify.verifyEquals("Verifying presence of Image", DM.widgetVisible(imageSection, 1, .5));
			Verify.verifyEquals("Verifying Image Path", DM.GUIWidget(imageSection).getAttribute("src").contains(rowData.get("backgroungImage")));
		}
		
		if(rowData.containsKey("addButton")) {
			WidgetInfo heroButton = new WidgetInfo("linktext="+rowData.get("buttonText").toUpperCase(), Link.class);
			Verify.verifyEquals("Verifying CTA Href", DM.GUIWidget(heroButton).getAttribute("href").contains(rowData.get("buttonLink")));
			DM.GUIWidget(heroButton).click();
			Verify.verifyEquals("Verifying window title", "Atlas of Stains", DM.getCurrentWebDriver().getTitle());
			DMHelper.getWebDriver().navigate().back();
		}
		
		if(rowData.containsKey("selectStyle")) {
			if(rowData.get("selectStyle").contains("Show Brand Bar"))
				Verify.verifyEquals("Verifying presence of Brandbar", DM.widgetVisible(brandBar, 1, .5));
		}else {
			Verify.verifyEquals("Verifying Nonpresence of Brandbar", !DM.widgetVisible(brandBar, 1, .5));
		}
			
		
		
	}

}
