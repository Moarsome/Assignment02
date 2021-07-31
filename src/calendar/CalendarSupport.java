package calendar;

import java.time.LocalDate;
import java.time.Month;

/**
* Provides the basis for organising dates into a visible grid layout
* Dates are recorded accurately using Java time LocalDate.
* @author Kylie Crump 19064114
**/

public class CalendarSupport {
	private int[][] datesCurrent;
	private LocalDate now;
	private LocalDate setTime;
	private Month currentMonth;
	private int currentYear;
	/**
	* Constructor for CalendarSupport class
	* @author 19064114
	* */
	public CalendarSupport()
	{
		now = LocalDate.now();
		setTime = LocalDate.of(now.getYear(), now.getMonth(), 1);
		
		setDates(-1);
	}
	/**
	* Sets correct dates upon moving between months
	* @param moveMonth the direction in which the month moves (-1 for previous and 1 for next month)
	* @author 19064114
	* */
	public void setDates(int moveMonth)
	{
		currentYear = setTime.getYear();
		boolean leapYear = setTime.isLeapYear();
		int moveMonthValue = setTime.getMonthValue()+moveMonth;
		
		// If the month value moves out of bounds, it means the year has changed
		if (moveMonthValue < 0) 
		{
			// Move year backwards
			moveMonthValue = 11;
			currentYear--;
		}
		if (moveMonthValue > 11)
		{
			// Move year forwards
			moveMonthValue = 0;
			currentYear++;
		}
		
		// Retrieve the current month in enum form
		// The enum form provides more functionality and management
		currentMonth = Month.values()[moveMonthValue];
		// Set LocalDate object to the destination time, with adjustments included
		setTime = LocalDate.of(currentYear, currentMonth, 1);
		
		int date = 1;
		// Establishing a new 2D array of date values
		datesCurrent = new int[6][7];
		
		// Iterate through each date in new array
		for (int row = 0; row < 6; row++)
		{
			for (int column = 0; column < 7; column++)
			{
				// If the current date is not higher than the length of the month
				if (!(date > currentMonth.length(leapYear)))
				{
					// If the day of the week matches the column it is in (e.g if monday(1) equals column+1(1))
					// Set the date
					if (setTime != null && setTime.getDayOfWeek().getValue() == column+1 )
					{
						datesCurrent[row][column] = date;
						setTime = null;
					}
					// If day of week does not match
					else if (setTime != null)
					{
						int monthValue = currentMonth.getValue()-2;
						if (monthValue < 0) 
							monthValue = 11;
						
						Month previousMonth = Month.values()[monthValue];
						int len = previousMonth.length(setTime.isLeapYear());
						
						datesCurrent[row][column] = len - setTime.getDayOfWeek().getValue() + column + 2;
					}
					
					if (setTime == null)
					{
						datesCurrent[row][column] = date;
						date++;
					}	
				}
				else
				{
					date = 1;
					datesCurrent[row][column] = date;
					date++;
				}
			}
		}
		
		setTime = LocalDate.of(currentYear, currentMonth, 1);
	}
	
	
	// Getters and Setters
	
	/**
	* Get method for current date
	* @param row destination row in datesCurrent array
	* @param column destination column in datesCurrent array
	* @return The current date if it exists
	* @author 19064114
	* */
	public int getDate(int row, int column)
	{
		return this.datesCurrent[row][column];
	}
	/**
	* Get method for current month
	* @return The current month in enum Month form
	* @author 19064114
	* */
	public Month getMonth()
	{
		return currentMonth;
	}
	/**
	* Get method for current year
	* @return The current year in int form
	* @author 19064114
	* */
	public int getYear()
	{
		return currentYear;
	}
	
}
