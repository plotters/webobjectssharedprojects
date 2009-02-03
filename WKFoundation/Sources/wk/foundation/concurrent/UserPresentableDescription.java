package wk.foundation.concurrent;

/**
 * Useful for a single unchanging description of a task unlike the status which
 * can be changing while the task is running.
 * Useful for monitor the task
 *
 * @author kieran
 *
 */
public interface UserPresentableDescription {
	public String userPresentableDescription();
}
