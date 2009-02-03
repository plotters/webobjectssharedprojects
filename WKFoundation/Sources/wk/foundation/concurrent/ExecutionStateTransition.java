package wk.foundation.concurrent;

/**
 * Intended as an interface to be implemented by Runnables so that they have the opportunity
 * to setup and clear ThreadLocal state when the Runnable is executed by a {@link WKTaskThreadPoolExecutor}
 *
 * @author kieran
 *
 */
public interface ExecutionStateTransition {

	public void beforeExecute();

	public void afterExecute();

}
