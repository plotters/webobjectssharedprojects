package wk.foundation;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.webobjects.foundation.NSArray;

/**
 * A <strong>simple</strong> interface for lat/lng coordinates.
 * Do <strong>NOT</strong> add any more to this interface! Keep it simple.
 * Also, we use natives here since these are not optional attributes.
 * If necessary, EO's and the like can return a GlobalCoordinate object or null if it does not know
 * its lat/lng
 * @author kieran
 *
 */
public interface GlobalCoordinate {
    public static final double MILES_PER_LAT = 69.1;
    public static final double DEGREES_PER_RADIAN = 57.2957795131;

    public double latitude();
    public double longitude();

    public interface Attribute {
        public GlobalCoordinate coordinate();
    }

    public static class Utilities {

    	public static GlobalCoordinate newInstance(double latitude, double longitude) {
    		return new WKCoordinate(latitude, longitude);
    	}

    	public static GlobalCoordinate newInstance(String latitude, String longitude) {
    		double lat = Double.parseDouble(latitude);
    		double lng = Double.parseDouble(longitude);
    		return new WKCoordinate(lat, lng);
    	}

        /** @return the distance between position1 and position2 */
        public static double approximateDistance( GlobalCoordinate position1, GlobalCoordinate position2 ) {
            // NPE prevention
            if ( position1 == null || position2 == null ) {
                throw new IllegalArgumentException("Cannot calculate distance to a null argument ( position1 = "
                        + (position1 == null ? "null" : position1.toString())
                        + "; position2 = " + (position2 == null ? "null" : position2.toString()));
            }

            double position1Lat = position1.latitude();
            double position1Lng = position1.longitude();

            double position2Lat = position2.latitude();
            double position2Lng = position2.longitude();

            // Calculate latitudinal distance in miles
            double distLat = MILES_PER_LAT * ( position1Lat - position2Lat );

            // Caculate longitudinal distance in miles
            double distLng = MILES_PER_LAT * ( position1Lng - position2Lng ) * Math.cos( position1Lat / DEGREES_PER_RADIAN );

            // Calculate distance using Pythagoras' Theorem
            double distance = Math.sqrt( Math.pow(distLat, 2.0) + Math.pow(distLng, 2.0) );

            return distance;

        }

        /**
         * TODO : Add generics parameter when generic NSArray supported by Apple's NSArray
         * @param cluster - NSArray of type NSArray<GlobalCoordinate.Attribute>.
         * @return the center of the cluster for all objects having non-null coordinates. Returns null if no non-null coordinates existed.
         */
        public static GlobalCoordinate clusterCentroid(NSArray cluster) {
        	BigDecimal latitudeSum = new BigDecimal("0.00000");
        	BigDecimal longitudeSum = new BigDecimal("0.00000");
        	long coordinateCount = 0;
        	for (java.util.Enumeration enumerator = cluster.objectEnumerator(); enumerator.hasMoreElements(); ) {
				GlobalCoordinate.Attribute element = (GlobalCoordinate.Attribute) enumerator.nextElement();
				GlobalCoordinate coordinate = element.coordinate();
				if (coordinate != null) {
					latitudeSum = latitudeSum.add(BigDecimal.valueOf(coordinate.latitude()));
					longitudeSum = longitudeSum.add(BigDecimal.valueOf(coordinate.longitude()));
					coordinateCount++;
				} //~ if (coordinate != null)
			} //~ for (java.util.Enumeration enumerator = ...
        	if (coordinateCount > 0) {
				BigDecimal bdLatitude = latitudeSum.divide(BigDecimal.valueOf(coordinateCount), RoundingMode.HALF_UP);
				BigDecimal bdLongitude = longitudeSum.divide(BigDecimal.valueOf(coordinateCount), RoundingMode.HALF_UP);
				WKCoordinate result = new WKCoordinate(bdLatitude.doubleValue(),bdLongitude.doubleValue());
				return result;
        	} else {
        		return null;
			} //~ if (latitudeCount)
        }

        /**
         * @param cluster
         * @return the radius of the cluster indicating the size of the cluster
         */
        public static Double clusterRadius(NSArray cluster) {
        	double maxLat;
        	double maxLong;
        	double minLat;
        	double minLong;

        	boolean didSet = false;

        	// Use random element as starting point for min/max

        	GlobalCoordinate initialValues = oneClusterElement(cluster);

        	if (initialValues == null) {
				// No centroid means no cluster at all
        		return null;
			} //~ if (initialValues == null)

        	maxLat = initialValues.latitude();
        	maxLong = initialValues.longitude();
        	minLat = maxLat;
        	minLong = maxLong;

        	for (java.util.Enumeration enumerator = cluster.objectEnumerator(); enumerator.hasMoreElements(); ) {
				GlobalCoordinate.Attribute object = (GlobalCoordinate.Attribute) enumerator.nextElement();
				GlobalCoordinate coordinate = object.coordinate();
				if (coordinate != null) {
					didSet = true;

					if (coordinate.latitude() > maxLat) {
						maxLat = coordinate.latitude();
					} //~ if (coordinate.latitude() > maxLat)

					if (coordinate.latitude() < minLat) {
						minLat = coordinate.latitude();
					} //~ if (coordinate.latitude() < minLat)

					if (coordinate.longitude() > maxLong) {
						maxLong = coordinate.longitude();
					} //~ if (coordinate.longitude() > maxLong)

					if (coordinate.longitude() < minLong) {
						minLong = coordinate.longitude();
					} //~ if (coordinate.longitude() < minLong)

				} //~ if (coordinate != null)
			} //~ for (java.util.Enumeration enumerator = ...

        	if (didSet) {
				WKCoordinate upperLeft = new WKCoordinate(maxLat, minLong);
				WKCoordinate upperRight = new WKCoordinate(maxLat, maxLong);
				WKCoordinate lowerLeft = new WKCoordinate(minLat,minLong);
				double width = approximateDistance(upperLeft, upperRight);
				double height = approximateDistance(upperLeft, lowerLeft);
				double maxRadius = Math.max(width, height) / 2;
				return new Double(maxRadius);
        	} else {
        		return null;
			} //~ if (didSet)

        }

        public static GlobalCoordinate oneClusterElement(NSArray cluster) {
        	GlobalCoordinate result = null;
        	for (java.util.Enumeration enumerator = cluster.objectEnumerator(); enumerator.hasMoreElements() && result == null; ) {
				GlobalCoordinate.Attribute object = (GlobalCoordinate.Attribute) enumerator.nextElement();
				GlobalCoordinate coordinate = object.coordinate();
				if (coordinate != null) {
					result = coordinate;
				} //~ if (coordinate != null)
			} //~ for (java.util.Enumeration enumerator = ...
        	return result;
        }
    }
}
