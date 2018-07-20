package com.adsbdata.logic;

import java.io.IOException;
import java.sql.SQLException;

import com.adsbdata.gadget.Epoch;
import com.adsbdata.recoder.AircraftInfo;
import com.adsbdata.surveyor.AirportConstant;
//import com.adsbdata.recoder.LandingTimeTable;

public class AircraftStatus {
	/**
	 * For debugging and protecting AircraftInfo record.
	 * @param timeNow
	 * @param record
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public static String getAircraftStatus(long timeNow, AircraftInfo record) throws IOException {
		//System.out.println((record.callsign == null ? "" : record.callsign);
		//record = AircraftStatusInterpolation.interpolate(timeNow, record);
		String Info = 
				AircraftStatus.getAircraftStatusByParameters(
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
		record.previousStatus = Info;
		return Info;
	}
	
	/**
	 * Parameters are as the method above.
	 * This method will give the aircrafts' status in text format, including:
	 * 	Static,
	 * 	Sliding in airport,
	 *  Normally flying, Climbing and declining,
	 *  Taking off and Landing on a specific runway,
	 *  Being on the Final 
	 *  and the estimate time left to Landing.
	 * and will print aircrafts' timeline.
	 * @param timeNow
	 * @param hex
	 * @param pst
	 * @param csn
	 * @param epc
	 * @param lat
	 * @param lon
	 * @param spd
	 * @param vrt
	 * @param alt
	 * @param trk
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	private static String getAircraftStatusByParameters(Long timeNow, String hex, String pst, String csn, Long epc, Double lat, Double lon, Double spd, Double vrt, Integer alt, Double trk) throws IOException {
		String Info = "Unknown Status";
		if (lon == null 
				|| lat == 0d 
				|| spd == null 
				|| vrt == null 
				|| trk == null) return Info;
		csn = (csn == null ? "An unknown callsign aircraft" : csn);
		
		if (spd == 0d) {
			Info = "Static"; //stay
		}
		if (vrt == 0d && spd > 0 && (pst == null || pst != null && !(pst.indexOf("TakingOff") >= 0) 
													&& !(pst.indexOf("InboundFinal") >= 0))) {
			Info = "Sliding"; //sliding
			if (AircraftStatusCalculator.getAircraftdetachRunway(lat, lon) >=0) {
				Info += "," + AircraftStatusCalculator.getAircraftdetachRunwayDirection(lat, lon, trk)
						+ ",Detachrunway," + AircraftStatusCalculator.getAircraftdetachRunwayText(lat, lon);
			} else {
				if (pst != null) {
					if (pst.indexOf("FromAirport") >= 0) {
						System.out.println(Epoch.epochTodate(timeNow, "HH:mm:ss") + " " + csn + " detached from airport.");
					}
					if (pst.indexOf("ToAirport") >= 0) {
						System.out.println(Epoch.epochTodate(timeNow, "HH:mm:ss") + " " + csn + " detached to airport.");
					} 
				}
			}
		}
		
		if (pst == null || pst != null && !(pst.indexOf("InboundFinal") >= 0) 
						   && !(pst.indexOf("Landing") >= 0)) {
			if (spd >= AircraftStatusCalculator.maxSlidingSpeed && Math.abs(vrt) == 0d && alt != null) Info = "Flying"; //normally flying
			if (spd >= AircraftStatusCalculator.maxSlidingSpeed && vrt > 0d) Info = "Climbing"; //gaining altitude
			if (spd >= AircraftStatusCalculator.maxSlidingSpeed && vrt < 0d) Info = "Declining"; //losing altitude
		}
		
		if (AircraftStatusCalculator.getAircraftMinTakingOffTrk(lat, lon, trk) < AircraftStatusCalculator.courseAngle
				&& AircraftStatusCalculator.getAircraftMinTakingOffDistance(lat, lon, trk) < 5d) {
			if (vrt > 0d || (pst != null && pst.indexOf("TakingOff") >= 0)) {
					Info = "TakingOff"; 
					Info += ",Runway," + AircraftStatusCalculator.getAircraftTakingOffRunwayText(lat, lon, trk);
					if (pst != null && (pst.indexOf("Sliding") >= 0 /*|| pst.indexOf("Flying") >= 0*/) 
									&& !(pst.indexOf("Landing") >= 0) 
									&& !(pst.indexOf("SlidingL") >= 0)) {
						System.out.println(Epoch.epochTodate(timeNow, "HH:mm:ss") + " " + csn + " Taked Off.");
					}
			}
		}
		
		if (AircraftStatusCalculator.getAircraftMinLandingDistance(lat, lon, trk) <= AircraftStatusCalculator.airportAreaRadius) { //Aircraft is in airport area)
			if (AircraftStatusCalculator.getAircraftMinLandingTrk(lat, lon, trk) < AircraftStatusCalculator.courseAngle * 1.5d //Aircraft is targeting a runway
					//&& Math.abs(AircraftStatusCalculator.getDepressionAngle(lat, lon, trk, alt - AircraftConstant.airportAltitude[AircraftStatusCalculator.getAircraftLandingRunway(lat, lon, trk)]) - AircraftStatusCalculator.glideAngle) <= AircraftStatusCalculator.glideAngleBand * 15d
					) {
				if (AircraftStatusCalculator.getAircraftLandingRunway(lat, lon, trk) >= 0
						&& alt != null) {//Aircraft is getting closer to the closest runway
					Info = "InboundFinal"; //in Final
					Info += ",Runway," + AircraftStatusCalculator.getAircraftLandingRunwayText(lat, lon, trk);
					Info += ",Time," + AircraftStatusCalculator.getAircraftLandingTimeText(timeNow, epc, lat, lon, spd, alt - AirportConstant.airportAltitude[AircraftStatusCalculator.getAircraftLandingRunway(lat, lon, trk)], trk, "HH:mm:ss");
					//Info += aircraftHightToGlideAngleRate(lat, lon, alt - AircraftConstant.airportAltitude[AircraftStatusCalculator.getAircraftLandingRunway(lat, lon, trk)]);
					if (pst != null 
							&& (pst.indexOf("Flying") >= 0 
								|| pst.indexOf("Climbing") >= 0 
								|| pst.indexOf("Declining") >= 0 
								|| pst.indexOf("Turning") >= 0)) {
						System.out.println("     " + Epoch.epochTodate(timeNow, "HH:mm:ss") + " " + csn + " joined the Final. Will arrive at " + 
								AircraftStatusCalculator.getAircraftLandingTimeText(timeNow, epc, lat, lon, spd, alt - AirportConstant.airportAltitude[AircraftStatusCalculator.getAircraftLandingRunway(lat, lon, trk)], trk, "HH:mm:ss"));
					}
				} else {// vrt==0 or the message trans type 2 is received
					if (vrt == 0 && (alt == null || alt == 0) 
							&& (pst == null || !(pst.indexOf("TakingOff") >= 0))) {
						Info = "Sliding";
						Info += ",Runway," + AircraftStatusCalculator.getAircraftTakingOffRunwayText(lat, lon, trk);
						if (pst != null && pst.indexOf("Landing") >= 0) {
							System.out.println("     " + Epoch.epochTodate(timeNow, "HH:mm:ss") + " " + csn + " Landed.");
							Info = "SlidingL";
							Info += ",Runway," + AircraftStatusCalculator.getAircraftTakingOffRunwayText(lat, lon, trk);
						}
					} else {
						if (pst != null && (pst.indexOf("InboundFinal") >= 0 
										|| pst.indexOf("Landing") >= 0)) {
							Info = "Landing";
							Info += ",Runway," + AircraftStatusCalculator.getAircraftTakingOffRunwayText(lat, lon, trk);
						}
					}
				}
			} else {
//				if (AircraftStatusCalculator.getAircraftMinLandingTrk(lat, lon, trk) < AircraftStatusCalculator.turningFinalAngle 
//						&& alt != null && AircraftStatusCalculator.getAircraftAltitude(lat, lon, trk, alt) < 900) {
//					Info = "TurningFinal";
//					if (pst != null && (pst.indexOf("Flying") >= 0 || pst.indexOf("Climbing") >= 0 || pst.indexOf("Declining") >= 0 || pst.indexOf("Turning") >= 0)) {
//						//AircraftsMapPlot.specialPosition[0][0] = lon;
//						//AircraftsMapPlot.specialPosition[1][0] = lat;
//						System.out.println((Epoch.epochTodate(timeNow, "HH:mm:ss") + " " + csn + " started turing Final.");
//						//System.out.println((lon + " " + lat);
//					}
//				}
			}
		}
		//Info += ", " + AircraftStatusCalculator.getAircraftGroundSpeed(spd);
		Info += ", " + (timeNow - epc);
		Info = csn + "," + Info;
		return Info;
	}
}