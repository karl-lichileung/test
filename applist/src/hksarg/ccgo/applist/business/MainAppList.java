package hksarg.ccgo.applist.business;

import hksarg.ccgo.applist.config.PropertyLoader;
import hksarg.ccgo.applist.constant.Constant;
import hksarg.ccgo.applist.util.FileUtil;
import hksarg.ccgo.applist.util.Util;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;
/**
 * DP Application List Generation Program
 * version 3.5
 * @author CCGO Development Team
 * @version 3.5 created on 2015/03/16  -L327 bugfix. L385 & L391 modified, using PropertyLoader.getConfig("idp.dept") for IDP entityID retrieval for SAML2 Intersite link construction. 
 */
public class MainAppList {
	
	// Global Variables
	static Logger logger = Logger.getLogger(MainAppList.class); //log4j	
		
	final String solution_provider = PropertyLoader.getConfig("applist.solution.provider"); // osdp //	
	static java.net.URL applistxmlurl = null; // remote applist
	static java.net.URL lapplistxmlurl = null; // local applist
	static java.net.URL ldschemaxmlurl = null; // remote ldschema
	static java.net.URL lldschemaxmlurl = null; // local ldschema
	static String[] conAttName = null; // array to store the ldschema attributes 
	static String[] conAttType = null; // array to store the ldschema attributes type 
	boolean canDisplay = false; // only show the application while the canDisplay is true
	static String serverName; // use for store IDP server name with port !!! karl
	static Hashtable ht = null; // user for store header information
	boolean isRemoteAccess = false; 
	/**
	 * Constructor
	 * @param s_serverName
	 * @param s_ht
	 */
	//public MainAppList(String s_serverName, Hashtable s_ht) {
	public MainAppList(Hashtable s_ht,String baseURL_h,int port_h) {
		//serverName = s_serverName;		// set Server Name
		
		logger.info("baseURL="+baseURL_h);
		//serverName = baseURL_h;
		isRemoteAccess = baseURL_h.indexOf("ccgo")>=0?true:false;
		
		serverName = PropertyLoader.getConfig("idp.name");
		
		/*
		serverName = PropertyLoader.getConfig("dp.dns.name");  // dp.osdp.ccgo.hksarg
		
		serverName+=":8443"; //Standard port of dprp and dpip agent
		if(baseURL_h!=null){
			String serverNamewithoutport  = serverName.substring(0, serverName.length()-5);
			if(!baseURL_h.equalsIgnoreCase(serverNamewithoutport)& port_h!=9443){ 
				//all port will handle by dprp, except the port detected is dpep agent. 
				serverName=serverName.replaceAll(".ccgo.", ".");}
		}
		*/
		
		ht = s_ht;						// set request parameter into hashtable
		Enumeration enumber = s_ht.keys();	
		logger.debug("[Header Value Loading Start]");	
		while (enumber.hasMoreElements()){
			String key = (String)enumber.nextElement();
			String value = (String)s_ht.get(key);
			logger.debug("[Header Name]"+key+"-[Value]"+value);
		}
		logger.debug("[Header Value Loading End]");		
	}//End Method
	/**
	 * Main Method to write Remote Applist HTML Table
	 * @param s_user_for
	 * @param site
	 * @return Applist in Vector
	 * @throws Exception
	 */
	public Vector getRemoteAppList(String s_user_for, String site) throws Exception{
		Vector result_list = null;
		try {
			if (site.equalsIgnoreCase("production")) {
				setldschema( PropertyLoader.getConfig("ldschema.xml.url") );
				result_list = constructApplist(PropertyLoader.getConfig("applist.xml.url"), s_user_for);
			} else if (site.equalsIgnoreCase("pre-production")){
				setldschema( PropertyLoader.getConfig("ldschema.xml.url") );
				result_list = constructApplist(PropertyLoader.getConfig("ppapplist.xml.url"), s_user_for);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(":getRemoteAppList" + e.getMessage(), e);
			throw new Exception(e.getMessage());
		}
		return result_list;
	}
	/**
	 * Main Method to write Local Applist HTML Table
	 * @param s_user_for
	 * @return
	 * @throws Exception
	 */
	public Vector getLocalAppList(String s_user_for) throws Exception{
		Vector result_list = null;
		try {					
			setldschema( PropertyLoader.getConfig("ldschema.xml.url") );
			setlldschema( PropertyLoader.getConfig("lldschema.xml.url") );						
			result_list = constructApplist(PropertyLoader.getConfig("lapplist.xml.url"), s_user_for);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(":getLocalAppList" + e.getMessage(), e);
			throw new Exception(e.getMessage());
		}			
		return result_list;		
	}	
	/**
	 * Initail Central LDAP Schema for XML Parser to handle 
	 * @param ldap_schema_url
	 * @throws Exception
	 */
	public void setldschema(String ldap_schema_url) throws Exception{		
		java.io.InputStream ldschemaxml = null;		
		try {
			ldschemaxmlurl = new java.net.URL(ldap_schema_url);		    
			ldschemaxml = ldschemaxmlurl.openStream();			
			javax.xml.parsers.DocumentBuilderFactory ldschemaDomFactory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
			javax.xml.parsers.DocumentBuilder ldschemaDB = ldschemaDomFactory.newDocumentBuilder();
			org.w3c.dom.Document ldschemaDom = ldschemaDB.parse(ldschemaxml);
			org.w3c.dom.Element ldschemaRootElement = ldschemaDom.getDocumentElement();
			org.w3c.dom.NodeList ldschemaNodeList = ldschemaRootElement.getChildNodes();
			org.w3c.dom.NodeList ldschemaChildNodeList;
			org.w3c.dom.Node templdschemaNode;
			// Get the number of the schema
			int schemaCount = 0;			
			for (int i=0; i<ldschemaNodeList.getLength(); i++) {
			  templdschemaNode = ldschemaNodeList.item(i);
			  if (templdschemaNode.getNodeName().startsWith(Constant.attNameElement)) schemaCount +=1;
			}
			//Dimension Attribute and Attribute Type Array  	
			conAttName = new String[schemaCount];
			conAttType = new String[schemaCount];
			//Input Attribute and Attribute Type Array Value
			int tempSchemaCount = 0;
			logger.debug("[Central LDAP schema Loading "+schemaCount+" attributes Start]");
			for (int i=0; i<ldschemaNodeList.getLength(); i++) {
				 templdschemaNode = ldschemaNodeList.item(i);		  		 	 
				 if (templdschemaNode.getNodeName().startsWith(Constant.attNameElement)) {
					conAttName[tempSchemaCount] = (templdschemaNode.getFirstChild().getNodeValue()).toUpperCase();				    
					conAttType[tempSchemaCount] = ((org.w3c.dom.Element)templdschemaNode).getAttribute("vtype");
					//Print Attribute and Attribute Type Array Information
					logger.debug("[Central LDAP schema Name]"+conAttName[tempSchemaCount]+" [Type] "+conAttType[tempSchemaCount]);
					tempSchemaCount +=1;				    
				 }
			}
			logger.debug("[Central LDAP schema Loading End]["+tempSchemaCount+" Elements Added");
		}catch(MalformedURLException e){
			e.printStackTrace();
			logger.error("[Central LDAP schema Loading]"+e.getMessage(), e);
			throw new Exception(e.getMessage());
		}catch(org.xml.sax.SAXParseException spe){
			logger.error("\n[Central LDAP schema Loading]** Parsing error, line " + spe.getLineNumber() + ", uri " + spe.getSystemId());
			logger.error("       " + spe.getMessage());
			throw new Exception(spe.getMessage());		
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("[Central LDAP schema Loading]"+ex.getMessage(), ex);
			throw new Exception(ex.getMessage());
		}finally{
			if ( ldschemaxml != null ) ldschemaxml.close();
		}		
	}
	/**
	 * Initail Schema for XML Parser to handle Local Applist
	 * @param lldap_schema_url
	 * @throws Exception
	 */
	public void setlldschema(String lldap_schema_url) throws Exception{		
		java.io.InputStream lldschemaxml = null;		
		try {			
			// Util.validateSchemaLocalLDAP(); // Validate the local ldap schema			
			lldschemaxmlurl = new java.net.URL(lldap_schema_url);		    
		    lldschemaxml = lldschemaxmlurl.openStream();		
			javax.xml.parsers.DocumentBuilderFactory lldschemaDomFactory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
			javax.xml.parsers.DocumentBuilder lldschemaDB = lldschemaDomFactory.newDocumentBuilder();
			org.w3c.dom.Document lldschemaDom = lldschemaDB.parse(lldschemaxml);
			org.w3c.dom.Element lldschemaRootElement = lldschemaDom.getDocumentElement();
			org.w3c.dom.NodeList lldschemaNodeList = lldschemaRootElement.getChildNodes();
		  	org.w3c.dom.NodeList lldschemaChildNodeList;
			org.w3c.dom.Node templldschemaNode;
			// Get the number of the schema
			int schemaCount = 0;		
			for (int i=0; i<lldschemaNodeList.getLength(); i++) {
		      templldschemaNode = lldschemaNodeList.item(i);
			  if (templldschemaNode.getNodeName().startsWith(Constant.attNameElement)) schemaCount +=1;
			}
			// Dimension new Attribute and Attribute Type Array  	
		  	String[] tmp_conAttName = new String[conAttName.length + schemaCount];
		  	String[] tmp_conAttType = new String[conAttType.length + schemaCount];
		  	// Reload old Attribute and Attribute Type to Temp Array  
		  	for (int i=0; i<conAttName.length; i++){
		  		tmp_conAttName[i] = conAttName[i];
		  		tmp_conAttType[i] = conAttType[i];
		  	}
			//Input Attribute and Attribute Type Array Value
			int tempSchemaCount = conAttName.length;
			logger.debug("[Local LDAP schema Loading Start]");
		    for (int i=0; i<lldschemaNodeList.getLength(); i++) {
		    	templldschemaNode = lldschemaNodeList.item(i);
		        if (templldschemaNode.getNodeName().startsWith(Constant.attNameElement)) {		         	
		        	if ( templldschemaNode.getFirstChild() == null ) throw new Exception("xml element called attibute_name not allow empty string");
		            tmp_conAttName[tempSchemaCount] = templldschemaNode.getFirstChild().getNodeValue();
				    tmp_conAttType[tempSchemaCount] = ((org.w3c.dom.Element)templldschemaNode).getAttribute("vtype");				    
					logger.debug("[Local LDAP schema Name] "+tmp_conAttName[tempSchemaCount]+" [Type] "+tmp_conAttType[tempSchemaCount]);
				    tempSchemaCount +=1;				    
		   		 }
			}
			if (tempSchemaCount == conAttName.length) {
				logger.debug("No Local LDAP Schema Attribute Added !");
			} else {
			// Reload Attribute and Attribute Type to Array  
				conAttName = new String[tempSchemaCount];
				conAttType = new String[tempSchemaCount];
				for (int i=0; i<conAttName.length; i++){
					conAttName[i] = tmp_conAttName[i];
					conAttType[i] = tmp_conAttType[i];
				}				 
			}
			logger.debug("[Local LDAP schema Loading End][Total "+tempSchemaCount+" Attriutes in Array]");
		} catch(MalformedURLException e){
			e.printStackTrace();			
			logger.error("[Local LDAP schema Loading]"+e.getMessage(), e);
			throw new Exception(e.getMessage());
		} catch(org.xml.sax.SAXParseException spe){
			logger.error("\n[Local LDAP schema Loading]** Parsing error, line " + spe.getLineNumber() + ", uri " + spe.getSystemId());
			logger.error("       " + spe.getMessage());
			throw new Exception(spe.getMessage());				
		} catch(Exception ex){
			ex.printStackTrace();
			logger.error("[Local LDAP schema Loading]"+ex.getMessage(), ex);
			throw new Exception(ex.getMessage());
		} finally {
			if ( lldschemaxml != null ) lldschemaxml.close();
		}
	}
	/**
	 * Construct Applist XML 
	 * @param applist_url
	 * @param s_user_for
	 * @return Vector of Applist
	 * @throws Exception
	 */
	public Vector constructApplist(String applist_url, String s_user_for) throws Exception{
		Vector result = new Vector();		
		String START_IN_NEW_WIN = "";
		String PROTECT_EXT = "";
		Hashtable ht_applist  = null;
		InputStream applistxml = null;		
		try{
			// if ( s_user_for.equalsIgnoreCase("local") )	Util.validateSchemaLocalApplist();
			applistxmlurl = new java.net.URL(applist_url);
			applistxml = applistxmlurl.openStream();		
			javax.xml.parsers.DocumentBuilderFactory domFactory = javax.xml.parsers.DocumentBuilderFactory.newInstance();		
			javax.xml.parsers.DocumentBuilder applistDB = domFactory.newDocumentBuilder();
			org.w3c.dom.Document applDom = applistDB.parse(applistxml);		
			org.w3c.dom.Element rootElement = applDom.getDocumentElement();
			org.w3c.dom.NodeList appNodeList = rootElement.getChildNodes();
			org.w3c.dom.NodeList childAppNodeList;
		   	org.w3c.dom.Node tempAppNode, tempChildAppNode, node;
			org.w3c.dom.NamedNodeMap namedNodeMap;	    
			//Check Purpose of Applist XML match with expected one
			String userFor = rootElement.getAttribute("usedfor");				
			if (!userFor.equalsIgnoreCase(s_user_for)) {						
				if (logger.isDebugEnabled()) logger.debug("Error: Element <userfor> in Applist XML is should be changed to <" + s_user_for+">.");
				throw new Exception(s_user_for+" Applist attribute userfor is not config for  " + userFor);
			}
			//Read 1st Level NodeList
			int applicationCount = 1;
			for (int i=0; i<appNodeList.getLength(); i++) {
			    canDisplay = false;
				tempAppNode = appNodeList.item(i);				
				//Handle only Application Element
		        if (tempAppNode.getNodeName().equalsIgnoreCase(Constant.appElement)) { // dp_app
					if (tempAppNode.hasChildNodes()) {
						//Read 2st Level NodeList
		                childAppNodeList = tempAppNode.getChildNodes();
						String[] buffer = new String[childAppNodeList.getLength()];						
						ht_applist = new Hashtable();
						logger.debug(s_user_for.toUpperCase()+" Application["+applicationCount+"] elements Loading Start");		  		       
		 		        for (int j=0; j<childAppNodeList.getLength(); j++) {
							// Break the checking to ignore conditioning
							//if (canDisplay) break;	//Required conditioning for different saml definition for new version applist
							// Read each XML Node in 2st Level NodeList
		                    tempChildAppNode = childAppNodeList.item(j);
		                    if (tempChildAppNode.getNodeName().equalsIgnoreCase(Constant.protectExtElement))
							    PROTECT_EXT = tempChildAppNode.getFirstChild().getNodeValue();
		                    else if (tempChildAppNode.getNodeName().equalsIgnoreCase(Constant.descElement))
		                    	ht_applist.put(Constant.descElement, tempChildAppNode.getFirstChild().getNodeValue()); 
		                    else if (tempChildAppNode.getNodeName().equalsIgnoreCase(Constant.urlElement))
		                    	ht_applist.put(Constant.urlElement, tempChildAppNode.getFirstChild().getNodeValue());	
		                    else if (tempChildAppNode.getNodeName().equalsIgnoreCase(Constant.destination))
		                    	ht_applist.put(Constant.destination, tempChildAppNode.getFirstChild().getNodeValue());
		                    else if (tempChildAppNode.getNodeName().equalsIgnoreCase(Constant.abbrElement))
		                        ht_applist.put(Constant.abbrElement, tempChildAppNode.getFirstChild().getNodeValue());
		                    else if (tempChildAppNode.getNodeName().equalsIgnoreCase(Constant.integrationElement)){
		                    	//Assign default value of integration type to avoid compilation error with old xml
		                    	ht_applist.put(Constant.integrationElement, tempChildAppNode.getFirstChild().getNodeValue());
		                    }else if (tempChildAppNode.getNodeName().equalsIgnoreCase(Constant.visualforElement)){ //visual for = all, display without checking
								ht_applist.put(Constant.visualforElement, tempChildAppNode.getFirstChild().getNodeValue());
								//Assign default value of saml and integration to avoid compilation error with old xml
								ht_applist.put(Constant.samlElement, Constant.saml_default);
								//ht_applist.put(Constant.samlElement, tempChildAppNode.getFirstChild().getNodeValue());
								if (((String)ht_applist.get(Constant.visualforElement)).equalsIgnoreCase(Constant.visualforAll))
									canDisplay = true;
		  					}else if (tempChildAppNode.getNodeName().equalsIgnoreCase(Constant.conditionElement)){								
								ht_applist.put(Constant.conditionElement, tempChildAppNode.getFirstChild().getNodeValue());			
								
								String[] condArray = ((String)ht_applist.get(Constant.conditionElement)).split(Constant.andCondition);							 	    
								logger.debug("[Applist] The condition: " + (String)ht_applist.get(Constant.conditionElement));
								//Visual Condition Logic
								if (checkVisualCondition(condArray)){
									canDisplay = true;
									//Get saml element value from xml
									if (tempChildAppNode.getFirstChild().getNextSibling() != null)
										if (tempChildAppNode.getFirstChild().getNextSibling().getNodeName().equalsIgnoreCase(Constant.samlElement))
											ht_applist.put(Constant.samlElement, tempChildAppNode.getFirstChild().getNextSibling().getFirstChild().getNodeValue());
								}//End Visual Condition Logic
								
							}//End Node Type Condition
							if (tempChildAppNode.getNodeName().startsWith("dp")) logger.debug("["+tempChildAppNode.getNodeName()+"] values ["+tempChildAppNode.getFirstChild().getNodeValue()+"] readed");
						}//End Node List Looping		
						if (canDisplay) {  //ONLY DISPLAY while the condition is passed 
                        	logger.debug("Application["+(String)ht_applist.get(Constant.descElement)+"] can be displayed."); 
                        	String saml = (String)ht_applist.get(Constant.samlElement);
                        	String relayState = (String)ht_applist.get(Constant.urlElement);
                        	if ("local".equalsIgnoreCase(s_user_for) && isRemoteAccess) {
                        		relayState = addccgoToURL(relayState);
                        	}
                        	String str = null;
                        	/*
						   	if ("crossdomain".equalsIgnoreCase(s_user_for)) { // validating the remote applist
						   	*/
						   	//Only display the link if the link is link to another dept <<old checking have issued - indexOf("dp."+PropertyLoader.getConfig("dp.dept.id")+".ccgo.hksarg")>>
						    	// if ((relayState).indexOf(PropertyLoader.getConfig("dp.dns.name")) == -1||PROTECT_EXT.equalsIgnoreCase("true") ){				
								//**** OSDP's Solution	//
								if ("OSDP".equalsIgnoreCase(solution_provider)) {
									if (saml.equalsIgnoreCase(Constant.saml_1))
										str = new String("<tr class='applistRow'><td class='appProvider'>" + (String)ht_applist.get(Constant.abbrElement) + "</td><td class='appName'><a href=\"javascript:launchApp('https://" + serverName + "/sso/SAMLAwareServlet?TARGET="+(String)ht_applist.get(Constant.urlElement) + "&NameIDFormat=" + URLEncoder.encode("urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified", "UTF-8") + "')\">"+(String)ht_applist.get(Constant.descElement)+"</a></td></tr>");
									else if (saml.equalsIgnoreCase(Constant.saml_2)){

										// Get the osdp mapping for entityID
										ht_applist.put(Constant.entityIDElement, FileUtil.getFileData("osdpmapping.xml.url",Constant.spElement,Constant.entityIDElement,(String)ht_applist.get(Constant.abbrElement)));
										
										// Get the OSDP mapping for RelayState vs Assertion Consumption Service
										ht_applist.put(Constant.acsElement, FileUtil.getFileData("acsmapping.xml.url",Constant.relayElement,Constant.acsElement,relayState));
/*	
										-- from Kai's Shibboleth Installatin Guide
										https://idp.osdp1.hksarg:8443/idp/profile/SAML2/Unsolicited/SSO?
										providerId=https%3A%2F%2Fsp.osdp1.hksarg%2FShibboleth.sso%2FMetadata&
										target=https%3A%2F%2Fsp.osdp1.hksarg%2F
										secure to test the single sign-on functionality.
										&shire=¡¨Artifact consumer url¡¨
*/	
									    if ( s_user_for.equalsIgnoreCase("local") ) {
										if (!isRemoteAccess) {
											// local user accessing local application
											str = new String("<tr class='applistRow'><td class='appProvider'>" + (String)ht_applist.get(Constant.abbrElement) + "</td><td class='appName'>"
											+ "<a href=\"javascript:launchApp('https://" + serverName + "/idp/profile/SAML2/Unsolicited/SSO"+ 
											"?providerId=" + URLEncoder.encode((String)ht_applist.get(Constant.entityIDElement),java.nio.charset.StandardCharsets.UTF_8.toString()) + 
											"&target="+ URLEncoder.encode(relayState,java.nio.charset.StandardCharsets.UTF_8.toString()) + 
											"&shire=" + URLEncoder.encode((String)ht_applist.get(Constant.acsElement),java.nio.charset.StandardCharsets.UTF_8.toString()) + "')\">"
											+(String)ht_applist.get(Constant.descElement)+"</a></td></tr>");
										} else {
											// remote user accessing local application
											str = new String("<tr class='applistRow'><td class='appProvider'>" + (String)ht_applist.get(Constant.abbrElement) + "</td><td class='appName'>"
											+ "<a href=\"javascript:launchApp('https://" + serverName + "/idp/profile/SAML2/Unsolicited/SSO"+ 
											"?providerId=" + URLEncoder.encode((String)ht_applist.get(Constant.entityIDElement),java.nio.charset.StandardCharsets.UTF_8.toString()) + 
											"&target="+ URLEncoder.encode(relayState,java.nio.charset.StandardCharsets.UTF_8.toString()) + 
											"&shire=" + URLEncoder.encode((String)ht_applist.get(Constant.acsElement),java.nio.charset.StandardCharsets.UTF_8.toString()) + "')\">"
											+(String)ht_applist.get(Constant.descElement)
											+"</a></td></tr>");
										}
									    } else {
									    	// accessing remote applist, all URL should contain ccgo
											str = new String("<tr class='applistRow'><td class='appProvider'>" + (String)ht_applist.get(Constant.abbrElement) + "</td><td class='appName'>"
											+ "<a href=\"javascript:launchApp('https://" + serverName + "/idp/profile/SAML2/Unsolicited/SSO"+ 
											"?providerId=" + URLEncoder.encode((String)ht_applist.get(Constant.entityIDElement),java.nio.charset.StandardCharsets.UTF_8.toString()) + 
											"&target="+ URLEncoder.encode((String)ht_applist.get(Constant.urlElement),java.nio.charset.StandardCharsets.UTF_8.toString()) + 
											"')\">"
											+(String)ht_applist.get(Constant.descElement)+"</a></td></tr>");
									    }
	    
									}
								result.add(str);
							}
						}//End Display Bracket

						logger.debug(s_user_for.toUpperCase()+" Applistion["+applicationCount+"] elements Loading End");
						applicationCount++;
					}// End If Have Child Node
		    	}//End Application NodeList		       
			}//End 1st Level Node Looping		     
		}catch(MalformedURLException e){
			e.printStackTrace();			
			logger.error("[Applist] " + e.getMessage(), e);
			throw new Exception(e.getMessage());
		}catch(org.xml.sax.SAXParseException spe){
			logger.error("\n[Applist]** Parsing error, line " + spe.getLineNumber() + ", uri " + spe.getSystemId());
			logger.error("       " + spe.getMessage());
			throw new Exception(spe.getMessage());				
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("[Applist] " + e.getMessage(), e);
			throw new Exception(e.getMessage());
		}finally{
			if ( applistxml != null ) applistxml.close();
		}
		return result;
	}//End Method
	
	/**
	 * Visual Condition Checking
	 * @param condArray
	 * @return boolean
	 * @throws Exception
	 */
	private boolean checkVisualCondition(final String[] condArray) throws Exception {
		//boolean isDisplay = true ;   // remarked by Leo YEUNG for testing
		boolean isDisplay = false ;
		int CondPassCount = 0; //Use to count the number of Boolean expressions passed						    
		int CondAmount = condArray.length;//Total number of Boolean expressions in this Node
		//Loop for all Boolean expressions in one conditon			                    
		for (int k=0; k<CondAmount; k++) {		 					
			if (CondPassCount<k) break;	 //Exit the following condition check while one of the Boolean expresssion is failure which save the loop time
			//Loop for sort out different Attribute
			for (int m=0; m<conAttName.length; m++) {
				int previousCondPassCount = CondPassCount;//a value use to check the Boolean expresssion is failure
				if (condArray[k].toUpperCase().startsWith(conAttName[m].toUpperCase())){ 
					final String tempConAttType = conAttType[m]; // ldap attribute type
					final String tempValue = condArray[k].substring(conAttName[m].length());
					final int tempColonIndex = condArray[k].indexOf("\"");
					final String attName = condArray[k].substring(0,conAttName[m].length());  	 			 		 				 //Attribute Name
					final String attCond = condArray[k].substring(conAttName[m].length(),tempColonIndex); //Attribute condition syntax
					final String attVal = condArray[k].substring(tempColonIndex+1,condArray[k].lastIndexOf("\""));//Attribute Value
					int k2 = k+1;
					logger.debug("Boolean expression " + k2 + " checking for " + condArray[k]+" Start");
					//Condition Greater Than
					if (attCond.equalsIgnoreCase(Constant.gt)){ 
						if (ht.get(attName.toUpperCase()) != null){
							int intCrtUserInfo;
							int intAttVal;
							String strCrtUserInfo;
							String strAttVal;														
							if (tempConAttType.equalsIgnoreCase("cis")){
								strCrtUserInfo = (String)ht.get(attName.toUpperCase());
								strAttVal = attVal;
								if (strCrtUserInfo.compareTo(strAttVal) > 0 ){
									CondPassCount += 1;
									logger.debug("Boolean expression " + k2 + " OK: " + attName + "- " + (String)ht.get(attName) + attCond + attVal);
								} else {
									logger.debug("Boolean expression " + k2 + " failed: " + attName + "- " + (String)ht.get(attName) + attCond + attVal + " failed!!");
								} 																										 	    		
							} else if(tempConAttType.equalsIgnoreCase("int")){
								try{
									intCrtUserInfo = Integer.parseInt((String)ht.get(attName.toUpperCase()));
									intAttVal = Integer.parseInt(attVal);
								}catch (NumberFormatException e){
									logger.error("checkVisualCondition:" + e.getMessage(), e);
									logger.error("Invalid attribute value: The attribute value is not numeric, the <Greater Than> condition cannot apply.");
									e.printStackTrace();
									throw new Exception(e.getMessage());
								}
								if (intCrtUserInfo > intAttVal){
									CondPassCount += 1;
									logger.debug("Boolean expression " + k2 + " OK: " + attName + "- " + (String)ht.get(attName) + attCond + attVal);
								} else {
									logger.debug("Boolean expression " + k2 + " failed: " + attName + "- " + (String)ht.get(attName) + attCond + attVal + " failed!!");
								} 															
							}													
						}
					//Condition Less Than
					} else if (attCond.equalsIgnoreCase(Constant.lt)){
						if (ht.get(attName.toUpperCase()) != null) {
							int intCrtUserInfo;
							int intAttVal;
							String strCrtUserInfo;
							String strAttVal;											 	    	
							if (tempConAttType.equalsIgnoreCase("cis")){
								strCrtUserInfo = (String)ht.get(attName.toUpperCase());
								strAttVal = attVal;
								if (strCrtUserInfo.compareTo(strAttVal) < 0 ){
									CondPassCount += 1;
									logger.debug("Boolean expression " + k2 + " OK: " + attName + "- " + (String)ht.get(attName) + attCond + attVal);
								} else {
									logger.debug("Boolean expression " + k2 + " failed: " + attName + "- " + (String)ht.get(attName) + attCond + attVal + " failed!!");																										 	    		
								}
							}else if(tempConAttType.equalsIgnoreCase("int")){
								try{
									intCrtUserInfo = Integer.parseInt((String)ht.get(attName.toUpperCase()));
									intAttVal = Integer.parseInt(attVal);
								}catch (NumberFormatException e){
									logger.error("checkVisualCondition:" + e.getMessage(), e);
									logger.error("Invalid attribute value: The attribute value is not numeric, the <Less than> condition cannot apply.");
									e.printStackTrace();
									throw new Exception(e.getMessage());
								}
								if (intCrtUserInfo < intAttVal){
									CondPassCount += 1;
									logger.debug("Boolean expression " + k2 + " OK: " + attName + "- " + (String)ht.get(attName) + attCond + attVal);
								} else {
									logger.debug("Boolean expression " + k2 + " failed: " + attName + "- " + (String)ht.get(attName) + attCond + attVal + " failed!!");
								} 															
							}
						}
					//Condition Greater and Equal	
					} else if (attCond.equalsIgnoreCase(Constant.ge)){ 
						if (ht.get(attName.toUpperCase()) != null) {
							int intCrtUserInfo;
							int intAttVal;
							String strCrtUserInfo;
							String strAttVal;											 	    	
							if (tempConAttType.equalsIgnoreCase("cis")){
								strCrtUserInfo = (String)ht.get(attName.toUpperCase());
								strAttVal = attVal;
								if (strCrtUserInfo.compareTo(strAttVal) >= 0 ){
									CondPassCount += 1;
									logger.debug("Boolean expression " + k2 + " OK: " + attName + "- " + (String)ht.get(attName) + attCond + attVal);
								} else {
									logger.debug("Boolean expression " + k2 + " failed: " + attName + "- " + (String)ht.get(attName) + attCond + attVal + " failed!!");
								} 																										 	    		
							}else if(tempConAttType.equalsIgnoreCase("int")){
								try{
									intCrtUserInfo = Integer.parseInt((String)ht.get(attName.toUpperCase()));
									intAttVal = Integer.parseInt(attVal);
								} catch (NumberFormatException e){
									logger.error("checkVisualCondition:" + e.getMessage(), e);
									logger.error("Invalid attribute value: The attribute value is not numeric, the <Greater and Equal to> condition cannot apply.");
									e.printStackTrace();
									throw new Exception(e.getMessage());
								}
								if (intCrtUserInfo >= intAttVal){
									CondPassCount += 1;
									logger.debug("Boolean expression " + k2 + " OK: " + attName + "- " + (String)ht.get(attName) + attCond + attVal);
								} else {
									logger.debug("Boolean expression " + k2 + " failed: " + attName + "- " + (String)ht.get(attName) + attCond + attVal + " failed!!");
								}			
							}
						}
					//Condition Less and Equal						
					} else if (attCond.equalsIgnoreCase(Constant.le)){ 
						if (ht.get(attName.toUpperCase()) != null) {
							int intCrtUserInfo;
							int intAttVal;
							String strCrtUserInfo;
							String strAttVal;											 	    	
							if (tempConAttType.equalsIgnoreCase("cis")){
								strCrtUserInfo = (String)ht.get(attName.toUpperCase());
								strAttVal = attVal;
								if (strCrtUserInfo.compareTo(strAttVal) <= 0 ){
									CondPassCount += 1;
									logger.debug("Boolean expression " + k2 + " OK: " + attName + "- " + (String)ht.get(attName) + attCond + attVal);
								} else {
									logger.debug("Boolean expression " + k2 + " failed: " + attName + "- " + (String)ht.get(attName) + attCond + attVal + " failed!!");
								} 																										 	    		
							}else if(tempConAttType.equalsIgnoreCase("int")){
								try{
									intCrtUserInfo = Integer.parseInt((String)ht.get(attName.toUpperCase()));
									intAttVal = Integer.parseInt(attVal);
								}catch (NumberFormatException e){
									logger.error("checkVisualCondition::" + e.getMessage(), e);
									logger.error("Invalid attribute value: The attribute value is not numeric, the <Less and Equal to> condition cannot apply.");
									e.printStackTrace();
									throw new Exception(e.getMessage());
								}
								if (intCrtUserInfo <= intAttVal){
									CondPassCount += 1;
									logger.debug("Boolean expression " + k2 + " OK: " + attName + "- " + (String)ht.get(attName) + attCond + attVal);
								} else {
									logger.debug("Boolean expression " + k2 + " failed: " + attName + "- " + (String)ht.get(attName) + attCond + attVal + " failed!!");
								}															
							}
						}
					//Condition Equal	
					} else if (attCond.equalsIgnoreCase(Constant.equal)){ //Equal
						if (ht.get(attName.toUpperCase()) != null) {
							int intCrtUserInfo;
							int intAttVal;
							String strCrtUserInfo;
							String strAttVal;											 	    	
							if (tempConAttType.equalsIgnoreCase("cis")){
								strCrtUserInfo = (String)ht.get(attName.toUpperCase());
								strAttVal = attVal;
									if (strCrtUserInfo.equalsIgnoreCase(strAttVal)){
										CondPassCount += 1;
										logger.debug("Boolean expression " + k2 + " OK: " + attName + "- " + (String)ht.get(attName) + attCond + attVal);
									} else {
										logger.debug("Boolean expression " + k2 + " failed: " + attName + "- " + (String)ht.get(attName) + attCond + attVal + " failed!!");
									}																										 	    		
							} else if(tempConAttType.equalsIgnoreCase("int")){
								try{
									intCrtUserInfo = Integer.parseInt((String)ht.get(attName.toUpperCase()));
									intAttVal = Integer.parseInt(attVal);
								}catch (NumberFormatException e){
									logger.error("checkVisualCondition:" + e.getMessage(), e);
									logger.error("Invalid attribute value: The attribute value is not numeric, the <equal to> condition cannot apply.");
									e.printStackTrace();
									throw new Exception(e.getMessage());
								}
								if (intCrtUserInfo == intAttVal){
									CondPassCount += 1;
									logger.debug("Boolean expression " + k2 + " OK: " + attName + "- " + (String)ht.get(attName) + attCond + attVal);
								} else {
									logger.debug("Boolean expression " + k2 + " failed: " + attName + "- " + (String)ht.get(attName) + attCond + attVal + " failed!!");
								}															
							}	
						}
					//Condition Not Equal to	
					} else if (attCond.equalsIgnoreCase(Constant.not_equal)){
						if (ht.get(attName.toUpperCase()) != null) {
							int intCrtUserInfo;
							int intAttVal;
							String strCrtUserInfo;
							String strAttVal;											 	    	
							if (tempConAttType.equalsIgnoreCase("cis")){
								strCrtUserInfo = (String)ht.get(attName.toUpperCase());
								strAttVal = attVal;
								if (!strCrtUserInfo.equalsIgnoreCase(strAttVal)){
									CondPassCount += 1;
									logger.debug("Boolean expression " + k2 + " OK: " + attName + "- " + (String)ht.get(attName) + attCond + attVal);
								} else {
									logger.debug("Boolean expression " + k2 + " failed: " + attName + "- " + (String)ht.get(attName) + attCond + attVal + " failed!!");
								} 																										 	    		
							} else if(tempConAttType.equalsIgnoreCase("int")){
								try{
									intCrtUserInfo = Integer.parseInt((String)ht.get(attName.toUpperCase()));
									intAttVal = Integer.parseInt(attVal);
								}catch (NumberFormatException e){
									logger.error("checkVisualCondition:" + e.getMessage(), e);
									logger.error("Invalid attribute value: The attribute value is not numeric, the <Not equal to> condition cannot apply.");
									e.printStackTrace();
									throw new Exception(e.getMessage());
								}
								if (intCrtUserInfo != intAttVal){
									CondPassCount += 1;
									logger.debug("Boolean expression " + k2 + " OK: " + attName + "- " + (String)ht.get(attName) + attCond + attVal);
								} else {
									logger.debug("Boolean expression " + k2 + " failed: " + attName + "- " + (String)ht.get(attName) + attCond + attVal + " failed!!");
								} 															
							}
						}
					//Condition Contain	
					} else if (attCond.equalsIgnoreCase(Constant.contain)){ 
						if (ht.get(attName.toUpperCase()) != null) {
							if (((String)ht.get(attName.toUpperCase())).indexOf(attVal)!=-1){
								CondPassCount += 1;
								logger.debug("Boolean expression " + k2 + " OK: " + attName + "- " + (String)ht.get(attName) + attCond + attVal);
							} else {
								logger.debug("Boolean expression " + k2 + " failed: " + attName + "- " + (String)ht.get(attName) + attCond + attVal + " failed!!");
							} 
						}
					//Condition Start with	 	 
					} else if (attCond.equalsIgnoreCase(Constant.start_with)){
						if (ht.get(attName.toUpperCase()) != null) {
							if (((String)ht.get(attName.toUpperCase())).startsWith(attVal)){
								CondPassCount += 1;
								logger.debug("Boolean expression " + k2 + " OK: " + attName + "- " + (String)ht.get(attName) + attCond + attVal);
							} else {
								logger.debug("Boolean expression " + k2 + " failed: " + attName + "- " + (String)ht.get(attName) + attCond + attVal + " failed!!");
							} 
						}
					//Condition	Not contain	
					} else if (attCond.equalsIgnoreCase(Constant.not_contain)){ 
						if (ht.get(attName.toUpperCase()) != null) {
							if (((String)ht.get(attName.toUpperCase())).indexOf(attVal)==-1){
								CondPassCount += 1;
								logger.debug("Boolean expression " + k2 + " OK: " + attName + "- " + (String)ht.get(attName) + attCond + attVal);
							} else {
								logger.debug("Boolean expression " + k2 + " failed: " + attName + "- " + (String)ht.get(attName) + attCond + attVal + " failed!!");
							} 
						}
					//Condition Not start with	
					} else if (attCond.equalsIgnoreCase(Constant.not_start_with)){
						if (!((String)ht.get(attName.toUpperCase())).startsWith(attVal)){
							CondPassCount += 1;
							logger.debug("Boolean expression " + k2 + " OK: " + attName + "- " + (String)ht.get(attName) + attCond + attVal);
						} else {
							logger.debug("Boolean expression " + k2 + " failed: " + attName + "- " + (String)ht.get(attName) + attCond + attVal + " failed!!");
						}
					//Finally, Syntax Error	 
					} else {
						logger.error("checkVisualCondition:Invalid condition syntax found: " + attCond);
					} 
				}	
				if (previousCondPassCount<CondPassCount) break; // condition passed. Exit the for loop
			}
			if (CondPassCount == CondAmount) isDisplay = true;
		}
		//logger.debug("checkVistualCondition: isDisplay = " + isDisplay);
		//logger.debug("checkVistualCondition: CondPassCount = " + CondPassCount);
		//logger.debug("checkVistualCondition: CondAmount = " + CondAmount);
		return isDisplay;
	}
	
	private String addccgoToURL(String url) {
		int pos1 = url.indexOf(".");
		int pos2 = url.indexOf(".",pos1+1);
		return url.substring(0, pos2+1) + "ccgo."+url.substring(pos2+1); 
	}
	
}// End Class