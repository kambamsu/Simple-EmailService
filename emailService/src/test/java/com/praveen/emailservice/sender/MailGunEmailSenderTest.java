package com.praveen.emailservice.sender;
import org.easymock.EasyMock.*;
import org.easymock.EasyMock;
import org.junit.Test;
import org.easymock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.praveen.emailservice.EmailMessage;
import com.praveen.emailservice.exceptions.IllegalRequestException;
import com.sendgrid.SendGridException;
import javax.ws.rs.core.MediaType;

import java.util.ArrayList;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.ClientFilter;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;


public class MailGunEmailSenderTest {

	Client mockClient = EasyMock.createMock(Client.class);
	WebResource mockWebResource = EasyMock.createMock(WebResource.class);
	WebResource.Builder mockBuilder = EasyMock.createMock(WebResource.Builder .class);
	MailGunEmailSender sender;// = new MailGunEmailSender(mockClient);
	
	@Test
	public void testSendEmail_Success() {
		//set expectations
		setExpectations();
		
		//behavior under test
		sender = new MailGunEmailSender(mockClient);
		EmailMessage validEmail = TestUtil.getValidEmail();
		boolean sendSuccessful = sender.sendEmail(validEmail);
		
		//verifications
		EasyMock.verify(mockClient,mockWebResource);
		assertTrue(sendSuccessful); 
		
	}
	
	@Test(expected=IllegalRequestException.class)
	public void testSendEmail_NullEmailMsg() throws SendGridException {
		//set expectations
		setExpectations();
		
		//behavior under test
		sender = new MailGunEmailSender(mockClient);
		final boolean sendSuccess = sender.sendEmail(null);
	}
	
	@Test(expected=IllegalRequestException.class)
	public void testSendEmail_InvalidEmailMsg() throws SendGridException {
	
		setExpectations();
		
		//behavior under test
		sender = new MailGunEmailSender(mockClient);
		EmailMessage email = TestUtil.getInvalidEmail();
		final boolean sendSuccess = sender.sendEmail(email);
		
		
	}
	
	@Test
	public void testSendEmail_FailOnceSucceedOnRetry() {
		//set expectations
		mockClient.addFilter(EasyMock.anyObject(ClientFilter.class));
		EasyMock.expectLastCall().anyTimes();
		
		EasyMock.expect(mockClient.resource(MailGunEmailSender.MAILGUN_URL)).andReturn(mockWebResource);
		EasyMock.expect(mockWebResource.type(MediaType.APPLICATION_FORM_URLENCODED)).andReturn(mockBuilder).times(2);
		
		//return a failure code when we try the first time.
		final ClientResponse resp = new ClientResponse(250, null, null, null);
		EasyMock.expect(mockBuilder.post(EasyMock.eq(ClientResponse.class), EasyMock.anyObject(MultivaluedMapImpl.class))).andReturn(resp).times(1);
		
		//return success when called the second time.
		ClientResponse validResp = new ClientResponse(200, null, null, null);
		EasyMock.expect(mockBuilder.post(EasyMock.eq(ClientResponse.class), EasyMock.anyObject(MultivaluedMapImpl.class))).andReturn(validResp).times(1);

		EasyMock.replay(mockClient, mockWebResource, mockBuilder);
		
		//behavior under test
		sender = new MailGunEmailSender(mockClient);
		EmailMessage validEmail = TestUtil.getValidEmail();
		boolean sendSuccessful = sender.sendEmail(validEmail);
		
		//verifications
		EasyMock.verify(mockClient,mockWebResource);
		assertTrue(sendSuccessful); 
		
	}
	
	@Test
	public void testSendEmail_FailAllRetries() {
		//set expectations
		mockClient.addFilter(EasyMock.anyObject(ClientFilter.class));
		EasyMock.expectLastCall().anyTimes();
		
		EasyMock.expect(mockClient.resource(MailGunEmailSender.MAILGUN_URL)).andReturn(mockWebResource);
		EasyMock.expect(mockWebResource.type(MediaType.APPLICATION_FORM_URLENCODED)).andReturn(mockBuilder).times(4);
		
		//return a failure code when we try everytime.
		final ClientResponse resp = new ClientResponse(250, null, null, null);
		EasyMock.expect(mockBuilder.post(EasyMock.eq(ClientResponse.class), EasyMock.anyObject(MultivaluedMapImpl.class))).andReturn(resp).times(4);
		
		EasyMock.replay(mockClient, mockWebResource, mockBuilder);
		
		//behavior under test
		sender = new MailGunEmailSender(mockClient);
		EmailMessage validEmail = TestUtil.getValidEmail();
		boolean sendSuccessful = sender.sendEmail(validEmail);
		
		//verifications
		EasyMock.verify(mockClient,mockWebResource);
		assertFalse(sendSuccessful); 
		
	}
	
	private void setExpectations() {
		mockClient.addFilter(EasyMock.anyObject(ClientFilter.class));
		EasyMock.expectLastCall().anyTimes();
		
		EasyMock.expect(mockClient.resource(MailGunEmailSender.MAILGUN_URL)).andReturn(mockWebResource);
		EasyMock.expect(mockWebResource.type(MediaType.APPLICATION_FORM_URLENCODED)).andReturn(mockBuilder).times(1);
		ClientResponse resp = new ClientResponse(200, null, null, null);
		EasyMock.expect(mockBuilder.post(EasyMock.eq(ClientResponse.class), EasyMock.anyObject(MultivaluedMapImpl.class))).andReturn(resp).times(1);
		EasyMock.replay(mockClient, mockWebResource, mockBuilder);
		
	}
}
