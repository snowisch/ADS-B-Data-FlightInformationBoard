package com.adsbdata.surveyor;

import com.adsbdata.gadget.GeographicCoordinate;
import com.adsbdata.gadget.Matrix;
import com.adsbdata.logic.AircraftStatusCalculator;

public class AirportConstant {
	/**
	 * Original airport constants include:
	 * For Airport group:
	 * 	Runway locations, names with airport name and altitudes.
	 */
	
	public final static String chartName = "Kunming Changshui International Airport";
	public final static String runwayName[] = {
	//		"Lijiang 02",
	//		"Lijiang 20",
			"Kunming 21",
			"KunMing 22",
			"Kunming 03",
			"Kunming 04"
			};
	public final static double runwayLandingPosition[][] = {
  	//		{100.249052, 26.689591}, 
  	//		{100.240252, 26.6638}, 
			{102.941455, 25.130408},
			{102.958391, 25.121480},
			{102.916991, 25.102247},
			{102.930125, 25.089669}
  	};
	public final static double runwayTakingOffPosition[][] = {
  	//		{100.240252, 26.6638}, 
  	//		{100.249052, 26.689591}, 
			{102.916991, 25.102247},
			{102.930125, 25.089669},
			{102.941455, 25.130408},
			{102.958391, 25.121480}
  	};
	public final static int airportAltitude[] = {
	//		2221,
	//		2221,
			2123,
			2123,
			2123,
			2123
	};
	public final static String detachRunwayName[] = {
			"22 in 1",
			"22 in 2",
			"22 in 3",
			"04 out 3",
			"04 out 2",
			"04 out 1",
			"22 out 1",
			"22 out 2",
			"22 out 3",
			"04 in 2",
			"04 in 1",
			
			"03 in 1",
			"03 in 2",
			"21 out 3",
			"21 out 2",
			"21 out 1",
			"03 out 1",
			"03 out 2",
			"03 out 3",
			"21 in 2",
			"21 in 1"
	};
	public final static double detachRunwayPosition[][] = {
			{GeographicCoordinate.dmsToAngle(102, 57, 26.06), GeographicCoordinate.dmsToAngle(25, 07, 19.80)},
			{GeographicCoordinate.dmsToAngle(102, 57, 16.05), GeographicCoordinate.dmsToAngle(25, 07, 06.50)},
			{GeographicCoordinate.dmsToAngle(102, 57, 11.22), GeographicCoordinate.dmsToAngle(25, 07, 01.01)},
			{GeographicCoordinate.dmsToAngle(102, 56, 54.57), GeographicCoordinate.dmsToAngle(25, 06, 40.56)},
			{GeographicCoordinate.dmsToAngle(102, 56, 45.78), GeographicCoordinate.dmsToAngle(25, 06, 30.71)},
			{GeographicCoordinate.dmsToAngle(102, 56, 36.94), GeographicCoordinate.dmsToAngle(25, 06, 20.74)},
			{GeographicCoordinate.dmsToAngle(102, 56, 27.85), GeographicCoordinate.dmsToAngle(25, 06, 10.33)},
			{GeographicCoordinate.dmsToAngle(102, 56, 19.19), GeographicCoordinate.dmsToAngle(25, 06, 00.24)},
			{GeographicCoordinate.dmsToAngle(102, 56, 11.16), GeographicCoordinate.dmsToAngle(25, 05, 49.78)},
			{GeographicCoordinate.dmsToAngle(102, 55, 52.68), GeographicCoordinate.dmsToAngle(25, 05, 30.90)},
			{GeographicCoordinate.dmsToAngle(102, 55, 47.80), GeographicCoordinate.dmsToAngle(25, 05, 25.31)},
			
			{GeographicCoordinate.dmsToAngle(102, 55, 04.00), GeographicCoordinate.dmsToAngle(25, 06, 06.42)},
			{GeographicCoordinate.dmsToAngle(102, 55, 08.95), GeographicCoordinate.dmsToAngle(25, 06, 11.94)},
			{GeographicCoordinate.dmsToAngle(102, 55, 25.70), GeographicCoordinate.dmsToAngle(25, 06, 31.86)},
			{GeographicCoordinate.dmsToAngle(102, 55, 34.54), GeographicCoordinate.dmsToAngle(25, 06, 41.95)},
			{GeographicCoordinate.dmsToAngle(102, 55, 43.42), GeographicCoordinate.dmsToAngle(25, 06, 52.08)},
			{GeographicCoordinate.dmsToAngle(102, 55, 52.23), GeographicCoordinate.dmsToAngle(25, 07, 02.27)},
			{GeographicCoordinate.dmsToAngle(102, 56, 00.95), GeographicCoordinate.dmsToAngle(25, 07, 12.40)},
			{GeographicCoordinate.dmsToAngle(102, 56, 10.43), GeographicCoordinate.dmsToAngle(25, 07, 22.37)},
			{GeographicCoordinate.dmsToAngle(102, 56, 27.21), GeographicCoordinate.dmsToAngle(25, 07, 42.25)},
			{GeographicCoordinate.dmsToAngle(102, 56, 32.04), GeographicCoordinate.dmsToAngle(25, 07, 47.75)}
	};
	public final static double detachAirportPosition[][] = {
			{GeographicCoordinate.dmsToAngle(102, 57, 26.89), GeographicCoordinate.dmsToAngle(25, 07, 20.20)},
			{GeographicCoordinate.dmsToAngle(102, 57, 15.64), GeographicCoordinate.dmsToAngle(25, 07, 06.76)},
			{GeographicCoordinate.dmsToAngle(102, 57, 11.45), GeographicCoordinate.dmsToAngle(25, 07, 01.16)},
			{GeographicCoordinate.dmsToAngle(102, 56, 54.44), GeographicCoordinate.dmsToAngle(25, 06, 39.19)},
			{GeographicCoordinate.dmsToAngle(102, 56, 45.49), GeographicCoordinate.dmsToAngle(25, 06, 30.26)},
			{GeographicCoordinate.dmsToAngle(102, 56, 37.25), GeographicCoordinate.dmsToAngle(25, 06, 20.08)},
			{GeographicCoordinate.dmsToAngle(102, 56, 27.88), GeographicCoordinate.dmsToAngle(25, 06, 09.74)},
			{GeographicCoordinate.dmsToAngle(102, 56, 19.86), GeographicCoordinate.dmsToAngle(25, 05, 59.73)},
			{GeographicCoordinate.dmsToAngle(102, 56, 11.08), GeographicCoordinate.dmsToAngle(25, 05, 49.88)},
			{GeographicCoordinate.dmsToAngle(102, 55, 54.75), GeographicCoordinate.dmsToAngle(25, 05, 31.39)},
			{GeographicCoordinate.dmsToAngle(102, 55, 48.38), GeographicCoordinate.dmsToAngle(25, 05, 25.58)},
			
			{GeographicCoordinate.dmsToAngle(102, 55, 04.36), GeographicCoordinate.dmsToAngle(25, 06, 06.16)},
			{GeographicCoordinate.dmsToAngle(102, 55, 09.04), GeographicCoordinate.dmsToAngle(25, 06, 12.04)},
			{GeographicCoordinate.dmsToAngle(102, 55, 25.31), GeographicCoordinate.dmsToAngle(25, 06, 31.66)},
			{GeographicCoordinate.dmsToAngle(102, 55, 33.90), GeographicCoordinate.dmsToAngle(25, 06, 41.77)},
			{GeographicCoordinate.dmsToAngle(102, 55, 42.94), GeographicCoordinate.dmsToAngle(25, 06, 51.96)},
			{GeographicCoordinate.dmsToAngle(102, 55, 51.97), GeographicCoordinate.dmsToAngle(25, 07, 02.02)},
			{GeographicCoordinate.dmsToAngle(102, 56, 00.66), GeographicCoordinate.dmsToAngle(25, 07, 12.52)},
			{GeographicCoordinate.dmsToAngle(102, 56, 09.71), GeographicCoordinate.dmsToAngle(25, 07, 22.56)},
			{GeographicCoordinate.dmsToAngle(102, 56, 26.82), GeographicCoordinate.dmsToAngle(25, 07, 41.79)},
			{GeographicCoordinate.dmsToAngle(102, 56, 32.50), GeographicCoordinate.dmsToAngle(25, 07, 47.46)}
	};
	
//	public final static String chartName = "Ningbo Lishe International Airport";
//	public final static String runwayName[] = {
//			"31",
//			"13"
//			};
//	public final static double runwayLandingPosition[][] = {
//			{GeographicCoordinate.dmsToAngle(121, 28, 32.74), GeographicCoordinate.dmsToAngle(29, 49, 07.98)},
//			{GeographicCoordinate.dmsToAngle(121, 26, 53.29), GeographicCoordinate.dmsToAngle(29, 50, 04.20)}
//  	};
//	public final static double runwayTakingOffPosition[][] = {
//			{GeographicCoordinate.dmsToAngle(121, 26, 53.29), GeographicCoordinate.dmsToAngle(29, 50, 04.20)},
//			{GeographicCoordinate.dmsToAngle(121, 28, 32.74), GeographicCoordinate.dmsToAngle(29, 49, 07.98)}
//  	};
//	public final static int airportAltitude[] = {
//			5,
//			5
//	};
//	public final static String detachRunwayName[] = {
//			"A13",
//			"A31",
//			"F",
//			"E",
//			"C",
//			"B"
//	};
//	public final static double detachRunwayPosition[][] = {
//			{GeographicCoordinate.dmsToAngle(121, 26, 52.67), GeographicCoordinate.dmsToAngle(29, 50, 03.26)},
//			{GeographicCoordinate.dmsToAngle(121, 28, 31.56), GeographicCoordinate.dmsToAngle(29, 49, 06.76)},
//			{GeographicCoordinate.dmsToAngle(121, 28, 10.12), GeographicCoordinate.dmsToAngle(29, 49, 19.00)},
//			{GeographicCoordinate.dmsToAngle(121, 27, 57.77), GeographicCoordinate.dmsToAngle(29, 49, 26.27)},
//			{GeographicCoordinate.dmsToAngle(121, 27, 29.31), GeographicCoordinate.dmsToAngle(29, 49, 41.38)},
//			{GeographicCoordinate.dmsToAngle(121, 27, 15.06), GeographicCoordinate.dmsToAngle(29, 49, 50.67)}
//	};
//	public final static double detachAirportPosition[][] = {
//			{GeographicCoordinate.dmsToAngle(121, 26, 52.15), GeographicCoordinate.dmsToAngle(29, 50, 02.61)},
//			{GeographicCoordinate.dmsToAngle(121, 28, 31.07), GeographicCoordinate.dmsToAngle(29, 49, 06.07)},
//			{GeographicCoordinate.dmsToAngle(121, 28, 09.30), GeographicCoordinate.dmsToAngle(29, 49, 17.88)},
//			{GeographicCoordinate.dmsToAngle(121, 27, 56.88), GeographicCoordinate.dmsToAngle(29, 49, 25.02)},
//			{GeographicCoordinate.dmsToAngle(121, 27, 27.89), GeographicCoordinate.dmsToAngle(29, 49, 41.46)},
//			{GeographicCoordinate.dmsToAngle(121, 27, 14.35), GeographicCoordinate.dmsToAngle(29, 49, 48.93)}
//	};

	
//	//Hangzhou
//	public final static String chartName = "Hangzhou Xiaoshan Intenational Airport";
//	public final static String runwayName[] = {
//			"W",
//			"E"
//	};
//	public final static double runwayLandingPosition[][] = {
//			{GeographicCoordinate.dmsToAngle(120, 26, 19.89), GeographicCoordinate.dmsToAngle(30, 15, 07.72)},
//			{GeographicCoordinate.dmsToAngle(120, 27, 03.74), GeographicCoordinate.dmsToAngle(30, 14, 12.81)}
//	};
//	public final static double runwayTakingOffPosition[][] = {
//			{GeographicCoordinate.dmsToAngle(120, 24, 32.07), GeographicCoordinate.dmsToAngle(30, 14, 17.34)},
//			{GeographicCoordinate.dmsToAngle(120, 25, 03.22), GeographicCoordinate.dmsToAngle(30, 13, 20.16)}
//	};
//	public static final int airportAltitude[] = {
//			10,
//			10
//	};
	
//	//Shanghai
//	public final static String chartName = "Shanghai Hongqiao Intenational Airport";
//	public final static String runwayName[] = {
//			"36L",
//			"36R"
//	};
//	public final static double runwayLandingPosition[][] = {
//			{GeographicCoordinate.dmsToAngle(121, 19, 59.49), GeographicCoordinate.dmsToAngle(31, 10, 59.82)},
//			{GeographicCoordinate.dmsToAngle(121, 20, 13.66), GeographicCoordinate.dmsToAngle(31, 10, 57.82)}
//	};
//	public final static double runwayTakingOffPosition[][] = {
//			{GeographicCoordinate.dmsToAngle(121, 19, 53.82), GeographicCoordinate.dmsToAngle(31, 12, 47.07)},
//			{GeographicCoordinate.dmsToAngle(121, 20, 07.75), GeographicCoordinate.dmsToAngle(31, 12, 47.35)}
//	};
//	public static final int airportAltitude[] = {
//			3,
//			3
//	};
	
	
	

