package application.machine.learning.computer.vision.utils;

public class TimeUtils {

	/**
	 * 	This is a method to compute time cost
	 * 
	 * @param startTime
	 * @return
	 */
	public static long computeTimeCost(long startTime, String comment) {
		
		long endTime = System.currentTimeMillis();
		System.out.println("Time cost for " + comment + ": " + (endTime - startTime) + "ms");
		return endTime;
	}
}
