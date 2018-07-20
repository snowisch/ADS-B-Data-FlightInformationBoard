package com.adsbdata.logic;

import com.adsbdata.gadget.Epoch;
import com.adsbdata.gadget.GeographicCoordinate;
import com.adsbdata.surveyor.AirportConstant;

public class AircraftStatusCalculator {
	
	public static final double ktToKms = 0.51444d / 1000d;
	public static final double ftmToMs = 0.3048d / 60d;
	
	//Aviation Constant
	public static final double courseAngle = 3.0d;
	public static final double turningFinalAngle = 30d;
	public static final double glideAngle = 3.0d;
	public static final double glideAngleBand = 0.4d;
	public static final double standardLandingSpeed = 140d;
	public static final double maxSlidingSpeed = 200d;
	
	public static final double airportAreaRadius = 40d;
	public static final double llzDistance = 0.3d;
	public static final double[] approachDistance = {llzDistance, llzDistance + 5d, llzDistance + 10d, llzDistance + 20d, llzDistance + 30d};
  	public final static int numberOfCheckpoints = AircraftStatusCalculator.approachDistance.length;
	
  	/**
  	 * Basic Info
  	 * @return
  	 */
	public static int getAircraftAltitude(Double lat, Double lon, Double trk, Integer alt) {
		if (getAircraftNearestRunway(lat, lon, trk) < 0) return alt;
		else return alt - AirportConstant.airportAltitude[getAircraftNearestRunway(lat, lon, trk)];
	}	
	public static String getAircraftTrack(Double trk) {
		return String.valueOf(trk);
	}	
	public static String getAircraftGroundSpeed(Double spd) {
		return String.valueOf(spd);
	}	
	public static String getAircraftVerticalRate(Double vrt) {
		return String.valueOf(vrt/64.0d);
	}
	
    
    public static double getAcceleration(Long timeInterval, Double trk1, Double trk2, Double spd1, Double spd2, Double vrt1, Double vrt2) {
    	return -1d;
//    	if (trk1 == null || trk2 == null || spd1 == null || spd2 == null) return -1d;
//    	double trackDifference = GeographicCoordinate.angleToRadian(GeographicCoordinate.getTrackDifference(trk1, trk2));
//    	double horizonSpeedDifference = Math.sqrt(spd1 * spd1 + spd2 * spd2 - 2 * spd1 * spd2 * Math.cos(trackDifference)) * ktToKms * 1000d;
//    	if (vrt1 == null || vrt2 == null) return horizonSpeedDifference / timeInterval * 1000;
//    	double verticalSpeedDiffrence = (vrt1 - vrt2) * ftmToMs;
//    	return Math.sqrt(horizonSpeedDifference * horizonSpeedDifference + verticalSpeedDiffrence * verticalSpeedDiffrence) / timeInterval * 1000;
    }
	
	/**
	 * Landing Info	
	 */
	private static long getAircraftLandingTime(long timeNow, Long epc, Double lat, Double lon, Double spd, Integer alt, Double trk) {
		double DistanceKm = getAircraftMinLandingDistance(lat, lon, trk);
		//DistanceKm = Math.sqrt(DistanceKm * DistanceKm + (alt * alt / 1000d / 1000d));
		long landingTime = (long) (DistanceKm / ((spd + Math.min(spd, standardLandingSpeed)) / 2d * ktToKms ) * 1000d) + timeNow;
		//long landingTime = (long) (DistanceKm / (spd * ktToKms ) * 1000d) + timeNow;
		return landingTime;
	}

