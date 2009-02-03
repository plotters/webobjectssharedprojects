package wk.foundation;

/**
 * A simple lat, lng coordinate object
 * @author kieran
 *
 */
public final class WKCoordinate implements GlobalCoordinate {
    private double _latitude;
    private double _longitude;

    public WKCoordinate(double newLatitude, double newLongitude) {
        _latitude = newLatitude;
        _longitude = newLongitude;
    }

    public double latitude() {
        return _latitude;
    }

    public double longitude() {
        return _longitude;
    }

    public String toString() {
        return "latitude: " + _latitude + "; longitude: " + _longitude;
    }

}
