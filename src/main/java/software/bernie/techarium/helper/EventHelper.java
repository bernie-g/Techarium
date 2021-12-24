package software.bernie.techarium.helper;

import java.util.Calendar;

public class EventHelper {

	public static boolean isChristmas() {
		Calendar calendar = Calendar.getInstance();
		if (calendar.get(2) + 1 == 12 && calendar.get(5) >= 24 && calendar.get(5) <= 26) 
			return true;
		
		else if (calendar.get(2) + 1 == 1 && calendar.get(5) >= 6 && calendar.get(5) <= 8)
			return true;
		
		return false;
	}
	
}
