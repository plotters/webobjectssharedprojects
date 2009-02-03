/**
 *
 */
package wk.foundation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Enumeration;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import wk.foundation.concurrent.TaskPercentComplete;
import wk.foundation.concurrent.TaskStatus;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSRange;

/**
 * @author kieran
 * A class that simplifies the handling of text files containing lines of similar data.
 * Typically this is used for database import files and list import files.
 * We use the LineNumberReader class as a basis for this convenience class.
 * Usage is simple:
 *         Create an instance passing a File object in the constructor.
 *         Get an enumerator instance and enumerate through the records which are
 *             returned as instances of NSDictionary with field names as keys and field values as values
 * This class is not threadsafe. Basically only one enumerator instance should be iterated at one time.
 * This class can be modified for multi-thread enumeration thru the file if requirements change later.
 */
public class DataRecordFileHelper implements TaskPercentComplete, TaskStatus {
    private static final Logger log = Logger
            .getLogger(DataRecordFileHelper.class);

    protected File _aFile;
    protected LineNumberReader _lineReader;
    protected String currentLine = "";
    protected boolean hasSkippedLines = false;
    boolean isStrict = true;

    int lastLineRead = -1;
    // ascii #32, aka EOF, aka CTRL-Z, aka Unicode \u001A
    private static final char END_OF_FILE = '\u001A';

    /**
     * @param aFile
     * This is the simplest constructor that takes a file only
     */
    public DataRecordFileHelper(File aFile){
        _aFile = aFile;
    }

    public DataRecordFileHelper(File aFile, boolean isStrict) {
    	_aFile = aFile;
    	this.isStrict = isStrict;
    }

    private Integer _numberOfLinesInFile;

	/** @return the number of lines in the file */
	public int numberOfLinesInFile() {
		if ( _numberOfLinesInFile == null ) {
			_numberOfLinesInFile = Integer.valueOf(WKFileUtilities.countLines(_aFile));
		}
		return _numberOfLinesInFile.intValue();
	}

    /**
     * @param aFile
     * @param fieldNames
     * A constructor where the filename and array of fieldnames is provided.
     * Typically this is used where we have a file provided without any skiplines
     * or field headings.
     * Since a constructor is called before any class fields are set, the skipLines
     * is set to zero as a default in the event that skipLines is not explicitly set later.
     */
    public DataRecordFileHelper(File aFile, NSArray fieldNames){
        setSkipLines(0);
        _aFile = aFile;
        _fieldNames = fieldNames;
    }

    /**
     * @return a java buffered line reader that reads one line at a time.
     */
    protected LineNumberReader lineReader() {
        if (_lineReader == null) {
            try {
                _lineReader = new LineNumberReader(new BufferedReader(new FileReader(_aFile)));
            } catch (FileNotFoundException exception) {
                log.error("Caught an error: ", exception);
                throw new RuntimeException("Caught an error: ", exception);
            }
        }

        return _lineReader;
    }

    protected int _skipLines = -1;

    /** @return the initial number of lines to skip
     * By default we assume we have one line of headings to skip. if no headings then this should be set
     * to zero */
    public int skipLines() {
        if ( _skipLines == -1 ) {
            _skipLines = 1;
        }
        return _skipLines;
    }

    public void setSkipLines(int skipLines){
        _skipLines = skipLines;
    }

    /**
    * @return the next line and increments the currentLine to reflect the line number (zero-based)
    */
    public String nextLine() {
//        if (log.isDebugEnabled())
//            log.debug("Before readline, line number = " + lineReader().getLineNumber());
        String nextLine = null;;
        try {
            lastLineRead = lineReader().getLineNumber();
            nextLine = lineReader().readLine();
            if (nextLine != null && (nextLine.length() == 1 && nextLine.charAt(0) == END_OF_FILE)) {
                nextLine = null;
            } //~ if (nextLine.equals(anObject))
        } catch (IOException exception) {
            log.error("Caught an error: ", exception);
            closeReaderStream();
        }

        if (nextLine == null) {
            // We have finished reading the file, so close it
            closeReaderStream();
        }

        if (log.isDebugEnabled())
            log.debug("Just read; lineNumber = " + lineReader().getLineNumber() + "; line = " + ObjectUtils.toString(nextLine,"null"));
        return nextLine;
    }

    private void closeReaderStream() {
        if ( _lineReader != null){
            try {
                _lineReader.close();
            } catch (Exception e) {
                // ignore
            }
        }
    }

