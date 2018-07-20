package com.adsbdata.executer;



import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import com.adsbdata.collector.SocketReader;
//import com.adsbdata.gadget.DatabaseConnection;
import com.adsbdata.recoder.AdsbMomentRecord;
import com.adsbdata.recoder.AircraftInfo;
//import com.adsbdata.recoder.LandingTimeTable;
import com.adsbdata.surveyor.AirportConstant;
import com.adsbdata.watcher.AircraftsMapPlot;

public class AdsbMain {
	private static final long maxDelyTime = 60L * 1000L;
	
	private static final String adsbServerAddress = "115.239.244.202";
	private static final int socketPort = 1106;
	
	//private static Connection conn = null;
	public static Connection conn = null;

	public static final long timeSys0 = System.currentTimeMillis();	
	private static long lastPlotTime = 0L;
	private static boolean aircraftsMapPlotted = false;
	private static int recordCounter = 0;
	private static int discardedRecordNumber = 0;
	
	public static Map<String, AircraftInfo> AircraftsMap = new HashMap<String, AircraftInfo>();  
	
	public static void adsbDataProccessingMultithreading() throws Exception {
		//conn = DatabaseConnection.connectDatabase("root", "", "testdatabase");
		
//		if (!DatabaseConnection.databaseExist("aircraftslandingtimedatabase")) {
//			LandingTimeTable.createAircraftsLandingTimeDatabase();
//		}
		AirportConstant.Initialize();
		
	    SocketReader client = new SocketReader();
	    try {
	        client.connect(socketPort, adsbServerAddress);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
//	    if (conn != null) {
//			DatabaseConnection.disconnectDatabase(conn, "testdatabase");
//		}
	}
	
	public static void adsbRecordProccessing(String rawRecord) throws Exception{
		recordCounter++;
		AdsbMomentRecord record = AdsbMomentRecord.convertCharToClass(rawRecord);
		long timeSys = System.currentTimeMillis();
		if (true) {//(AircraftsMapPlot.checkAircraftOnMap(record)) {
			if (timeSys - record.timeMsgLog < maxDelyTime) {
				AircraftInfo.recordAddToAircraftsMap(record);
				//System.out.println(rawRecord);
				//AdsbMomentRecord.writeToConsole(record);
				if (timeSys - lastPlotTime >= AircraftsMapPlot.mapRefreshTime && recordCounter > 1) {
					lastPlotTime = timeSys;
					AircraftInfo.cleanAircraftsMap(record.timeMsgLog);
					aircraftsMapPlotted = AircraftsMapPlot.showAircraftsMapNow(record.timeMsgLog, aircraftsMapPlotted);
					//System.out.println((timeSys - record.timeMsgLog) + "ms Delyed");
					if (discardedRecordNumber / recordCounter > 0.1d || true) {
						System.out.println(String.format("%.2f", 100.0f * discardedRecordNumber / recordCounter) + "% of " + recordCounter + " records discarded.");
					}
					discardedRecordNumber = 0;
					recordCounter = 0;
				}
			} else {
				discardedRecordNumber++;
			}
		}
	}
}
