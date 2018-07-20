package com.adsbdata.gadget;

public class GeographicCoordinate {
	private final static double earthRediusKm = 6356.8;
    
    public static double angleToRadian(double angle) {
    	return angle / 180d * Math.PI;
    }
    public static double radianToAngle(double radian) {
    	return radian / Math.PI * 180d;
    }
    public static double dmsToAngle(int degree, int minute, double second) {
    	return degree + minute / 60d + second / 3600d;
    }
    
    public static double getTrackDifference(double track1, double track2) {
    	double trackDifference = track2 - track1;
    	if (trackDifference > 180d) return trackDifference - 180d;
    	if (trackDifference <= -180d) return trackDifference + 180d;
    	return trackDifference;
    }
	
    public static double[] getNewLatLon(double distanceKm, double track, double originalLatLon[]) {
    	double lona = angleToRadian(originalLatLon[0]);
    	double lata = angleToRadian(originalLatLon[1]);
    	double radian = angleToRadian(- track);
    	//System.out.println(originalLatLon[0] + "," + originalLatLon[1]);
    	double c = distanceKm / earthRediusKm;
    	double a = Math.acos(Math.cos(Math.PI / 2d - lata) * Math.cos(c) + Math.sin(Math.PI / 2d - lata) * Math.sin(c) * Math.cos(radian));
    	double C = Math.acos(Math.sin(c) * Math.sin(radian) / Math.sin(a));
    	double[] newLatLon = new double[2];
    	newLatLon[0] = radianToAngle(lona + C - Math.PI / 2d);
    	newLatLon[1] = radianToAngle(Math.PI / 2d - a);
    	//System.out.println(distanceKm + "," + track);
    	//System.out.println(newLatLon[0] + "," + newLatLon[1]);
    	return newLatLon;
    }
    
    public static double getDistanceKm(double[] position1, double[] position2) {
    	double lona = angleToRadian(position1[0]);
    	double lata = angleToRadian(position1[1]);
    	double lonb = angleToRadian(position2[0]);
    	double latb = angleToRadian(position2[1]);
    	
        double hsinX = Math.sin((lona - lonb) * 0.5d);
        double hsinY = Math.sin((lata - latb) * 0.5d);
        double h = hsinY * hsinY + (Math.cos(lata) * Math.cos(latb) * hsinX * hsinX);
        return 2d * Math.atan2(Math.sqrt(h), Math.sqrt(1 - h)) * earthRediusKm;
    }
    
    public static double getTrack(double[] position1, double[] position2) {
    	double lona = angleToRadian(position1[0]);
    	double lata = angleToRadian(position1[1]);
    	double lonb = angleToRadian(position2[0]);
    	double latb = angleToRadian(position2[1]);
    	
        double hsinX = Math.sin((lona - lonb) * 0.5d);
        double hsinY = Math.sin((lata - latb) * 0.5d);
        double h = hsinY * hsinY + (Math.cos(lata) * Math.cos(latb) * hsinX * hsinX);
        double c = 2d * Math.atan2(Math.sqrt(h), Math.sqrt(1 - h));
        double track = radianToAngle(Math.asin(Math.sin(Math.PI / 2d - latb) * Math.sin(lonb - lona) / Math.sin(c)));
        //System.out.println(track);
        if (lata >= latb) track = - 180 - track;
        if (lata < lonb) track = + track;
        track = track >= 360d ? track - 360d : track < 0d ? track + 360d : track;
        //System.out.println(track);
        return track;
    }
}