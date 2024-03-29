package wk.emailbutler;
// Generated by the WOLips Templateengine Plug-in at Apr 15, 2007 12:30:55 PM

import java.util.Timer;

import wk.emaildata.EDEmail;
import wk.emailer.Mailer;
import wk.emailer.MailerTimerTask;

import com.webobjects.foundation.NSLog;

import er.extensions.appserver.ERXApplication;
import er.extensions.foundation.ERXProperties;

public class Application extends ERXApplication {
	
	// The mail timer for daemon mode
	protected Timer mailTimer;
	
    public static void main(String argv[]) {
        ERXApplication.main(argv, Application.class);
    }

    public Application() {
        NSLog.out.appendln("Welcome to " + this.name() + " !");
        /* ** put your initialization code in here ** */
    }
    
    /**
     * Method invoked when the application has finished launching.
     * Either processes the outgoing mail and then exits or sets up
     * a daemon process to process the outgoing mail at the specified
     * daemon frequency which is specified in the property:
     * <b>er.javamail.mailer.ERBatchMailerDaemonFrequency</b>
     */
    public void didFinishLaunching() {
        if (ERXProperties.booleanForKey("wk.emailer.TestSendingMail"));
            testSendingMail();
        int frequency = ERXProperties.intForKey("wk.emailer.BatchMailerDaemonFrequency");
        if (frequency > 0) {
            log.debug("Scheduling timer for frequency: " + frequency + "(s)");
            mailTimer = new Timer(true);
            mailTimer.schedule(new MailerTimerTask(), frequency*1000l, frequency*1000l);
        } else {
            Mailer.instance().processOutgoingMail();
            log.debug("Done processing mail. Exiting.");
            System.exit(0);            
        }
    }

    public void testSendingMail() {
        log.info("Sending test mail");
        EDEmail.Test.sendTestEmails();
        log.info("Done.");
    }
}