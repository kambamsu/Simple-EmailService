package com.praveen.emailservice.sender;

import com.praveen.emailservice.EmailMessage;
import com.praveen.emailservice.exceptions.IllegalRequestException;

/**
 * An interface to be used by the servlet to send emails. This helps in abstracting
 * out the actual API used to send the email by the clients calling this.
 * 
 * @author Praveen
 *
 */
public interface EmailSender {
	public boolean sendEmail(final EmailMessage email)  throws IllegalRequestException ;
}
