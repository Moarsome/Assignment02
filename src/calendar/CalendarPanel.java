package calendar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.border.Border;

/**
* Establishes the left panel of the Calendar GUI; 
* Visualises the calendar days, months and years in a grid format.
* @author Kylie Crump 19064114
**/

public class CalendarPanel extends JPanel {
	private DayObject[][] days;
	private String[] dayNames;
	private CalendarSupport cal;
	private JLayeredPane layer;
	private JLabel MonthLabel;
	private JLayeredPane lastSelected;
	private JLabel forwards, backwards;
	/**
	* Constructor for CalendarPanel class
	* @author 19064114
	* */
	public CalendarPanel() throws IOException
	{	
		cal = new CalendarSupport();
		
		PrintWriter out = new PrintWriter(new FileWriter("dateSelected.txt", false));
		out.print("null");
		
		out.close();
		
		// Create back and forward icons
		forwards = new JLabel(scaleImage("forwards.png"));
		forwards.setName("forwards");
		backwards = new JLabel(scaleImage("backwards.png"));
		backwards.setName("backwards");
		
		// Creating month label
		MonthLabel = new JLabel(cal.getMonth().toString()+" "+cal.getYear(), SwingConstants.CENTER);
		MonthLabel.setFont(new Font("Helvetica", Font.PLAIN, 25));
		MonthLabel.setPreferredSize(new Dimension(300,30));
		add(backwards);
		add(MonthLabel);
		add(forwards);
		
		// Creating dates GUI along with day names
		days = new DayObject[6][7];
		
		dayNames = new String[]{ "MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};
		
		Color textColor = Color.BLACK;
		
		for (int column = 0; column < 7; column++)
		{
			JLabel dayLabel = new JLabel(dayNames[column], SwingConstants.CENTER);
			dayLabel.setPreferredSize(new Dimension(50,30));
			add(dayLabel);
		}
		
		for (int row = 0; row < 6; row++)
		{
			for (int column = 0; column < 7; column++)
			{
				int date = cal.getDate(row, column);
				
				// If dates are not part of current month
				if ((date > 14 && row == 0) || (date < 14 && (row == 5 || row == 4))) 
				{
					textColor = Color.GRAY;
				}
				else
				{
					textColor = Color.BLACK;
				}
				
				this.days[row][column] = new DayObject(date, row, column, cal);
				
				add(days[row][column].getLayer());
			}
		}
		
		setBackground(Color.getHSBColor(0.58f, 0.2f, 1.0f));
		setPreferredSize(new Dimension(400,420));
	}
	/**
	* Retrieves instance of a .png image and transforms and converts it to an ImageIcon
	* @param imgPath A string denoting location of .png image
	* @return A converted ImageIcon, ready to utilise for JComponents
	* @author 19064114
	* */
	public ImageIcon scaleImage(String imgPath)
	{
		ImageIcon imgIcon = new ImageIcon("icons/"+imgPath);
		
		Image image = imgIcon.getImage(); // transform it 
		Image newimg = image.getScaledInstance(35, 40,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
		imgIcon = new ImageIcon(newimg);  // transform it back
		
		return imgIcon;
	}
	/**
	* Updates the JPanel by changing the texts of each date to the correct one according to month and year
	* @author 19064114
	* */
	public void updatePanel()
	{
		// UPDATE MONTH
		MonthLabel.setText(cal.getMonth().toString()+" "+cal.getYear());
		
		// UPDATE DATES
		for (int row = 0; row < 6; row++)
		{
			for (int column = 0; column < 7; column++)
			{
				int date = cal.getDate(row, column);
				
				days[row][column].changeDate(date, cal);
				days[row][column].updateEvents();
			}
		}
	}
	/**
	* Retrieves an instance of DayObject from date and month values
	* @param dateIn the date destination in int form
	* @param monthIn the month destination in int form
	* @return A DayObject object, if it exists
	* @author 19064114
	* */
	public DayObject getDayObj(int dateIn, int monthIn)
	{
		for (int row = 0; row < 6; row++)
		{
			for (int column = 0; column < 7; column++)
			{
				int date = cal.getDate(row, column);
				if (dateIn == date && monthIn == days[row][column].getMonth(dateIn, row, column))
					return days[row][column];
			}
		}
		return null;
	}
	/**
	* Get method for last selected date
	* @return The last selected date if it exists
	* @author 19064114
	* */
	public JLayeredPane getLastSelected()
	{
		return lastSelected;
	}
	/**
	* Set method for last selected date
	* @param layer the JLayeredPane which is set to be the last selected
	* @author 19064114
	* */
	public void setLastSelected(JLayeredPane layer)
	{
		this.lastSelected = layer;
	}
	
	/**
	* Get method for the current CalendarSupport object
	* @return The CalendarSupport object if it exists
	* @author 19064114
	* */
	public CalendarSupport getCalendar()
	{
		return this.cal;
	}
	
	/**
	* Get method for DayObject 2D array
	* @return The current DayObject 2D array
	* @author 19064114
	* */
	public DayObject[][] getDays()
	{
		return this.days;
	}
	
	/**
	* Get method for forwards icon button
	* @return The forwards icon in JLabel form
	* @author 19064114
	* */
	public JLabel getForwards()
	{
		return this.forwards;
	}
	/**
	* Get method for backwards icon button
	* @return The backwards icon in JLabel form
	* @author 19064114
	* */
	public JLabel getBackwards()
	{
		return this.backwards;
	}
}