    /**
    * Resets reader stream and certain ivars so we can enumerate from scratch again
    * Also should be called when disposing of the object to close the filestream.
    * Do not call in the middle of enumerating
    */
    public void reset() {
        closeReaderStream();
        _lineReader = null;
        // Commented out this next line since it does not make sense as the file contents are not changing <em>and</em>
        // we now allow fieldNames to be set once in the constructor
        //_fieldNames = null;
        currentLine = "";

        // New boolean for case of no headings, so we must purposefully skip the skip lines
        hasSkippedLines = false;

    }

    protected int _headingsLine = -1;

    /** @return the line number (1-based) containing the field names/headings */
    public int headingsLine() {
        if ( _headingsLine == -1 ) {
            _headingsLine = 1;
        }
        return _headingsLine;
    }

    public void setHeadingsLine(int headingsLine){
        _headingsLine = headingsLine;
    }

    protected NSArray _fieldNames;

    /**
    * @return the field names parsed into an array.
    * This is where the first line read occurs
    * FIXME: For field names, we should just open a reader for the file, read to the headings line, grab it, and close the reader keeping this separate to skipLines
    */
    public NSArray fieldNames() {
        if (_fieldNames == null) {
            if (log.isDebugEnabled())
                log.debug("Initializing fieldNames with skipLines = " + skipLines() + " and headingsLine = " + headingsLine() );
            currentLine = nextLine();

            while (currentLine != null && lineReader().getLineNumber() < skipLines() + 1) {
                hasSkippedLines = true;

                if (headingsLine() == lineReader().getLineNumber()) {
                    // We have the headings line, parse and store it
                    if (log.isDebugEnabled())
                        log.debug("selected headings line = currentLine = "
                                + (currentLine == null ? "null" : currentLine
                                        .toString()));
                    _fieldNames = parseLine(currentLine);
                }
                currentLine = nextLine();
            }
            // TODO : Validate that the headings match the expected headings without consideration of ordering in filehelper

        }

        return _fieldNames;
    }

    public void setFieldNames( NSArray fieldNames){
        // If skipLines has not been set, then default it to 0
        // This maintains same behaviour as constructor that takes the fieldNames arg
        if (_skipLines == -1) {
            setSkipLines(0);
        } //~ if (_skipLines == -1)
        _fieldNames = fieldNames;
    }

    public int currentLineNumber() {
        return lastLineRead;
//        int lineNumber = -1;
//        try {
//            if (_lineReader != null && _lineReader.ready()) {
//                lineNumber = _lineReader.getLineNumber();
//            }
//        } catch (IOException exception) {
//            // Ignore
//        }
//        return lineNumber - 1;
    }

    /**
    * @return an enumerator for the parsed lines. The enumerator itself
    * returns dictionaries where the keys are the headings
    */
    public Enumeration<NSDictionary> objectEnumerator() {
        return new RecordDictionaryEnumerator();
    }

    private class RecordDictionaryEnumerator implements Enumeration {
        /**
        *
        */
        public RecordDictionaryEnumerator() {
            // Reset the file reader when this is constructed
            reset();

            // We need to call fieldNames at least once before iterating to
            // lazily initialiaze the fieldnames, skip headings and get into position for reading data
            NSArray headings = fieldNames();
            if (log.isDebugEnabled())
                log.debug("headings = "
                        + (headings == null ? "null" : headings.toString()));

            // In the case of already provided fieldNames that are not in the file headings, this will be executed
            if (!hasSkippedLines) {
                skipTheLines();
            } //~ if (!hasSkippedLines)


        }

        /* (non-Javadoc)
        * @see java.util.Enumeration#hasMoreElements()
        */
        public boolean hasMoreElements() {
            return currentLine == null ? false : true;
        }

        /* (non-Javadoc)
        * @see java.util.Enumeration#nextElement()
        */
        public Object nextElement() {
            NSDictionary aDict = valuesForLine(currentLine);
            currentLine = nextLine();
            return aDict;
        }
    }

    private void skipTheLines(){
        for (int i = 0; i < skipLines(); i++) {
            @SuppressWarnings("unused")
            String skippedLine = nextLine();
        }
        currentLine = nextLine();
    }

    protected StrTokenizer _lineParser;

