package calendar;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

/**
* The main Calendar application; 
* The JFrame is created and ran here
* @author Kylie Crump 19064114
**/

public class Calendar extends JFrame{
	private static CalendarPanel calPane;
	private static TaskPanel  taskPane;
	private static CalendarSupport cal;
	
	/**
	* Constructor method
	* @param title Establishes title of the new window
	* @author 19064114
	* */
	public Calendar(String title) throws IOException
	{
		super(title);
		
		setLayout(new FlowLayout());
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                File closeFile = new File("eventList.txt");
                closeFile.delete();
            }
        });

		calPane = new CalendarPanel();
		taskPane = new TaskPanel();
		cal = calPane.getCalendar();
		
		for (int row = 0; row < 6; row++)
		{
			for (int column = 0; column < 7; column++)
			{
				
				calPane.getDays()[row][column].getLayer().addMouseListener(new dateClick());
			}
		}
		
		calPane.getForwards().addMouseListener(new iconClick());
		calPane.getBackwards().addMouseListener(new iconClick());
		
		getContentPane().add(calPane);
		getContentPane().add(taskPane);
		setResizable(false);
		setBackground(Color.getHSBColor(0.58f, 0.2f, 1.0f));
		
		pack();
	}
	
	/**
	* Main method (runs the constructor method)
	* @param args contains arguments (unused)
	* @author 19064114
	* */
	
	public static void main(String[] args) throws IOException {
		JFrame frame = new Calendar("Calendar");
		frame.setVisible(true);
	}
	
	/**
	* Allows the user to click back and forth through the months and years through arrow icons
	* @author Kylie Crump 19064114
	**/
	
	private static class iconClick implements MouseListener
	{
		/**
		* Event for user clicking on either the forwards or backwards buttons
		* @param e Retrieves the event of the user's interaction. The component is sourced from this parameter.
		* @author 19064114
		* */
		@Override
		public void mouseClicked(MouseEvent e) {
			JLabel comp = (JLabel) e.getComponent();

			int monthValue = cal.getMonth().getValue();
			if (comp.getName().equals("forwards"))
			{
				
				cal.setDates(0);
			}
			else if (comp.getName().equals("backwards"))
			{
				cal.setDates(-2);
			}
			calPane.updatePanel();
		}

		/**
		* Event fired when mouse button on forwards/backwards buttons are pressed. Changes icon upon activation for purely aesthetic purposes.
		* @param e Retrieves the event of the user's interaction. The component is sourced from this parameter.
		* @author 19064114
		* */
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			JLabel comp = (JLabel) e.getComponent();
			comp.setIcon(calPane.scaleImage(comp.getName()+"Clicked.png"));
		}
		/**
		* Event fired when mouse is released from pressing on forwards/backwards buttons. Changes icon for purely aesthetic purposes.
		* @param e Retrieves the event of the user's interaction. The component is sourced from this parameter.title Establishes title of the new window
		* @author 19064114
		* */
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			JLabel comp = (JLabel) e.getComponent();

			comp.setIcon(calPane.scaleImage(comp.getName()+"Selected.png"));
			try {
				PrintWriter out = new PrintWriter(new FileWriter("dateSelected.txt", false));
				out.print("null");
				
				out.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		/**
		* Event fired when user enters the icon. Responds with a slight icon change. (method for aesthetics)
		* @param e Retrieves the event of the user's interaction. The component is sourced from this parameter.
		* @author 19064114
		* */
		
		@Override
		public void mouseEntered(MouseEvent e) {
			JLabel comp = (JLabel) e.getComponent();
			comp.setIcon(calPane.scaleImage(comp.getName()+"Selected.png"));
		}

		/**
		* Event fired when user enters the icon. Responds by changing icon back to default. (method for aesthetics)
		* @param e Retrieves the event of the user's interaction. The component is sourced from this parameter.
		* @author 19064114
		* */
		@Override
		public void mouseExited(MouseEvent e) {
			JLabel comp = (JLabel) e.getComponent();
			comp.setIcon(calPane.scaleImage(comp.getName()+".png"));
		}
	}
	
	/**
	* Provides clicking interaction with each date on the Calendar GUI
	* @author Kylie Crump 19064114
	**/
	
	private static class dateClick implements MouseListener
	{
		/**
		* Event fired when the user clicks. This is unused as mousePressed() performs the function sufficiently.
		* @param e Retrieves the event of the user's interaction.
		* @author 19064114
		* */
		@Override
		public void mouseClicked(MouseEvent e) {
		}

		/**
		* Event fired when the user presses mouse down on a date on the calendar GUI. Manages event adding and removing through file manipulation.  
		* @param e Retrieves the event of the user's interaction.
		* @author 19064114
		* */
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			JLayeredPane layer = (JLayeredPane) e.getComponent();
			JLabel comp = (JLabel) layer.getComponents()[layer.getComponentCount()-1];
			DayObject dateObj = calPane.getDayObj(Integer.valueOf(comp.getText()), cal.getMonth().getValue());
			JLayeredPane lastSelected = calPane.getLastSelected();
			
			if (lastSelected != null && dateObj.getEventsNum() != 5)
			{
				JLabel lastComp = (JLabel) lastSelected.getComponents()[lastSelected.getComponentCount()-1];
				lastComp.setBackground(Color.getHSBColor(0.58f, 0.01f, 1.0f));
				DayObject lastObj = calPane.getDayObj(Integer.valueOf(lastComp.getText()), cal.getMonth().getValue());
				
				try {
					File file = new File("enteredEvents.txt");
					
					BufferedReader in = new BufferedReader(new FileReader("enteredEvents.txt"));
					
					String text = in.readLine();
					
					if (Integer.valueOf(text.substring(0,2)) != Integer.valueOf(lastComp.getText()))
					{
						lastObj.removeEvent();
					}
					
					file.delete();
					
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					lastObj.removeEvent();
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			calPane.setLastSelected(layer);
			
			comp.setName("selected");
			comp.setBackground(Color.getHSBColor(0.58f, 0.1f, 1.0f));
			
			try {
				PrintWriter out = new PrintWriter(new FileWriter("dateSelected.txt", false));
				int dateVal = Integer.valueOf(comp.getText());
				int monthVal = cal.getMonth().getValue();
				int yearVal = cal.getYear();
				if (comp.getForeground().equals(Color.GRAY))
				{
					if (dateVal > 14)
						monthVal--;
					else
						monthVal++;
					if (monthVal == 13) // Going forwards 1 year
					{
						monthVal = 1;
						yearVal++;
					}
					else if(monthVal == 0) // Going backwards 1 year
					{
						monthVal = 12;
						yearVal--;
					}
				}
				
				Random random = new Random();
				EventType colour = EventType.values()[random.nextInt(EventType.values().length)];
				dateObj.addEvent(colour);
				
				out.print(String.format("%02d/%02d/%d %s", dateVal, monthVal, yearVal, colour.toString()));
				
				calPane.updatePanel();
				
				out.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				
		}
		/**
		* Event fired when the user clicks. This is unused as it is not needed.
		* @param e Retrieves the event of the user's interaction.
		* @author 19064114
		* */
		@Override
		public void mouseReleased(MouseEvent e) {
			
		}
		/**
		* Event fired when the user enters a date component. Responds with a change of background colour for aesthetic purposes.
		* @param e Retrieves the event of the user's interaction. Component is sourced from here.
		* @author 19064114
		* */
		@Override
		public void mouseEntered(MouseEvent e) {
			JLayeredPane layer = (JLayeredPane) e.getComponent();
			JLabel comp = (JLabel) layer.getComponents()[layer.getComponentCount()-1];
			
			if (!(layer.equals(calPane.getLastSelected())))
			{
				comp.setBackground(Color.getHSBColor(0.58f, 0.1f, 1.0f));
			}
			
		}
		/**
		* Event fired when the user exits a date component. Responds with changing background colour to default.
		* @param e Retrieves the event of the user's interaction. Component is sourced from here.
		* @author 19064114
		* */
		@Override
		public void mouseExited(MouseEvent e) {
			JLayeredPane layer = (JLayeredPane) e.getComponent();
			JLabel comp = (JLabel) layer.getComponents()[layer.getComponentCount()-1];

			if (!(layer.equals(calPane.getLastSelected())))
			{
				comp.setBackground(Color.getHSBColor(0.58f, 0.01f, 1.0f));
			}
		}
	}
	
}