  	public final static int numberOfRunways = runwayLandingPosition.length;
  	public final static int numberOfDetachRunways = detachRunwayPosition.length;
  	
    private final static double airportReadingPosition[][] = new double[numberOfRunways * 2][2];
    private final static double detachReadingPosition[][] = new double[numberOfDetachRunways * 2][2];
    /**
     * Llzs marks are just for demonstration display, can be removed in release version.
     */
    private final static double llz[][] = new double[numberOfRunways][2];
    private final static double ilsReadingPosition[][] = new double[numberOfRunways * (2 * AircraftStatusCalculator.numberOfCheckpoints + 1)][2];
    
  	public static final double runwayTrack[] = new double[numberOfRunways];
  	public static final double detachRunwayTrack[] = new double[numberOfDetachRunways];
  	public static double airportLocation[] = new double[2];
    public static double airportPosition[][] = new double[2][numberOfRunways * 2];
    public static double detachPosition[][] = new double[2][numberOfDetachRunways * 2];
    public static double ilsPosition[][] = new double[2][numberOfRunways * (2 * AircraftStatusCalculator.numberOfCheckpoints + 1)];
  	
    /**
     * Calculate Runways' tracks and Llzs marks.
     */
  	public static void Initialize() {
  		double tmplon = 0d;
  		double tmplat = 0d;
  		for (int i = 0; i < numberOfDetachRunways; i++) {
  			detachRunwayTrack[i] = GeographicCoordinate.getTrack(detachRunwayPosition[i], detachAirportPosition[i]);
  			detachReadingPosition[i * 2] = detachRunwayPosition[i];
  			detachReadingPosition[i * 2 + 1] = detachAirportPosition[i];
  		}
  		for (int i = 0; i < numberOfRunways; i++) {
  			tmplon += runwayLandingPosition[i][0] + runwayTakingOffPosition[i][0];
  			tmplat += runwayLandingPosition[i][1] + runwayTakingOffPosition[i][1];
  			runwayTrack[i] = GeographicCoordinate.getTrack(runwayLandingPosition[i], runwayTakingOffPosition[i]);
  			//System.out.println("                "+runwayTrack[i]);
  			airportReadingPosition[i * 2] = runwayLandingPosition[i];
  			airportReadingPosition[i * 2 + 1] = runwayTakingOffPosition[i];
  		    llz[i] = GeographicCoordinate.getNewLatLon(AircraftStatusCalculator.llzDistance, runwayTrack[i], runwayLandingPosition[i]); //may need to calculate again
  		    ilsReadingPosition[AircraftStatusCalculator.numberOfCheckpoints * (i * 2 + 1) + i] = llz[i];
  		    for (int j = 0; j < AircraftStatusCalculator.numberOfCheckpoints; j++) {
  		    	ilsReadingPosition[AircraftStatusCalculator.numberOfCheckpoints * (i * 2 + 1) + i - j - 1] = GeographicCoordinate.getNewLatLon(AircraftStatusCalculator.approachDistance[j], 180d + runwayTrack[i] - AircraftStatusCalculator.courseAngle, llz[i]);
  		    	ilsReadingPosition[AircraftStatusCalculator.numberOfCheckpoints * (i * 2 + 1) + i + j + 1] = GeographicCoordinate.getNewLatLon(AircraftStatusCalculator.approachDistance[j], 180d + runwayTrack[i] + AircraftStatusCalculator.courseAngle, llz[i]);
  		    }
  		}
  		airportLocation[0] = tmplon / numberOfRunways / 2;
  		airportLocation[1] = tmplat / numberOfRunways / 2;
  		airportPosition = Matrix.transPosition(airportReadingPosition);
  		ilsPosition = Matrix.transPosition(ilsReadingPosition);
  		detachPosition = Matrix.transPosition(detachReadingPosition);
  	}
}
