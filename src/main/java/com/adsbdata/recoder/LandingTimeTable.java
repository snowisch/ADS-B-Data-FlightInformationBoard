package com.adsbdata.recoder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.adsbdata.executer.AdsbMain;

public class LandingTimeTable {
	
//	public String hexident;
//	public String callsign;
//	public String checkPoint30Km;
//	public String checkPoint20Km;
//	public String checkPoint10Km;
//	public String checkPonit05Km;
//	public String checkPonit00Km;
	
	public static void createAircraftsLandingTimeDatabase() throws SQLException {
        Statement stat = AdsbMain.conn.createStatement();
        stat.executeUpdate("create table aircraftslandingtimedatabase("
        		+ "hexident varchar(20) primary key, "
        		+ "callsign varchar(20) not null, "
        		+ "checkPoint30Km varchar(20), "
        		+ "checkPoint20Km varchar(20), "
        		+ "checkPoint10Km varchar(20), "
        		+ "checkPoint05Km varchar(20), "
        		+ "checkPoint00Km varchar(20) "
        		+ ")");
	}
	
	public static void recordAddToLandingTimeTable(String hexident, String callsign, double distance, String predictTimeText) throws SQLException {
		PreparedStatement psql = AdsbMain.conn.prepareStatement("select * from aircraftslandingtimedatabase where hexident = '" + hexident +"'");
		ResultSet rs = psql.executeQuery();
		if (!rs.next()) {
			psql = AdsbMain.conn.prepareStatement("insert into aircraftslandingtimedatabase ("
				+ "hexident,"
				+ "callsign"
				+ ") values (?, ?) ");
			psql.setObject(1, hexident);
			psql.setObject(2, callsign);
			psql.executeUpdate();
			//System.out.println(callsign);
		}
		psql = null;
		//System.out.println(predictTimeText);
		int distanceCase = (int) (distance / 5d);
		//System.out.println((int) (distance / 5d));
		switch (distanceCase) {
			case 0:
				psql = AdsbMain.conn.prepareStatement("update aircraftslandingtimedatabase set "
						+ "checkPoint00Km = ? "
						+ "where hexident = ?");
				psql.setObject(1, predictTimeText);
				psql.setObject(2, hexident);
				psql.executeUpdate();
				//System.out.println(predictTimeText);
				break;
			case 1:
				psql = AdsbMain.conn.prepareStatement("update aircraftslandingtimedatabase set "
						+ "checkPoint05Km = ? "
						+ "where hexident = ?");
				psql.setObject(1, predictTimeText);
				psql.setObject(2, hexident);
				psql.executeUpdate();
				//System.out.println(predictTimeText);
				break;
			case 2:
			case 3:
					psql = AdsbMain.conn.prepareStatement("update aircraftslandingtimedatabase set "
						+ "checkPoint10Km = ? "
						+ "where hexident = ?");
				psql.setObject(1, predictTimeText);
				psql.setObject(2, hexident);
				psql.executeUpdate();
				//System.out.println(predictTimeText);
				break;
			case 4:
			case 5:
				psql = AdsbMain.conn.prepareStatement("update aircraftslandingtimedatabase set "
						+ "checkPoint20Km = ? "
						+ "where hexident = ?");
				psql.setObject(1, predictTimeText);
				psql.setObject(2, hexident);
				psql.executeUpdate();
				//System.out.println(predictTimeText);
				break;
			default:
				if (distanceCase <= 10) {
					psql = AdsbMain.conn.prepareStatement("update aircraftslandingtimedatabase set "
							+ "checkPoint30Km = ? "
							+ "where hexident = ?");
					psql.setObject(1, predictTimeText);
					psql.setObject(2, hexident);
					psql.executeUpdate();
					//System.out.println(predictTimeText);
				}
		}
	}
}

