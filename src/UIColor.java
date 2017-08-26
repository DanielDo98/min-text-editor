import java.awt.Color;

public enum UIColor{
	
	GREEN(new Color(0, 255, 0)),
	BLACK(new Color(0, 0, 0)),
	PARCHMENT(new Color(255, 243, 216)),
	DEEP_PURPLE(new Color(24, 0, 36)),
	SKY_BLUE(new Color(104, 145, 172)),
	EARTH(new Color(25, 4, 0)),
	RED(new Color(50, 10, 10)),
	DULL_BLUE(new Color(218, 223, 225));
	
	private Color color;
	
	private UIColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
}
