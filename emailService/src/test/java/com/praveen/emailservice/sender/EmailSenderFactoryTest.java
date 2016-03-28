package com.praveen.emailservice.sender;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EmailSenderFactoryTest {

	@Test
	public void testGetPrimaryAndSecondaryEmailSender() {
		EmailSender senderPrimary = EmailSenderFactory.getPrimaryEmailSender();
		EmailSender senderFallback = EmailSenderFactory.getFallbackEmailSender();
		
		assertTrue(senderPrimary.getClass().equals(SendGridEmailSender.class));
		assertTrue(senderFallback.getClass().equals(MailGunEmailSender.class));
	}
	
}
