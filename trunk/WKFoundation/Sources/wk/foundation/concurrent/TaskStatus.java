package wk.foundation.concurrent;

public interface TaskStatus {

	/**
	 * @return the status of a task. Useful for a long running task to implement a status message.
	 */
	public String status();

}
