//
// ERMailer.java
// Project ERMailer
//
// Created by max on Tue Oct 22 2002
//
package wk.emailer;

import org.apache.log4j.Logger;

import wk.emaildata.EDEmail;
import wk.emaildata.EDTextBlob;

import com.webobjects.eoaccess.EOGeneralAdaptorException;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;

import er.extensions.eof.ERXEC;

/**
 * Mailer bridge class. Used to pull mail out of the
 * EDEmail entity and send it via the ERJavaMail
 * framework for sending mail.
 */
public class Mailer {

    //	===========================================================================
    //	Class Constant(s)
    //	---------------------------------------------------------------------------    

    /** logging support */
    public final static Logger log = Logger.getLogger(Mailer.class);

    //	===========================================================================
    //	Class Variable(s)
    //	---------------------------------------------------------------------------
    
    /** holds a reference to the shared instance */
    protected static Mailer instance;

    protected static Factory factory;

    //	===========================================================================
    //	Class Method(s)
    //	---------------------------------------------------------------------------

    /**
     * Gets the current factory.  If the factory is unset, sets the factory to the default
     * factory.
     *
     * @return the factory
     */
    public static Factory factory() {
        if ( factory == null )
            factory = new DefaultFactory();

        return factory;
    }

    /**
     * Sets the factory.
     *
     * @param value new factory value
     */
    public static void setFactory(Factory value) {
        factory = value;
    }

    /**
     * Instantiates a new mailer instance using the factory and returns it.
     *
     * @return a new mailer instance.
     */
    public static Mailer newMailer() {
        return factory().newMailer();
    }

//    protected static boolean shouldDeleteSentMail() {
//        return ERXProperties.booleanForKeyWithDefault("er.javamail.mailer.ERMailer.ShouldDeleteSentMail", true);
//    }
    
    /**
     * Gets the shared mailer instance.
     * @return mailer singleton
     */
    public static Mailer instance() {
        if ( instance == null )
            instance = newMailer();

        return instance;
    }

    //	===========================================================================
    //	Instance Variable(s)
    //	---------------------------------------------------------------------------

    /** Caches the message title prefix */
    protected String messageTitlePrefix;

    //	===========================================================================
    //	Instance Method(s)
    //	---------------------------------------------------------------------------    
    
    /**
     * Fetches all mail that is ready to
     * be sent from the ERMailMessage table
     * and sends the message using the
     * ERJavaMail framework for sending
     * messages.
     */
    public void processOutgoingMail() {
        if (log.isDebugEnabled())
            log.debug("Starting outgoing mail processing.");
        
        boolean continueSending = true;
        while (continueSending) {
            EOEditingContext ec = ERXEC.newEditingContext();
            ec.lock();
            try {
				NSArray emailsToBeSent = ec.objectsWithFetchSpecification(EDEmail.fsForEmailsToBeSent());
				
				if (emailsToBeSent.count() == 0) {
					log.info("Found zero emails to be sent");
					continueSending = false;
				} else {
					log.info("Sending next " + emailsToBeSent.count() + " emails.");
					// First lock the emails
					emailsToBeSent.takeValueForKey(EDEmail.STATE_PROCESSING, EDEmail.KEY_STATE);
					ec.saveChanges();
					
					// Now send the emails
					for (java.util.Enumeration emailEnumerator = emailsToBeSent
							.objectEnumerator(); emailEnumerator.hasMoreElements();) {
						EDEmail eoEmail = (EDEmail) emailEnumerator.nextElement();
						eoEmail.sendBlocking();
						
						// For now, we delete those successfully sent
						if (eoEmail.state().equals(EDEmail.STATE_SENT)) {
							ec.deleteObject(eoEmail);
						}
					}
					
					// Now save changes
					ec.saveChanges();
					
				}
            } catch (EOGeneralAdaptorException eoGeneralAdaptorException) {
				// This means we are probably running multiple instances
				; // Ignore
				
			} catch (Exception e) {
				log.error("Error processing outgoing email", e);
			} finally {
				ec.unlock();
				ec.dispose();
			}
            
		}
         
        if (log.isDebugEnabled())
            log.debug("Done outgoing mail processing.");
        
        // Next delete the orphan text and html bodies
        EDTextBlob.deleteOrphanBlobs();
    }
    

    //	===========================================================================
    //	Factory-related things
    //	---------------------------------------------------------------------------

    public static interface Factory {
        /**
         * Vends new instances of a mailer.  This is primarily used to set the static instance
         * of ERMailer.
         *
         * @return A new instance of an ERMailer or a subclass.
         */
        public Mailer newMailer();
    }

    /**
     * Default factory.  Just vends back an ERMailer instance.
     */
    public static class DefaultFactory implements Factory {
        public Mailer newMailer() {
            return new Mailer();
        }
    }
}
