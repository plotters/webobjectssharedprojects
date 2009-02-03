/**
 * 
 */
package wk.foundation;

import java.io.File;

/**
 * @author kieran
 * An interface for handling files and processing them
 */
public interface FileHandler {
	/**
	 * @param aFile the file to be handled
	 */
	public void setFile(File aFile);
	
	public void processFile();
}
