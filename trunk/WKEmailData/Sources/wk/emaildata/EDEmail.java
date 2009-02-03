// EDEmail.java
//
// Copyright (c) 2007 Kieran Kelleher & Warren Cohn. All rights reserved.
// Written by Kieran Kelleher
//
// Sat Apr 14 2007 Kieran: Created (eogenerator/JavaSubclassSourceEOF5.eotemplate).
package wk.emaildata;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;

import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOFetchSpecification;
import com.webobjects.eocontrol.EOKeyValueQualifier;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;
import com.webobjects.foundation.NSValidation;

import er.extensions.eof.ERXEC;
import er.extensions.foundation.ERXProperties;
import er.javamail.ERMailDelivery;
import er.javamail.ERMailDeliveryHTML;
import er.javamail.ERMailDeliveryPlainText;

public class EDEmail extends _EDEmail {
	private static Logger log = Logger.getLogger( EDEmail.class );

	public static final Integer STATE_READY_TO_BE_SENT = new Integer(0);
	public static final Integer STATE_PROCESSING = new Integer(1);
	public static final Integer STATE_SENT = new Integer(2);
	public static final Integer STATE_ERROR = new Integer(3);

    public EDEmail() {
        super();
    }

    public void awakeFromInsertion(EOEditingContext editingContext){
    	super.awakeFromInsertion(editingContext);
    	setState(STATE_READY_TO_BE_SENT);
    	setCreated(new NSTimestamp());
    }

    public static class Test {
    	public static void sendTestEmails() {
    		String toEmailAddress = ERXProperties.stringForKey("wk.emailer.testemailaddress");
    		String subject = "EDEmail Test";
    		String plainTextBodyContext = "This is plain text body";
    		String htmlBodyContent = "<html><head></head><body><p>This is HTML body</p></body></html>";

    		EOEditingContext ec = ERXEC.newEditingContext();
    		ec.lock();
    		try {
    			EDTextBlob plainTextBody = (EDTextBlob)EOUtilities.createAndInsertInstance(ec, EDTextBlob.ENTITY_NAME);
    			plainTextBody.setText(plainTextBodyContext);
    			plainTextBody.setType(EDTextBlob.TYPE_PLAIN_TEXT);

    			EDTextBlob htmlTextBody = (EDTextBlob)EOUtilities.createAndInsertInstance(ec, EDTextBlob.ENTITY_NAME);
    			htmlTextBody.setText(htmlBodyContent);
    			htmlTextBody.setType(EDTextBlob.TYPE_HTML_TEXT);

    			// First email
				EDEmail email = (EDEmail) EOUtilities.createAndInsertInstance(ec, EDEmail.ENTITY_NAME);
				email.setToEmailAddress(toEmailAddress);
				email.setFromEmailAddress(toEmailAddress);
				email.setFromPersonalName("From Kieran");
				email.setToPersonalName("Kieran To");
				email.setSubject(subject + " (Plain Text)");
				email.setPlainTextBodyRelationship(plainTextBody);


				// Second email
				email = (EDEmail) EOUtilities.createAndInsertInstance(ec, EDEmail.ENTITY_NAME);
				email.setToEmailAddress(toEmailAddress);
				email.setFromEmailAddress(toEmailAddress);
				email.setFromPersonalName("From Kieran");
				email.setToPersonalName("Kieran To");
				email.setSubject(subject + " (HTML with alt Plain Text)");
				email.setPlainTextBodyRelationship(plainTextBody);
				email.setHtmlContentBodyRelationship(htmlTextBody);

				ec.saveChanges();


			} catch (Exception e) {
				log.error("Error creating test emails", e);
				throw new RuntimeException("Error creating test emails", e);
			} finally {
				ec.unlock();
			}
    	}
    }

    protected static EOFetchSpecification _fsForEmailsToBeSent;

