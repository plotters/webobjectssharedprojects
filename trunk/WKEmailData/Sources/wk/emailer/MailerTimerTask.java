//
// ERMailDaemonTask.java
// Project ERMailer
//
// Created by max on Tue Oct 22 2002
//
package wk.emailer;

import java.util.TimerTask;

import org.apache.log4j.Logger;

/**
 * Timer task used when running the ERMailer
 * in daemon mode.
 */
public class MailerTimerTask extends TimerTask {

    /** logging support */
    public final static Logger log = Logger.getLogger(MailerTimerTask.class);

    /**
     * Processes the outgoing mail.
     */
    public void run() {
        if (log.isDebugEnabled()) log.debug("Timer firing to process outgoing mail.");
        Mailer.instance().processOutgoingMail();
    }
}
