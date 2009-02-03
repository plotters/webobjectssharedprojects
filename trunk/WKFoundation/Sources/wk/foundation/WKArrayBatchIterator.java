package wk.foundation;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSRange;

/**
 * A utility class for batching thru an array.
 * An example of usage would be batching thru raw rows, or primary keys, or EOGlobalIDs and
 * hydrating each batch to do some processing.
 *
 * @author kieran
 *
 */
public class WKArrayBatchIterator implements Iterator {
	private final NSArray array;
	private int batchSize = 100;
	private int currentIndex = 0;
	private int numberOfbatches = 0;

	public WKArrayBatchIterator(NSArray array, int batchSize) {
		// Make a copy if this is anything other than the immutable NSArray
		if (array.getClass().equals(NSArray.class)) {
			this.array = array;
		} else {
			this.array = array.immutableClone();
		} //~ if (array.getClass().equals(NSArray.class))

		this.batchSize = batchSize;

		numberOfbatches = array.count() / batchSize;
		if ((array.count() % batchSize) > 0) {
			// We have a partial batch at the end
			numberOfbatches++;
		} //~ if (array.count() % batchSize > 0)
	}

	public boolean hasNext() {
		return (currentIndex < numberOfbatches);
	}

	public Object next() {
		if (!hasNext()) {
			throw new NoSuchElementException("Iteration has no more elements.");
		} //~ if (!hasNext())

		// Get the next batch
		int location = currentIndex * batchSize;
		int length = Math.min(batchSize, array.count() - location);

		NSRange range = new NSRange(location, length);

		// Increment index for next time thru
		currentIndex++;

		// Return the range of objects that we want
		return array.subarrayWithRange(range);
	}

	public void remove() {
		throw new UnsupportedOperationException("This iterator does not support the remove() method.");
	}
}
