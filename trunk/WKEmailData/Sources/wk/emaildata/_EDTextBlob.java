// $LastChangedRevision: 4733 $ DO NOT EDIT.  Make changes to EDTextBlob.java instead.
package wk.emaildata;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;
import org.apache.log4j.Logger;

@SuppressWarnings("all")
public abstract class _EDTextBlob extends er.extensions.eof.ERXGenericRecord {
    public static final String ENTITY_NAME = "EDTextBlob";

    public static final String ENTITY_TABLE_NAME = "TextBlob";

    // Attributes
    public static final String KEY_LAST_USED = "lastUsed";
    public static final String KEY_LOCKING_PROPERTY = "lockingProperty";
    public static final String KEY_OID = "oid";
    public static final String KEY_TEXT = "text";
    public static final String KEY_TYPE = "type";

    // External Column Names capitalized (since EOF always returns raw row keys capitalized)
    public static final String COLKEY_LAST_USED = "lastUsed".toUpperCase();
    public static final String COLKEY_LOCKING_PROPERTY = "lockingProperty".toUpperCase();
    public static final String COLKEY_OID = "oid".toUpperCase();
    public static final String COLKEY_TEXT = "textBlob".toUpperCase();
    public static final String COLKEY_TYPE = "type".toUpperCase();

    // Relationships

  private static Logger LOG = Logger.getLogger(_EDTextBlob.class);

  public EDTextBlob localInstanceIn(EOEditingContext editingContext) {
    EDTextBlob localInstance = (EDTextBlob)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }

  public NSTimestamp lastUsed() {
    return (NSTimestamp) storedValueForKey("lastUsed");
  }

  public void setLastUsed(NSTimestamp value) {
    if (_EDTextBlob.LOG.isDebugEnabled()) {
        _EDTextBlob.LOG.debug( "updating lastUsed from " + lastUsed() + " to " + value);
    }
    takeStoredValueForKey(value, "lastUsed");
  }

  public Integer lockingProperty() {
    return (Integer) storedValueForKey("lockingProperty");
  }

  public void setLockingProperty(Integer value) {
    if (_EDTextBlob.LOG.isDebugEnabled()) {
        _EDTextBlob.LOG.debug( "updating lockingProperty from " + lockingProperty() + " to " + value);
    }
    takeStoredValueForKey(value, "lockingProperty");
  }

  public String text() {
    return (String) storedValueForKey("text");
  }

  public void setText(String value) {
    if (_EDTextBlob.LOG.isDebugEnabled()) {
        _EDTextBlob.LOG.debug( "updating text from " + text() + " to " + value);
    }
    takeStoredValueForKey(value, "text");
  }

  public Integer type() {
    return (Integer) storedValueForKey("type");
  }

  public void setType(Integer value) {
    if (_EDTextBlob.LOG.isDebugEnabled()) {
        _EDTextBlob.LOG.debug( "updating type from " + type() + " to " + value);
    }
    takeStoredValueForKey(value, "type");
  }


  public static EDTextBlob createEDTextBlob(EOEditingContext editingContext, Integer lockingProperty
, Integer type
) {
    EDTextBlob eo = (EDTextBlob) EOUtilities.createAndInsertInstance(editingContext, _EDTextBlob.ENTITY_NAME);
        eo.setLockingProperty(lockingProperty);
        eo.setType(type);
    return eo;
  }

  public static NSArray<EDTextBlob> fetchAllEDTextBlobs(EOEditingContext editingContext) {
    return _EDTextBlob.fetchAllEDTextBlobs(editingContext, null);
  }

  public static NSArray<EDTextBlob> fetchAllEDTextBlobs(EOEditingContext editingContext, NSArray<EOSortOrdering> sortOrderings) {
    return _EDTextBlob.fetchEDTextBlobs(editingContext, null, sortOrderings);
  }

  public static NSArray<EDTextBlob> fetchEDTextBlobs(EOEditingContext editingContext, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EDTextBlob.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray<EDTextBlob> eoObjects = (NSArray<EDTextBlob>)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EDTextBlob fetchEDTextBlob(EOEditingContext editingContext, String keyName, Object value) {
    return _EDTextBlob.fetchEDTextBlob(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EDTextBlob fetchEDTextBlob(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray<EDTextBlob> eoObjects = _EDTextBlob.fetchEDTextBlobs(editingContext, qualifier, null);
    EDTextBlob eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EDTextBlob)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one EDTextBlob that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EDTextBlob fetchRequiredEDTextBlob(EOEditingContext editingContext, String keyName, Object value) {
    return _EDTextBlob.fetchRequiredEDTextBlob(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EDTextBlob fetchRequiredEDTextBlob(EOEditingContext editingContext, EOQualifier qualifier) {
    EDTextBlob eoObject = _EDTextBlob.fetchEDTextBlob(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no EDTextBlob that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EDTextBlob localInstanceIn(EOEditingContext editingContext, EDTextBlob eo) {
    EDTextBlob localInstance = (eo == null) ? null : (EDTextBlob)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
