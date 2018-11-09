<%@page import="java.io.*"%>
<%@page import="java.util.*"%>
<%@page import="hksarg.ccgo.applist.business.*"%>
<%@page import="hksarg.ccgo.applist.config.*"%>
<%@page import="hksarg.ccgo.applist.constant.*"%>

<%
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Pragma", "no-cache");	    
	response.setContentType("text/html; charset=iso-8859-1");
    
	// set parameter
	Hashtable ht = new Hashtable();	
	
	// Get Solution provider name
	String vendor = PropertyLoader.getConfig("applist.solution.provider"); 	

	// get all the headers		

	Enumeration enumber = request.getHeaderNames(); 

	while (enumber.hasMoreElements()) { 
    
		String name = (String)enumber.nextElement();
    	             	
    	String value = request.getHeader(name);
    	
    	if ( name.indexOf("x-") != -1  )  // For Novell Solution only
    	  name = name.substring(name.indexOf("x-")+2, name.length());   	    	  
    	
    	ht.put(name.toUpperCase(), value);	    		
	}
		
	/**
	Enumeration enumber = request.getParameterNames(); 

	while (enumber.hasMoreElements()) { 
    
		String name = (String)enumber.nextElement();
    	             	
    	String value = request.getParameter(name);
    	
    	if ( name.indexOf("x-") != -1  )  // For Novell Solution only
    	  name = name.substring(name.indexOf("x-")+2, name.length());   	    	  
    	
    	ht.put(name.toUpperCase(), value);	    		
	}	
	*/	
%>

<html>
<head>
<title>Welcome to Departmental Portal Application List</title>
<meta http-equiv="Content-Type" content="text/html;">
<!-- Fireworks 4.0  Dreamweaver 4.0 target.  Created Thu May 06 17:22:44 GMT+0800 (China Standard Time) 2004-->
<link rel="stylesheet" href="theme/style.css" type="text/css">
<SCRIPT Language="JavaScript">
<!--
function launchApp(url)	{
	//Comment out the either One Option
	window.open(url,"SubWindow","width=800,height=600"); //Option 1. for pop-up window 
	//window.location = url; //Option 2. for same window
}
// -->
</SCRIPT>
</head>
<body bgcolor="#ffffff" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td bgcolor="#FF9933"><img name="logo" src="images/dept_logo.gif" width="111" height="49" border="0" alt="Department Logo"><a href="#" target="dept_name"><img name="index_r1_c7" src="images/dept_name.gif" width="371" height="49" border="0" alt="Department Name "></a><img name="top_bg" src="images/top_bg.gif" width="263" height="49" border="0"></td>
        </tr>
      </table>
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td background="images/bar_bg.gif"><img name="bar_bg" src="images/bar_bg.gif" width="32" height="22" border="0"></td>
        </tr>
      </table>
      
    </td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td background="images/head_spacer.gif"> 
      <table width="745" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td><img name="bn_provider" src="images/bnr_wtdpal.gif" width="528" height="36" border="0" alt="Welcome to Departmental Portal Applications List"><img src="images/hear_bg.gif" width="152" height="36"><a href="logout.jsp" ><img src="images/logout.gif" border="0" width="65" height="36" alt="Logout"></a></td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<table width="745" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td> 
      <table width="745" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="73" valign="top"><img name="welcome_bg" src="images/welcome_bg.gif" width="73" height="398" border="0" alt="Welcome"></td>
          <td valign="top"> 
            <table width="650" border="0" cellspacing="0" cellpadding="1" class="chiTextSmall" align="center" bgcolor="#FF9933" height="100%">
              <tr>
                <td>
                  <table width="650" border="0" cellspacing="0" cellpadding="0" class="chiTextSmall" bgcolor="#FFFFFF" height="100%">
                    <tr> 
                      <td> 
                        <table width="650" border="0" cellspacing="0" cellpadding="5" class="engText">
                          <tr>
                            <td>Hello! <%=(String)ht.get("CN")%></td>
                            <td>
                              <div align="right">&nbsp;</div>
                            </td>
                          </tr>
                        </table>
                        <table width="650" border="0" cellspacing="0" cellpadding="0" class="bar" height="1">
                          <tr> 
                            <td>&nbsp;</td>
                          </tr>
                        </table>
 
                        <table width="650" border="0" cellspacing="0" cellpadding="0" class="engTitle" height="18">
                          <tr> 
                            <td class="engSubTitle" background="images/spacer.gif"><img name="index_r7_c4" src="images/point.gif" width="7" height="18" border="0"><img src="images/spacer.gif" width="15" height="18"><img src="images/spacer.gif" width="15" height="18"><img src="images/spacer.gif" width="15" height="18"><img src="images/spacer.gif" width="15" height="18"><img src="images/spacer.gif" width="15" height="18"><img name="index_r7_c6" src="images/bn_provide.gif" width="60" height="18" border="0" alt="Provide"><img src="images/spacer.gif" width="15" height="18"><img name="bn_crossbd" src="images/bn_crossbd.gif" width="73" height="18" border="0"></td>
                          </tr>
                        </table>
                        <br>
                        
                        <table width="500" border="1" cellspacing="0" cellpadding="2" class="engTitle" height="18" bgcolor="#FFFFFF" bordercolor="#000000" align="center">
<%	
    // Create business object to retrieve a list of remote applications
	// DPdnsname is changed to be retrived from configuration file since version 3.0
	//MainAppList mainAppList = new MainAppList(request.getServerName(), ht);		
	MainAppList mainAppList = new MainAppList( ht );
	try{
		
		Vector result_list = mainAppList.getRemoteAppList("crossdomain","pre-production");	
		
		for (int i=0; i< result_list.size(); i++) {
			out.println((String)result_list.get(i));	
		}
		
	}catch(Exception e){ 
		out.println("<tr><td class=\"engSubTitle\">ERROR! "+e.getMessage()+"!</td></tr>"); 
	}
%>                          
                        <br>

</table>
                        <br>
                        <table width="650" border="0" cellspacing="0" cellpadding="0" class="engTitle" height="18">
                          <tr> 
                            <td class="engSubTitle" background="images/spacer.gif"><img name="index_r7_c4" src="images/point.gif" width="7" height="18" border="0"><img src="images/spacer.gif" width="15" height="18"><img src="images/spacer.gif" width="15" height="18"><img src="images/spacer.gif" width="15" height="18"><img src="images/spacer.gif" width="15" height="18"><img src="images/spacer.gif" width="15" height="18"><img name="bn_internatlappurl" src="images/bn_internatlappurl.gif" width="166" height="18" border="0"></td>
                          </tr>
                        </table>
                        <br>
                        <table width="500" border="1" cellspacing="0" cellpadding="2" class="engTitle" height="18" bgcolor="#FFFFFF" bordercolor="#000000" align="center">
<%
	try{
		
		// Create business object to retrieve a list of local applications
		Vector local_app_list = mainAppList.getLocalAppList("local");
		for (int i=0; i< local_app_list.size(); i++) {
			out.println((String)local_app_list.get(i));				
		}		
		
	}catch(Exception e){ out.println("<tr><td class=\"engSubTitle\">ERROR! "+e.getMessage()+"!</td></tr>"); }
%>	
                        </table>
                        <br>
                        <table width="650" border="0" cellspacing="0" cellpadding="0">
                          <tr> 
                            <td>
                              <div align="right"><img name="btm" src="images/bottom.gif" width="247" height="17" border="0"></div>
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                  
                </td>
              </tr>
            </table>

          </td>
        </tr>
      </table>      
    </td>
  </tr>
</table>
</body>
</html>
