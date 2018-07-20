package com.adsbdata.recoder;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import com.adsbdata.executer.AdsbMain;
import com.adsbdata.gadget.Epoch;
import com.adsbdata.logic.AircraftStatusCalculator;

public class AircraftInfo {
	
	public static final long expireTime = 60000L;
	public static final long maxExpireTime = 120000L; 
	
	/**
	 * The Info Object including:
	 *  aircraft id in hex format,
	 *  Message epoch, which is replaced by the epoch the message received, because that is always available,
	 *  latitude & longitude,
	 *  altitude /m,
	 *  Horizontal speed /kt, or later maybe /m*s^-1,
	 *  vertical speed /ft*min^-1, which is a multiple of 64,
	 *  track /degree,
	 *  ICAO Code
	 *  and the previous status text judged by class AircraftStatus.
	 */
	public String hexident;
	public Long timeMsgLog;
	public Double latitude;
	public Double longitude;
	public Integer altitude;
	public Double groundSpeed;
	public Double verticalRate;
	public Double track;
	public String callsign;
	public Long velocityEpoch;
	public Double acceleration;
	public String previousStatus;

	/**
	 * Add ADS-B message record to AircraftsMap, which is an information table for all valid aircrafts in ADS-B reciever's vision.
	 * There are 4 ADS-B message types
	 * The "timeMsgLog" parameter is recorded as the last epoch we received a location message, 
	 * 	because it's assumed that speed changes are much less sensitive than location in Interpolation process.
	 * @param record
	 * @throws IOException
	 */
	public static void recordAddToAircraftsMap(AdsbMomentRecord record) {
		try {
		if (AdsbMain.AircraftsMap.containsKey(record.hexident)) {
			AircraftInfo AircraftNewInfo = AdsbMain.AircraftsMap.get(record.hexident);
			switch (record.transType) {
			case 4:
//				if (AircraftNewInfo.groundSpeed > 0d) {
//					AircraftNewInfo.acceleration = AircraftStatusCalculator.getAcceleration(
//							record.timeMsg - AircraftNewInfo.velocityEpoch, 
//							AircraftNewInfo.track, record.track,
//							AircraftNewInfo.groundSpeed, record.groundSpeed,
//							AircraftNewInfo.verticalRate, record.verticalRate);
//				}
				AircraftNewInfo.groundSpeed = record.groundSpeed;
				AircraftNewInfo.verticalRate = record.verticalRate;
				AircraftNewInfo.track = record.track;
				AircraftNewInfo.velocityEpoch = record.timeMsgLog;
				if (AircraftNewInfo.timeMsgLog == null) {
					AircraftNewInfo.timeMsgLog = record.timeMsgLog;
				}
				break;
			case 3:
				AircraftNewInfo.latitude = record.latitude;
				AircraftNewInfo.longitude = record.longitude;
				AircraftNewInfo.altitude = record.altitude;
				AircraftNewInfo.timeMsgLog = record.timeMsgLog;
				break;
			case 2:
//				if (AircraftNewInfo.groundSpeed > 0d) {
//					AircraftNewInfo.acceleration = AircraftStatusCalculator.getAcceleration(
//							record.timeMsg - AircraftNewInfo.velocityEpoch, 
//							AircraftNewInfo.track, record.track,
//							AircraftNewInfo.groundSpeed, record.groundSpeed,
//							AircraftNewInfo.verticalRate, record.verticalRate);
//				}
				AircraftNewInfo.groundSpeed = record.groundSpeed;
				AircraftNewInfo.verticalRate = record.verticalRate;
				AircraftNewInfo.track = record.track;
				AircraftNewInfo.latitude = record.latitude;
				AircraftNewInfo.longitude = record.longitude;
				AircraftNewInfo.altitude = record.altitude;
				AircraftNewInfo.velocityEpoch = record.timeMsgLog;
				AircraftNewInfo.timeMsgLog = record.timeMsgLog;
				break;
			case 1:
				AircraftNewInfo.callsign = record.callsign;
				if (AircraftNewInfo.timeMsgLog == null) {
					AircraftNewInfo.timeMsgLog = record.timeMsgLog;
				}
				break;
			default:
				AdsbMomentRecord.writeToConsole(record);
			}
			AdsbMain.AircraftsMap.put(record.hexident, AircraftNewInfo);
		} else {
			AircraftInfo AircraftNewInfo = new AircraftInfo();
			AircraftNewInfo.groundSpeed = -1d;
			AircraftNewInfo.longitude = -1d;
			AircraftNewInfo.hexident = record.hexident;
			AdsbMain.AircraftsMap.put(record.hexident, AircraftNewInfo);
			recordAddToAircraftsMap(record);
		}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void statusUpdate(String key, String newStatus) {
		AircraftInfo record = AdsbMain.AircraftsMap.get(key);
		record.previousStatus = newStatus;
		AdsbMain.AircraftsMap.put(key, record);
	}
	
	/**
	 * Clean inactive aircrafts from AircraftMap, judging by the time since the last message received.
	 * This method is thread safe.
	 * @param timeNow
	 * @throws IOException
	 */	
	public static void cleanAircraftsMap(Long timeNow) {
        Iterator<Map.Entry<String, AircraftInfo>> it = AdsbMain.AircraftsMap.entrySet().iterator();  
        while (it.hasNext()) {  
        	Map.Entry<String, AircraftInfo> item = it.next();
            if(timeNow - item.getValue().timeMsgLog > expireTime) {
            	it.remove();
            }
        }
	}
	
	/**
	 * For exception outputting
	 * @param aircraftsMap
	 * @throws IOException
	 * @throws RuntimeException
	 */
	public static void writeToConsole(AircraftInfo record) throws IOException, RuntimeException {
		String logTime = Epoch.epochTodate(record.timeMsgLog, "yyyy/MM/dd HH:mm:ss.SSS");
		System.out.println(
				  "Id " + record.hexident
				+ " Time " + logTime
				+ " Csn " + record.callsign
				+ " Lat " + record.latitude
				+ " Lon " + record.longitude
				+ " Spd " + record.groundSpeed
				+ " Vrt " + record.verticalRate
				+ " Trk " + record.track
				+ " Alt " + record.altitude
				+ " PSt " + record.previousStatus
 				+ " ");
	}
}

