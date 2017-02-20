package Sensor;

import java.util.Random;

import org.eclipse.paho.client.mqttv3.MqttException;

import Storage.Storage;

public class Monitoring implements Runnable {
		
	private static float wantedTemperature;
	private static int humidity;
	private static int elecConsumption;
	private static int people_LivingRoom;
	private static int people_Kitchen;
	private static int people_Bathroom;
	private static int people_SleepingRoom;	
	
	/**
	 * Sends updates every 2 seconds
	 * @throws InterruptedException if cannot wait 2 seconds
	 * @throws MqttException 
	 */
	public static void start() throws InterruptedException, MqttException {
		Storage s = null;
		Paho p = new Paho();
		
		int LightsOn = 0, Lights_LivingRoom = 0, Lights_Kitchen = 0, Lights_SleepingRoom = 0, Lights_Bathroom = 0;
		String LivingRoom_Lights_l1 = "OFF", LivingRoom_Lights_l2 = "OFF", LivingRoom_Lights_l3 = "OFF", Kitchen_Lights_l1 = "OFF";
		String Kitchen_Lights_l2 = "OFF", SleepingRoom_Lights_l1 = "OFF", SleepingRoom_Lights_l2 = "OFF", Bathroom_Lights_l1 = "OFF";
		int Windows_Number = 0, Windows_LivingRoom = 0, Windows_Kitchen = 0, Windows_SleepingRoom = 0, Windows_Bathroom = 0;
		int LivingRoom_People = 0, Kitchen_People = 0, SleepingRoom_People = 0, Bathroom_People = 0;
		String LivingRoom_Windows_l1 = "OFF", LivingRoom_Windows_l2 = "OFF", LivingRoom_Windows_l3 = "OFF", Kitchen_Windows_l1 = "OFF", Kitchen_Windows_l2 = "OFF";
		String SleepingRoom_Windows_l1 = "OFF", Bathroom_Windows_l1 = "OFF";
		Object t = null;
		boolean init = true;
		
		while(true) {
			if(init) { //inventing random values to begin
				//should be read from DB, but it is useless since electricity consumption value is not involved in any OpenHAB rule
				elecConsumption = new Random().nextInt(267) + 1;
				humidity = new Random().nextInt(100);
				//send a random temperature, no more than 30 degrees
				setWantedTemperature(t == null ? new Random().nextFloat() * 30.0f : (float) t);
			}
			else { //setting values for electricity, humidity and temperature
				if(new Random().nextBoolean())
					elecConsumption++;
				if(new Random().nextBoolean())
					humidity++;
				else
					humidity--;
				//if an older value has already been set, return that value
				s = new Storage();
				t = s.read("temperature", "degrees", "ORDER BY ID DESC LIMIT 1");
				wantedTemperature = t == null ? wantedTemperature : (float) t;
			}
			t = GUI.queue_people_livingroom.poll();
			LivingRoom_People   = t == null ? LivingRoom_People : (int) t;
			t = GUI.queue_people_kitchen.poll();
			Kitchen_People      = t == null ? Kitchen_People : (int) t;
			t = GUI.queue_people_bathroom.poll();
			Bathroom_People     = t == null ? Bathroom_People : (int) t;
			t = GUI.queue_people_sleepingroom.poll();
			SleepingRoom_People = t == null ? SleepingRoom_People : (int) t;
			
			System.out.println("Beginning to read from DB");
			
			s = new Storage();
			t = s.read("general", "number", "WHERE subject=\"LightsOn\"");
			LightsOn = (t != null ? (int)t : LightsOn );
			s = new Storage();
			t = s.read("general", "number", "WHERE subject=\"Lights_LivingRoom\"");
			Lights_LivingRoom = (t != null ? (int)t : Lights_LivingRoom );
			s = new Storage();
			t = s.read("general", "number", "WHERE subject=\"Lights_Kitchen\"");
			Lights_Kitchen = (t != null ? (int)t : Lights_Kitchen );
			s = new Storage();
			t = s.read("general", "number", "WHERE subject=\"Lights_SleepingRoom\"");
			Lights_SleepingRoom = (t != null ? (int)t : Lights_SleepingRoom );
			s = new Storage();
			t = s.read("general", "number", "WHERE subject=\"Lights_Bathroom\"");
			Lights_Bathroom = (t != null ? (int)t : Lights_Bathroom );
			s = new Storage();
			t = s.read("light", "state", "WHERE name=\"LivingRoom_Lights_l1\"");
			LivingRoom_Lights_l1 = (t != null ? (String)t : LivingRoom_Lights_l1 );
			s = new Storage();
			t = s.read("light", "state", "WHERE name=\"LivingRoom_Lights_l2\"");
			LivingRoom_Lights_l2 = (t != null ? (String)t : LivingRoom_Lights_l2 );
			System.out.println("ATT: " + LivingRoom_Lights_l2);
			s = new Storage();
			t = s.read("light", "state", "WHERE name=\"LivingRoom_Lights_l3\"");
			LivingRoom_Lights_l3 = (t != null ? (String)t : LivingRoom_Lights_l3 );
			s = new Storage();
			t = s.read("light", "state", "WHERE name=\"Kitchen_Lights_l1\"");
			Kitchen_Lights_l1 = (t != null ? (String)t : Kitchen_Lights_l1 );
			s = new Storage();
			t = s.read("light", "state", "WHERE name=\"Kitchen_Lights_l2\"");
			Kitchen_Lights_l2 = (t != null ? (String)t : Kitchen_Lights_l2 );
			s = new Storage();
			t = s.read("light", "state", "WHERE name=\"SleepingRoom_Lights_l1\"");
			SleepingRoom_Lights_l1 = (t != null ? (String)t : SleepingRoom_Lights_l1 );
			s = new Storage();
			t = s.read("light", "state", "WHERE name=\"SleepingRoom_Lights_l2\"");
			SleepingRoom_Lights_l2 = (t != null ? (String)t : SleepingRoom_Lights_l2 );
			s = new Storage();
			t = s.read("light", "state", "WHERE name=\"Bathroom_Lights_l1\"");
			Bathroom_Lights_l1 = (t != null ? (String)t : Bathroom_Lights_l1 );
			
			s = new Storage();
			t = s.read("general", "number", "WHERE subject=\"Windows_Number\"");
			Windows_Number = (t != null ? (int)t : Windows_Number );
			s = new Storage();
			t = s.read("general", "number", "WHERE subject=\"Windows_LivingRoom\"");
			Windows_LivingRoom = (t != null ? (int)t : Windows_LivingRoom );
			s = new Storage();
			t = s.read("general", "number", "WHERE subject=\"Windows_Kitchen\"");
			Windows_Kitchen = (t != null ? (int)t : Windows_Kitchen );
			s = new Storage();
			t = s.read("general", "number", "WHERE subject=\"Windows_SleepingRoom\"");
			Windows_SleepingRoom = (t != null ? (int)t : Windows_SleepingRoom );
			s = new Storage();
			t = s.read("general", "number", "WHERE subject=\"Windows_Bathroom\"");
			Windows_Bathroom = (t != null ? (int)t : Windows_Bathroom );
			s = new Storage();
			t = s.read("window", "state", "WHERE name=\"LivingRoom_Windows_l1\"");
			LivingRoom_Windows_l1 = (t != null ? (String)t : LivingRoom_Windows_l1 );
			s = new Storage();
			t = s.read("window", "state", "WHERE name=\"LivingRoom_Windows_l2\"");
			LivingRoom_Windows_l2 = (t != null ? (String)t : LivingRoom_Windows_l2 );
			s = new Storage();
			t = s.read("window", "state", "WHERE name=\"LivingRoom_Windows_l3\"");
			LivingRoom_Windows_l3 = (t != null ? (String)t : LivingRoom_Windows_l3 );
			s = new Storage();
			t = s.read("window", "state", "WHERE name=\"Kitchen_Windows_l1\"");
			Kitchen_Windows_l1 = (t != null ? (String)t : Kitchen_Windows_l1 );
			s = new Storage();
			t = s.read("window", "state", "WHERE name=\"Kitchen_Windows_l2\"");
			Kitchen_Windows_l2 = (t != null ? (String)t : Kitchen_Windows_l2 );
			s = new Storage();
			t = s.read("window", "state", "WHERE name=\"SleepingRoom_Windows_l1\"");
			SleepingRoom_Windows_l1 = (t != null ? (String)t : SleepingRoom_Windows_l1 );
			s = new Storage();
			t = s.read("window", "state", "WHERE name=\"Bathroom_Windows_l1\"");
			Bathroom_Windows_l1 = (t != null ? (String)t : Bathroom_Windows_l1 );
			
			System.out.println("Seding with MQTT...");
			
			//Send updates
			p.send("home/general/electricity", elecConsumption + "");
			p.send("home/general/humidity", humidity + "");
			p.send("home/general/temperature", getWantedTemperature() + "");
			
			p.send("home/general/lights", LightsOn + "");
			p.send("home/livingroom/lights", Lights_LivingRoom + "");
			p.send("home/kitchen/lights", Lights_Kitchen + "");
			p.send("home/sleepingroom/lights", Lights_SleepingRoom + "");
			p.send("home/bathroom/lights", Lights_Bathroom + "");
			p.send("home/livingroom/lights/1", LivingRoom_Lights_l1 + "");
			p.send("home/livingroom/lights/2", LivingRoom_Lights_l2 + "");
			p.send("home/livingroom/lights/3", LivingRoom_Lights_l3 + "");
			p.send("home/kitchen/lights/1", Kitchen_Lights_l1 + "");
			p.send("home/kitchen/lights/2", Kitchen_Lights_l2 + "");
			p.send("home/sleepingroom/lights/1", SleepingRoom_Lights_l1 + "");
			p.send("home/sleepingroom/lights/2", SleepingRoom_Lights_l2 + "");
			p.send("home/bathroom/lights/1", Bathroom_Lights_l1 + "");
			
			p.send("home/general/windows", Windows_Number + "");
			p.send("home/livingroom/windows", Windows_LivingRoom + "");
			p.send("home/kitchen/windows", Windows_Kitchen + "");
			p.send("home/sleepingroom/windows", Windows_SleepingRoom + "");
			p.send("home/bathroom/windows", Windows_Bathroom + "");
			p.send("home/livingroom/windows/1", LivingRoom_Windows_l1 + "");
			p.send("home/livingroom/windows/2", LivingRoom_Windows_l2 + "");
			p.send("home/livingroom/windows/3", LivingRoom_Windows_l3 + "");
			p.send("home/kitchen/windows/1", Kitchen_Windows_l1 + "");
			p.send("home/kitchen/windows/2", Kitchen_Windows_l2 + "");
			p.send("home/sleepingroom/windows/1", SleepingRoom_Windows_l1 + "");
			p.send("home/bathroom/windows/1", Bathroom_Windows_l1 + "");
			
			p.send("home/livingroom/people", LivingRoom_People + "");
			p.send("home/kitchen/people", Kitchen_People + "");
			p.send("home/sleepingroom/people", SleepingRoom_People + "");
			p.send("home/bathroom/people", Bathroom_People + "");
			
			System.out.println("_______________________________");

			init = false;
			Thread.sleep(6000);
		} //while true
	}

