package com.praveen.emailservice.sender;

/**
 * This class is used abstract out the email sender picked for sending
 * from the servlet.
 * 
 * @author Praveen
 *
 */
public class EmailSenderFactory {

	/**
	 * This method returns the primary email sender to be used for 
	 * sending emails. In this case, we use SendGrid as our primary sender.
	 * 
	 * @return instance of an EmailSender to be used by servlet.
	 */
	public static EmailSender getPrimaryEmailSender() {
		return new SendGridEmailSender();
	}

	/**
	 * This method returns the fallback email sender to be used for 
	 * sending emails. In this case, we use MailGun as our primary sender.
	 * 
	 * @return instance of an EmailSender to be used by servlet.
	 */
	public static EmailSender getFallbackEmailSender() {
		return new MailGunEmailSender();
	}
}
