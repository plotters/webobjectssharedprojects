//
//  WKStandardResultsContainer.java
//  cheetah
//
//  Created by Kieran Kelleher on 10/13/06.
//  Copyright 2006 Kieran Kelleher. All rights reserved.
//

package wk.foundation;

import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;

/** Defines standard a standard results container 
having a Boolean for TRUE or FALSE indicating success result or not
and an array of problem descriptions for failure situation and
a dictionary for other values that might be required by the context */
public class WKStandardResultsContainer {
    protected boolean _isSuccess = false;
    protected NSMutableArray _messages;
    protected NSMutableDictionary _userInfo;
    
    public WKStandardResultsContainer() { }
    
    public WKStandardResultsContainer( boolean success ) {
        setIsSuccess( success );
    }
    
    /** Set the success value to true or false */
    public void setIsSuccess( boolean newSuccess ) {
        _isSuccess = newSuccess;
    }
    
    /** @return true or false */
    public boolean isSuccess() {
        return _isSuccess;
    }
    
    /** Add a String message to the messages. */
    public void addMessage( String newMessage ) {
        messages().addObject( newMessage );
    }
    
    /** @return the array of String messages */
    public NSMutableArray messages() {
        if ( _messages == null ) {
            _messages = new NSMutableArray();
        }
        return _messages;
    }
    
    /** @return a string representation of this object */
    public String toString() {
        return "isSuccess = " + _isSuccess
        + ";\n messages = " + ( _messages == null ? "null" : WKStringUtilities.arrayToString(_messages))
        + ";\n userInfo = " + ( _userInfo == null ? "null" : _userInfo.toString() );
    }
    
    /** A dictionary for holding additional information depending on the context of this results container. */
    public NSMutableDictionary userInfo() {
        if ( _userInfo == null ) {
            _userInfo = new NSMutableDictionary();
        }
        return _userInfo;
    }

}
