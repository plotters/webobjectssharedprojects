package wk.foundation.concurrent;

import org.apache.commons.lang.time.StopWatch;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

public class WKTaskThread extends Thread {
	public WKTaskThread(Runnable target) {
		super(target);
	}

	private Runnable _task;
	private StopWatch _stopWatch;

	/** @return the current task being executed */
	public Runnable task() {
		return _task;
	}

	/** @param task the current task being executed */
	public void setTask(Runnable task){
		_task = task;
	}

	/**
	 * @return NSArray of background tasks
	 */
	public static NSArray tasks() {
		NSMutableArray  tasks = new NSMutableArray();

		Thread threads[] = new Thread[Thread.activeCount()];
		Thread.enumerate(threads);

		for (int i = 0; i < threads.length; i++) {
			Thread thread = threads[i];
			if (thread instanceof WKTaskThread) {
				Runnable task = ((WKTaskThread)thread).task();
				if (task != null) {
					tasks.add(task);
				} //~ if (task != null)
			} //~ if (thread instanceof WKTaskThread)
		}
		return tasks.immutableClone();
	}

	/**
	 * @return NSArray of {@link WKTaskInfo}
	 */
	public static NSArray taskInfos() {
		NSMutableArray  taskInfos = new NSMutableArray();

		Thread threads[] = new Thread[Thread.activeCount()];
		Thread.enumerate(threads);

		for (int i = 0; i < threads.length; i++) {
			Thread thread = threads[i];
			if (thread instanceof WKTaskThread) {
				Runnable task = ((WKTaskThread)thread).task();
				if (task != null) {
					String elapsedTime = ((WKTaskThread)thread).elapsedTime();
					WKTaskInfo info = new WKTaskInfo(task, elapsedTime);
					taskInfos.add(info);
				} //~ if (task != null)
			} //~ if (thread instanceof WKTaskThread)
		}
		return taskInfos.immutableClone();
	}

	public void startStopWatch() {
		_stopWatch = new StopWatch();
		_stopWatch.start();
	}

	public String elapsedTime() {
		return (_stopWatch == null ? null : _stopWatch.toString());
	}

	public void stopStopWatch() {
		if (_stopWatch != null) {
			_stopWatch.stop();
		} //~ if (_stopWatch != null)
	}
}