    /** @return a parser to tokenize the fields in a line of data */
    protected StrTokenizer lineParser() {
        if ( _lineParser == null ) {
            _lineParser = StrTokenizer.getCSVInstance();

        }
        return _lineParser;
    }

    /**
    * @param lineToParse
    * @return a NSDictionary where keys are the field names and values are the field values
    */
    private NSDictionary valuesForLine(String lineToParse) {
        NSDictionary result = null;
        NSArray fieldValues = parseLine(lineToParse);

        if (fieldNames().count() != fieldValues.count()) {
        	// Excel CSV export will drop trailing empty fields, so maybe we want to handle that situation
        	if (isStrict) {
                String message = "Field values count do not match field names count. Skipping line Number = " + lineReader().getLineNumber()
                + "; field values (" + fieldValues.count() + ") = " + fieldValues
                + "; field headings (" + fieldNames().count() + ")= " + fieldNames();

                log.error(message);
                throw new RuntimeException(message);
        	} else {
        		// Just make a dictionary with a subset of the headings if values are less than headings
        		if (fieldValues.count() > fieldNames().count()) {
                    String message = "There are more field values than headings. Skipping line Number = " + lineReader().getLineNumber()
                    + "; field values (" + fieldValues.count() + ") = " + fieldValues
                    + "; field headings (" + fieldNames().count() + ")= " + fieldNames();

                    log.error(message);
                    throw new RuntimeException(message);
        		} else {
        			NSArray tempFieldHeadings = fieldNames().subarrayWithRange(new NSRange(0, fieldValues.count()));
        			result = new NSDictionary(fieldValues, tempFieldHeadings);
				} //~ if (fieldValues.count() > fieldNames().count())
			} //~ if (condition)


            if (log.isDebugEnabled()) {
            	for (int i = 0; i < lineToParse.length(); i++) {
            		char character = lineToParse.charAt(0);
            		log.debug("Unicode value at position 1 = " + CharUtils.unicodeEscaped(character) + " and the char itself is " + character);
            		log.debug("isAsciiControlChar = " + CharUtils.isAsciiControl(character));
            	}
            } //~ if (log.isDebugEnabled())

        } else {
            result = new NSDictionary(fieldValues, fieldNames());
        }
        return result;
    }

    protected NSArray parseLine(String lineToParse) {
        lineParser().reset(lineToParse);
        NSArray tokensArray = new NSArray( lineParser().getTokenArray() );
        if (log.isDebugEnabled())
            log.debug("tokensArray = "
                    + (tokensArray == null ? "null" : tokensArray.toString()));
        return tokensArray;
    }


    /**
     * @author kieran
     * A class for testing the functionality of this file
     *
     */
    public static class Test {
        /**
        * @param args
        */
        public static void main(String[] args) {
            // Initialize logging
            BasicConfigurator.configure();

            System.out.println("Test.main(): Testing ...");

            File aFile = new File("/Users/kieran/DevProjects/Cheetah/DataImport/CustomerImportTestFile.csv");
            DataRecordFileHelper fileHelper = new DataRecordFileHelper(aFile);
            fileHelper.setHeadingsLine(2);
            fileHelper.setSkipLines(2);
            Enumeration dictEnumerator = fileHelper.objectEnumerator();
            while (dictEnumerator.hasMoreElements()) {
                NSDictionary lineDict = (NSDictionary) dictEnumerator.nextElement();
                System.out.println("Test.main(): lineDict = " + lineDict);
            }

            dictEnumerator = fileHelper.objectEnumerator();
            while (dictEnumerator.hasMoreElements()) {
                NSDictionary lineDict = (NSDictionary) dictEnumerator.nextElement();
                System.out.println("Test.main(): lineDict = " + lineDict);
            }

        }

    }

	/* (non-Javadoc)
	 * @see wk.foundation.concurrent.TaskPercentComplete#percentComplete()
	 */
	public Double percentComplete() {
		if (currentLineNumber() < 0 || numberOfLinesInFile() < 1) {
			return 0.01;
		} else {
			return ((double)currentLineNumber() / (double)numberOfLinesInFile());
		} //~ if (currentLineNumber() < 0 || numberOfLinesInFile() < 1)
	}

	/* (non-Javadoc)
	 * @see wk.foundation.concurrent.TaskStatus#status()
	 */
	public String status() {
		return currentLineNumber() > -1 ? "Reading file: " + currentLineNumber() + " of " + numberOfLinesInFile() : "Starting...";
	}
}
