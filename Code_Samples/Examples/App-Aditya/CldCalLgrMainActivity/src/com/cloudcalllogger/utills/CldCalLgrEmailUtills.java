package com.cloudcalllogger.utills;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;
import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class CldCalLgrEmailUtills extends javax.mail.Authenticator {
	
	private static final String TAG = "CldCalLgrEmailUtills";

	
	  private String _user; 
	  private String _pass; 

	  private String[] _to; 
	  private String _from; 

	  private String _port; 
	  private String _sport; 

	  private String _host; 

	  private String _subject; 
	  private String _body; 
	  
	  private String[] _sms_List;
	  private String[] _calllog_list;

	  private boolean _auth; 

	  private boolean _debuggable; 

	  private Multipart _multipart; 


	  public CldCalLgrEmailUtills() { 
	    _host = "smtp.gmail.com"; // default smtp server 
	    _port = "465"; // default smtp port 
	    _sport = "465"; // default socketfactory port 

	    _user = ""; // username 
	    _pass = ""; // password 
	    _from = ""; // email sent from 
	    _subject = ""; // email subject 
	    _body = ""; // email body 

	    _debuggable = false; // debug mode on or off - default off 
	    _auth = true; // smtp authentication - default on 

	    _multipart = new MimeMultipart(); 

	    // There is something wrong with MailCap, javamail can not find a handler for the multipart/mixed part, so this bit needs to be added. 
	    MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap(); 
	    mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html"); 
	    mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml"); 
	    mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain"); 
	    mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed"); 
	    mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822"); 
	    CommandMap.setDefaultCommandMap(mc); 
	  } 

	  public  CldCalLgrEmailUtills(String user, String pass) { 
	    this(); 

	    _user = user; 
	    _pass = pass; 
	  } 

	  public boolean send() throws Exception { 
	    Properties props = _setProperties(); 
	     Multipart lObjMultipart =  new MimeMultipart();
	    if(!_user.equals("") && !_pass.equals("") && _to.length > 0 && !_from.equals("") && !_subject.equals("") ) { 
	        Session session = Session.getInstance(props, this);
	        DataHandler handler = new DataHandler(new ByteArrayDataSource(_body.getBytes(), "text/plain")); 
	        MimeMessage msg = new MimeMessage(session); 
	        msg.setFrom(new InternetAddress(_from)); 
	        msg.setDataHandler(handler);
	        InternetAddress[] addressTo = new InternetAddress[_to.length];
	        for (int i = 0; i < _to.length; i++) { 
	          //Log.d("mas"," --> to ="+_to[i]);	
	          addressTo[i] = new InternetAddress(_to[i]); 
	        } 
	        msg.setRecipients(MimeMessage.RecipientType.TO, addressTo); 

	        msg.setSubject(_subject); 
	        msg.setSentDate(new Date()); 

	        // setup message body 
	        //BodyPart messageBodyPart = new MimeBodyPart(); 
	        //messageBodyPart.setText(_body); 
	        
	        
	        //BodyPart smsMessageBodyPart = new MimeBodyPart(); 
	        //int len  = _sms_List.length
    		BodyPart  smsemptyLineMessageBodyPart = new MimeBodyPart(); 
    		smsemptyLineMessageBodyPart.setText("                               \n");
    		lObjMultipart.addBodyPart(smsemptyLineMessageBodyPart); 
    		
    		BodyPart  smsHeadingLineMessageBodyPart = new MimeBodyPart(); 
    		smsHeadingLineMessageBodyPart.setText("                  ****************       SMS Report      ****************                 \n");
    		lObjMultipart.addBodyPart(smsHeadingLineMessageBodyPart); 	   
    		
    		if(_sms_List.length == 1) {
    			//Log.d("mas", "1 mas  sms "+_sms_List.length); 
    			BodyPart smsMessageBodyPart = new MimeBodyPart(); 
    			// Add this to message
    			smsMessageBodyPart.setText(_sms_List[0]);
    			lObjMultipart.addBodyPart(smsMessageBodyPart); 
			}
			else {
				//Log.d("mas", "2 mas  sms "+_sms_List.length); 
				for(int i=0; i< _sms_List.length ; i++ ) {
					//BodyPart smsMessageBodyPart = new MimeBodyPart();
					BodyPart smsMessageBodyPart = new MimeBodyPart(); 
					smsMessageBodyPart.setText(_sms_List[i]);
					lObjMultipart.addBodyPart(smsMessageBodyPart); 
					}
			}
    		
    		BodyPart  callemptyLineMessageBodyPart = new MimeBodyPart(); 
    		callemptyLineMessageBodyPart.setText("                               \n");
    		lObjMultipart.addBodyPart(callemptyLineMessageBodyPart); 
    		
    		BodyPart  callHeadingLineMessageBodyPart = new MimeBodyPart(); 
    		callHeadingLineMessageBodyPart.setText("                  ****************      Voice Call Report       ************      \n");
    		lObjMultipart.addBodyPart(callHeadingLineMessageBodyPart); 
    		
    		if(_calllog_list.length == 1) {
    			//Log.d("mas", "1 mas call "+_calllog_list.length);
    			BodyPart callMessageBodyPart = new MimeBodyPart(); 
    			// Add this to message
    			callMessageBodyPart.setText(_calllog_list[0]);
    			lObjMultipart.addBodyPart(callMessageBodyPart); 
			}
			else {
				//Log.d("mas", "2 mas call "+_calllog_list.length);
				for(int i=0; i< _calllog_list.length ; i++ ) {
					//BodyPart smsMessageBodyPart = new MimeBodyPart();
					BodyPart callMessageBodyPart = new MimeBodyPart(); 
					callMessageBodyPart.setText(_calllog_list[i]);
					lObjMultipart.addBodyPart(callMessageBodyPart); 
					}
			}
    		

	        // Put parts in message 
	        msg.setContent(lObjMultipart); 
	        	
	        // send email 
	        //Log.d("mas", "Before Send");
	        Transport.send(msg); 
	        Log.v("mas", "Email was  send");
	        return true; 
	    } 
	    else { 
	         Log.v("mas", "Email was  not send");
	         return false; 
	    } 
	  } 
	  public class ByteArrayDataSource implements DataSource {   
	      private byte[] data;   
	      private String type;   

	      public ByteArrayDataSource(byte[] data, String type) {   
	          super();   
	          this.data = data;   
	          this.type = type;   
	      }   

	      public ByteArrayDataSource(byte[] data) {   
	          super();   
	          this.data = data;   
	      }   

	      public void setType(String type) {   
	          this.type = type;   
	      }   

	      public String getContentType() {   
	          if (type == null)   
	              return "application/octet-stream";   
	          else  
	              return type;   
	      }   

	      public InputStream getInputStream() throws IOException {   
	          return new ByteArrayInputStream(data);   
	      }   

	      public String getName() {   
	          return "ByteArrayDataSource";   
	      }   

	      public OutputStream getOutputStream() throws IOException {   
	          throw new IOException("Not Supported");   
	      }   
	  }   

	  @Override 
	  public PasswordAuthentication getPasswordAuthentication() { 
	    return new PasswordAuthentication(_user, _pass); 
	  } 

	  private Properties _setProperties() { 
	    Properties props = new Properties(); 

	    props.put("mail.smtp.host", _host); 

	    if(_debuggable) { 
	      props.put("mail.debug", "true"); 
	    } 

	    if(_auth) { 
	      props.put("mail.smtp.auth", "true"); 
	    } 

	    props.put("mail.smtp.port", _port); 
	    props.put("mail.smtp.socketFactory.port", _sport); 
	    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); 
	    props.put("mail.smtp.socketFactory.fallback", "false"); 

	    return props; 
	  } 

	  // the getters and setters 
	  public String getBody() { 
	    return _body; 
	  } 

	  public void setBody(String _body) { 
	    this._body = null;
	    this._body = _body; 
	  }

	public void setTo(String[] toArr) {
	    // TODO Auto-generated method stub
	    this._to=toArr;
	}

	public void setFrom(String string) {
	    // TODO Auto-generated method stub
	    this._from=string;
	}

	public void setSubject(String string) {
	    // TODO Auto-generated method stub
	    this._subject=string;
	} 	
	
	public void setSmsRec(String []string) {
	    // TODO Auto-generated method stub
		_sms_List = null;

	    _sms_List=string;
	}
	
	public void setCalRec(String []string) {
	    // TODO Auto-generated method stub
		_calllog_list = null;

	    _calllog_list= string;
	} 

}
