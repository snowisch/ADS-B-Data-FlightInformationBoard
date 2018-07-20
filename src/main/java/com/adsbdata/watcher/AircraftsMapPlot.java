package com.adsbdata.watcher;
 
//import java.awt.Color;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
//import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
//import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
//import org.jfree.chart.renderer.category.LineAndShapeRenderer;
//import org.jfree.chart.renderer.xy.XYItemRenderer;
//import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
//import org.jfree.data.category.CategoryDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.ui.RefineryUtilities;

import com.adsbdata.executer.AdsbMain;
import com.adsbdata.gadget.Epoch;
import com.adsbdata.logic.AircraftStatus;
import com.adsbdata.logic.AircraftStatusInterpolation;
//import com.adsbdata.logic.AircraftStatusInterpolation;
import com.adsbdata.recoder.AdsbMomentRecord;
import com.adsbdata.recoder.AircraftInfo;
import com.adsbdata.surveyor.AirportConstant;
 
public class AircraftsMapPlot {
	
	public final static long mapRefreshTime = 1000L;
    public static final double mapSize = 0.25d;
    public static JFreeChart aircraftsMapChart = null;
    public static String lastPlotLogTime = "";
    //public static double specialPosition[][] = {{0d}, {0d}};

//    private static double maxLon = AirportConstant.airportLocation[0] + mapSize * 2d;
//    private static double minLon = AirportConstant.airportLocation[0] - mapSize * 2d;
//    private static double maxLat = AirportConstant.airportLocation[1] + mapSize;
//    private static double minLat = AirportConstant.airportLocation[1] - mapSize;
    private static double maxLon = AirportConstant.airportLocation[0] + mapSize * 2d;
    private static double minLon = AirportConstant.airportLocation[0] - mapSize * 2d;
    private static double maxLat = AirportConstant.airportLocation[1] + mapSize;
    private static double minLat = AirportConstant.airportLocation[1] - mapSize;
	
	private static DefaultXYDataset aircraftsPositionDataset = new DefaultXYDataset();
	private static XYPlot aircraftsMapPlot = new XYPlot();
	//private static XYItemRenderer renderer;
	private static String chartTitle = AirportConstant.chartName + " ADS-B";
	
