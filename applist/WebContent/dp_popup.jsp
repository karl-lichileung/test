<script src="js/jquery-3.3.1.min.js"></script>

<% 
	String applistUrl=(String)request.getParameter("applistUrl");
	String XLOUrl=(String)request.getParameter("SLOUrl");
%>

<html>
	<head>
		<SCRIPT Language="JavaScript">
		<!--
		
		function launchApp(url)	{
			//Comment out the either One Option
			window.open(url,"SubWindow","width=1024,height=768,resizable=yes"); //Option 1. for pop-up window - parameters updated in version 3.5
			//window.location = url; //Option 2. for same window
		}
		
		$(document).ready(function(){
		});
		
		// -->
		</SCRIPT>	
	</head>
	<div style="margin:0px;padding:0px;">
		<img src="images\dp_header.jpg"/>
	</div>

	<div style="margin-left:20px;">
		<div style="display:inline-block">
			<a href="#">
				<img src="images\icon_welcome.jpg"/>
			</a>
		</div>
		<div style="display:inline-block">
			<a href="javascript:launchApp('<%=applistUrl%>')">
				<img src="images\icon_app_list.jpg"/>
			</a>
		</div>
		<div style="display:inline-block">
			<a href="#">
				<img src="images\icon_change_password.jpg"/>
			</a>
		</div>
		<div style="display:inline-block">
			<a href="javascript:launchApp('<%=XLOUrl%>')">
				<img src="images\icon_logout.jpg"/>
			</a>
		</div>
	</div>
	
	<div style="color:red;text-align:center;font-size:25px;">
		<span>Please do not close this windows until you are LOGOUT from ITSD Departmental Portal</span>
	</div>


</html>