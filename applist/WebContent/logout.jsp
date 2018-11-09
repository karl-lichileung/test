<%@page import="java.io.*"%>
<%@page import="java.util.*"%>
<%@page import="hksarg.ccgo.applist.business.*"%>
<%@page import="hksarg.ccgo.applist.config.*"%>
<%@page import="hksarg.ccgo.applist.constant.*"%>
<html>
<head>
<title>Logout</title>
<frameset rows="1,*" frameborder="no">
<frame src="http://applist.dpp.ccgo.hksarg/dp/logout/logoutall.html" name="all_domain" scrolling="no"><!--------------------------------------------------------------------->
<!--   You should add your own DP logout path in main frame          -->
<!--   For the DP of Sun Micro System Solution, the example is :     --> 
<!--   <frame src="../amserver/UI/Logout" name='home' scrolling="no">-->
<!--								     -->
<!--   For the DP of IBM solution, the example is :           	     --> 
<!--   <frame src="../pkmslogout" name='home' scrolling="no"> 	     -->
<!--								     -->
<!--   For the DP of Novell solution, the example is :               --> 
<!--   <frame src="../cmd/BM-Logout" name='home' scrolling="no">     -->
<!--								     -->
<!--   For the DP of CA solution, the example is :  	             --> 
<!--   <frame src="../saml/logout.jsp"" name='home' scrolling="no">  -->
<!--								     -->
<!--------------------------------------------------------------------->
<%  
String url = request.getRequestURL().toString();
String baseURL = url.substring(8, url.length() - request.getRequestURI().length());
String exturl = baseURL.substring(baseURL.length() - 4);

	String vendor = PropertyLoader.getConfig("applist.solution.provider"); 
	String serverName = PropertyLoader.getConfig("dp.dns.name");
	if ( vendor.equalsIgnoreCase("ICHAIN22") || vendor.equalsIgnoreCase("ICHAIN23")) 
		out.println("<frame src=\"/../cmd/BM-Logout\" name=\"home\" scrolling=\"no\"></frameset>");
	
	if ( vendor.equalsIgnoreCase("TAM41") || vendor.equalsIgnoreCase("TAM51")) 
		out.println("<frame src=\"/../pkmslogout\" name=\"home\" scrolling=\"no\"></frameset>");
	
	if ( vendor.equalsIgnoreCase("WAC1") ) 
		out.println("<frame src=\"/../saml/logout.jsp\" name=\"home\" scrolling=\"no\"></frameset>");
	
	if ( vendor.equalsIgnoreCase("SID6") ) 
		out.println("<frame src=\"/../amserver/UI/Logout\" name=\"home\" scrolling=\"no\"></frameset>");

	if ( vendor.equalsIgnoreCase("WAC6") ) 
		out.println("<frame src=\"/../saml/logout.jsp\" name=\"home\" scrolling=\"no\"></frameset>");
		
	if ( vendor.equalsIgnoreCase("NAM3") ) 
		out.println("<frame src=\"https://"+serverName+"/AGLogout\" name=\"home\" scrolling=\"no\"></frameset>");

	if ( vendor.equalsIgnoreCase("NAM31") ) 
		out.println("<frame src=\"https://"+serverName+"/AGLogout\" name=\"home\" scrolling=\"no\"></frameset>");
	
	if ( vendor.equalsIgnoreCase("FIM6") ) 
		out.println("<frame src=\"/../pkmslogout\" name=\"home\" scrolling=\"no\"></frameset>");

	if ( vendor.equalsIgnoreCase("OSDP")&& !serverName.equalsIgnoreCase(baseURL)&& !exturl.equalsIgnoreCase("9443"))	 
		out.println("<frame src=\"https://"+baseURL+":8443/sso/UI/Logout\" name=\"home\" scrolling=\"no\"></frameset>");
	if ( vendor.equalsIgnoreCase("OSDP")&& serverName.equalsIgnoreCase(baseURL)&& !exturl.equalsIgnoreCase("9443")) 
		out.println("<frame src=\"https://"+serverName+":8443/sso/UI/Logout\" name=\"home\" scrolling=\"no\"></frameset>");
	if ( vendor.equalsIgnoreCase("OSDP")&& !serverName.equalsIgnoreCase(baseURL)&& exturl.equalsIgnoreCase("9443"))	 
	out.println("<frame src=\"https://"+serverName+":8443/sso/UI/Logout\" name=\"home\" scrolling=\"no\"></frameset>");

				
%>
<noframes>This page should be run on frame.
</noframes>

</head>
</html>
