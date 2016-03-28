package com.praveen.emailservice.sender;
import java.util.List;
import org.junit.Test;

import com.praveen.emailservice.EmailMessage;
import com.sendgrid.SendGrid;
import com.sendgrid.SendGrid.Response;
import com.sendgrid.SendGridException;
import com.sendgrid.SendGrid.Email;
import org.easymock.EasyMock.*;
import org.easymock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import com.praveen.emailservice.exceptions.IllegalRequestException;
import java.util.ArrayList;

public class SendGridEmailSenderTest {

	private final SendGrid sendgrid = EasyMock.createMock(SendGrid.class);
	private final SendGridEmailSender sender = new SendGridEmailSender(sendgrid);
	
	@Test
	public void testSendEmail_Success() throws SendGridException {
		//set expectations
		EasyMock.expect(sendgrid.send(EasyMock.anyObject(Email.class))).andReturn(new Response(250, "OK")).times(1);
		EasyMock.replay(sendgrid);
		
		//behavior under test
		EmailMessage email = TestUtil.getValidEmail();
		final boolean sendSuccess = sender.sendEmail(email);
		
		//verify/validations
		EasyMock.verify(sendgrid);
		assertTrue(sendSuccess);
		
		
	}
	
	@Test(expected=IllegalRequestException.class)
	public void testSendEmail_NullEmailMsg() throws SendGridException {
		EasyMock.expect(sendgrid.send(EasyMock.anyObject(Email.class))).andReturn(new Response(250, "OK")).times(1);
		EasyMock.replay(sendgrid);
		
		//behavior under test
		final boolean sendSuccess = sender.sendEmail(null);
	}
	
	@Test(expected=IllegalRequestException.class)
	public void testSendEmail_InvalidEmailMsg() throws SendGridException {
		EasyMock.expect(sendgrid.send(EasyMock.anyObject(Email.class))).andReturn(new Response(250, "OK")).times(1);
		EasyMock.replay(sendgrid);
		
		//behavior under test
		EmailMessage email = TestUtil.getInvalidEmail();
		final boolean sendSuccess = sender.sendEmail(email);
		
		
	}
	
	@Test
	public void testSendEmail_FailFirstTry() throws SendGridException {
		EasyMock.expect(sendgrid.send(EasyMock.anyObject(Email.class))).andReturn(new Response(552, "FAIL")).times(1);
		EasyMock.expect(sendgrid.send(EasyMock.anyObject(Email.class))).andReturn(new Response(250, "OK")).times(1);
		EasyMock.replay(sendgrid);
		
		//behavior under test
		EmailMessage email = TestUtil.getValidEmail();
		final boolean sendSuccess = sender.sendEmail(email);
		
		EasyMock.verify(sendgrid);
		assertTrue(sendSuccess);
	}
	
	@Test
	public void testSendEmail_FailAllTries() throws SendGridException {
		EasyMock.expect(sendgrid.send(EasyMock.anyObject(Email.class))).andReturn(new Response(550, "FAIL")).times(4);
		EasyMock.replay(sendgrid);
		
		//behavior under test
		EmailMessage email = TestUtil.getValidEmail();
		final boolean sendSuccess = sender.sendEmail(email);
		
		EasyMock.verify(sendgrid);
		assertFalse(sendSuccess);
	}
	
	/*@Test
	public void testSendEmail_ExceptionFromSendGrid() throws SendGridException {
		EasyMock.expect(sendgrid.send(EasyMock.anyObject(Email.class))).andThrow(new SendGridException(""));
		EasyMock.replay(sendgrid);
		
		//behavior under test
		EmailMessage email = TestUtil.getValidEmail();
		final boolean sendSuccess = sender.sendEmail(email);
		
		EasyMock.verify(sendgrid);
		assertFalse(sendSuccess);
	}*/
	
	@Test
	public void testSendEmail_TooManyReceipients() {
		
	}
	 
}
