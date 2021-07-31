package calendar;

/**
* This custom enum provides visible icons for events
* Helps to identify events by colour
* @author Kylie Crump 19064114
**/

public enum EventType {
	BLUE("blue_icon.png"),
	RED("red_icon.png"),
	GREEN("green_icon.png"),
	YELLOW("yellow_icon.png"),
	ORANGE("orange_icon.png"),
	PURPLE("purple_icon.png");
	
	private String pathway;
	
	/**
	* Constructor for the pathway for icon colour
	* @param pathway String pathway for the .png icon
	* @author 19064114
	* */
	
	EventType(String pathway)
	{
		this.pathway = pathway;
	}
	
	/**
	* Get method for pathway of enum component icon
	* @return The string for the pathway
	* @author 19064114
	* */
	
	public String getPathway()
	{
		return this.pathway;
	}
}
