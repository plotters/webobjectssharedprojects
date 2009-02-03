package wk.foundation;

import java.math.BigDecimal;

import org.apache.log4j.Logger;


public class WKRuntimeUtilities {
	private static final Logger log = Logger
			.getLogger(WKRuntimeUtilities.class);

	/**
	 * @return true if free memory is less than the threshold
	 */
	public static boolean isMemoryStarved() {
		String thresholdString = System.getProperty("wk.foundation.WKRuntimeUtilities.memoryThreshold", "0.9");
		BigDecimal MEMORY_THRESHOLD  = new BigDecimal(thresholdString);

		long max = Runtime.getRuntime().maxMemory();
		long total = Runtime.getRuntime().totalMemory();
		long free = Runtime.getRuntime().freeMemory() + (max - total);
		long used = max - free;
		long threshold = (long) (MEMORY_THRESHOLD.doubleValue() < 1.0 ? MEMORY_THRESHOLD.doubleValue() * max : (max - (MEMORY_THRESHOLD.doubleValue() * 1024 * 1024)));


		boolean result = (used > threshold);
		if (log.isDebugEnabled())
			log.debug("max = " + max + "; total = " + total + "; free = " + free + "; used = " + used + "; threshold = " + threshold
					+ "; isMemoryStarved = " + result);
		return result;
	}

	public static String memoryStatistics() {
		long max = Runtime.getRuntime().maxMemory();
		long total = Runtime.getRuntime().totalMemory();
		long free = Runtime.getRuntime().freeMemory() + (max - total);
		long used = max - free;

		StringBuilder s = new StringBuilder("Memory Statistics");
		s.append("\nMax = " + WKFormatUtilities.userFriendlyByteCountString(max));
		s.append("\nTotal = " + WKFormatUtilities.userFriendlyByteCountString(total));
		s.append("\nFree = " + WKFormatUtilities.userFriendlyByteCountString(free));
		s.append("\nUsed = " + WKFormatUtilities.userFriendlyByteCountString(used));
		s.append("\nUsed Percent = " + used * 100 / max + " %");
		return s.toString();
	}

	/**
	 * Checks if memory is starved and then forces full garbage collection
	 */
	public static void forceGarbageCollectionIfNecessary() {
		try {

	        if (WKRuntimeUtilities.isMemoryStarved()) {
	        	StringBuilder s = new StringBuilder("Notification of Forced Garbage Collection");
	        	s.append("\nBEFORE Forced GC = " + WKRuntimeUtilities.memoryStatistics());


				WKRuntimeUtilities.forceGC(0);

	        	s.append("\nAFTER Forced GC = " + WKRuntimeUtilities.memoryStatistics());

				// For now log.error so I get emails, but when satisfied change to log.warn
	        	log.error("NOT an ERROR - Just Notification so Kieran is aware of the occurrence.\n"
	        			+ s.toString(), new Throwable());
	        }
		} catch (Exception e) {
			log.error("Error performing forced garbage collection. Check the code", e);
		}

	}

    /**
     * Forces the garbage collector to run. The
     * max loop parameter determines the maximum
     * number of times to run the garbage collector
     * if the memory footprint is still going down.
     * In normal cases you would just need to call
     * this method with the parameter 1. If called
     * with the parameter 0 the garbage collector
     * will continue to run until no more free memory
     * is available to collect. <br/>
     * <br/>
     * Note: This can be a very costly operation and
     * should only be used in extreme circumstances.
     * @param maxLoop maximum times to run the garbage
     *		collector. Passing in 0 will cause the
     *		collector to run until all free objects
     *		have been collected.
     */
    public static void forceGC(int maxLoop) {
        log.warn("Forcing full Garbage Collection", new Throwable());
        Runtime runtime = Runtime.getRuntime();
        long isFree = runtime.freeMemory();
        long wasFree;
        int i=0;
        do {
            wasFree = isFree;
            runtime.gc();
            runtime.runFinalization();
            isFree = runtime.freeMemory();
            i++;
        } while (isFree > wasFree && (maxLoop<=0 || i<maxLoop) );
    }

}
