package com.adsbdata.gadget;

import java.text.*;
import java.util.Date;

public class Epoch {

	public static Long dateToEpoch(String timeString, String dateFormat) throws ParseException{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		Date tmpdate = simpleDateFormat.parse(timeString);
		Long epochvalue = tmpdate.getTime();		
		return epochvalue;
	}
	
	public static String epochTodate(Long epochvalue, String dateFormat) {
        String timeString;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        Date tmpdate = new Date(epochvalue);
        timeString = simpleDateFormat.format(tmpdate);
        return timeString;
	}
}