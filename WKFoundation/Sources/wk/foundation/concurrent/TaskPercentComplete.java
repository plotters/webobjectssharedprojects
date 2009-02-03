package wk.foundation.concurrent;

public interface TaskPercentComplete {
	/**
	 * @return a Double between 0 and 1.0 indicating how far a task has progressed toward completion
	 * null return means progress is unknown
	 */
	public Double percentComplete();
}
