<%@page import="java.io.*"%>
<%@page import="java.util.*"%>
<%@page import="hksarg.ccgo.applist.business.*"%>
<%@page import="hksarg.ccgo.applist.constant.*"%>
<%
try{
	HttpServletRequest req = request;
    JspWriter jout = out;
	
	Hashtable ht = new Hashtable();	
	
	Enumeration enumber = request.getHeaderNames();
	
	if (enumber == null){
		
		out.println("Enumeration enumber is null");
		
	}else{
		// get all headers 		
		out.println("<table border='1'>");
		out.println("<tr>");
		out.println("<td>Header</td>");
		out.println("<td>Header Value</td>");
		while (enumber.hasMoreElements()) {		
			
			out.println("<tr>");			
			
        	String name = (String)enumber.nextElement();                	
        	String value = request.getHeader(name);       
        	
        	out.println("<td>");
        	out.println(name);
        	out.println("</td>");
        	out.println("<td>");
        	out.println(value);
        	out.println("</td>");
        	
        	out.println("</tr>");			
    	} 		
    	
    	out.println("</table>");
	}	
}catch(Exception e){
	out.println(e.getMessage());
}
%>
