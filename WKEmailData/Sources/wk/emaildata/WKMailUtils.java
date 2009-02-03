package wk.emaildata;
//
//  WkMailUtils.java
//  cheetah
//
//  Created by Kieran Kelleher on 1/28/05.
//  Copyright (c) 2005 Kieran Kelleher. All rights reserved.
//


import com.webobjects.appserver.WOComponent;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;

import er.extensions.foundation.ERXSystem;
import er.javamail.ERMailDelivery;
import er.javamail.ERMailDeliveryHTML;
import er.javamail.ERMailDeliveryPlainText;

/** This class needs to be made thread-safe */
public class WKMailUtils {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger( WKMailUtils.class );


    public static void composeAndSendComponentMail( WOComponent emailPage,
                                                    NSDictionary emailHeaders ) {
        // Requires no null values

        composeAndSendComponentMail(emailPage,
                                    (String)emailHeaders.valueForKey("fromAddress"),
                                    (String)emailHeaders.valueForKey("fromPersonalName"),
                                    (String)emailHeaders.valueForKey("toAddress"),
                                    (String)emailHeaders.valueForKey("toPersonalName"),
                                    (NSArray)emailHeaders.valueForKey("toAddresses"),
                                    (String)emailHeaders.valueForKey("toPersonalName"),
                                    (String)emailHeaders.valueForKey("toPersonalName") );
    }



    public static void composeAndSendComponentMail( WOComponent emailPage,
                                                    String fromAddress,
                                                    String fromPersonalName,
                                                    String toAddress,
                                                    String toPersonalName,
                                                    NSArray toAddresses,
                                                    String replyToAddress,
                                                    String subject) {

        if (log.isDebugEnabled()) log.debug("Sending email with subject '"
                                            + subject + "' and addressed to '"
                                            + (toAddress == null ? "" : toAddress )
                                            + (toAddresses == null ? "" : toAddresses.toString() ) );

        // Create a new mail delivery instance to allow for multiple threads
        ERMailDeliveryHTML eMail = new ERMailDeliveryHTML();

        // Set the WOComponent to be used for rendering the mail
        eMail.setComponent( emailPage );

        String bccEmailAddress = ERXSystem.getProperty( "bccEmailAddress" );

        try {
            eMail.newMail();

            // fromAddress with optional fromPersonalName
            if ( fromAddress != null && fromPersonalName != null ) {
                eMail.setFromAddress( fromAddress, fromPersonalName );
            } else if (fromAddress != null) {
                eMail.setFromAddress( fromAddress );
            }

            // optional toAddress and optional toPersonalName
            if ( toAddress != null && toPersonalName != null ) {
                eMail.setToAddress( toAddress, toPersonalName );
            } else if (toAddress != null) {
                eMail.setToAddress( toAddress );
            }

            // optional toAddresses (NSArray)
            if ( toAddresses != null ) eMail.setToAddresses( toAddresses );

            // reply to address
            if ( replyToAddress != null ) eMail.setReplyToAddress( replyToAddress );

            eMail.setSubject( subject );

            // Admin secret bcc email
            if ( bccEmailAddress != null ) {
                NSArray bccAddresses = new NSArray( bccEmailAddress );
                if (log.isDebugEnabled()) log.debug( "BccAddresses: " + bccAddresses.toString());

                eMail.setBCCAddresses( bccAddresses );
            }

            eMail.sendMail();

        } catch (Exception e) {
            log.error("Exception sending email.", e);
        }

        // Now that we are done with it
        eMail = null;
    }

    public static void sendHTMLMail( WOComponent htmlPage,
                                     WOComponent alternateTextPage,
                                     NSDictionary emailHeaders ) {

        ERMailDeliveryHTML delivery = ERMailDeliveryHTML.newMailDelivery();

        sendHTMLMail( delivery,
                      htmlPage,
                      alternateTextPage,
                      emailHeaders );

    }

    /** ENHANCEME: Examine the HTML META tags for the character encoding
        and set it. */
    public static void composeAndSendMail( String htmlContent,
                                           String plainTextContent,
                                           NSDictionary emailHeaders ) {

        ERMailDelivery mail = null;

        try {
            if ( htmlContent != null ) {
                mail = ERMailDeliveryHTML.newMailDelivery();

                ((ERMailDeliveryHTML)mail).setHTMLContent( htmlContent );

                if (plainTextContent != null) {
                    ((ERMailDeliveryHTML)mail).setHiddenPlainTextContent( plainTextContent );
                }

            } else {
                mail = new ERMailDeliveryPlainText();
                ((ERMailDeliveryPlainText)mail).setTextContent( plainTextContent );
            }

            appendHeadersToMailDelivery( mail, emailHeaders );

            mail.sendMail();
        } catch ( Exception e ) {
            log.error("Error sending mail.", e);
        }

    }

