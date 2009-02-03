//
//  WKFileUtilities.java
//  cheetah
//
//  Created by Kieran Kelleher on 5/4/06.
//  Copyright 2006 Kieran Kelleher. All rights reserved.
//

package wk.foundation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import org.apache.log4j.Logger;

import com.Ostermiller.util.CSVParser;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;

public class WKFileUtilities {
	private static Logger log = Logger.getLogger( WKFileUtilities.class );

    // The default path for temporary file storage on app server
    private static String _tempFileUploadPath;

    /**
    * Returns the directory where uploaded files will be written to disk. If possible, it will use the
     * system temp directory, but will default to the user's home directory if not.
     */
    public static String tempFileUploadPath() {
        if (_tempFileUploadPath == null) {
            String tmpdir = System.getProperty("java.io.tmpdir");
            if (tmpdir != null) {
                File tmpPath = new File(tmpdir);
                if (tmpPath.exists()) {
                    _tempFileUploadPath = tmpPath.getAbsolutePath();
                }
            }
            if (_tempFileUploadPath == null) {
                _tempFileUploadPath = "/tmp";
                log.error("FileUpload: 'java.io.tmpdir' does not exist. Setting to '/tmp'. Please launch this application again with the 'java.io.tmpdir' System Property set to a directory to which you have write permissions.");
            }
        }
        return _tempFileUploadPath;
    }

    public static NSData nsDataFromFile( String filePath ) throws FileNotFoundException, IOException {
        File theFile = new File( filePath );
        FileInputStream stream = new FileInputStream( theFile );
        NSData nsData = null;
        try {
			nsData = new NSData( stream, (int)theFile.length() );
		} catch (Exception e) {
			;
		} finally {
			stream.close();
		}
        return nsData;
    }

    /** @return a full file path and name that can be directly used as a target for a
        new file. */
    public static String randomTempFileNameWithPath() {
        return tempFileUploadPath() + File.separator + UniqueStringGenerator.sharedInstance().nextUnique();
    }

    public static File newTempFile() {
    	return new File(randomTempFileNameWithPath());
    }

    public static Writer newTextFileWriter(File aFile) throws IOException {
    	return new PrintWriter(new BufferedWriter(new FileWriter(aFile)));
    }

    /** @return the first 'n' lines of a a CSV file as an array of arrays */
    public static NSArray csvFileHead( File file, int numberOfLines ) {
        NSMutableArray lines = new NSMutableArray();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader( new FileReader( file ) );
            String dataLine = reader.readLine();
            for (int i = 0; i < numberOfLines && dataLine != null; i++) {
                String[][] fieldValuesParsed = CSVParser.parse( dataLine );
                lines.addObject( new NSArray( fieldValuesParsed[0] ) );
                dataLine = reader.readLine();
            }
            reader.close();
        } catch (IOException ioException ) {
            throw new RuntimeException( "Error reading CSV file named " + file.getAbsolutePath(), ioException );
        } finally {
        	try {
				reader.close();
			} catch (Exception e) {
				;
			}
        }
        return lines.immutableClone();
    }

    /**
     * @param aFile
     * @return the number of lines in a file
     */
    public static int countLines(File aFile) {
    	int count = 0;
    	BufferedReader reader = null;
    	try {
    		String line;
        	reader = new BufferedReader(new FileReader(aFile));
        	while ((line = reader.readLine()) != null) {
    			count++;
    		}
		} catch (Exception e) {
			throw new RuntimeException("Error counting lines in the file", e);
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
				;
			}
		}
    	return count;
    }
    
    /**
     * @param file
     * @return an array of dictionaries. The first row is assumed to be column headings and are used as keys. The result is held in memory.
     */
    public static NSMutableArray recordsFromCSVFile(File file) {
    	DataRecordFileHelper fileHelper = new DataRecordFileHelper(file);
    	NSMutableArray result = new NSMutableArray();
    	for (java.util.Enumeration fileEnumerator = fileHelper.objectEnumerator(); fileEnumerator.hasMoreElements();) {
			NSDictionary record = (NSDictionary) fileEnumerator.nextElement();
			result.addObject(record);
		} //~ for (java.util.Enumeration fileEnumerator = ...
    	fileHelper.reset();
    	return result;
    }
}