	public static double getAircraftMinLandingDistance(Double lat, Double lon, Double trk) {
		double aircraftLocation[] = {lon, lat};
		int RunwaySerialNumber = getAircraftLandingRunway(lat, lon, trk);
		if (RunwaySerialNumber >= 0) {
			return GeographicCoordinate.getDistanceKm(AirportConstant.runwayLandingPosition[RunwaySerialNumber], aircraftLocation) / Math.cos(GeographicCoordinate.angleToRadian(glideAngle));
		} else {
			double minDistanceKm = 100d;
			for (int i = 0; i < AirportConstant.numberOfRunways; i++) {
				double distanceToRunway = GeographicCoordinate.getDistanceKm(AirportConstant.runwayLandingPosition[i], aircraftLocation);
				if (distanceToRunway < minDistanceKm) {
					minDistanceKm = distanceToRunway;
				}
			}
			return minDistanceKm;
		}
	}
	
	public static String getAircraftLandingTimeText(long timeNow, Long epc, Double lat, Double lon, Double spd, Integer alt, Double trk, String format) {
		long landingTime = getAircraftLandingTime(timeNow, epc, lat, lon, spd, alt, trk);
		return Epoch.epochTodate(landingTime, format);
	}
	
	public static int getAircraftLandingRunway(Double lat, Double lon, Double trk) {
		double aircraftLocation[] = {lon, lat};
		double minTrackError = 90d;
		int headingRunwaySerialNumber = -1;
		for (int i = 0; i < AirportConstant.numberOfRunways; i++) {
			double trackToRunway = GeographicCoordinate.getTrack(aircraftLocation, AirportConstant.runwayLandingPosition[i]);
			if (Math.abs(trackToRunway - AirportConstant.runwayTrack[i]) < courseAngle && Math.abs(trackToRunway - trk) < minTrackError) {
				headingRunwaySerialNumber = i;
				minTrackError = Math.abs(trackToRunway - trk);
			}
		}
		return headingRunwaySerialNumber;
	}
	
	public static double getAircraftMinLandingTrk(Double lat, Double lon, Double trk) {
		double aircraftLocation[] = {lon, lat};
		double minTrackError = 360d;
		for (int i = 0; i < AirportConstant.numberOfRunways; i++) {
			double trackToRunway = GeographicCoordinate.getTrack(aircraftLocation, AirportConstant.runwayLandingPosition[i]);
			if (Math.abs(trackToRunway - trk) < minTrackError) {
				minTrackError = Math.abs(trackToRunway - trk);
			}
		}
		return minTrackError;
	}
	
	public static String getAircraftLandingRunwayText(Double lat, Double lon, Double trk) {
		int RunwaySerialNumber = getAircraftLandingRunway(lat, lon, trk);
		if (RunwaySerialNumber >= 0) {
			return AirportConstant.runwayName[RunwaySerialNumber];
		} else {
			return "";
		}
	}
	
	public static int getAircraftNearestRunway(Double lat, Double lon, Double trk) {
		double minDistance = airportAreaRadius;
		int minDistanceRunway = -1;
		for (int i = 0; i < AirportConstant.numberOfRunways; i++) {
			if (getAircraftMinLandingDistance(lat, lon, trk) < minDistance) {
				minDistanceRunway = i;
				minDistance = getAircraftMinLandingDistance(lat, lon, trk);
			}
		}
		return minDistanceRunway;
	}
	
	/**
	 * Taking Off Info
	 */
	public static double getAircraftMinTakingOffDistance(Double lat, Double lon, Double trk) {
		double aircraftLocation[] = {lon, lat};
		int RunwaySerialNumber = getAircraftTakingOffRunway(lat, lon, trk);
		if (RunwaySerialNumber >= 0) {
			return GeographicCoordinate.getDistanceKm(AirportConstant.runwayTakingOffPosition[RunwaySerialNumber], aircraftLocation);
		} else {
			return 100d;
		}
	}
	
	public static int getAircraftTakingOffRunway(Double lat, Double lon, Double trk) {
		double aircraftLocation[] = {lon, lat};
		double minTrackError = 360d;
		int headingRunwaySerialNumber = -1;
		for (int i = 0; i < AirportConstant.numberOfRunways; i++) {
			double trackToRunway = GeographicCoordinate.getTrack(aircraftLocation, AirportConstant.runwayTakingOffPosition[i]);
			if (Math.abs(trackToRunway - AirportConstant.runwayTrack[i]) < courseAngle && Math.abs(trackToRunway - trk) < minTrackError) {
				headingRunwaySerialNumber = i;
				minTrackError = Math.abs(trackToRunway - trk);
			}
		}
		return headingRunwaySerialNumber;
	}
	
