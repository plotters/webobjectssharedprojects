//
//  WKExecutorService.java
//  cheetah
//
//  Created by Kieran Kelleher on 4/13/06.
//  Copyright 2006 Kieran Kelleher. All rights reserved.
//

package wk.foundation.concurrent;

//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * A simple class that provides a single general purpose executor service for a
 * single application. Resource efficient.
 *
 * Implements special Thread and ThreadPoolExecutor subclasses that cooperate to
 * maintain reference to currently executing task while executing only.
 *
 */
public class WKConcurrentUtils {
	private static int CONSTRAINED_POOL_THREAD_COUNT = 5;

	private static final ExecutorService _executorService = new WKTaskThreadPoolExecutor(0, Integer.MAX_VALUE, 60L,
					TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), new WKTaskThreadFactory());

	private static final ExecutorService _constrainedExecutorService = new WKTaskThreadPoolExecutor(
					CONSTRAINED_POOL_THREAD_COUNT, CONSTRAINED_POOL_THREAD_COUNT, 0L, TimeUnit.MILLISECONDS,
					new LinkedBlockingQueue<Runnable>(), new WKTaskThreadFactory());

	private static final ExecutorService _singleThreadExecutorService = Executors
					.unconfigurableExecutorService(new WKTaskThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
									new LinkedBlockingQueue<Runnable>(), new WKTaskThreadFactory()));;

	/** @return a global ExecutorService for executing runnables. */
	public static ExecutorService executorService() {
		return _executorService;
	}

	/**
	 * @return an executor service that is limited in the number of threads it
	 *         can expand to. Once max is reached, the tasks are queue in a FIFO
	 *         queue. This is preferred for autonomous tasks to not exceed
	 *         system capacity.
	 */
	public static ExecutorService constrainedExecutorService() {
		return _constrainedExecutorService;
	}

	public static ExecutorService singleThreadExecutorService() {
		return _singleThreadExecutorService;
	}
}
