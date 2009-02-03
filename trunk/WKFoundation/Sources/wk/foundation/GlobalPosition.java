//
//  GlobalPosition.java
//  cheetah
//
//  Created by Kieran Kelleher on 5/9/05.
//  Copyright 2005 Kieran Kelleher. All rights reserved.
//

package wk.foundation;


import com.webobjects.foundation.NSKeyValueCodingAdditions;



/** An object implementing this interface can be represented by a global position
having a latitude and longitude.
It needs NSKeyValueCodingAdditions interface to I can use EOQualifiers to
filter/search in my utility methods. this is fine since this interface
is typically implemented by my Enterprise objects which
already implement NSKeyValueCodingAdditions */
public interface GlobalPosition extends NSKeyValueCodingAdditions {
    public static final double MILES_PER_LAT = 69.1;
    public static final double DEGREES_PER_RADIAN = 57.2957795131;

    /** @return the global latitude of the object's position.
        West of Greenwich is negative. East of Greenwich is positive.
        Value range -180.000000 to +180.000000*/
    public Double latitude();

    /** @return the global longitude of the object's position.
        South of equator is negative. North of equator is positive.
        Value range -90.000000 to +90.000000*/
    public Double longitude();

    public void setLatitude( Double latitude );

    public void setLongitude( Double longitude );

    /** @return the distance in miles from this to the argument's position */
    public Double distance( GlobalPosition toPosition );

    public boolean requiresGeoCoding();
}