	@Override
	public void run() {
		try {
			Monitoring.start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	public static float getWantedTemperature() {
		return wantedTemperature;
	}

	public static void setWantedTemperature(float wantedTemperature) {
		Monitoring.wantedTemperature = wantedTemperature;
		Storage s = new Storage();
		s.write("temperature", "degrees", getWantedTemperature() + "");
	}

	public static int getHumidity() {
		return humidity;
	}

	public static void setHumidity(int humidity) {
		Monitoring.humidity = humidity;
	}

	public static int getElecConsumption() {
		return elecConsumption;
	}

	public static void setElecConsumption(int elecConsumption) {
		Monitoring.elecConsumption = elecConsumption;
	}

	public static int getPeople_LivingRoom() {
		return people_LivingRoom;
	}

	public static void setPeople_LivingRoom(int people_LivingRoom) {
		Monitoring.people_LivingRoom = people_LivingRoom;
	}

	public static int getPeople_Kitchen() {
		return people_Kitchen;
	}

	public static void setPeople_Kitchen(int people_Kitchen) {
		Monitoring.people_Kitchen = people_Kitchen;
	}

	public static int getPeople_Bathroom() {
		return people_Bathroom;
	}

	public static void setPeople_Bathroom(int people_Bathroom) {
		Monitoring.people_Bathroom = people_Bathroom;
	}

	public static int getPeople_SleepingRoom() {
		return people_SleepingRoom;
	}

	public static void setPeople_SleepingRoom(int people_SleepingRoom) {
		Monitoring.people_SleepingRoom = people_SleepingRoom;
	}
	
}
