package com.praveen.emailservice;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.oreilly.servlet.MultipartRequest;
import com.praveen.emailservice.exceptions.IllegalRequestException;
import com.praveen.emailservice.sender.EmailSender;
import com.praveen.emailservice.sender.EmailSenderFactory;

/**
 * This class will serve the requests from the client and call the corresponding
 * email sender and respond according to whether or not the email was sent
 * 
 * @author sugavana
 *
 */
public class EmailServiceServlet extends HttpServlet {
	/**
	 * 
	 */
	public static final long serialVersionUID = -2392646818301456899L;
	public static final String FROM_EMAIL = "fromEmail";
	public static final String TO_EMAIL = "toEmail";
	public static final String CC_LIST = "ccEmail";
	public static final String EMAIL_SUBJECT = "emailSubject";
	public static final String EMAIL_BODY = "emailBody";

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

	}
	
	/**
	 * 
	 */
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		doSendEmail(req, resp);
	}

	/**
	 * This method performs the actual work. It first picks the email sender to use
	 * and tries to send the email using it. If sending fails, it tries the fallback sender.
	 * 
	 * @param req
	 *            Httpservlet request containing the data sent from the client
	 * @param resp
	 *            HttpServletResponse sent back to the client
	 * @throws IOException
	 */
	public void doSendEmail(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
		boolean isSendSuccessful = false;
		String errorMessage = "";
		EmailMessage email = null;
		try {
			//We use a factory here because the servlet should not know which 
			//sender is being used to send the email.
			final EmailSender sender = EmailSenderFactory.getPrimaryEmailSender();
			email = createEmailMessageFromRequest(req);

			isSendSuccessful = sender.sendEmail(email);
			if (!isSendSuccessful) {
				final EmailSender secondarySender = EmailSenderFactory.getFallbackEmailSender();
				isSendSuccessful = secondarySender.sendEmail(email);
			}
		} catch(IllegalRequestException e) {
			errorMessage = e.getMessage();
		}catch (Exception ex) {
			//If there were any exceptions, we try to pass it back to the client.
			errorMessage = ex.getMessage();
		}
		
		updateClientResponse(isSendSuccessful, errorMessage, resp);
	}

	/**
	 * Update the client response based on whether or not sending was successful.
	 * 
	 * @param isSendSuccessful Was the email sent successfully ?
	 * @param resp Servlet response.
	 * @throws IOException
	 */
	public void updateClientResponse(final boolean isSendSuccessful, final String errorMessage,
			final HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		if (isSendSuccessful) {
			resp.getWriter().println("Email sent successfully! \n\n");
		} else {
			if (errorMessage != null && !errorMessage.equals("")) {
				resp.getWriter().println(errorMessage);
			}
			resp.getWriter().println("Email sending was not successful.\n");
		}

	}

	/**
	 * This method creates an EmailMessage object from the request parameters
	 * 
	 * @param req
	 * @return an object of type EmailMessage formed from the client request.
	 */
	public EmailMessage createEmailMessageFromRequest(final HttpServletRequest req) {
		String fromEmail = req.getParameter(FROM_EMAIL);
		List<String> toEmails = null;
		String toList = req.getParameter(TO_EMAIL);
		if (toList != null) {
			toEmails = new ArrayList<String>();
			String[] toAddresses = toList.split(",");
			for (String to : toAddresses) {
				toEmails.add(to.trim());
			}
		}

		List<String> ccEmails = null;
		String ccList = req.getParameter(CC_LIST);
		if (ccList != null) {
			ccEmails = new ArrayList<String>();
			String[] ccAddresses = ccList.split(",");
			for (String cc : ccAddresses) {
				ccEmails.add(cc.trim());
			}
		}
		String emailSubject = req.getParameter(EMAIL_SUBJECT);
		String emailBody = req.getParameter(EMAIL_BODY);

		return new EmailMessage(fromEmail, toEmails, ccEmails, emailSubject, emailBody);
	}
}
