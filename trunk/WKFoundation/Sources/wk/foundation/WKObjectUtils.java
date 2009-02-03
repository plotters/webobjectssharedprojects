package wk.foundation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;

/**
 * Adding some more useful methods to apache's ObjectUtils
 *
 * @author kieran
 *
 */
public class WKObjectUtils extends ObjectUtils {

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(WKObjectUtils.class);

    public static final Class[] EmptyClassArray = new Class[0];
    public static final Object[] EmptyObjectArray = new Object[] {};

	/**
	 * Copied from ERXCloneableThreadLocal
	 *
	 * @param sourceObject, must implement {@link Cloneable}.
	 * @return clone (copy) of the object using object.clone() method
	 */
    public static Object cloneObject(Object sourceObject) {
    	Object child = null;
    	if (sourceObject != null) {
    		// This is very lame. clone() is a protected method off of object and the Cloneable
    		// interface doesn't specify any methods.
    		try {
    			Method m = sourceObject.getClass().getMethod("clone", EmptyClassArray);
    			child = m.invoke(sourceObject, EmptyObjectArray);
    		} catch (InvocationTargetException ite) {
    			log.error("Invocation exception occurred when invoking clone in ERXClonableThreadLocal:"
    							+ ite.getTargetException() + " backtrace: ", ite);
    		} catch (NoSuchMethodException nsme) {
    			log.error("No clone method for the class: " + sourceObject.getClass() + " very strange.");
    		} catch (IllegalAccessException iae) {
    			log.error("Clone method has protected or private access for the object: "
    							+ sourceObject.getClass() + " " + sourceObject.toString()
    							+ " exception: " + iae.getMessage());
    		}
    	}
    	return child;

    }
}
