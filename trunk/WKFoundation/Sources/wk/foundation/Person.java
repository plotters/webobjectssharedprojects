//
//  Person.java
//  cheetah
//
//  Created by Kieran Kelleher on 3/24/06.
//  Copyright 2006 Kieran Kelleher. All rights reserved.
//

package wk.foundation;

import org.apache.commons.lang.StringUtils;

public interface Person {
    public String firstName();
    public void setFirstName( String firstName );

    public String lastName();
    public void setLastName( String lastName );

    public String fullName();

    public static class Utilities {
        public static void copyFromPersonToPerson( Person fromPerson, Person toPerson ) {
            toPerson.setFirstName( fromPerson.firstName() );
            toPerson.setLastName( fromPerson.lastName() );
        }

        /**
         * @return the fullname of the person as firstName - space - lastName. If both components are null
         * an empty string is returned. If only one component is null, then the other is returned.
         * @param aPerson
         */
        public static String fullName( Person aPerson ) {
        	StringBuffer buff = new StringBuffer( "" );
        	if ( aPerson.firstName() != null ) buff.append( aPerson.firstName() );
        	if ( aPerson.firstName() != null && aPerson.lastName() != null ) buff.append( ' ' );
        	if ( aPerson.lastName() != null ) buff.append( aPerson.lastName() );
        	return buff.toString();
        }

        /**
         * @param fullName
         * @return the first word after splitting the string on spaces
         */
        public static String firstFromFullName(String fullName) {
        	String result = null;
        	if (fullName != null) {
            	String[] words = StringUtils.split(StringUtils.trimToEmpty(fullName), ' ');
            	if (words.length > 0 && !StringUtils.isEmpty(words[0])) {
    				result = words[0];
    			}
			} //~ if (fullName == null)
        	return result;
        }

        /**
         * @param fullName
         * @return the last word after splitting the string on spaces
         */
        public static String lastFromFullName(String fullName) {
        	String result = null;
        	if (fullName != null) {
            	String[] words = StringUtils.split(StringUtils.trimToEmpty(fullName), ' ');
            	if (words.length > 1) {
            		int lastIndex = words.length - 1;
    				result = words[lastIndex];
    			}
			} //~ if (fullName == null)
        	return result;
        }
    }

}
