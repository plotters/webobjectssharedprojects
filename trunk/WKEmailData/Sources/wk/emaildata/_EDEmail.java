// $LastChangedRevision: 4733 $ DO NOT EDIT.  Make changes to EDEmail.java instead.
package wk.emaildata;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;
import org.apache.log4j.Logger;

@SuppressWarnings("all")
public abstract class _EDEmail extends er.extensions.eof.ERXGenericRecord {
    public static final String ENTITY_NAME = "EDEmail";

    public static final String ENTITY_TABLE_NAME = "Email";

    // Attributes
    public static final String KEY_CREATED = "created";
    public static final String KEY_FROM_EMAIL_ADDRESS = "fromEmailAddress";
    public static final String KEY_FROM_PERSONAL_NAME = "fromPersonalName";
    public static final String KEY_OID = "oid";
    public static final String KEY_OID_HTML_CONTENT_BODY = "oidHtmlContentBody";
    public static final String KEY_OID_PLAIN_TEXT_BODY = "oidPlainTextBody";
    public static final String KEY_STATE = "state";
    public static final String KEY_SUBJECT = "subject";
    public static final String KEY_TO_EMAIL_ADDRESS = "toEmailAddress";
    public static final String KEY_TO_PERSONAL_NAME = "toPersonalName";

    // External Column Names capitalized (since EOF always returns raw row keys capitalized)
    public static final String COLKEY_CREATED = "created".toUpperCase();
    public static final String COLKEY_FROM_EMAIL_ADDRESS = "fromEmailAddress".toUpperCase();
    public static final String COLKEY_FROM_PERSONAL_NAME = "fromPersonalName".toUpperCase();
    public static final String COLKEY_OID = "oid".toUpperCase();
    public static final String COLKEY_OID_HTML_CONTENT_BODY = "oidHtmlContentBody".toUpperCase();
    public static final String COLKEY_OID_PLAIN_TEXT_BODY = "oidPlainTextBody".toUpperCase();
    public static final String COLKEY_STATE = "state".toUpperCase();
    public static final String COLKEY_SUBJECT = "subject".toUpperCase();
    public static final String COLKEY_TO_EMAIL_ADDRESS = "toEmailAddress".toUpperCase();
    public static final String COLKEY_TO_PERSONAL_NAME = "toPersonalName".toUpperCase();

    // Relationships
    public static final String KEY_HTML_CONTENT_BODY = "htmlContentBody";
    public static final String KEY_PLAIN_TEXT_BODY = "plainTextBody";

  private static Logger LOG = Logger.getLogger(_EDEmail.class);

  public EDEmail localInstanceIn(EOEditingContext editingContext) {
    EDEmail localInstance = (EDEmail)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }

  public NSTimestamp created() {
    return (NSTimestamp) storedValueForKey("created");
  }

  public void setCreated(NSTimestamp value) {
    if (_EDEmail.LOG.isDebugEnabled()) {
        _EDEmail.LOG.debug( "updating created from " + created() + " to " + value);
    }
    takeStoredValueForKey(value, "created");
  }

  public String fromEmailAddress() {
    return (String) storedValueForKey("fromEmailAddress");
  }

  public void setFromEmailAddress(String value) {
    if (_EDEmail.LOG.isDebugEnabled()) {
        _EDEmail.LOG.debug( "updating fromEmailAddress from " + fromEmailAddress() + " to " + value);
    }
    takeStoredValueForKey(value, "fromEmailAddress");
  }

  public String fromPersonalName() {
    return (String) storedValueForKey("fromPersonalName");
  }

  public void setFromPersonalName(String value) {
    if (_EDEmail.LOG.isDebugEnabled()) {
        _EDEmail.LOG.debug( "updating fromPersonalName from " + fromPersonalName() + " to " + value);
    }
    takeStoredValueForKey(value, "fromPersonalName");
  }

  public Integer oidHtmlContentBody() {
    return (Integer) storedValueForKey("oidHtmlContentBody");
  }

  public void setOidHtmlContentBody(Integer value) {
    if (_EDEmail.LOG.isDebugEnabled()) {
        _EDEmail.LOG.debug( "updating oidHtmlContentBody from " + oidHtmlContentBody() + " to " + value);
    }
    takeStoredValueForKey(value, "oidHtmlContentBody");
  }

  public Integer oidPlainTextBody() {
    return (Integer) storedValueForKey("oidPlainTextBody");
  }

  public void setOidPlainTextBody(Integer value) {
    if (_EDEmail.LOG.isDebugEnabled()) {
        _EDEmail.LOG.debug( "updating oidPlainTextBody from " + oidPlainTextBody() + " to " + value);
    }
    takeStoredValueForKey(value, "oidPlainTextBody");
  }

  public Integer state() {
    return (Integer) storedValueForKey("state");
  }

