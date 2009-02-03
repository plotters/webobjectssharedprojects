package wk.foundation.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class WKTaskThreadPoolExecutor extends ThreadPoolExecutor {

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(WKTaskThreadPoolExecutor.class);

	public WKTaskThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
					BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	public WKTaskThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
					BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
	}

	public WKTaskThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
					BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
	}

	public WKTaskThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
					BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
	}

	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		// Store reference to the task
		if (t instanceof WKTaskThread) {
			((WKTaskThread)t).setTask(r);
			((WKTaskThread)t).startStopWatch();
			if (log.isDebugEnabled()) {
				log.debug("About to execute " + (r == null ? "null" : r) + " in thread " + t);
			}
		}

		if (r instanceof ExecutionStateTransition) {
			((ExecutionStateTransition)r).beforeExecute();
		} //~ if (r instanceof ExecutionStateTransition)

		super.beforeExecute(t, r);
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		super.afterExecute(r, t);

		// Clear reference to the task
		if (Thread.currentThread() instanceof WKTaskThread) {
			WKTaskThread thread = (WKTaskThread)Thread.currentThread();
			thread.setTask(null);
			thread.stopStopWatch();
			String elapsedTime = thread.elapsedTime();
			if (log.isDebugEnabled())
				log.debug("Finished executing " + (r == null ? "null" : r) + " after " + elapsedTime);
		}

		if (r instanceof ExecutionStateTransition) {
			((ExecutionStateTransition)r).afterExecute();
		} //~ if (r instanceof ExecutionStateTransition)
	}

}
