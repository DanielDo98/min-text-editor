package com.danieldo.mintext;
import java.awt.Color;

public enum Theme {
	BASIC("Monospaced", UIColor.DEEP_PURPLE.getColor(), UIColor.DEEP_PURPLE.getColor(), UIColor.PARCHMENT.getColor()),
	MATRIX("Monospaced", UIColor.GREEN.getColor(), UIColor.GREEN.getColor(), UIColor.BLACK.getColor()),
	SKY("Monospaced", UIColor.EARTH.getColor(), UIColor.EARTH.getColor(), UIColor.SKY_BLUE.getColor()),
	HALL("Monospaced", UIColor.DULL_BLUE.getColor(), UIColor.DULL_BLUE.getColor(), UIColor.RED.getColor());
	
	 private String fontName;
	 private Color carotColor;
	 private Color foreground;
	 private Color background;
	 
	 private Theme(String fontName, Color carotColor, Color foreground, Color background) {
		 this.fontName = fontName;
		 this.carotColor = carotColor;
		 this.foreground = foreground;
		 this.background = background;
	 }

	public String getFontName() {
		return fontName;
	}

	public Color getCarotColor() {
		return carotColor;
	}

	public Color getForeground() {
		return foreground;
	}

	public Color getBackground() {
		return background;
	}
}
