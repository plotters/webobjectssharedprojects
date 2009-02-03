package wk.foundation;

public class GeocodingResultImpl implements GeocodingResult {
	private final GlobalCoordinate coordinate;
	private final String geocodeLevel;

	public GeocodingResultImpl(GlobalCoordinate coordinate, String geocodeLevel) {
		this.coordinate = coordinate;
		this.geocodeLevel = geocodeLevel;
	}

	public GlobalCoordinate coordinate() {
		return coordinate;
	}

	public String geocodeLevel() {
		return geocodeLevel;
	}

}