	public static double getAircraftMinTakingOffTrk(Double lat, Double lon, Double trk) {
		double aircraftLocation[] = {lon, lat};
		double minTrackError = 360d;
		for (int i = 0; i < AirportConstant.numberOfRunways; i++) {
			double trackToRunway = GeographicCoordinate.getTrack(aircraftLocation, AirportConstant.runwayTakingOffPosition[i]);
			if (Math.abs(trackToRunway - trk) < minTrackError) {
				minTrackError = Math.abs(trackToRunway - trk);
			}
		}
		return minTrackError;
	}
	
	public static String getAircraftTakingOffRunwayText(Double lat, Double lon, Double trk) {
		int RunwaySerialNumber = getAircraftTakingOffRunway(lat, lon, trk);
		if (RunwaySerialNumber >= 0) {
			return AirportConstant.runwayName[RunwaySerialNumber];
		} else {
			return "";
		}
	}
	
	/**
	 * Detach Info
	 */
	public static int getAircraftdetachRunway(Double lat, Double lon) {
		double aircraftLocation[] = {lon, lat};
		int detachRunwaySerialNumber = -1;
		for (int i = 0; i < AirportConstant.numberOfDetachRunways; i++) {
			if (GeographicCoordinate.getDistanceKm(AirportConstant.detachAirportPosition[i], aircraftLocation) +
					GeographicCoordinate.getDistanceKm(AirportConstant.detachRunwayPosition[i], aircraftLocation)
					<= 3d * GeographicCoordinate.getDistanceKm(AirportConstant.detachAirportPosition[i], AirportConstant.detachRunwayPosition[i])) {
				detachRunwaySerialNumber = i;
			}
		}
		return detachRunwaySerialNumber;
	}
	
	public static String getAircraftdetachRunwayDirection(Double lat, Double lon, Double trk) {
		int detachRunwaySerialNumber = getAircraftdetachRunway(lat, lon);
		if (detachRunwaySerialNumber >= 0) {
			if (Math.abs(GeographicCoordinate.getTrackDifference(AirportConstant.detachRunwayTrack[detachRunwaySerialNumber], trk)) < 90d) {
				return "ToAirport";
			} else {
				return "FromAirport";
			}
		} else {
			return "";
		}
	}
	
	public static String getAircraftdetachRunwayText(Double lat, Double lon) {
		int detachRunwaySerialNumber = getAircraftdetachRunway(lat, lon);
		if (detachRunwaySerialNumber >= 0) {
			return AirportConstant.detachRunwayName[detachRunwaySerialNumber];
		} else {
			return "";
		}
	}
		
	
	/**
	 * Obsoleted
	 * @param lat
	 * @param lon
	 * @param trk
	 * @param alt
	 * @return
	 */
	public static String aircraftHightToGlideAngleRate(Double lat, Double lon, Double trk, Integer alt) {
		return String.format("%.2f", getDepressionAngle(lat, lon, trk, alt) - glideAngle);
//		if (getDepressionAngle(lat, lon, alt) - glideAngle > glideAngleBand) {
//			return ", H";
//		} else {
//			if (getDepressionAngle(lat, lon, alt) - glideAngle < 0d - glideAngleBand) {
//				return ", L";
//			} else {
//				return ", 0";
//			}
//		}
	}
	
	/**
	 * Obsoleted
	 * @param lat
	 * @param lon
	 * @param trk
	 * @param alt
	 * @return
	 */
	public static double getDepressionAngle(Double lat, Double lon, Double trk, Integer alt) {
		double DistanceKm = getAircraftMinLandingDistance(lat, lon, trk);
		return GeographicCoordinate.radianToAngle(Math.atan(alt / 1000d / DistanceKm));
	}
}