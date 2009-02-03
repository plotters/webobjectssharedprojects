//
//  MailingAddress.java
//  cheetah
//
//  Created by Kieran Kelleher on 1/6/06.
//  Copyright 2006 Kieran Kelleher. All rights reserved.
//

package wk.foundation;

import org.apache.commons.lang.StringUtils;

import com.webobjects.foundation.NSTimestamp;

/**
 * Defines the interface for a mailing address. Since we deal with massive
 * amounts of mailing list data containing address data and since we also have
 * address objects in the model design, I created this interface to alleviate
 * having millions of list records with related addresses.
 */
public interface MailingAddress {
    public static final String CERTIFICATION_DATE_TIME = "certificationDateTime";
    public static final String CITY = "city";
    public static final String DO_NOT_CERTIFY = "doNotCertify";
    public static final String DPV_FAIL_STATUS = "dpvFailStatus";
    public static final String ERROR_CODE = "errorCode";
    public static final String IS_AUTOMATION_MAILABLE = "isAutomationMailable";
    public static final String STATE = "state";
    public static final String STREET1 = "street1";
    public static final String STREET2 = "street2";
    public static final String ZIP = "zip";
    public static final int CERTIFICATION_VALID_DAYS = Integer.parseInt(System.getProperty("certificationValidForDays",
            "80"));

    public String street1();

    public void setStreet1(String street1);

    public String street2();

    public void setStreet2(String street2);

    public String city();

    public void setCity(String city);

    public String state();

    public void setState(String state);

    public String zip();

    public void setZip(String zip);

    /**
     * Essentially this is derived from the errorCode, however setting it as an
     * attribute is important for efficiency of selects against large data sets.
     * This is calculated and automatically set every time an address is
     * processed thru address hygiene service
     */
    public Boolean isAutomationMailable();

    public void setIsAutomationMailable(Boolean isAutomationMailable);

    /** CASS/DPV error code. */
    public Integer errorCode();

    public void setErrorCode(Integer errorCode);

    /** USPS Cass Date number */
    public Integer cassDate();

    public void setCassDate(Integer cassDate);

    /** Our system CASS Date and Time */
    public NSTimestamp certificationDateTime();

    public void setCertificationDateTime(NSTimestamp certificationDateTime);

    /**
     * @return whether the MailingAddress should be certified. TRUE means DO NOT
     *         CERTIFY this address, just leave it as entered.
     */
    public Boolean doNotCertify();

    public void setDoNotCertify(Boolean doNotCertify);

    /**
     * @return whether or not this MailingAddress requires certification taking
     *         into account current certification date and automation mailable
     *         values
     * @see MailingAddressServices#processAddress()
     * @see MailingAddressUtilities#shouldCertify()
     */
    public boolean requiresCertification();

    public static class Utilities {
        /**
         * @param address
         * @return true if the certification date has expired or is null better
         *         to use MailingAddressUtilities.shouldCertify if using that
         *         framework
         */
        public static boolean requiresCertification(MailingAddress address) {
            if (address.certificationDateTime() == null)
                return true;

            NSTimestamp validAfter = WKTimestampUtilities.timestampByAddingDays(new NSTimestamp(),
                    -CERTIFICATION_VALID_DAYS);
            return address.certificationDateTime().before(validAfter);
        }

        public static void copyAttributesFromAddressToAddress(MailingAddress fromAddress, MailingAddress toAddress) {
            toAddress.setStreet1(fromAddress.street1());
            toAddress.setStreet2(fromAddress.street2());
            toAddress.setCity(fromAddress.city());
            toAddress.setState(fromAddress.state());
            toAddress.setZip(fromAddress.zip());

            toAddress.setCassDate(fromAddress.cassDate());
            toAddress.setCertificationDateTime(fromAddress.certificationDateTime());
            toAddress.setDoNotCertify(fromAddress.doNotCertify());
            toAddress.setErrorCode(fromAddress.errorCode());
            toAddress.setIsAutomationMailable(fromAddress.isAutomationMailable());
        }

        public static String toString(MailingAddress address) {
            return address.street1() + ", " + address.city() + ", " + address.state() + " " + address.zip();
        }

        public static boolean hasAtLeastOneNonEmptyAddressField(MailingAddress address) {

            return ( StringUtils.isNotBlank( address.street1() )
                     || StringUtils.isNotBlank( address.street2() )
                     || StringUtils.isNotBlank( address.city() )
                     || StringUtils.isNotBlank( address.zip() ) ? true : false );
        }

        /**
         * @param address1
         * @param address2
         * @return true if street1, street2, city and state are the same
         */
        public static boolean haveSameAddressFields(MailingAddress address1, MailingAddress address2) {

            if (!StringUtils.equalsIgnoreCase(address1.street1(), address2.street1())) {
                return false;
            } //~ if (!StringUtils.equalsIgnoreCase(address1.street1(), address2.street1()))

            if (!StringUtils.equalsIgnoreCase(address1.street2(), address2.street2())) {
                return false;
            } //~ if (!StringUtils.equalsIgnoreCase(address1.street2(), address2.street2()))

            if (!StringUtils.equalsIgnoreCase(address1.city(), address2.city())) {
                return false;
            } //~ if (!StringUtils.equalsIgnoreCase(address1.city(), address2.city()))

            if(!StringUtils.equalsIgnoreCase(address1.state(), address2.state())) {
                return false;
            }

            return true;

        }
    }

}
