package hksarg.ccgo.applist.util;

import org.apache.log4j.Logger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
/**
 * DP Application List Gerneration Program
 * version 2.0 on 2004/03/13
 * @author CCGO Development Team
 * @version 2.1.1 create on 2006/1/5
 */
public class XsdErrorHandler implements ErrorHandler {
	
	static Logger logger = Logger.getLogger(XsdErrorHandler.class.getName()); //log4j	
    /**
     * Enrich Exception Description
     * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
     */
    public void warning(SAXParseException anException) throws SAXException {    	
        System.out.println("[warning] " + anException);
        logger.error("[warning] " + anException);        
        throw new SAXException(  anException );
    }
    /**
     * Enrich Exception Description
     * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
     */
    public void error(SAXParseException anException) throws SAXException {
        System.out.println("[error] " + anException);
        anException.printStackTrace();
        logger.error("[error] " + anException);
        throw new SAXException(  anException );
    }
    /**
     * Enrich Exception Description
     * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
     */
    public void fatalError(SAXParseException anException) throws SAXException {        
        System.out.println("[fatal error] " + anException);
        logger.error("[fatal error] " + anException);        
        throw new SAXException(  anException );        
    }
}
