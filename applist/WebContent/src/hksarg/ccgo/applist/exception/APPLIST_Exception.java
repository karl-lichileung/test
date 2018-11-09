package hksarg.ccgo.applist.exception;

import java.util.Vector;

/**
 * This exception is the base class for all the web runtime exceptions.
 */
public class APPLIST_Exception extends RuntimeException
    implements java.io.Serializable {

    private Throwable t;
    private String code;
    private String info; 
    private Vector params;

    public APPLIST_Exception(String code) {
        super(code);
        this.code = code;
    }
    
    public APPLIST_Exception(String code, String info){
    	super(code + ":" + info);
    	this.code = code;
    	this.info = info;
    }

    public APPLIST_Exception(String code, Throwable t) {
        super(code);
        this.code = code;
        this.t = t;
    }
    
    public APPLIST_Exception(String code, Vector parameters){
    	super(code);
    	this.code = code;
    	this.params = parameters;
    }

    public String getThrowable() {
        return ("Received throwable with Message: "+ t.getMessage());
    }
    
    public String getExpCode(){
    	return code;
    }
    
    public String getExpInfo(){
    	return info;
    }
    
    public String getExpMessage(String code){
    	    	    	     	
    	return code;
    }
    
    public Vector getParams(){
    	
    	return params;
    }
}
