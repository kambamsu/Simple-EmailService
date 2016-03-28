package com.praveen.emailservice.sender;

import java.io.IOException;

import javax.ws.rs.core.MediaType;

import com.praveen.emailservice.EmailMessage;
import com.praveen.emailservice.exceptions.IllegalRequestException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * This class implements the emailSender interface to send emails via the
 * MailGun API.
 * @author Praveen
 *
 */
public class MailGunEmailSender implements EmailSender {

	static final String API_KEY = "key-67403f76bc98af290c1de270e6e79d2d";
	static final String MAILGUN_URL = "https://api.mailgun.net/v3/sandboxc2574801af5043b5819d9fe6bef56a74.mailgun.org/messages";
	private final String FROM_KEY = "from";
	private final String TO_KEY = "to";
	private final String SUBJECT_KEY = "subject";
	private final String CC_KEY = "cc";
	private final String BODY_KEY = "text";
	static final String API = "api";

	private final int OK_RESPONSE = 200;
	private static final int MAX_RETRY = 3;
	private static final int SLEEP_TIME = 200;

	//This object will be used for sending and getting information back from
	//the MailGun url.
	private final WebResource webResource;
	
	public MailGunEmailSender() {
		Client client = Client.create();
		webResource = createWebResourceFromClient(client);
	}
	
	public MailGunEmailSender(final Client client) {
		webResource = createWebResourceFromClient(client);
	}
	
	/**
	 * This method creates the WebResource object which will be used during this send
	 * operation to send the email.
	 * @param client 
	 * @return
	 */
	private WebResource createWebResourceFromClient(final Client client) {
		client.addFilter(new HTTPBasicAuthFilter(API, API_KEY));
		return client.resource(MAILGUN_URL);
	}
	
	/**
	 * This method does the work of sending the email via MailGun API. 
	 * When sending fails, we retry three times before failing.
	 *
	 * @param email The email message object containing the details of the email to be sent.
	 * @return boolean value, true if sending was successful. False otherwise.
	 */
	@Override
	public boolean sendEmail(final EmailMessage email) {
		
		try {
			ClientResponse response = prepareAndSendMessage(email);
			if (response.getStatus() == OK_RESPONSE) {
				return true;
			}
			int retryCount = 0;
			// retry three times before quitting.
			while (retryCount < MAX_RETRY && response.getStatus() != OK_RESPONSE) {

				Thread.sleep(SLEEP_TIME);

				retryCount++;
				response = prepareAndSendMessage(email);
			}

			if (response.getStatus() == OK_RESPONSE) {
				return true;
			}
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		} catch (ClientHandlerException ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * This is a helper method that prepares the data to be sent first and then
	 * uses the webResource object to use the MailGun API to send out the email.
	 * 
	 * @param email Generic email object containing the contents of the email.
	 * @return
	 */
	private ClientResponse prepareAndSendMessage(final EmailMessage email) {
		
		final MultivaluedMapImpl formData = constructEmailForm(email);

		return webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData);
	}

	private MultivaluedMapImpl constructEmailForm(final EmailMessage email) {
		if (email == null) {
			String errMsg = "Email is null.  Please verify all email addresses provided.";
			// LOGGER.log(Level.SEVERE, errMsg);
			throw new IllegalRequestException(errMsg);
		}
		if (!email.isValid()) {
			throw new IllegalRequestException(
					"Invalid email present in the form. Please verify all email addresses provided.");
		}

		MultivaluedMapImpl formData = new MultivaluedMapImpl();

		formData.add(FROM_KEY, email.getFromEmail());
		for (String toEmail : email.getToReceipients()) {
			formData.add(TO_KEY, toEmail);
		}
		if (email.getCCReceipients() != null) {
			for (String ccEmail : email.getCCReceipients()) {
				formData.add(CC_KEY, ccEmail);
			}
		}
		formData.add(SUBJECT_KEY, email.getEmailSubject());
		formData.add(BODY_KEY, email.getEmailMessageBody());

		return formData;
	}

}
