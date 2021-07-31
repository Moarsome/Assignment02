package calendar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import javax.swing.*;

/**
* Each date on the calendar is a component
* Most of its role is to provide support for event management
* @author Kylie Crump 19064114
**/

public class DayObject {
	private CalendarSupport cal;
	private int eventsNum;
	private int dateNum;
	private JLayeredPane layer;
	private JLabel dateLab;
	private ArrayList<JLabel> events;
	private int row;
	private int column;
	
	/**
	* DayObject constructor
	* @param dateNum current date integer
	* @param row the row index set
	* @param column the column index set
	* @param cal the CalendarSupport object set to current date
	* @author 19064114
	* */
	
	public DayObject(int dateNum, int row, int column, CalendarSupport cal)
	{
		this.cal = cal;
		this.row = row;
		this.eventsNum = 0;
		this.column = column;
		this.dateNum = dateNum;
		this.dateLab = new JLabel(""+dateNum+"", SwingConstants.CENTER);
		this.layer = new JLayeredPane();
		
		this.layer.setPreferredSize(new Dimension(50,50));
		events = new ArrayList<JLabel>();
		
		dateLab.setBackground(Color.getHSBColor(0.58f, 0.01f, 1.0f));
		dateLab.setOpaque(true);
		dateLab.setName("null");
		layer.add(dateLab,10);
		layer.setLayer(dateLab, -10);
		dateLab.setBounds(0, 0, 50, 50);
	}
	
	/**
	* Adds an event to the top layer of ArrayList and displays an icon
	* @param type An EventType enum to indicate colour of event
	* @author 19064114
	* */
	
	public void addEvent(EventType type)
	{
		if (eventsNum != 5)
		{
			JLabel event = new JLabel();
			events.add(event);
			event.setBounds(eventsNum*8+1+1*eventsNum, 1, 10, 10);
			event.setIcon(scaleImage(type.getPathway()));
			event.setName(String.format("%02d", eventsNum));
			layer.add(event,0);
			
			try {
				PrintWriter out = new PrintWriter(new FileWriter("eventList.txt", true));
				
				out.print(String.format("%02d/%02d/%04d %s\n", this.dateNum,cal.getMonth().getValue(), cal.getYear(), event.getName()));
				
				out.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			eventsNum++;
		}
	}
	
	/**
	* Removes the top-most event from ArrayList events
	* @author 19064114
	* */
	
	public void removeEvent()
	{
		RandomAccessFile f;
		try {
			f = new RandomAccessFile("eventList.txt", "rw");

			long length = f.length() - 1;
			byte b;
			do {                     
			  length -= 1;
			  f.seek(length);
			  b = f.readByte();
			} while(b != 10 && length > 0);
			if (length == 0) { 
				f.setLength(length);
			} 
			else {
				f.setLength(length + 1);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (int i = 0; i < events.size();i++)
		{
			events.get(i).setVisible(false);
		}
		
		events.remove(events.size()-1);
		eventsNum--;
	}
	
	/**
	* Sets visibility of existing and non-existing events
	* @author 19064114
	* */
	
	public void updateEvents()
	{
		for (int i = 0; i < events.size();i++)
		{
			events.get(i).setVisible(false);
		}
		
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader("eventList.txt"));
			
			String text;
			
			while ((text = in.readLine()) != null)
			{
				int parsedDate = Integer.parseInt(text.substring(0, 2));
				int parsedMonth = Integer.parseInt(text.substring(3,5));
				int parsedYear = Integer.parseInt(text.substring(6,10));
				int parsedEventNo = Integer.parseInt(text.substring(11,13));
				
				for (int i = 0; i < events.size();i++)
				{
					
					int parsedCurrentEvent = Integer.parseInt(events.get(i).getName());
					if (dateNum ==  parsedDate && cal.getMonth().getValue() == parsedMonth && cal.getYear() == parsedYear && parsedCurrentEvent == parsedEventNo)
					{
						events.get(i).setVisible(true);
					}
					
				}
			}
			
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	* Produces an ImageIcon object from a .png image pathway and scales accordingly
	* @param imgPath pathway directory to the .png image
	* @return a converted ImageIcon object, ready to be utilised by JComponents
	* @author 19064114
	* */
	
	public ImageIcon scaleImage(String imgPath)
	{
		ImageIcon imgIcon = new ImageIcon("icons/"+imgPath);
		
		Image image = imgIcon.getImage(); // transform it 
		Image newimg = image.getScaledInstance(8, 8,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
		imgIcon = new ImageIcon(newimg);  // transform it back
		
		return imgIcon;
	}
	
	/**
	* Set the text to a new date and changes text colour accordingly
	* @param newDate integer date to change to
	* @param newCal new calendar to reference
	* @author 19064114
	* */
	
	public void changeDate(int newDate, CalendarSupport newCal)
	{
		this.cal = newCal;
		this.dateNum = newDate;
		this.dateLab.setText(""+newDate+"");
		
		Color textColor;
		
		if ((this.dateNum > 14 && row == 0) || (this.dateNum < 14 && (row == 5 || row == 4))) 
		{
			textColor = Color.GRAY;
		}
		else
		{
			textColor = Color.BLACK;
		}
		
		dateLab.setForeground(textColor);
	}
	
	/**
	* Get the current month given date and row and column indexes
	* @param dateIn destination date
	* @param row destination row index of array
	* @param column destination column index of array
	* @return The month in integer form
	* @author 19064114
	* */
	
	public int getMonth(int dateIn, int row, int column)
	{
		if (dateIn > 14 && row == 0)
		{
			return cal.getMonth().getValue() - 1;
		}
		else if (dateIn < 14 && row >= 4)
		{
			return cal.getMonth().getValue()+1;
		}
		return cal.getMonth().getValue();
	}
	
	// Getters and Setters
	
	/**
	* Get method for layered pane
	* @return JLayeredPane object if it exists
	* @author 19064114
	* */
	public JLayeredPane getLayer()
	{
		return this.layer;
	}
	/**
	* Get method for counter for events
	* @return The integer value for event count
	* @author 19064114
	* */
	public int getEventsNum() {
		return eventsNum;
	}
	
	/**
	* Get current date 
	* @return The date number in integer form
	* @author 19064114
	* */
	public int getDateNum() {
		return dateNum;
	}
	
	/**
	* Set method for a new date number
	* @param dateNum the new date to be set
	* @author 19064114
	* */
	public void setDateNum(int dateNum) {
		this.dateNum = dateNum;
	}
	
	/**
	* Get method for current label for the date
	* @return The JLabel object of the date
	* @author 19064114
	* */
	public JLabel getDateLab() {
		return dateLab;
	}
	
	/**
	* Get method for ArrayList of JLabels for events
	* @return The list of event icons
	* @author 19064114
	* */
	public ArrayList<JLabel> getEvents() {
		return events;
	}
	
}
