//
//  GeocodeLevel.java
//  cheetah
//
//  Created by Kieran Kelleher on 8/2/06.
//  Copyright 2006 Kieran Kelleher. All rights reserved.
//

package wk.foundation;

/**
 * Defines constants and methods for geocode level attribute which indicates the quality/accuracy/tolerance of geocoding
 * on a GeocodeLevel object
 * @author kieran
 *
 */
public interface GeocodeLevel extends GeocodeLevelReadOnly {

    /** geocodeLevel constant - ZIP+4 geocoded*/
    public static final String GEOCODE_LEVEL_ZIP_PLUS_4 = "ZIP4";

    /** geocodeLevel constant - ZIP+2 geocoded*/
    public static final String GEOCODE_LEVEL_ZIP_PLUS_2 = "ZIP2";

    /** geocodeLevel constant - 5-digit ZIP centroid geocoded */
    public static final String GEOCODE_LEVEL_ZIP_5 = "ZIP5";

    /** geocodeLevel constant - Rooftop geocoded. */
    public static final String GEOCODE_LEVEL_ROOFTOP = "ROOF";

    /** geocodeLevel constant - User set manually */
    public static final String GEOCODE_LEVEL_USER = "USER";

    public void setGeocodeLevel( String geocodeLevel );

    public static class Utilities {
    	public static void copyFromTo(GeocodeLevel from, GeocodeLevel to) {
    		to.setGeocodeLevel(from.geocodeLevel());
    	}
    }

}
