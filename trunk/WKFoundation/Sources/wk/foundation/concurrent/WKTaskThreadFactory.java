package wk.foundation.concurrent;

import java.util.concurrent.ThreadFactory;

/**
 * A Thread subclass that is used for my background long running tasks
 * so that I can enumerate the threads, recognizing these thread classes
 * amd thus get status info
 *
 * @author kieran
 *
 */
public class WKTaskThreadFactory implements ThreadFactory {

	public Thread newThread(Runnable r) {
		return new WKTaskThread(r);
	}

}
