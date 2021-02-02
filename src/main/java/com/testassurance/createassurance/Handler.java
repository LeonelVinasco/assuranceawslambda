/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.testassurance.createassurance;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.*;
import redis.clients.jedis.Jedis;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

   

//import javax.mail.BodyPart;
//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.Multipart;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeBodyPart;
//import javax.mail.internet.MimeMessage;
//import javax.mail.internet.MimeMultipart;
//import javax.mail.Authenticator;
//import javax.mail.PasswordAuthentication;

//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.sql.Connection;
import java.util.Properties;
    
import java.io.*;
import java.util.*;
/**
 *
 * @author leo
 */

//class EmailUtil {

	/**
	 * Utility method to send simple HTML email
	 * @param session
	 * @param toEmail
	 * @param subject
	 * @param body
	 */
//	public static void sendEmail(Session session, String toEmail, String subject, String body){
//		try
//	    {
//	      MimeMessage msg = new MimeMessage(session);
//	      //set message headers
//	      msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
//	      msg.addHeader("format", "flowed");
//	      msg.addHeader("Content-Transfer-Encoding", "8bit");
//
//	      msg.setFrom(new InternetAddress("leonelvinasco@gmail.com", "NoReply-JD"));
//
//	      msg.setReplyTo(InternetAddress.parse("leonelvinasco@gmail.com", false));
//
//	      msg.setSubject(subject, "UTF-8");
//
//	      msg.setText(body, "UTF-8");
//
//	      msg.setSentDate(new Date());
//
//	      msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
//	      System.out.println("Message is ready " + msg);
//    	  Transport.send(msg);  
//
//	      System.out.println("EMail Sent Successfully!!");
//	    }
//	    catch (Exception e) {
//	      e.printStackTrace();
//	    }
//	}
//}

public class Handler implements RequestStreamHandler {
    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
                 
        
        
        
        
        
        
        
        
        String url = "jdbc:mysql://database-1.cluster-civqhsmx6dcp.us-east-2.rds.amazonaws.com";
        String user = "admin";
        String passworddb = "admin12345";
//        try (java.sql.Connection conn = DriverManager.getConnection(url, user, passworddb)) {
// 
//    if (conn != null) {
//        System.out.println("Connected to the database");
//    }
//} catch (SQLException ex) {
//    System.out.println("An error occurred. Maybe user/password is invalid");
//    ex.printStackTrace();
//}
        System.out.println("Initializing...");
        System.out.println("SimpleEmail Start");
	final String fromEmail = "AKIAS65ARX52N2TRBPX5"; //requires valid gmail id
		final String password = "BID1w2XcTL7aq7a4HngqxMnNigqhbMhO3EzG1MSb8JIV"; // correct password for gmail id
		final String toEmail = "leonelvinasco@gmail.com"; // can be any email id 
		
		System.out.println("TLSEmail Start");
//		Properties props = new Properties();
//		props.put("mail.smtp.host", "email-smtp.us-east-2.amazonaws.com"); //SMTP Host
//		props.put("mail.smtp.port", "587"); //TLS Port
//		props.put("mail.smtp.auth", "true"); //enable authentication
//		props.put("mail.smtp.starttls.enable", "false"); //enable STARTTLS
//		
//                //create Authenticator object to pass in Session.getInstance argument
//		Authenticator auth = new Authenticator() {
//			//override the getPasswordAuthentication method
//			protected PasswordAuthentication getPasswordAuthentication() {
//				return new PasswordAuthentication(fromEmail, password);
//			}
//		};
//		Session session = Session.getInstance(props, auth);
//		
//		//EmailUtil.sendEmail(session, toEmail,"TLSEmail Testing Subject", "TLSEmail Testing Body");	
//	    
        JSONParser parser = new JSONParser();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
         //Connection conn1 = null;
        JSONObject responseJson = new JSONObject ();
        
        
        try {
            JSONObject  event = (JSONObject ) parser.parse(reader);
            Jedis jedis = new Jedis("assurance-inmem.gfmdlf.0001.use2.cache.amazonaws.com");

            if (event.get("body") != null) {
                Assurance assurance = new Assurance((String) event.get("body").toString());
                System.out.println(assurance.getAssuranceName());
                 
                System.out.println("Connection Successful");
                String value= "'"+assurance.toString()+"'";
                System.out.println("The server is running " + jedis.ping());
                jedis.set(assurance.getId(), value );
                
                System.out.println("Stored string in redis:: "+ jedis.get(assurance.getId()));
            }
            System.out.println(event.toJSONString());
            JSONObject responseBody = new JSONObject();
            responseBody.put("message", "New item created");

            JSONObject headerJson = new JSONObject();
            headerJson.put("Content-Type", "application/json");

            responseJson.put("statusCode", 200);
            responseJson.put("headers", headerJson);
            responseJson.put("body", responseBody.toString());
            
              ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("ssl://b-c45fb5f9-3015-4e76-be6c-9a1686dedd6e-1.mq.us-east-2.amazonaws.com:61617");
            connectionFactory.setPassword("admin");
            connectionFactory.setUserName("administrator12345");
            javax.jms.Connection connection = connectionFactory.createConnection();
        connection.start();
         Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
         // Create a Session
                Session sessionBrk = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                // Create the destination (Topic or Queue)
                Destination destination = sessionBrk.createQueue("TEST.FOO");

                // Create a MessageProducer from the Session to the Topic or Queue
                MessageProducer producer = sessionBrk.createProducer(destination);
                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

                // Create a messages
                String text = "Hello world! From: " + Thread.currentThread().getName() + " : " + this.hashCode();
                TextMessage message = sessionBrk.createTextMessage(text);

                // Tell the producer to send the message
                System.out.println("Sent message: "+ message.hashCode() + " : " + Thread.currentThread().getName());
                producer.send(message);
                 
                 // Create a MessageConsumer from the Session to the Topic or Queue
                MessageConsumer consumer = session.createConsumer(destination);

                // Wait for a message
                Message messageBrk = consumer.receive(1000);

                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) messageBrk;
                    String textBrk = textMessage.getText();
                    System.out.println("Received: " + textBrk);
                } else {
                    System.out.println("Received: " + messageBrk);
                }
                // Clean up
                session.close();
                connection.close();

        }catch (ParseException pex) {
            responseJson.put("statusCode", 400);
            responseJson.put("exception", pex);
        }catch(Exception e) {
            System.out.println(e);
        }

        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        writer.write(responseJson.toString());
        writer.close();
    }
}
