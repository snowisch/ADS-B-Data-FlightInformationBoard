package com.adsbdata.recoder;

import java.io.*;
import java.sql.*;
import java.text.*;

import com.adsbdata.executer.AdsbMain;
import com.adsbdata.gadget.Epoch;

	public class AdsbMomentRecord {
		
								//*: not null ':type A ":type B
		public String msgType;			//1*	Data type
		public Integer transType;		//2*	transformation type
		public String sessionId;		//3*
		public String aircraftId;		//4
		public String hexident;		//5*	Actually aircraftId hex
		public Long flightId;			//6*	Actually messageId
		/*
		Date DATA_MSG			//7	data date
		Date TIME_MSG			//8 data time
		Date DATA_MSG_LOG		//9* log date
		Date TIME_MSG_LOG		//10* log time
		*/
		public Long timeMsg;			//comebined date and time, epoch format
		public Long timeMsgLog;
		
		public String callsign;			//11* 
		public Integer altitude;		//12' 
		public Double groundSpeed;		//13" 
		public Double track;			//14" direction
		public Double latitude;			//15' 
		public Double longitude;		//16' 
		public Double verticalRate;		//17" 
		public String squawk;			//18 squawk id
		public String alert;			//19 
		public String emergency;		//20 
		public String spi;				//21 tag sign
		//empty							//22 blank for backup
		
		public static void writeToConsole(AdsbMomentRecord record) throws IOException {
			String logTime = Epoch.epochTodate(record.timeMsgLog, "yyyy/MM/dd HH:mm:ss.SSS");
			System.out.println("AircraftId " + record.hexident
					+ "   MessageID " + record.flightId 
					+ "   LogTime " + logTime 
					+ "   Alt " + record.altitude 
					+ "   Lat " + record.latitude 
					+ "   Lon " + record.longitude
					+ "   Spd " + record.groundSpeed
					+ "   Trk " + record.track
					+ "   VTR " + record.verticalRate);
		}
	
		public static AdsbMomentRecord convertCharToClass(String rawRecord) throws IOException {
			rawRecord = rawRecord + " ,";
//			System.out.println(rawRecord);
			AdsbMomentRecord record = new com.adsbdata.recoder.AdsbMomentRecord();
//			if (rawRecord.length() > 100) {
//				System.out.println(rawRecord.length());
//			}
			String arr[] = new String[22];
			arr = rawRecord.split(",");
			//System.out.println(arr[21]);
//			for (int i = 0; i <= 20; i++) {
//				System.out.println(i + " " + arr[i]);
//			}
			record.msgType = arr[0];
			record.transType = arr[1].isEmpty() ? null : Integer.parseInt(arr[1]);
			record.sessionId = arr[2];
			record.aircraftId = arr[3];
			record.hexident = arr[4];
			record.flightId = Long.parseLong(arr[5]);
			
			if (arr[6] + " " + arr[7] != " ") {
				try {
					record.timeMsg = Epoch.dateToEpoch(arr[6] + " " + arr[7], "yyyy/MM/dd HH:mm:ss.SSS");
				} catch (ParseException e) {
				}
			}
			if (arr[8] + " " + arr[9] != " ") {
				try {
					record.timeMsgLog = Epoch.dateToEpoch(arr[8] + " " + arr[9], "yyyy/MM/dd HH:mm:ss.SSS");
				} catch (ParseException e) {
				}
			}
			
			record.callsign = arr[10].isEmpty() ? null : arr[10];
			record.altitude = arr[11].isEmpty() ? null : Integer.parseInt(arr[11]);
			record.latitude = arr[14].isEmpty() ? null : Double.parseDouble(arr[14]);
			record.longitude = arr[15].isEmpty() ? null : Double.parseDouble(arr[15]);
			record.groundSpeed	= arr[12].isEmpty() ? null : Double.parseDouble(arr[12]);
			record.track = arr[13].isEmpty() ? null : Double.parseDouble(arr[13]);
			record.verticalRate = arr[16].isEmpty() ? null : Double.parseDouble(arr[16]);
			
			record.squawk = arr[17];
			record.alert = arr[18];
			record.emergency = arr[19];
			record.spi = arr[20];
			//blank
			return record;
		}	
		
		public static void recordAddToLogDatabase(AdsbMomentRecord record) throws SQLException { //may expire later
			PreparedStatement psql;
			psql = AdsbMain.conn.prepareStatement("insert into adsbdatabase ("
					+ "hexident, "
					+ "flightId, "
					+ "timeMsgLog, "
					+ "altitude, "
					+ "latitude, "
					+ "longitude, "
					+ "groundSpeed, "
					+ "track, "
					+ "verticalRate) "
					+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?)"); //only key parameters are added to database. the list can be extended later
			psql.setObject(1, record.hexident);
			psql.setObject(2, record.flightId);
			psql.setObject(3, record.timeMsgLog);
			psql.setObject(4, record.altitude);
			psql.setObject(5, record.latitude);
			psql.setObject(6, record.longitude);
			psql.setObject(7, record.groundSpeed);
			psql.setObject(8, record.track);
			psql.setObject(9, record.verticalRate);
			psql.executeUpdate();
		}
	}