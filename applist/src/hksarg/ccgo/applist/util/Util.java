package hksarg.ccgo.applist.util;

import hksarg.ccgo.applist.config.PropertyLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
/**
 * DP Application List Gerneration Program
 * @author CCGO Development Team
 * @version 2.1.1 create on 2004/12/19
 */
public class Util {
	static Logger logger = Logger.getLogger(Util.class.getName()); //log4j
	/**
	 * Validate Local Applist XML Schema
	 * @return boolean
	 * @throws Exception
	 */
    public static boolean validateSchemaLocalApplist() throws Exception {        
        XsdErrorHandler myErrorHandler = new XsdErrorHandler();
        DOMParser myParser = new DOMParser();
        Document myDocument = null;
        boolean result = true;
        InputStream applistxml = null;
		// Enable validating configuration in parser with an xsd.
        try {
        	//Validate the document and report validity errors. 
            myParser.setFeature("http://xml.org/sax/features/validation", true);
            //Turn on XML Schema validation by inserting XML Schema 
            // validator in the pipeline.  
            myParser.setFeature("http://apache.org/xml/features/validation/schema",true);           
            //Set the external schema location.
           	myParser.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation","dpapplist "+PropertyLoader.getConfig("applist.xsd.url")); 
            myParser.setErrorHandler(myErrorHandler);            
        } catch (SAXNotRecognizedException e) {
			logger.error("Unrecognized propertyt\r\n"+e.getMessage());
            result = false;
            throw new Exception(e);
        } catch (SAXNotSupportedException e) {
			logger.error("Unsupported propertyt\r\n"+e.getMessage());
            result = false;
            throw new Exception(e);
        }   
        // Parse an XML document
        try {
        	java.net.URL applistxmlurl = new java.net.URL(PropertyLoader.getConfig("lapplist.xml.url"));
			applistxml = applistxmlurl.openStream();			        	
            myParser.parse(new org.xml.sax.InputSource(applistxml));
            myDocument = myParser.getDocument();
        } catch (IOException ie) {
			logger.error("Could not read file\r\n"+ie.getMessage());
            result = false;
            throw new Exception(ie);
        } catch (SAXException e) {
            logger.error("Could not create Document\r\n"+e.getMessage());
          	result = false;
            throw new Exception(e);
        } finally {
        	if (applistxml != null) applistxml.close();
        }		
		logger.info("validateSchemaLocalApplist():Local Applist XML Schema Validation Result is " + result);
        return result;
    }
	/**
	 * Validate local LDAP XML Schema 
	 * @return boolean
	 * @throws Exception
	 */
    public static boolean validateSchemaLocalLDAP() throws Exception {        
        XsdErrorHandler myErrorHandler = new XsdErrorHandler();        
        DOMParser myParser = new DOMParser(); //Dom Parser as XML Handler
        Document myDocument = null;
        boolean result = false;
        InputStream applistxml = null;
        // Enable validating configuration in parser with an xsd.
        try {
			//Validate the document and report validity errors. 
            myParser.setFeature("http://xml.org/sax/features/validation",true);
            //Turn on XML Schema validation by inserting XML Schema 
            // validator in the pipeline.  
            myParser.setFeature("http://apache.org/xml/features/validation/schema",true);           
            //Set the external schema location.
         	myParser.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation","dpuser " + PropertyLoader.getConfig("lldschema.xsd.url")); 
            myParser.setErrorHandler(myErrorHandler);            
        } catch (SAXNotRecognizedException e) {
			logger.error("Unrecognized property\r\n",e);
            result = false;
            throw new Exception(e);
        } catch (SAXNotSupportedException e) {
			logger.error("Unsupported property\r\n",e);
            result = false;
            throw new Exception(e);
        }   
        // Validate XML file by Parsing document 
        try {
			java.net.URL applistxmlurl = new java.net.URL(PropertyLoader.getConfig("lldschema.xml.url"));
			applistxml = applistxmlurl.openStream();			        	
            myParser.parse(new org.xml.sax.InputSource(applistxml));
            myDocument = myParser.getDocument();
       	} catch (IOException ie) {
            logger.error("Could not read file.",ie);
            result = false;
            throw new Exception(ie);
        } catch (SAXException e) {
			logger.error("Could not create Document",e);
            System.out.println(e.getMessage());
	        result = false;
            throw new Exception(e);
        } finally{
        	if (applistxml != null) applistxml.close();
        }		
		logger.info("validateSchemaLocalLDAP():Local LDAP XML Schema Validation result is " + result);
        return result;
    }
  
}//End Class