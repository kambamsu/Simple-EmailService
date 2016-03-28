/**
 * 
 */
package com.praveen.emailservice.sender;

import org.apache.commons.logging.*;
//import org.apache.log4j.Logger;
import com.praveen.emailservice.EmailMessage;
import com.praveen.emailservice.exceptions.IllegalRequestException;
import com.sendgrid.*;
import com.sendgrid.SendGrid.Response;
import com.sendgrid.SendGrid.Email;

/**
 * This class implements the EmailSender interface to send an email using
 * the SendGrid email API.
 * 
 * @author Praveen KS
 *
 */
public class SendGridEmailSender implements EmailSender {
	// final static Logger logger = Logger.getLogger(SendGridEmailSender.class);
	private static final String API_KEY = "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";
	private static final int SUCCESS_THRESHOLD = 550;
	private static final int MAX_RETRY = 3;
	
	private SendGrid sendgrid;

	public SendGridEmailSender() {
		sendgrid = new SendGrid(API_KEY);
	}

	public SendGridEmailSender(final SendGrid sg) {
		sendgrid = sg;
	}
	
	/**
	 * This method does the work of sending the email via SendGrid API. 
	 * When sending fails, we retry three times before failing.
	 * 
	 * @param email The email message object containing the details of the email to be sent.
	 * @return boolean value, true if sending was successful. False otherwise.
	 */
	@Override
	public boolean sendEmail(final EmailMessage email) {
		try {
			Email sendgridEmail = convertToSendGridEmail(email);
			Response r = sendgrid.send(sendgridEmail);
			if (r.getCode() >= SUCCESS_THRESHOLD) {
				int retryCount = 0;
				// retry three times before quitting.
				while (retryCount < MAX_RETRY && r.getCode() >= SUCCESS_THRESHOLD) {
					Thread.sleep(200);
					retryCount++;
					r = sendgrid.send(sendgridEmail);
				}

				if (r.getCode() >= SUCCESS_THRESHOLD) {
					return false;
				}
			}
			// LOGGER.log(Level.INFO, "Response message : " + r.getMessage());
			return true;
		} catch (SendGridException ex) {
			System.err.println("Exception" + ex.getMessage());
			return false;
			// LOGGER.log(Level.SEVERE, null, ex);
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}

	}
	
	/**
	 * This is a helper method to convert the generic email object into the SendGrid
	 * email object.
	 * 
	 * @param email The generic email object containing the message details.
	 * @return The SendGrid email object containing the details in the email object.
	 */
	private Email convertToSendGridEmail(EmailMessage email) {
		//Sanity check. If email is null, we throw an illegal request exception.
		if (email == null) {
			String errMsg = "Email is null";
			// LOGGER.log(Level.SEVERE, errMsg);
			throw new IllegalRequestException(errMsg);
		}
		//If the email object contains invalid contents, we throw an illegalRequestException.
		if (!email.isValid()) {
			throw new IllegalRequestException("Invalid email present in the form");
		}
		SendGrid.Email sgEmail = new SendGrid.Email();
		sgEmail.setFrom(email.getFromEmail());

		for (String toEmail : email.getToReceipients()) {
			sgEmail.addTo(toEmail);
		}
		if (email.getCCReceipients() != null) {
			for (String ccEmail : email.getCCReceipients()) {
				sgEmail.addCc(ccEmail);
			}
		}

		sgEmail.setSubject(email.getEmailSubject());
		sgEmail.setText(email.getEmailMessageBody());
		return sgEmail;
	}

}
