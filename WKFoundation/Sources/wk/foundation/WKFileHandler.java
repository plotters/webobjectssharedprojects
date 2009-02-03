/**
 * 
 */
package wk.foundation;

import java.io.File;

/**
 * @author kieran
 * A class that processes a file and does something with it
 */
public interface WKFileHandler {
	
	/**
	 * @param aFile
	 */
	public void setFile(File aFile);
	
	
	/**
	 * @return a result object. It might be null
	 */
	public Object processFile();
	
}
