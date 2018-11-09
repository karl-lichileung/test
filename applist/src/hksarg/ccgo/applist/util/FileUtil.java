package hksarg.ccgo.applist.util;

import java.io.InputStream;

import org.apache.log4j.Logger;

import hksarg.ccgo.applist.config.PropertyLoader;
import hksarg.ccgo.applist.constant.Constant;

public class FileUtil {

	static Logger logger = Logger.getLogger(FileUtil.class);
	
	public static String getFileData(String fileParam, String param1, String param2, String matchString) throws Exception {
	InputStream osdpmappingxml = null;
	try
	{
		java.net.URL osdpmappingxmlurl = new java.net.URL(PropertyLoader.getConfig(fileParam)); //"osdpmapping.xml.url"
		osdpmappingxml = osdpmappingxmlurl.openStream();		
		javax.xml.parsers.DocumentBuilderFactory osdpdomFactory = javax.xml.parsers.DocumentBuilderFactory.newInstance();		
		javax.xml.parsers.DocumentBuilder mappingDB = osdpdomFactory.newDocumentBuilder();
		org.w3c.dom.Document mappingDom = mappingDB.parse(osdpmappingxml);		
		org.w3c.dom.Element mappingRoot = mappingDom.getDocumentElement();
		org.w3c.dom.NodeList mappingNodeList = mappingRoot.getChildNodes();
		org.w3c.dom.NodeList childMappingNodeList;
	   	org.w3c.dom.Node tempMappingNode, tempChildMappingNode; 
	   	
		//Read 1st Level NodeList
		for (int j=0; j<mappingNodeList.getLength(); j++) {
			tempMappingNode = mappingNodeList.item(j);				
			//Handle only service provider Element
	        if (tempMappingNode.getNodeName().equalsIgnoreCase(param1)) {  // Constant.spElement
				//Find the matched sp for mapping
	        	if (tempMappingNode.hasChildNodes() && 
						tempMappingNode.getFirstChild().getNodeValue().equalsIgnoreCase(matchString)) { // (String)ht_applist.get(Constant.abbrElement))
					//Read 2st Level NodeList
	                childMappingNodeList = tempMappingNode.getChildNodes();					
	 		        for (int k=0; k<childMappingNodeList.getLength(); k++) {
						// Read each XML Node in 2st Level NodeList
	                    tempChildMappingNode = childMappingNodeList.item(k);
	                    //Find the EntityID Element
	                    if (tempChildMappingNode.getNodeName().equalsIgnoreCase(param2))
	                    	return tempChildMappingNode.getFirstChild().getNodeValue();
	                    	// ht_applist.put(param1, tempChildMappingNode.getFirstChild().getNodeValue());
	                    }
	        	}
	        }
		}
	}catch (Exception e) {
		logger.error("[OSDP Mapping] " + e.getMessage(), e);
		throw e;
	}finally{
		if ( osdpmappingxml != null ) osdpmappingxml.close();
	}
	return "NOT FOUND";
	}
}