    protected static void appendHeadersToMailDelivery( ERMailDelivery mail,
                                                       NSDictionary emailHeaders )
        throws javax.mail.MessagingException {


            if ( emailHeaders.valueForKey("fromAddress") != null ) {
                mail.setFromAddress( (String)emailHeaders.valueForKey("fromAddress") );
            }

            if ( emailHeaders.valueForKey("toAddress") != null ) {
                mail.setToAddress( (String)emailHeaders.valueForKey("toAddress") );
            }

            if ( emailHeaders.valueForKey("subject") != null ) {
                mail.setSubject( (String)emailHeaders.valueForKey("subject") );
            }

            if ( emailHeaders.valueForKey("replyToAddress") != null ) {
                mail.setReplyToAddress( (String)emailHeaders.valueForKey("replyToAddress") );
            } else {
                // Alternateively use the fromAddress as the replyToAddress
                if ( emailHeaders.valueForKey("fromAddress") != null ) {
                    mail.setReplyToAddress( (String)emailHeaders.valueForKey("fromAddress") );
                }
            }

        }

    public static void sendHTMLMail( ERMailDeliveryHTML eMail,
                                     WOComponent htmlPage,
                                     WOComponent alternateTextPage,
                                     NSDictionary emailHeaders ) {

        if ( log.isDebugEnabled() ) log.debug("System mail.smtp.host = " + ERXSystem.getProperty("mail.smtp.host") );
        if ( log.isDebugEnabled() ) log.debug("System er.javamail.smtpHost = " + ERXSystem.getProperty("er.javamail.smtpHost") );
        if ( log.isDebugEnabled() ) log.debug("System WOSMTPHost = " + ERXSystem.getProperty("WOSMTPHost") );

        eMail.setComponent( htmlPage );

        // String bccEmailAddress = ERXSystem.getProperty( "bccEmailAddress" );
        String bccEmailAddress = null;

        try {
            eMail.newMail();

            if (alternateTextPage != null) {

                String alternateString =
                alternateTextPage.generateResponse().contentString();



                if ( log.isDebugEnabled() ) log.debug("Alternate Text:\r\n" + alternateString);

                if (alternateString != null) {
                    eMail.setHiddenPlainTextContent(alternateString);
                    // alternateMailTemplate.session().terminate();
                }
            }

            // FROM
            String fromAddress = (String)emailHeaders.objectForKey( "fromAddress" );
            String fromPersonalName = (String)emailHeaders.objectForKey( "fromPersonalName" );

            // fromAddress with optional fromPersonalName
            if ( fromAddress != null && fromPersonalName != null ) {
                eMail.setFromAddress( fromAddress, fromPersonalName );
            } else if (fromAddress != null) {
                eMail.setFromAddress( fromAddress );
            }

            String toAddress = (String)emailHeaders.objectForKey( "toAddress" );
            String toPersonalName = (String)emailHeaders.objectForKey( "toPersonalName" );

            // TO
            // optional toAddress and optional toPersonalName
            if ( toAddress != null && toPersonalName != null ) {
                eMail.setToAddress( toAddress, toPersonalName );
            } else if (toAddress != null) {
                eMail.setToAddress( toAddress );
            }

            // REPLY TO
            String replyToAddress = (String)emailHeaders.objectForKey( "replyToAddress" );

            // reply to address
            if ( replyToAddress != null ) eMail.setReplyToAddress( replyToAddress );


            // SUBJECT
            String subject = (String)emailHeaders.objectForKey( "subject" );
            eMail.setSubject( ( subject == null ? " " : subject ) );

            // Admin secret bcc email
            // Currently class ERMailDelivery has a bug where bccAddress replace ALL addresses up to now!
            if ( bccEmailAddress != null ) {
                NSArray bccAddresses = new NSArray( bccEmailAddress );
                if (log.isDebugEnabled()) log.debug( "BccAddresses: " + bccAddresses.toString());

                eMail.setBCCAddresses( bccAddresses );
            }

            eMail.sendMail();

        } catch (Exception e) {
            log.error("Exception sending email.", e);
        }

    }


    /** Wrapper convenience method to overcome some of the short-comings of
        the ERCMailDelivery entity code. */
    public static EDEmail composeEDEmailMessage( EDTextBlob htmlContent,
                                                        EDTextBlob plainTextContent,
                                                        NSDictionary emailHeaders,
                                                        EOEditingContext ec ) {

        EDEmail message = (EDEmail)EOUtilities.createAndInsertInstance( ec, EDEmail.ENTITY_NAME );

        if ( htmlContent != null ) {
            message.setHtmlContentBodyRelationship( htmlContent );
        }

        if ( plainTextContent != null ) {
            message.setPlainTextBodyRelationship( plainTextContent );
        }

        if ( emailHeaders.valueForKey("fromAddress") != null ) {
            message.setFromEmailAddress( (String)emailHeaders.valueForKey("fromAddress") );
        }

        if ( emailHeaders.valueForKey("toAddress") != null ) {
            message.setToEmailAddress( (String)emailHeaders.valueForKey("toAddress") );
        }

        if ( emailHeaders.valueForKey("subject") != null ) {
            message.setSubject( (String)emailHeaders.valueForKey("subject") );
        }

        return message;

    }


}