	public static boolean checkAircraftOnMap (AdsbMomentRecord record) {
		if (record.latitude == null || record.longitude == null ||
				(record.latitude >= minLat - mapSize && record.latitude <= maxLat + mapSize
				&& record.longitude >= minLon - mapSize * 2d && record.longitude <= maxLon + mapSize * 2d)) {
			//System.out.println(minLat+" "+maxLat+" "+record.latitude+"      "+minLon+" "+maxLon+" "+record.longitude);
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean showAircraftsMapNow(Long timeNow, boolean plotted) throws IOException {
		int aircraftsNumber = AdsbMain.AircraftsMap.size();
		System.out.println(aircraftsNumber + " aircarfts on Aircrafts Map Now.");
		int aircraftsCounter = 0;
		AircraftInfo record = new AircraftInfo();
		String[] aircraftsInfo = new String[aircraftsNumber];
    	double[][] aircraftsPosition = new double[2][aircraftsNumber];
		
        Iterator<Map.Entry<String, AircraftInfo>> it = AdsbMain.AircraftsMap.entrySet().iterator();
        while (it.hasNext()) {
        	Map.Entry<String, AircraftInfo> entry = it.next();
        	record = entry.getValue();
    		if (record.longitude != null && 
    				record.groundSpeed != null) {
    			record = AircraftStatusInterpolation.interpolate(timeNow, record);
    			aircraftsInfo[aircraftsCounter] = AircraftStatus.getAircraftStatus(timeNow, record);
    			AircraftInfo.statusUpdate(record.hexident, aircraftsInfo[aircraftsCounter]);
    			//AircraftInfo.writeToConsole(record);
    			aircraftsPosition[0][aircraftsCounter] = (double) record.longitude;
    			aircraftsPosition[1][aircraftsCounter] = (double) record.latitude;
    		} else {
    			//AircraftInfo.writeToConsole(record);
    			aircraftsInfo[aircraftsCounter] = "";
    			aircraftsPosition[0][aircraftsCounter] = 0d;
    			aircraftsPosition[1][aircraftsCounter] = 0d;
    		}
			aircraftsCounter++;
        }
        
        System.out.println("painting");
		if (!plotted) {
			aircraftsMapChart = AircraftsMapPlot.mapPlot("          " + Epoch.epochTodate(timeNow, "HH:mm:ss"), 
					Epoch.epochTodate(timeNow, "yyyy-MM-dd"), 
					aircraftsInfo, aircraftsPosition);
		} else {
			try {
			mapPlotUpdate("          " + Epoch.epochTodate(timeNow, "HH:mm:ss"), 
					aircraftsInfo, aircraftsPosition, 
					aircraftsMapChart);
			} catch(Exception e) {System.out.println(e);}
		}
		return true;		
	}
	
    public static JFreeChart mapPlot(String logTimeNow, String dateNow, String[] aircraftsInfo, double aircraftsPosition[][]) {
    	chartTitle += "  " + dateNow; 
        aircraftsPositionDataset.addSeries("Runway Landing Positions", AirportConstant.airportPosition);
        aircraftsPositionDataset.addSeries("Detach Runway Positions", AirportConstant.detachPosition);
        aircraftsPositionDataset.addSeries("ILS System Checkpoints", AirportConstant.ilsPosition);
        //aircraftsPositionDataset.addSeries("Check Point", specialPosition);
    	lastPlotLogTime = logTimeNow;
    	aircraftsPositionDataset.addSeries("Aircrafts" + lastPlotLogTime, AirportConstant.airportPosition); 
        JFreeChart chart = ChartFactory.createScatterPlot(chartTitle, "Longitude E", "Latitude N", aircraftsPositionDataset, PlotOrientation.VERTICAL, true, false, false);
        
        aircraftsMapPlot = (XYPlot) chart.getPlot();
        NumberAxis LonAxis = (NumberAxis) aircraftsMapPlot.getDomainAxis();
        NumberAxis LatAxis = (NumberAxis) aircraftsMapPlot.getRangeAxis();
        LonAxis.setUpperBound(maxLon);
        LonAxis.setLowerBound(minLon);
        LatAxis.setUpperBound(maxLat);
        LatAxis.setLowerBound(minLat);
        LonAxis.setTickUnit(new NumberTickUnit((maxLat - minLat) / 10.0));
        LatAxis.setTickUnit(new NumberTickUnit((maxLat - minLat) / 10.0));
        
       ChartFrame frame = new ChartFrame(logTimeNow, chart);  
       frame.pack();  
       RefineryUtilities.centerFrameOnScreen(frame);
       frame.setVisible(true);
       System.out.println("Aircrafts Map Plotted.");
       return chart;
    } 
    
    public static void mapPlotUpdate(String logTimeNow, String[] aircraftsInfo, double aircraftsPosition[][], JFreeChart chart) throws IOException {
    	//aircraftsPositionDataset.removeSeries("Check Point");
        //aircraftsPositionDataset.addSeries("Check Point", specialPosition);
    	aircraftsPositionDataset.removeSeries("Aircrafts" + lastPlotLogTime);
    	lastPlotLogTime = logTimeNow;
    	aircraftsPositionDataset.addSeries("Aircrafts" + lastPlotLogTime, aircraftsPosition);    	
    	
    	aircraftsMapPlot.clearAnnotations();
    	int AircraftsNumber = aircraftsInfo.length;
        for (int aircraftsCounter = 0; aircraftsCounter < AircraftsNumber; aircraftsCounter++) {
        	if (aircraftsPosition[0][aircraftsCounter] != 0d && aircraftsPosition[1][aircraftsCounter] != 0d) {
        		aircraftsMapPlot.addAnnotation(new XYTextAnnotation(aircraftsInfo[aircraftsCounter], 
        			aircraftsPosition[0][aircraftsCounter], 
        			aircraftsPosition[1][aircraftsCounter]));
        	}
        	//System.out.println(aircraftsName[aircraftsCounter] + " " + aircraftsPosition[0][aircraftsCounter] + " " + aircraftsPosition[1][aircraftsCounter]);
        }
        
//        renderer = aircraftsMapPlot.getRenderer();
//        renderer.setSeriesPaint(2, Color.white);
        //aircraftsMapPlot.setBackgroundPaint(Color.white);
    }
}