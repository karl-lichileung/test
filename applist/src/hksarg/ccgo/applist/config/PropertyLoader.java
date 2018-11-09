package hksarg.ccgo.applist.config;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
/**
 * DP Application List Gerneration Program
 * @author CCGO Development Team
 * @version 2.1.1 create on 2004/12/19
 */
public class PropertyLoader{
		
	//log4j
	static Logger logger = Logger.getLogger(PropertyLoader.class.getName());		
	
	private static ResourceBundle config;	
	private static ResourceBundle reportLayout;	
	public static final String configFileName = "applist_config";	
	static{		
		logger.debug("Loading " + configFileName + ".properties");			
		try{						
			config = ResourceBundle.getBundle(configFileName);			
		}catch(MissingResourceException e){			
			logger.error(configFileName + ".properties is Missing!");			
		}		
	}
	/**
	 * Check Debug Mode parameter
	 * @return boolean
	 */
	public static boolean isDebugMode(){		
		String debug_mode = PropertyLoader.getConfig("applist.app.debug");		
		if(debug_mode.equalsIgnoreCase("true")) 
			return true;
		else
			return false;
	}	
	/**
	 * Get String Parameter
	 * @param key
	 * @return Parameter (String)
	 */
	public static String getConfig(String key){		
		return config.getString(key);
	}
	/**
	 * Get Integer Parameter
	 * @param key
	 * @return integer
	 */
	public static int getConfigIntVal(String key){		
		int value = 1;		
		try{			
			value = Integer.parseInt(config.getString(key));			
		} catch(NumberFormatException e){			
			logger.error("Integer expected: "+key+" in " + configFileName + ".properties");
		}		
		return value;
	}	
}