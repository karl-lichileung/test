<%@page import="java.io.*"%>
<%@page import="java.util.*"%>
<%@page import="hksarg.ccgo.applist.business.*"%>
<%@page import="hksarg.ccgo.applist.util.*"%>
<%@page import="hksarg.ccgo.applist.config.*"%>
<%@page import="hksarg.ccgo.applist.constant.*"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<script src="js/jquery-3.3.1.min.js"></script>

<%
String url = request.getRequestURL().toString();
String uri = request.getRequestURI();
String baseURL = url.substring(8, url.length() - uri.length());
boolean printHeader = false;
int port=request.getServerPort();
	response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
	
	// set parameter
	Hashtable ht = new Hashtable();
	//String DPdnsname = "dp.namit.ccgo.hksarg";
	//String DPdnsname = request.getServerName();

	// Get Solution provider name - osdp
	String vendor = PropertyLoader.getConfig("applist.solution.provider");

	// get all the headers
	/**    	
		Enumeration enumber = request.getHeaderNames();

	while (enumber.hasMoreElements()) {

		String name = (String) enumber.nextElement();

		String value = request.getHeader(name);

		if (name.indexOf("x-") != -1) // For Novell Solution only
			name = name.substring(name.indexOf("x-") + 2, name.length());

		ht.put(name.toUpperCase(), value);
	}
		
	*/
	// Test Case
	//http://localhost:8091/applist/applist.jsp?cn=User&SN=cheng&EMPLOYEETYPE=99&DPSTAFFGROUP=1&DPHKID=11121&DPRANKCODE=29

	Enumeration enumber = request.getHeaderNames();

	if (printHeader){
		out.println("Dump Header/n");
		out.println("<table>");
	}
	while (enumber.hasMoreElements()) {

		String name = (String)enumber.nextElement();
    	String value = request.getHeader(name);

    	if ( name.indexOf("x-") != -1  )  // For Novell Solution only
    	  name = name.substring(name.indexOf("x-")+2, name.length());

    	ht.put(name.toUpperCase(), value);
    	if (printHeader){
	    	out.println("<tr>");
	  		out.println("<td>");
	  		out.println(name);
	  		out.println("</td>");
	  		out.println("<td>");
	  		out.println(value);
	  		out.println("</td>");
	  		out.println("/tr");
    	}
	}
	if (printHeader){
		out.println("</table>");
	}
	String IDPEntityID=(String)ht.get("Shib-Identity-Provider".toUpperCase());
	String SLO_URL = FileUtil.getFileData("SLO.xml.url",Constant.entityIDElement,Constant.SingleLogOutElement,IDPEntityID);
%>

<html><head>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<META http-equiv="Content-Style-Type" content="text/css">
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width,initial-scale=1.0">
        <title>OSDP Applications List</title>
        <link rel="stylesheet" type="text/css" href="osdp_app_list_files/main.css">
        <link rel="stylesheet" type="text/css" href="osdp_app_list_files/leftmenu.css">
		<link rel="stylesheet" href="osdp_app_list_files/style.css">
		<link rel="stylesheet" href="osdp_app_list_files/jquery-ui.css">
		<link rel="stylesheet" href="osdp_app_list_files/cus.css">
		<SCRIPT Language="JavaScript">
		<!--
		
		function launchApp(url)	{
			//Comment out the either One Option
			window.open(url,"SubWindow","width=1024,height=768,resizable=yes"); //Option 1. for pop-up window - parameters updated in version 3.5
			//window.location = url; //Option 2. for same window
		}
		
		$(document).ready(function(){
			var applistUrl = window.location.href;
			var popupReference;
			popupReference = window.open("dp_popup.jsp?applistUrl="+applistUrl+"&SLOUrl=<%=SLO_URL%>","Popup","fullscreen=no,toolbar=no,status=no,menubar=no,scrollbars=no,resizable=no,width=500,height=400");
			popupReference.focus();
		});
		
		// -->
		</SCRIPT>
    </head>
<body>
	<div id="wrapper" class="clearfix">
      <div id="container" class="clearfix">
		<header>
          <img src="osdp_app_list_files/banner_smartcity_1.jpg" alt="Replace or remove this logo" class="header_banner">
		      <img alt="é¦æ¸¯ç¹å¥è¡æ¿åæ¿åº,æ¿åºè³è¨ç§æç¸½ç£è¾¦å¬å®¤" src="osdp_app_list_files/logo-main-tc.png" class="header-logo__long">
        </header>
		<div id="content" class="clearfix">
			<form class="form form--user">
				<div class="container" class="clearfix">
					<div class="inner-content">
						<div class="form__field">
							<div class="form__field-body">
								<div class="form__field-col">
									<h2 class="page-title">Welcome to Departmental Portal Applications List</h2>
								</div>
								<div class="form__controller" style="padding-top: 40px;">
									<button type="submit" class="btn btn-common" onclick="">Logout</button>
								</div>
							</div>
							<p>Hello! <%=(String)ht.get("CN")%></p>	
						</div>
						<h3>Remote Applications List</h3>
						<table class="table table-bordered table-hover table--user-account" style="width: 802px;">

							<tr>
								<th>Provider</th>
								<th>URL</th>
							</tr>

