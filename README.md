For the Uber coding challenge, I’ve decided to implement the Simple Email Service using SendGrid and MailGun java APIs.

The simple email service can be viewed and tried at : simpleemailservice-praveen.appspot.com


The implementation consists of a simple HTML client page where users can input the details of their email. When the user clicks on the Send button, the request is passed onto a Java Servlet which handles the email sending. All dependencies in the project are handled by Maven.


The Java backend consists of two parts. One, the servlet that handles the request. And two, the Email senders, to whom the actual work of sending the emails is delegated.

The servlet class (EmailServiceServlet.java) first parses the input provided by the user to create a generic EmailMessage object, which can be consumed by the EmailSenders. Once the parsing is done, the servlet gets the primary email sender from the EmailSenderFactory. This factory helps in abstracting out which sender is being used as primary vs fallback from the servlet. 

In our case, currently the primary sender is SendGrid. With the primary email sender object, the servlet tries to send the email with it’s contents. If sending succeeds, we are redirected to a success page. If sending fails, the email sender retries 3 times before quitting. If it fails after three times, the servlet falls back on the the secondary provider (which in our case is MailGun). If email sending fails even after trying the secondary provider, we are forwarded to a failure page.

Testing

Each class has been tested with a significant number of use cases using Junit tests.

Deployment

The code and service is deployed on Google App Engine.


=====

Certain restrictions

1. Since one of the email services restrict the max recipient list at 50, i have mandated that the SimpleEmailService also takes at most 50 recipients.
2. The service currently does not support Bcc. This can be added as a future enhancement.
3. Any restrictions of SendGrid and MailGun apply to the SimpleEmailService too.

=====

Attributions

1. The CSS design for the client UI was inspired from: http://www.formget.com/different-form-styles-using-html-css3/
2. 
