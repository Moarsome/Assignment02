package calendar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.*;

/**
* Establishes right GUI panel
* Showcases the events/tasks the user has manually entered
* @author Kylie Crump 19064114
**/

public class TaskPanel extends JPanel {
	private ArrayList<String> tasks;
	private JTextField field;
	private JScrollPane scroll;
	private JList list;
	
	/**
	* Constructor for the JPanel of TaskPanel
	* @author 19064114
	* */
	
	public TaskPanel()
	{
		tasks = new ArrayList<String>();
		list = new JList(tasks.toArray());
		
		JLabel header = new JLabel(" EVENTS");
		header.setFont(new Font("Helvetica", Font.PLAIN, 25));
		header.setPreferredSize(new Dimension(400,30));
		add(header);
		
		field = new JTextField("Add event to selected date");
		field.addActionListener(new EnterText());
		field.addFocusListener(new FocusText());
		field.setPreferredSize(new Dimension(400,40));
		add(field);
		
		list.setFont(new Font("Helvetica", Font.PLAIN, 15));
		list.setFixedCellHeight(30);
		scroll = new JScrollPane(list);
		scroll.setPreferredSize(new Dimension(400,325));
		add(scroll);
		
		setPreferredSize(new Dimension(400,420));
		setBackground(Color.getHSBColor(0.58f, 0.05f, 1.0f));
	}
	
	/**
	* Allows JTextField functionality
	* Adds an event to the selected date when the user presses 'enter'
	* @author Kylie Crump 19064114
	**/
	
	private class EnterText implements ActionListener
	{
		/**
		* When the user presses enter for the text field
		* @param e Retrieves the event of the user's interaction. Component is sourced from here.
		* @author 19064114
		* */
		@Override
		public void actionPerformed(ActionEvent e) {
			JTextField comp = (JTextField) e.getSource();
			
			try {
				BufferedReader in = new BufferedReader(new FileReader("dateSelected.txt"));
				String text = in.readLine();
				if (text.equals("null") == false)
				{
					try {
						PrintWriter out = new PrintWriter(new FileWriter("enteredEvents.txt", false));
						out.print(text.substring(0,2)+" "+text.substring(11));
						
						out.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					tasks.add("<html>"+text.substring(0,10)+" // <font color='"+text.substring(11)+"'>"+e.getActionCommand()+"</font></html>");
					
					update();
				}
				else
				{
					comp.setText("You must select a date on the calendar!");
				}
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
	}
	
	/**
	* When user is not using the text field, this class sets the default text to provide helpful information
	* It also removes any text to allow the user to type in an empty box
	* @author Kylie Crump 19064114
	**/
	
	private class FocusText implements FocusListener
	{
		/**
		* When the user clicks/gives focus to the text field, clear the box
		* @param e Retrieves the event of the user's interaction. Component is sourced from here.
		* @author 19064114
		* */
		@Override
		public void focusGained(FocusEvent e) {
			JTextField comp = (JTextField) e.getComponent();
			comp.setText("");
		}
		/**
		* If the user clicks away from the text field/loses focus
		* @param e Retrieves the event of the user's interaction. Component is sourced from here.
		* @author 19064114
		* */
		@Override
		public void focusLost(FocusEvent e) {
			JTextField comp = (JTextField) e.getComponent();
			comp.setText("Add event to selected date");
		}
	}
	/**
	* Updates the events panel with a sorted ArrayList (sorts by date)
	* @author 19064114
	* */
	private void update()
	{
		Collections.sort(tasks, new Comparator<String>() {
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		    @Override
		    public int compare(String s1, String s2) {
		       try {
		            return df.parse(s1.substring(6,16)).compareTo(df.parse(s2.substring(6,16)));
		        } catch (ParseException e) {
		            throw new IllegalArgumentException(e);
		        }
		    }
		});
		
		list.setListData(tasks.toArray());
	}
	
}