<%  // Create business object to retrieve a list of remote applications
	//MainAppList mainAppList = new MainAppList(DPdnsname, ht);
	MainAppList mainAppList = new MainAppList( ht ,baseURL,port);
	try{
		Vector result_list = mainAppList.getRemoteAppList("crossdomain","production");
		for (int i=0; i< result_list.size(); i++) {
			out.println((String)result_list.get(i));
		}
	}catch(Exception e){
		out.println("<tr><td class=\"engSubTitle\">ERROR! "+e.getMessage()+"!</td></tr>");
	}
%>		
<!--
							<tr>
								<td>NOVELLIT_iChain2.3</td>
								<td><a class="btn-edit" href="#">App1</a></td>
							</tr>
-->		
						</table>
						<br>
						<h3>Local Applications List</h3>
						<table class="table table-bordered table-hover table--user-account" style="width: 802px;">
<%
	try{

		// Create business object to retrieve a list of local applications
		Vector local_app_list = mainAppList.getLocalAppList("local");
		for (int i=0; i< local_app_list.size(); i++) {
			out.println((String)local_app_list.get(i));
		}

	}catch(Exception e){ out.println("<tr><td class=\"engSubTitle\">ERROR! "+e.getMessage()+"!</td></tr>"); }
%>
<!--
							<tr>
								<td><a class="btn-edit" href="#">OSDP App1</a></td>
							</tr>
-->		
						</table>
					</div>
				</div>
			</form>
		</div>
      <footer>
        <div class="container container-footer">
			<div class="footer__bottom">
					<div class="footer__logos">
						<a id="wcagLogo" href="http://www.w3.org/WAI/WCAG2AA-Conformance" rel="external" class="footer__logo" title="éµå®2Aç´ç¡éç¤åç¤ºï¼è¬ç¶­ç¶²è¯çï¼W3Cï¼- ç¡éç¤ç¶²é å¡è­°ï¼WAIï¼ Web Content Accessibility Guidelines 2.0" target="_blank"><img src="osdp_app_list_files/img-footer-logo-1.png" alt="éµå®2Aç´ç¡éç¤åç¤ºï¼è¬ç¶­ç¶²è¯çï¼W3Cï¼- ç¡éç¤ç¶²é å¡è­°ï¼WAIï¼ Web Content Accessibility Guidelines 2.0"></a>
						<a href="http://www.ipv6forum.com/" rel="external" class="footer__logo" title="IPv6 é»è¦å¨æå¯çè¦½æ¬ç¶²ç«" target="_blank"><img src="osdp_app_list_files/img-footer-logo-2.png" alt="IPv6 é»è¦å¨æå¯çè¦½æ¬ç¶²ç«"></a>
						<a href="https://www.ogcio.gov.hk/tc/our_work/community/web_mobileapp_accessibility/nurturing_expertise/recognition_scheme/index.html" rel="external" class="footer__logo" title="ç¡éç¤ç¶²é åè¨±è¨å" target="_blank"><img src="osdp_app_list_files/img-footer-logo-3.png" alt="ç¡éç¤ç¶²é åè¨±è¨å"></a>
						<a href="http://www.erb.org/md/tc/Main/" rel="external" class="footer__logo" title="å±å¡åå¹è¨å±è¨å" target="_blank"><img src="osdp_app_list_files/img-footer-logo-7.png" alt="å±å¡åå¹è¨å±è¨å"></a>
						<a href="http://www.caringcompany.org.hk/" rel="external" class="footer__logo" title="caringorganisation åå¿å±éæ·" target="_blank"><img src="osdp_app_list_files/img-footer-logo-4.png" alt="caringorganisation åå¿å±éæ·"></a>
						<a href="http://www.brandhk.gov.hk/index.html" rel="external" class="footer__logo" title="é¦æ¸¯åçå½¢è±¡ - äºæ´²åéé½æ" target="_blank"><img src="osdp_app_list_files/img-footer-logo-5.png" alt="é¦æ¸¯åçå½¢è±¡ - äºæ´²åéé½æ"></a>
					</div>
					<div class="footer__copyright"><span>2018 Â© æ¿åºè³è¨ç§æç¸½ç£è¾¦å¬å®¤</span><span class="footer__bottom-sp">|</span><span>è¦æª¢æ¥æï¼ </span><span id="revisionDate">27 July 2018</span></div>
			</div>        
		</div>
      </footer>
	</div>
</div>
</body>
</html>