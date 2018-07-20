package com.adsbdata.logic;

import com.adsbdata.gadget.GeographicCoordinate;
import com.adsbdata.recoder.AircraftInfo;

public class AircraftStatusInterpolation {
	public static AircraftInfo interpolate(Long timeNow, AircraftInfo record) {
		if (record.latitude == null
				|| record.groundSpeed == null
				|| record.longitude == 0d
				|| record.track == null
				|| record.verticalRate == null) {
			return record;
		} else {
			AircraftInfo newRecord = interpolateByParameters(
					timeNow, 
					record.hexident, 
					record.previousStatus,
					record.callsign, 
					record.timeMsgLog, 
					record.latitude, 
					record.longitude, 
					record.groundSpeed, 
					record.verticalRate, 
					record.altitude, 
					record.track);
			return newRecord;
		}
	}
	
	public static AircraftInfo interpolateByParameters(Long timeNow, String hex, String pst, String csn, Long epc, Double lat, Double lon, Double spd, Double vrt, Integer alt, Double trk) {
		AircraftInfo record = new AircraftInfo();
		record.hexident = hex;
		record.previousStatus = pst;
		record.callsign = csn;
		record.groundSpeed = spd;
		record.verticalRate = vrt;
		record.track = trk;
		double[] aircraftRecordLocation = {lon, lat};
		//System.out.println("calc NewLatLon");
		double[] newLocation =  GeographicCoordinate.getNewLatLon(
				spd * AircraftStatusCalculator.ktToKms * (timeNow - epc) / 1000d, 
				trk, 
				aircraftRecordLocation);
		record.longitude = newLocation[0];
		record.latitude = newLocation[1];
		if (alt != null) {
			record.altitude = (int) (alt + vrt * AircraftStatusCalculator.ftmToMs * (timeNow - epc) / 1000d);
		} else {
			record.altitude = null;
		}
		record.timeMsgLog = epc;
		//newRecord.timeMsgLog = timeNow;
		return record;
	}

	public static String getInterpolationTime(long timeNow, long epc) {
		return String.format("%.2f", (timeNow - epc) / 1000d);
	}
}