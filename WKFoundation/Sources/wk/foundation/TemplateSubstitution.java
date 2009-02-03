
package wk.foundation;

import java.util.StringTokenizer;

import com.webobjects.foundation.NSKeyValueCodingAdditions;


/**
 * Provides simple template substituion functions using keyPath extensions to key-value coding and objects implementing NSKeyValueCoding.
 *
 * @author Copyright (c) 2001-2005  Global Village Consulting, Inc.  All rights reserved.
 * This software is published under the terms of the Educational Community License (ECL) version 1.0,
 * a copy of which has been included with this distribution in the LICENSE.TXT file.
 * @version $Revision: 6$
 */
public class TemplateSubstitution extends Object
{
    /**
     * String used to indicate the start of a token (keyword).  Used by <a href="#substituteValuesInStringFromDictionary(String, NSDictionary)">substituteValuesInStringFromDictionary(String, NSDictionary)</a>.
     */
    public static final String tokenStartElement = "<<";

    /**
     * String used to indicate the end of a token (keyword).  Used by <a href="#substituteValuesInStringFromDictionary(String, NSDictionary)">substituteValuesInStringFromDictionary(String, NSDictionary)</a>.
     */
    public static final String tokenEndElement = ">>";

    /**
     * Delimiters which seperate tokens.  Don't use any of these in a token start or end element!
     */
    public static final String tokenDelimiters = " \t\n\r,;.:-'\"!$%()&?";


    /**
     * Static methods only.  You'll never need to instantiate this class.
     */
    private TemplateSubstitution()
    {
        super();
    }



    /**
     * Searches template for keywords and substitutes a value from the object (using valueForKeyPath) for the keyword.  Keywords must be set off with white space and marked with <code>tokenStart</code> and <code>tokenEnd</code>.  If the value for a keyword is null, nothing is added to the template.  An example template.<br>
     * &gt.&gt.lastName&lt.&lt., &gt.&gt.lastName&lt.&lt., born &gt.&gt.dateOfBirth&lt.&lt. in &gt.&gt.birthPlace.city&lt.&lt..
     *
     * @param template template in which to substitute values for keywords
     * @param object object containing values to be substituted for keywords
     * @param tokenStart string that marks the start of a keyword token
     * @param tokenEnd string that marks the end of a keyword token
     * @return result of substituting values in dictionary into template
     * @exception RuntimeException if the template has tokenStart but not tokenEnd
     * @see TemplateSubstitution#substituteValuesInStringFromDictionary
     */
    public static String substituteValuesInStringFromDictionary(String template,
                                                                NSKeyValueCodingAdditions object,
                                                                String tokenStart,
                                                                String tokenEnd)
    {
        /** require
        [valid_template_param] template != null;
        [valid_object_param] object != null;
        [valid_tokenStart_param] tokenStart != null;
        [valid_tokenEnd_param] tokenEnd != null; **/

        StringTokenizer templateTokenizer = new StringTokenizer(template, tokenDelimiters, true);

        int keywordStartIndex = tokenStart.length();            // Index in a token where the acutal keyword starts
        int tokenEndElementAdjustment = tokenEnd.length();  // Index adjustment to account for ending delimiter

        String resultString = "";

        while (templateTokenizer.hasMoreTokens())
        {
            String currentToken = templateTokenizer.nextToken();

            if (currentToken.startsWith(tokenStart))
            {
                // The . which seperates keys in a keyPath is also a delimiter.  This loop reconstructs the whole keyPath in this case.
                while (templateTokenizer.hasMoreTokens() && ( ! currentToken.endsWith(tokenEnd)))
                {
                    currentToken += templateTokenizer.nextToken();
                }

                // If we don't have an end token, there is no point in trying to process this template.
                if ( ! currentToken.endsWith(tokenEnd))
                {
                    throw new RuntimeException("Malformed element " + currentToken + " in template " + template);
                }

                // Translate the token into its value
                String keyword = null;
                try
                {
                    keyword = currentToken.substring(keywordStartIndex, currentToken.length() - tokenEndElementAdjustment);
                }
                catch (StringIndexOutOfBoundsException e) { }

                // We might get null back (key not found on object or bound to null).  There is not much we can do it handle this, so just ignore it.  Or should we raise an exception?  Or just insert the keyword / token?
                Object value = object.valueForKeyPath(keyword);

                if (value != null)
                {
                    resultString += value.toString();
                }
                currentToken.endsWith(tokenEnd); // Token is too small or not a keyword, just output
            }
            else
            {
                resultString += currentToken;
            }
        }

        return resultString;

        /** ensure [valid_result] Result != null; **/
    }



    /**
     * Convenience method that calls <code>substituteValuesInStringFromDictionary(String, EOEnterpriseObject, String, String)</code> passing the default values <code>tokenStartElement</code> and <code>tokenEndElement</code> for the tokenStart and tokenEnd.
     *
     * @param template template in which to substitute values for keywords
     * @param object object containing values to be substituted for keywords
     * @return result of substituting values in dictionary into template
     * @see TemplateSubstitution#substituteValuesInStringFromDictionary
     */
    public static String substituteValuesInStringFromDictionary(String template, NSKeyValueCodingAdditions object)
    {
        /** require [valid_template_param] template != null; [valid_object_param] object != null; **/

        String resultString = substituteValuesInStringFromDictionary(template, object, TemplateSubstitution.tokenStartElement, TemplateSubstitution.tokenEndElement);
        return resultString;

        /** ensure [valid_result] Result != null; **/
    }



}