  public void setState(Integer value) {
    if (_EDEmail.LOG.isDebugEnabled()) {
        _EDEmail.LOG.debug( "updating state from " + state() + " to " + value);
    }
    takeStoredValueForKey(value, "state");
  }

  public String subject() {
    return (String) storedValueForKey("subject");
  }

  public void setSubject(String value) {
    if (_EDEmail.LOG.isDebugEnabled()) {
        _EDEmail.LOG.debug( "updating subject from " + subject() + " to " + value);
    }
    takeStoredValueForKey(value, "subject");
  }

  public String toEmailAddress() {
    return (String) storedValueForKey("toEmailAddress");
  }

  public void setToEmailAddress(String value) {
    if (_EDEmail.LOG.isDebugEnabled()) {
        _EDEmail.LOG.debug( "updating toEmailAddress from " + toEmailAddress() + " to " + value);
    }
    takeStoredValueForKey(value, "toEmailAddress");
  }

  public String toPersonalName() {
    return (String) storedValueForKey("toPersonalName");
  }

  public void setToPersonalName(String value) {
    if (_EDEmail.LOG.isDebugEnabled()) {
        _EDEmail.LOG.debug( "updating toPersonalName from " + toPersonalName() + " to " + value);
    }
    takeStoredValueForKey(value, "toPersonalName");
  }

  public wk.emaildata.EDTextBlob htmlContentBody() {
    return (wk.emaildata.EDTextBlob)storedValueForKey("htmlContentBody");
  }

  public void setHtmlContentBodyRelationship(wk.emaildata.EDTextBlob value) {
    if (_EDEmail.LOG.isDebugEnabled()) {
      _EDEmail.LOG.debug("updating htmlContentBody from " + htmlContentBody() + " to " + value);
    }
    if (value == null) {
        wk.emaildata.EDTextBlob oldValue = htmlContentBody();
        if (oldValue != null) {
            removeObjectFromBothSidesOfRelationshipWithKey(oldValue, "htmlContentBody");
      }
    } else {
        addObjectToBothSidesOfRelationshipWithKey(value, "htmlContentBody");
    }
  }

  public wk.emaildata.EDTextBlob plainTextBody() {
    return (wk.emaildata.EDTextBlob)storedValueForKey("plainTextBody");
  }

  public void setPlainTextBodyRelationship(wk.emaildata.EDTextBlob value) {
    if (_EDEmail.LOG.isDebugEnabled()) {
      _EDEmail.LOG.debug("updating plainTextBody from " + plainTextBody() + " to " + value);
    }
    if (value == null) {
        wk.emaildata.EDTextBlob oldValue = plainTextBody();
        if (oldValue != null) {
            removeObjectFromBothSidesOfRelationshipWithKey(oldValue, "plainTextBody");
      }
    } else {
        addObjectToBothSidesOfRelationshipWithKey(value, "plainTextBody");
    }
  }


  public static EDEmail createEDEmail(EOEditingContext editingContext, String fromEmailAddress
, Integer state
, String subject
, String toEmailAddress
) {
    EDEmail eo = (EDEmail) EOUtilities.createAndInsertInstance(editingContext, _EDEmail.ENTITY_NAME);
        eo.setFromEmailAddress(fromEmailAddress);
        eo.setState(state);
        eo.setSubject(subject);
        eo.setToEmailAddress(toEmailAddress);
    return eo;
  }

  public static NSArray<EDEmail> fetchAllEDEmails(EOEditingContext editingContext) {
    return _EDEmail.fetchAllEDEmails(editingContext, null);
  }

  public static NSArray<EDEmail> fetchAllEDEmails(EOEditingContext editingContext, NSArray<EOSortOrdering> sortOrderings) {
    return _EDEmail.fetchEDEmails(editingContext, null, sortOrderings);
  }

  public static NSArray<EDEmail> fetchEDEmails(EOEditingContext editingContext, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    EOFetchSpecification fetchSpec = new EOFetchSpecification(_EDEmail.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray<EDEmail> eoObjects = (NSArray<EDEmail>)editingContext.objectsWithFetchSpecification(fetchSpec);
    return eoObjects;
  }

  public static EDEmail fetchEDEmail(EOEditingContext editingContext, String keyName, Object value) {
    return _EDEmail.fetchEDEmail(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EDEmail fetchEDEmail(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray<EDEmail> eoObjects = _EDEmail.fetchEDEmails(editingContext, qualifier, null);
    EDEmail eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = (EDEmail)eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one EDEmail that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EDEmail fetchRequiredEDEmail(EOEditingContext editingContext, String keyName, Object value) {
    return _EDEmail.fetchRequiredEDEmail(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static EDEmail fetchRequiredEDEmail(EOEditingContext editingContext, EOQualifier qualifier) {
    EDEmail eoObject = _EDEmail.fetchEDEmail(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no EDEmail that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static EDEmail localInstanceIn(EOEditingContext editingContext, EDEmail eo) {
    EDEmail localInstance = (eo == null) ? null : (EDEmail)EOUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
