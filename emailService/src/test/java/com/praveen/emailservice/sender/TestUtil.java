package com.praveen.emailservice.sender;

import java.util.ArrayList;
import java.util.List;

import com.praveen.emailservice.EmailMessage;

public class TestUtil {
	public static EmailMessage getValidEmail() {
		String fromEmail = "praveensugavanam@gmail.com";
		String toEmail1 = "xyz@test.com";
		String toEmail2 = "qwe@test.com";
		String subject = "This is a test subject";
		String msgBody = "This is the email body";
		List<String> toList = new ArrayList<String>();
		toList.add(toEmail1);
		toList.add(toEmail2);
		
		EmailMessage email = new EmailMessage(fromEmail, toList, subject, msgBody);
		return email;
	}
	
	public static EmailMessage getInvalidEmail() {
		String fromEmail = "abc@.com";
		String toEmail1 = "xyz@test.com";
		String toEmail2 = "qwe@test.com";
		String subject = "This is a test subject";
		String msgBody = "This is the email body";
		List<String> toList = new ArrayList<String>();
		toList.add(toEmail1);
		toList.add(toEmail2);
		
		EmailMessage email = new EmailMessage(fromEmail, toList, subject, msgBody);
		return email;
	}
}
