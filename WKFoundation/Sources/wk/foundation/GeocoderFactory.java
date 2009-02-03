package wk.foundation;

/**
 * Abstract factory that can return a Geocoder
 *
 * @author kieran
 *
 */
public abstract class GeocoderFactory {
	private static final String FACTORY_CLASS_PROPERTY = "wk.foundation.defaultGeocoderFactory";
	/**
	 * @return a Geocoder object
	 */
	public abstract Geocoder createGeocoder();

	/**
	 * @return factory, determined from system properties using boilerplate code
	 */
	public static GeocoderFactory defaultFactory() {
        String defaultFactoryClassName = System.getProperty(FACTORY_CLASS_PROPERTY);
        if (defaultFactoryClassName == null) {
            throw new RuntimeException("The system property '" + FACTORY_CLASS_PROPERTY + "' is not defined!");
        }

        GeocoderFactory factory = null;
        try {
            Class clazz = Class.forName(defaultFactoryClassName);
            factory = (GeocoderFactory) clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate class with fully qualified name '" + defaultFactoryClassName + "'");
        }

        return factory;
	}
}
