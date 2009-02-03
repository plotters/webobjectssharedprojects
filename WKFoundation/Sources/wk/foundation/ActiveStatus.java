//
//  ActiveStatus.java
//  cheetah
//
//  Created by Kieran Kelleher on 8/25/06.
//  Copyright 2006 Kieran Kelleher. All rights reserved.
//

package wk.foundation;

import com.webobjects.foundation.NSComparator;
import com.webobjects.foundation.NSTimestamp;

/** Denotes behaviour where an entity is active or not active
    based on a activity start date and an activity end date.
    Customers, Locations and Programs are example of entities
    that might want to implement this common behaviour */
public interface ActiveStatus {

    /** @return the date at which activity begins. May be null */
    public NSTimestamp startDate();

    public void setStartDate( NSTimestamp startDate );

    /** @return the date at which activity ends. May be null */
    public NSTimestamp endDate();

    public void setEndDate( NSTimestamp endDate );

    /** @return whether the entity is active at the current time */
    public Boolean isActive();

    /** @return whether the entity is active at a certain time, in past or future */
    public Boolean isActive( NSTimestamp dateToCheck );

    /** Deactivates an entity */
    public void deactivate();


    /**
     * Activates an entity
     */
    public void activate();

    // TODO: Take this class out of here so we can put some EOQualifier stuff in there.
    public static class Utilities {
        /** Based on the startDate and endDate, this calculates whether the object is active
            on a specified date. If endDate is null, it is assumed to be distant infinite future.
            If startDate is null, it is assumed to be distant infinite past.
            @param object the object to check it's active state
            @param checkDate the date to check.
            @return whether the object is active on checkDate or not.*/
        public static Boolean isActive( ActiveStatus object, NSTimestamp checkDate ) {
            NSTimestamp aStartDate = ( object.startDate() == null ? NSTimestamp.DistantPast : object.startDate() );
            NSTimestamp aEndDate = ( object.endDate() == null ? NSTimestamp.DistantFuture : object.endDate() );
            return ( checkDate.compare( aStartDate ) != NSComparator.OrderedAscending && checkDate.compare( aEndDate ) != NSComparator.OrderedDescending ) ?
                Boolean.TRUE : Boolean.FALSE;
        }

        /** @param object the object to check it's active state
            @return whether the object is active now or not. */
        public static Boolean isActive( ActiveStatus object ) {
            return isActive( object, new NSTimestamp() );
        }

        /** Deactivates an entity by setting the endDate to now */
        public static void deactivate( ActiveStatus object ) {
            if ( isActive( object ).booleanValue() ) object.setEndDate( new NSTimestamp() );
        }

        /**
         * @param object
         * Activates an entity by setting start date to yesterday if necessary and nullifying end date
         */
        public static void activate( ActiveStatus object ) {
            if (object.startDate() != null && object.startDate().after(new NSTimestamp())) {
                object.setStartDate(WKTimestampUtilities.timestampByAddingDays(new NSTimestamp(), -1));
            }
            object.setEndDate(null);
        }



    }
}
