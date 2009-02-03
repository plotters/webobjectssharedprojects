package wk.foundation.concurrent;

/**
 * A simple class to capture a Runnable and some snapshot states about it. Used for monitoring.
 *
 * @author kieran
 *
 */
public final class WKTaskInfo {
	private final Runnable r;
	private final String elapsedTime;

	public WKTaskInfo(Runnable r, String elapsedTime) {
		this.r = r;
		this.elapsedTime = elapsedTime;
	}

	public Runnable task() {
		return r;
	}

	public String elapsedTime() {
		return elapsedTime;
	}

}
