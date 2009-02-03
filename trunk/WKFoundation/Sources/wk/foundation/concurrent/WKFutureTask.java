package wk.foundation.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import com.webobjects.foundation.NSKeyValueCoding;

/**
 * A FutureTask that implements @link {@link TaskStatus} and @link {@link TaskPercentComplete}
 * and @link {@link NSKeyValueCoding}. Additional methods are provided in this FutureTask
 * for checking if those interfaces are implemented in the wrapped @link {@link Callable} and if so the
 * values are passed thru from the @link {@link Callable}
 *
 * @author kieran
 *
 */
public class WKFutureTask<V> extends FutureTask<V> implements ExecutionStateTransition, TaskStatus, TaskPercentComplete, UserPresentableDescription, NSKeyValueCoding {
	private final Callable _callable;

	public WKFutureTask(Callable<V> callable) {
		super(callable);
		_callable = callable;
	}

	public Callable callable() {
		return _callable;
	}

	public String status() {
		return (hasStatus() && _callable != null)? ((TaskStatus)_callable).status() : null;
	}

	public Double percentComplete() {
		return (hasPercentComplete() && _callable != null) ? ((TaskPercentComplete)_callable).percentComplete() : null;
	}

    public void takeValueForKey(Object value, String key) {
        NSKeyValueCoding.DefaultImplementation.takeValueForKey(this, value, key);
    }

    public Object valueForKey(String key) {
        return NSKeyValueCoding.DefaultImplementation.valueForKey(this, key);
    }

	public Boolean _hasStatus;

	/** @return whether the wrapped @link {@link Callable} has @link {@link TaskStatus} interface */
	public boolean hasStatus() {
		if (_hasStatus == null) {
			_hasStatus = Boolean.valueOf(_callable instanceof TaskStatus);
		} //~ if (_hasStatus == null)
		return _hasStatus.booleanValue();
	}

	private Boolean _hasPercentComplete;

	/** @return whether the wrapped @link {@link Callable} has @link {@link TaskPercentComplete} interface */
	public boolean hasPercentComplete() {
		if ( _hasPercentComplete == null ) {
			_hasPercentComplete = Boolean.valueOf(_callable instanceof TaskPercentComplete);
		}
		return _hasPercentComplete;
	}

	public String userPresentableDescription() {
		return (hasUserPresentableDescription() && _callable != null) ? ((UserPresentableDescription)_callable).userPresentableDescription() : _callable.toString();
	}

	private Boolean _userPresentableDescription;

	/** @return Callable has task description feature */
	public boolean hasUserPresentableDescription() {
		if (_userPresentableDescription == null) {
			_userPresentableDescription = Boolean.valueOf(_callable instanceof UserPresentableDescription);
		}
		return _userPresentableDescription.booleanValue();
	}

	public void afterExecute() {
		if (_callable instanceof ExecutionStateTransition) {
			((ExecutionStateTransition)_callable).afterExecute();
		} //~ if (_callable instanceof ExecutionStateTransition)

	}

	public void beforeExecute() {
		if (_callable instanceof ExecutionStateTransition) {
			((ExecutionStateTransition)_callable).beforeExecute();
		} //~ if (_callable instanceof ExecutionStateTransition)
	}

}
