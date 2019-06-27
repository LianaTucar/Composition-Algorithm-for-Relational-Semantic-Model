import java.util.HashMap;
import java.util.Map;


public class TimeTracker {
	
	private static Map <Part, Long> timeSpent = new HashMap<>();
	private static Map <Part, Long> start = new HashMap<>();
	
	public static void init() {
		for (Part p : Part.values())
			timeSpent.put(p, new Long(0));	
	}
	
	
	public static void startTimer(Part part) {
		start.put(part, System.currentTimeMillis());
	}
	
	public static void stopTimer(Part part) {
		Long startTime = start.get(part);
		Long oldTime = timeSpent.get(part);
		timeSpent.put(part, oldTime+System.currentTimeMillis() - startTime);
	}
	
	public static String printValues() {
		StringBuilder sb = new StringBuilder();
		for (Part p:Part.values()) {
			sb.append(p.name() + " " + timeSpent.get(p)+";    ");
		}
		return sb.toString();
	}
	
	public static double getTotalTime() {
		return timeSpent.get(Part.all);
	}
}
