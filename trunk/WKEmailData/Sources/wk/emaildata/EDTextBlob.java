// EDTextBlob.java
//
// Copyright (c) 2007 Kieran Kelleher & Warren Cohn. All rights reserved.
// Written by Kieran Kelleher
//
// Sat Apr 14 2007 Kieran: Created (eogenerator/JavaSubclassSourceEOF5.eotemplate).
package wk.emaildata;

import org.apache.log4j.Logger;

import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSTimestamp;

import er.extensions.eof.ERXEC;

public class EDTextBlob extends _EDTextBlob {
    private static Logger log = Logger.getLogger( EDTextBlob.class );

    public static Integer TYPE_PLAIN_TEXT = new Integer(0);
    public static Integer TYPE_HTML_TEXT = new Integer(1);

    public EDTextBlob() {
        super();
    }

    /* (non-Javadoc)
    * @see er.extensions.ERXGenericRecord#awakeFromInsertion(com.webobjects.eocontrol.EOEditingContext)
    */
    public void awakeFromInsertion(EOEditingContext editingContext) {

        super.awakeFromInsertion(editingContext);

        setLockingProperty(new Integer(0));
        setType(TYPE_PLAIN_TEXT);
        setLastUsed(new NSTimestamp());
    }

    /**
    * Uses custom SQL to select orphan blob rows and deletes them
    */
    public static void deleteOrphanBlobs() {
        // TODO : Consider multiple instances and optimistic locking exceptions for deletes
        String modelName = EOModelGroup.defaultGroup().entityNamed(ENTITY_NAME).model().name();
        if (log.isDebugEnabled())
            log.debug("modelName = "
                    + (modelName == null ? "null" : modelName.toString()));

        // Selects orphans not used in 10 days, MySQL 4.1.1 or greater required for the SUBTIME function
        // We use SQL here since we needed a LEFT JOIN which cannot be done in EOF
        String sqlString = "select t0.oid, t0.type, t0.lastUsed, t1.oidPlainTextBody, t2.oidHtmlContentBody from TextBlob as t0 left join Email as t1 on t0.oid = t1.oidPlainTextBody left join Email as t2 on t0.oid=t2.oidHtmlContentBody where t1.oidPlainTextBody is null and t2.oidHtmlContentBody is null and lastUsed < SUBTIME(NOW(),'10 0:0:0.0');";

        NSArray keys = NSArray.componentsSeparatedByString("oid,type,oidPlainTextBody,oidHtmlContentBody", ",");
        EOEditingContext ec = ERXEC.newEditingContext();
        ec.lock();
        try {
            // FIXME: We need to timestamp these and *ONLY* delete orphans that are more than X days old
            // since on 9/10/2007, we had a case where the orphans were deleted before the next batch of 500 emails could be saved
            // by cheetah
            // We have to use plain SQL for this since too much mucking around otherwise
            NSArray orphanRawRows = EOUtilities.rawRowsForSQL(ec, modelName, sqlString, keys);
            if (orphanRawRows.count() > 0) {
                if (log.isInfoEnabled())
                    log.info("We have " + orphanRawRows.count() + " body blobs to delete.");

                for (java.util.Enumeration enumerator = orphanRawRows
                        .objectEnumerator(); enumerator.hasMoreElements();) {
                    NSDictionary rawRow = (NSDictionary) enumerator.nextElement();

                    // Inflate it to delete it
                    EOEnterpriseObject eo = EOUtilities.objectFromRawRow(ec, ENTITY_NAME, rawRow);
                    if (log.isDebugEnabled())
                        log.debug("eo to delete = "
                                + (eo == null ? "null" : eo
                                        .toString()));

                    ec.deleteObject(eo);
                    ec.saveChanges();
                }
            } else {
                log.debug("We have " + orphanRawRows.count() + " body blobs to delete.");
            }
        } catch (Exception e) {
            log.error("Exception deleting orphan blobs", e);
        } finally {
            ec.unlock();
        }


    }

//    protected static EOFetchSpecification _orphanBlobsFetchSpecification;
//
//    /**
//     * @return the fetch spec for orphan blobs
//     */
//    public static EOFetchSpecification orphanBlobsFetchSpecification() {
//
//    }

