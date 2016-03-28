package com.praveen.emailservice;

import java.util.List;

public class EmailMessage {
	final String from;
	final List<String> toReceipient;
	final List<String> ccReceipient;
	final String messageBody;
	final String messageSubject;
	final boolean isValid;
	
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public EmailMessage(final String fromEmail, final List<String> toEmail, final String msgSubject,
			final String msgBody) {
		from = fromEmail;
		toReceipient = toEmail;
		ccReceipient = null;
		messageBody = msgBody;
		messageSubject = msgSubject;
		isValid = isValidEmail();
	}

	public EmailMessage(final String fromEmail, final List<String> toEmail, final List<String> ccEmail,
			final String msgSubject, final String msgBody) {
		from = fromEmail;
		toReceipient = toEmail;
		ccReceipient = ccEmail;
		messageBody = msgBody;
		messageSubject = msgSubject;
		isValid = isValidEmail();
		//error = ccReceipient.size() + "";
	}

	public String getFromEmail() {
		return from;
	}

	public List<String> getToReceipients() {
		return toReceipient;
	}

	public List<String> getCCReceipients() {
		return ccReceipient;
	}

	public String getEmailSubject() {
		return messageSubject;
	}

	public String getEmailMessageBody() {
		return messageBody;
	}
	
	public boolean isValid() {
		return isValid;
	}

	private boolean isValidEmail() {
		if (from == null) {
			return false;
		}
		if (from.matches(EMAIL_PATTERN)) {
			if (toReceipient == null) {
				return false;
			}
			for (String to : toReceipient) {
				if (!to.matches(EMAIL_PATTERN)) {
					return false;
				}
			}
			if (ccReceipient != null) {

				for (String cc : ccReceipient) {
					if (!cc.matches(EMAIL_PATTERN)) {
						return false;
					}
				}
				if (toReceipient.size() + ccReceipient.size() > 50) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
}
