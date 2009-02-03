package wk.foundation;

import java.util.Enumeration;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;


/**
 * A class that accepts a string of an array of widths and returns the string separated into strings of width lengths.  You can either use the enumeration interface to return the strings one by one or get them all at once using the <code>allRemainingTokens()</code> method.
 *
 * @author Copyright (c) 2001-2005  Global Village Consulting, Inc.  All rights reserved.
 * This software is published under the terms of the Educational Community License (ECL) version 1.0,
 * a copy of which has been included with this distribution in the LICENSE.TXT file.
 * @version $Revision: 6$
 */
public class FixedWidthStringTokenizer implements Enumeration
{
    protected String stringToTokenize;
    protected NSArray tokenWidths;
    protected Enumeration widthEnumerator;
    protected int mark = 0;
    protected NSArray tokenNames = null;
    protected Enumeration nameEnumerator = null;
    protected String nameOfCurrentToken;


    /**
     * Designated constructor.
     *
     * @param str the string to tokenize
     * @param widths the widths of the tokens
     * @param names the names of the tokens
     */
    public FixedWidthStringTokenizer(String str, NSArray widths, NSArray names)
    {
        super();
        /** require
        [valid_str_param] str != null;
        [valid_widths_param] widths != null;
        [widths_equals_string_length] ((Number)widths.valueForKeyPath("@sum")).intValue() == str.length();
        [has_name_for_each_width] names != null ? widths.count() == names.count() : true; **/

        stringToTokenize = str;
        tokenWidths = widths;
        tokenNames = names;
        widthEnumerator = widths.objectEnumerator();

        if (tokenNames != null)
        {
            nameEnumerator = names.objectEnumerator();
        }
    }



    /**
     * Equivilant to <code>FixedWidthStringTokenizer(str, widths, null)</code>.
     *
     * @param str the string to tokenize
     * @param widths the widths of the tokens
     */
    public FixedWidthStringTokenizer(String str, NSArray widths)
    {
        this(str, widths, null);
        /** require
        [valid_str_param] str != null;
        [valid_widths_param] widths != null;
        [widths_equals_string_length] ((Number)widths.valueForKeyPath("@sum")).intValue() == str.length(); **/
    }



    /**
     * Returns true if there are more tokens.
     *
     * @return true if there are more tokens
     */
    public boolean hasMoreElements()
    {
        return mark != stringToTokenize.length();
    }



    /**
     * Returns the next token as an Object.
     *
     * @return the next token
     */
    public Object nextElement()
    {
        /** require [has_more_elements] hasMoreElements(); **/

        int tokenLength = ((Integer)widthEnumerator.nextElement()).intValue();

        if (nameEnumerator != null)
        {
            nameOfCurrentToken = (String)nameEnumerator.nextElement();
        }

        Object token = stringToTokenize.substring(mark, mark + tokenLength);
        mark += tokenLength;
        return token;

        /** ensure [valid_result] Result != null; **/
    }



    /**
     * Returns true if there are more tokens.
     *
     * @return true if there are more tokens
     */
    public boolean hasMoreTokens()
    {
        return hasMoreElements();
    }



    /**
     * Returns the next token as a String.
     *
     * @return the next token
     */
    public String nextToken()
    {
        /** require [has_more_tokens] hasMoreTokens(); **/
        return (String)nextElement();
        /** ensure [valid_result] Result != null; **/
    }



    /**
     * Returns the array of token names.
     *
     * @return the array of token names
     */
    public NSArray tokenNames()
    {
        return tokenNames;
    }



    /**
     * Returns the current token's name.
     *
     * @return the current token's name
     */
    public String currentTokenName()
    {
        /** require [has_token_names] tokenNames() != null; **/
        return nameOfCurrentToken;
        /** ensure [valid_result] Result != null; **/
    }



    /**
     * Returns all remaining tokens in an NSArray.  Calling this before using <code>nextElement()</code> or <code>nextToken()</code> will return all tokens.
     *
     * @return all remaining tokens
     */
    public NSArray allRemainingTokens()
    {
        NSMutableArray allTokens = new NSMutableArray();

        while (hasMoreTokens())
        {
            allTokens.addObject(nextToken());
        }

        return allTokens;

        /** ensure [valid_result] Result != null; **/
    }



    /**
     * Returns all remaining tokens in an NSDictioanry, with the token names as keys and their values as values.
     *
     * @return all tokens
     */
    public NSDictionary allRemainingTokensWithNames()
    {
        NSMutableDictionary allTokensWithNames = new NSMutableDictionary();

        while (hasMoreTokens())
        {
            String token = nextToken();
            String name = currentTokenName();
            allTokensWithNames.setObjectForKey(token, name);
        }

        return allTokensWithNames;

        /** ensure [valid_result] Result != null; **/
    }



    /** invariant
    [has_string_to_tokenize] stringToTokenize != null;
    [has_token_widths] tokenWidths != null;
    [has_width_enumerator] widthEnumerator != null;
    [mark_is_consistant] mark <= stringToTokenize.length(); **/



}
