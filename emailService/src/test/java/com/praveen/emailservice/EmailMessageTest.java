package com.praveen.emailservice;

import java.util.List;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

public class EmailMessageTest {
	public EmailMessageTest() {
		
	}
	
	@Test
	public void createValidEmailMessage() {
		String fromEmail = "abc@test.com";
		String toEmail1 = "xyz@test.com";
		String toEmail2 = "qwe@test.com";
		String subject = "This is a test subject";
		String msgBody = "This is the email body";
		List<String> toList = new ArrayList<String>();
		toList.add(toEmail1);
		toList.add(toEmail2);
		
		EmailMessage email = new EmailMessage(fromEmail, toList, subject, msgBody);
		
		assertTrue(email.isValid());
		assertEquals("From email must be same.", fromEmail, email.getFromEmail());
		assertEquals("subject must be same", subject, email.getEmailSubject());
		assertNull("CC-list must be null", email.getCCReceipients());
		
	}
	
	@Test
	public void createValidEmailMessage_WithCC() {
		String fromEmail = "abc@test.com";
		String toEmail1 = "xyz@test.com";
		String toEmail2 = "qwe@test.com";
		String ccEmail1 = "testcc@email.com";
		String subject = "This is a test subject";
		String msgBody = "This is the email body";
		List<String> toList = new ArrayList<String>();
		toList.add(toEmail1);
		toList.add(toEmail2);
		
		List<String> ccList = new ArrayList<String>();
		ccList.add(ccEmail1);
		
		EmailMessage email = new EmailMessage(fromEmail, toList, ccList, subject, msgBody);
		
		assertTrue(email.isValid());
		assertEquals("From email must be same.", fromEmail, email.getFromEmail());
		assertEquals("subject must be same", subject, email.getEmailSubject());
		assertEquals("To-list must be of same size", toList.size(), email.getToReceipients().size());
		assertEquals("CC-list must be of same size", ccList.size(), email.getCCReceipients().size());
		
	}
	
	@Test
	public void createValidEmailMessage_IncorrectFromEmail() {
		String fromEmail = "abc@.com";
		String toEmail1 = "xyz@test.com";
		String toEmail2 = "qwe@test.com";
		String subject = "This is a test subject";
		String msgBody = "This is the email body";
		List<String> toList = new ArrayList<String>();
		toList.add(toEmail1);
		toList.add(toEmail2);
		
		EmailMessage email = new EmailMessage(fromEmail, toList, subject, msgBody);
		
		assertFalse(email.isValid());
		assertEquals("From email must be same.", fromEmail, email.getFromEmail());
		assertEquals("subject must be same", subject, email.getEmailSubject());
		
	}
	
	@Test
	public void createValidEmailMessage_NullToEmail() {
		String fromEmail = "abc@.com";
		String subject = "This is a test subject";
		String msgBody = "This is the email body";
		List<String> toList = null;
		
		
		EmailMessage email = new EmailMessage(fromEmail, toList, subject, msgBody);
		
		assertFalse(email.isValid());
				
	}
	
	@Test
	public void createValidEmailMessage_WithLargeNumReceipients() {
		String fromEmail = "abc@test.com";
		String toEmail1 = "xyz@test.com";
		String ccEmail1 = "testcc@email.com";
		String subject = "This is a test subject";
		String msgBody = "This is the email body";
		List<String> toList = new ArrayList<String>();
		for (int i = 0; i < 100; i++) {
			toList.add(i + toEmail1);
		}
		
		
		List<String> ccList = new ArrayList<String>();
		ccList.add(ccEmail1);
		
		EmailMessage email = new EmailMessage(fromEmail, toList, ccList, subject, msgBody);
		
		assertFalse(email.isValid());
		
	}
}
