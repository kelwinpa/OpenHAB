package Sensor;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.UIManager;

import Storage.Storage;

import java.awt.event.ActionListener;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.awt.event.ActionEvent;

public class GUI {

	private JFrame frmMonitoringSimulator;
	private JTextField textField_people_bathroom;
	private JTextField textField_people_sleepingroom;
	private JTextField textField_people_kitchen;
	private JTextField textField_people_livingroom;
	private JTextField textField_temperature;
	private JButton button_people_sleepingroom;
	private JButton button_people_kitchen;
	private JButton button_people_livingroom;
	private JButton button_temperature;
	
	static BlockingQueue<Integer> queue_people_livingroom = new LinkedBlockingQueue<Integer>(1); //Needed to communicate with Monitoring thread (to pass people amount)
	static BlockingQueue<Integer> queue_people_kitchen = new LinkedBlockingQueue<Integer>(1);
	static BlockingQueue<Integer> queue_people_sleepingroom = new LinkedBlockingQueue<Integer>(1);
	static BlockingQueue<Integer> queue_people_bathroom = new LinkedBlockingQueue<Integer>(1);
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frmMonitoringSimulator.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		Thread t = new Thread(new Monitoring());
		t.start();
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmMonitoringSimulator = new JFrame();
		frmMonitoringSimulator.setTitle("Monitoring Simulator");
		frmMonitoringSimulator.setBounds(100, 100, 450, 211);
		frmMonitoringSimulator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMonitoringSimulator.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Temperature (\u00B0C)");
		lblNewLabel.setBounds(10, 11, 112, 14);
		frmMonitoringSimulator.getContentPane().add(lblNewLabel);
		
		JLabel lblPeopleInLiving = new JLabel("People in Living Room");
		lblPeopleInLiving.setBounds(10, 67, 112, 14);
		frmMonitoringSimulator.getContentPane().add(lblPeopleInLiving);
		
		JLabel lblPeopleInKitchen = new JLabel("People in Kitchen");
		lblPeopleInKitchen.setBounds(10, 92, 112, 14);
		frmMonitoringSimulator.getContentPane().add(lblPeopleInKitchen);
		
		JLabel lblPeopleInSleeping = new JLabel("People in Sleeping Room");
		lblPeopleInSleeping.setBounds(10, 117, 133, 14);
		frmMonitoringSimulator.getContentPane().add(lblPeopleInSleeping);
		
		JLabel lblPeopleInBathroom = new JLabel("People in Bathroom");
		lblPeopleInBathroom.setBounds(10, 142, 112, 14);
		frmMonitoringSimulator.getContentPane().add(lblPeopleInBathroom);
		
		textField_people_bathroom = new JTextField();
		textField_people_bathroom.setToolTipText("int");
		textField_people_bathroom.setBounds(187, 139, 86, 20);
		frmMonitoringSimulator.getContentPane().add(textField_people_bathroom);
		textField_people_bathroom.setColumns(10);
		
		textField_people_sleepingroom = new JTextField();
		textField_people_sleepingroom.setToolTipText("int");
		textField_people_sleepingroom.setBounds(187, 114, 86, 20);
		frmMonitoringSimulator.getContentPane().add(textField_people_sleepingroom);
		textField_people_sleepingroom.setColumns(10);
		
		textField_people_kitchen = new JTextField();
		textField_people_kitchen.setToolTipText("int");
		textField_people_kitchen.setBounds(187, 89, 86, 20);
		frmMonitoringSimulator.getContentPane().add(textField_people_kitchen);
		textField_people_kitchen.setColumns(10);
		
		textField_people_livingroom = new JTextField();
		textField_people_livingroom.setToolTipText("int");
		textField_people_livingroom.setText("1");
		textField_people_livingroom.setBounds(187, 64, 86, 20);
		frmMonitoringSimulator.getContentPane().add(textField_people_livingroom);
		textField_people_livingroom.setColumns(10);
		
		textField_temperature = new JTextField();
		textField_temperature.setText("10.0");
		textField_temperature.setToolTipText("Temperature that you want to reach (float)");
		textField_temperature.setBounds(187, 8, 86, 20);
		frmMonitoringSimulator.getContentPane().add(textField_temperature);
		textField_temperature.setColumns(10);
		
		JButton button_people_bathroom = new JButton("Confirm");
		button_people_bathroom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(textField_people_bathroom.getText().equals(""))
					return;
				try {
					int f = Integer.parseInt(textField_people_bathroom.getText());
					queue_people_bathroom.put(f);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				} catch(NumberFormatException e1) {
					return;
				}
			}
		});
		button_people_bathroom.setBackground(Color.LIGHT_GRAY);
		button_people_bathroom.setBounds(312, 138, 89, 23);
		frmMonitoringSimulator.getContentPane().add(button_people_bathroom);
		
		button_people_sleepingroom = new JButton("Confirm");
		button_people_sleepingroom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textField_people_sleepingroom.getText().equals(""))
					return;
				try {
					int f = Integer.parseInt(textField_people_sleepingroom.getText());
					queue_people_sleepingroom.put(f);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				} catch(NumberFormatException e1) {
					return;
				}
			}
		});
		button_people_sleepingroom.setBackground(Color.LIGHT_GRAY);
		button_people_sleepingroom.setBounds(312, 113, 89, 23);
		frmMonitoringSimulator.getContentPane().add(button_people_sleepingroom);
		
		button_people_kitchen = new JButton("Confirm");
		button_people_kitchen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textField_people_kitchen.getText().equals(""))
					return;
				try {
					int f = Integer.parseInt(textField_people_kitchen.getText());
					queue_people_kitchen.put(f);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				} catch(NumberFormatException e1) {
					return;
				}
			}
		});
		button_people_kitchen.setBackground(Color.LIGHT_GRAY);
		button_people_kitchen.setBounds(312, 88, 89, 23);
		frmMonitoringSimulator.getContentPane().add(button_people_kitchen);
		
		button_people_livingroom = new JButton("Confirm");
		button_people_livingroom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(textField_people_livingroom.getText().equals(""))
					return;
				try {
					int f = Integer.parseInt(textField_people_livingroom.getText());
					queue_people_livingroom.put(f);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch(NumberFormatException e) {
					return;
				}
			}
		});
		button_people_livingroom.setBackground(Color.LIGHT_GRAY);
		button_people_livingroom.setBounds(312, 63, 89, 23);
		frmMonitoringSimulator.getContentPane().add(button_people_livingroom);
		
		button_temperature = new JButton("Confirm");
		button_temperature.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(textField_temperature.getText().equals(""))
					return;
				try {
					Float.parseFloat(textField_temperature.getText());
				} catch(NumberFormatException e) {
					return;
				}
				float f = Float.parseFloat(textField_temperature.getText());
				Monitoring.setWantedTemperature(f);
			}
		});
		button_temperature.setBackground(Color.LIGHT_GRAY);
		button_temperature.setBounds(312, 7, 89, 23);
		frmMonitoringSimulator.getContentPane().add(button_temperature);
	}
}