    // The To Many Relationships have the following utility methods for each one:
    //        A getter
    //        A addTo (adds one item to the relationship)
    //        A removeFrom (removes one item)
    //        A removeAllFrom (removes all items)
    //        A create (creates a new EO and adds it to the relationship) - I don't like this one so I comment it out of template
    //        A delete (removes a single EO from the relationship and DELETES the EO)
    //        A deleteAll (removes and DELETES all EO's in the relationship)
    // *****************************************************************************


//       /** @return  */
//    public NSArray emailsWithHtmlBody() {
//        EOQualifier emailsWithHtmlBodyQualifier = new EOKeyValueQualifier(EDEmail.KEY_OID_HTML_CONTENT_BODY,EOQualifier.QualifierOperatorEqual,rawPrimaryKey());
//        EOFetchSpecification fs = new EOFetchSpecification(EDEmail.ENTITY_NAME,emailsWithHtmlBodyQualifier,null);
//        int count
//        return editingContext().objectsWithFetchSpecification(fs);
//    }



//    /** Sets  */
//    public void setEmailsWithHtmlBody(NSMutableArray aValue) {
//        takeStoredValueForKey(aValue, "emailsWithHtmlBody");
//    }
//    /** Adds to  */
//    public void addToEmailsWithHtmlBody(wk.emaildata.EDEmail object) {
//        NSMutableArray array = (NSMutableArray)emailsWithHtmlBody();
//        willChange();
//        array.addObject(object);
//    }
//    /** Removes from  */
//    public void removeFromEmailsWithHtmlBody(wk.emaildata.EDEmail object) {
//        NSMutableArray array = (NSMutableArray)emailsWithHtmlBody();
//        willChange();
//        array.removeObject(object);
//    }
//    /** Adds to  */
//    public void addToEmailsWithHtmlBodyRelationship(wk.emaildata.EDEmail object) {
//        addObjectToBothSidesOfRelationshipWithKey(object, "emailsWithHtmlBody");
//    }
//    /** Removes from  */
//    public void removeFromEmailsWithHtmlBodyRelationship(wk.emaildata.EDEmail object) {
//        removeObjectFromBothSidesOfRelationshipWithKey(object, "emailsWithHtmlBody");
//    }
//
//    /** Deletes  */
//    public void deleteEmailsWithHtmlBodyRelationship(wk.emaildata.EDEmail object) {
//        removeObjectFromBothSidesOfRelationshipWithKey(object, "emailsWithHtmlBody");
//        editingContext().deleteObject(object);
//    }
//    public void deleteAllEmailsWithHtmlBodyRelationships() {
//    Enumeration objects = emailsWithHtmlBody().immutableClone().objectEnumerator();
//    while ( objects.hasMoreElements() )
//        deleteEmailsWithHtmlBodyRelationship((wk.emaildata.EDEmail)objects.nextElement());
//    }

    /** @return  */
    public NSArray emailsWithPlainTextBody() {
        return (NSArray)storedValueForKey("emailsWithPlainTextBody");
    }
//    /** Sets  */
//    public void setEmailsWithPlainTextBody(NSMutableArray aValue) {
//        takeStoredValueForKey(aValue, "emailsWithPlainTextBody");
//    }
//    /** Adds to  */
//    public void addToEmailsWithPlainTextBody(wk.emaildata.EDEmail object) {
//        NSMutableArray array = (NSMutableArray)emailsWithPlainTextBody();
//        willChange();
//        array.addObject(object);
//    }
//    /** Removes from  */
//    public void removeFromEmailsWithPlainTextBody(wk.emaildata.EDEmail object) {
//        NSMutableArray array = (NSMutableArray)emailsWithPlainTextBody();
//        willChange();
//        array.removeObject(object);
//    }
//    /** Adds to  */
//    public void addToEmailsWithPlainTextBodyRelationship(wk.emaildata.EDEmail object) {
//        addObjectToBothSidesOfRelationshipWithKey(object, "emailsWithPlainTextBody");
//    }
//    /** Removes from  */
//    public void removeFromEmailsWithPlainTextBodyRelationship(wk.emaildata.EDEmail object) {
//        removeObjectFromBothSidesOfRelationshipWithKey(object, "emailsWithPlainTextBody");
//    }
//
//    /** Deletes  */
//    public void deleteEmailsWithPlainTextBodyRelationship(wk.emaildata.EDEmail object) {
//        removeObjectFromBothSidesOfRelationshipWithKey(object, "emailsWithPlainTextBody");
//        editingContext().deleteObject(object);
//    }
//    public void deleteAllEmailsWithPlainTextBodyRelationships() {
//    Enumeration objects = emailsWithPlainTextBody().immutableClone().objectEnumerator();
//    while ( objects.hasMoreElements() )
//        deleteEmailsWithPlainTextBodyRelationship((wk.emaildata.EDEmail)objects.nextElement());
//    }





/*
    public static EDTextBlob createInstance( EOEditingContext ec ) {
        return (EDTextBlob)EOUtilities.createAndInsertInstance( ec, ENTITY_NAME );
    }

    // If you add instance variables to store property values you
    // should add empty implementions of the Serialization methods
    // to avoid unnecessary overhead (the properties will be
    // serialized for you in the superclass).
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    }

    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
    }
*/

}