    public static EOFetchSpecification fsForEmailsToBeSent() {
    	if (_fsForEmailsToBeSent == null) {
    		// First build the qualifier
    		EOQualifier qualifierForEmailsToBeSent = new EOKeyValueQualifier(EDEmail.KEY_STATE,EOQualifier.QualifierOperatorEqual,STATE_READY_TO_BE_SENT);

    		// Let's apply sort orderings so that we hit them all in the same order
    		EOSortOrdering sortOrdering = new EOSortOrdering(EDEmail.KEY_OID,EOSortOrdering.CompareAscending);

			_fsForEmailsToBeSent = new EOFetchSpecification(ENTITY_NAME,qualifierForEmailsToBeSent,new NSArray(sortOrdering));

			// And I only want to send in batches and allow other instances to send without tripping over ourselves
			_fsForEmailsToBeSent.setPromptsAfterFetchLimit(false);
			_fsForEmailsToBeSent.setRefreshesRefetchedObjects(true);
			_fsForEmailsToBeSent.setFetchLimit(100);
		}
		return _fsForEmailsToBeSent;
    }

    /**
     * Sends this email eo using ERJavaMail and sets state to sent if successful, otherwise sets to error.
     * Not sent in multi-threaded fashion since we want to capture errors
     */
    public void sendBlocking() {

    	try {
			ERMailDelivery email = createMailDeliveryForMailMessage();
			if (email != null) {
				email.sendMail(true);
				setState(STATE_SENT);
			} else {
				log.error("Unable to create mail delivery for mail message: " + this);
				setState(STATE_ERROR);
			}

		} catch (Exception e) {
			log.error("Error sending email " + this, e);
			setState(STATE_ERROR);
		}
    }

    /**
     * Creates a ERMailDelivery for a given
     * MailMessage.
     * @param message mail message
     * @return a mail delevery object
     */
    // ENHANCEME: Not handling double byte (Japanese) language or file attachments.
    public ERMailDelivery createMailDeliveryForMailMessage() throws MessagingException {
        ERMailDelivery mail = null;
        if (htmlContentBody() != null && htmlContentBody().text() != null) {
            mail = ERMailDeliveryHTML.newMailDelivery();
            mail.setCharset("UTF-8");
            ((ERMailDeliveryHTML)mail).setHTMLContent(htmlContentBody().text());

            if (plainTextBody() != null && plainTextBody().text() != null)
                ((ERMailDeliveryHTML)mail).setHiddenPlainTextContent(plainTextBody().text());
        } else {
            mail = new ERMailDeliveryPlainText();
            mail.setCharset("UTF-8");
            ((ERMailDeliveryPlainText)mail).setTextContent(plainTextBody().text());
        }

        // From
        if (fromPersonalName() == null) {
        	mail.setFromAddress(fromEmailAddress());
		} else {
			mail.setFromAddress(fromEmailAddress(), fromPersonalName());
		}

        // To
        if (toPersonalName() == null) {
        	mail.setToAddress(toEmailAddress());
		} else {
			mail.setToAddress(toEmailAddress(), toPersonalName());
		}



        // Set the subject
        mail.setSubject(subject());

        return mail;
    }

    public String validateToEmailAddress(Object aValue) throws NSValidation.ValidationException {
    	if (log.isDebugEnabled())
			log.debug("Incoming aValue = "
					+ (aValue == null ? "null" : aValue.toString()));

    	if (aValue != null && aValue.toString().length() > 100) {
			aValue = aValue.toString().substring(0, 100);
			if (log.isDebugEnabled())
				log.debug("Return shortened aValue = "
						+ (aValue == null ? "null" : aValue.toString()));

		}

    	if (log.isDebugEnabled())
			log.debug("Length is OK");
    	return (String)aValue;
    }

    public String toString() {
    	return toLongString();
    }




/*
	public static EDEmail createInstance( EOEditingContext ec ) {
		return (EDEmail)EOUtilities.createAndInsertInstance( ec, ENTITY_NAME );
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