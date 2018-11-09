package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.io.*;
import java.util.*;
import hksarg.ccgo.applist.business.*;
import hksarg.ccgo.applist.config.*;
import hksarg.ccgo.applist.constant.*;

public final class applist_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;
String urll="";
int port=0;
  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html; charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");

	response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
	
	// set parameter
	Hashtable ht = new Hashtable();
	//String DPdnsname = "dp.namit.ccgo.hksarg";
	//String DPdnsname = request.getServerName();

	// Get Solution provider name
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

	//out.println("<tr>");

	while (enumber.hasMoreElements()) {

		String name = (String)enumber.nextElement();
    	String value = request.getHeader(name);

    	if ( name.indexOf("x-") != -1  )  // For Novell Solution only
    	  name = name.substring(name.indexOf("x-")+2, name.length());

    	ht.put(name.toUpperCase(), value);
		/*
  		out.println("<td>");
  		out.println(name);
  		out.println("</td>");
  		out.println("<td>");
  		out.println(value);
  		out.println("</td>");
		*/    	
	}
	//out.println("</tr>");

      out.write("\r\n");
      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("<title>Departmental Portal Applist Generation Program version 2.1</title>\r\n");
      out.write("<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n");
      out.write("<META http-equiv=\"Content-Style-Type\" content=\"text/css\">\r\n");
      out.write("<link rel=\"stylesheet\" href=\"theme/style.css\" type=\"text/css\">\r\n");
      out.write("<SCRIPT Language=\"JavaScript\">\r\n");
      out.write("<!--\r\n");
      out.write("function launchApp(url)\t{\r\n");
      out.write("\t//Comment out the either One Option\r\n");
      out.write("\twindow.open(url,\"SubWindow\",\"width=800,height=600\"); //Option 1. for pop-up window \r\n");
      out.write("\t//window.location = url; //Option 2. for same window\r\n");
      out.write("}\r\n");
      out.write("// -->\r\n");
      out.write("</SCRIPT>\r\n");
      out.write("</head>\r\n");
      out.write("<body bgcolor=\"#ffffff\" leftmargin=\"0\" topmargin=\"0\" marginwidth=\"0\" marginheight=\"0\">\r\n");
      out.write("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r\n");
      out.write("  <tr>\r\n");
      out.write("    <td>\r\n");
      out.write("      <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r\n");
      out.write("        <tr>\r\n");
      out.write("          <td bgcolor=\"#FF9933\"><img name=\"logo\" src=\"images/dept_logo.gif\" width=\"111\" height=\"49\" border=\"0\" alt=\"Department Logo\"><a href=\"#\" target=\"dept_name\"><img name=\"index_r1_c7\" src=\"images/dept_name.gif\" width=\"371\" height=\"49\" border=\"0\" alt=\"Department Name \"></a><img name=\"top_bg\" src=\"images/top_bg.gif\" width=\"263\" height=\"49\" border=\"0\"></td>\r\n");
      out.write("        </tr>\r\n");
      out.write("      </table>\r\n");
      out.write("      <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r\n");
      out.write("        <tr>\r\n");
      out.write("          <td background=\"images/bar_bg.gif\"><img name=\"bar_bg\" src=\"images/bar_bg.gif\" width=\"32\" height=\"22\" border=\"0\"></td>\r\n");
      out.write("        </tr>\r\n");
      out.write("      </table>\r\n");
      out.write("\r\n");
      out.write("    </td>\r\n");
      out.write("  </tr>\r\n");
      out.write("</table>\r\n");
      out.write("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r\n");
      out.write("  <tr>\r\n");
      out.write("    <td background=\"images/head_spacer.gif\">\r\n");
      out.write("      <table width=\"745\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r\n");
      out.write("        <tr>\r\n");
      out.write("          <td><img name=\"bn_provider\" src=\"images/bnr_wtdpal.gif\" width=\"528\" height=\"36\" border=\"0\" alt=\"Welcome to Departmental Portal Applications List\"><img src=\"images/hear_bg.gif\" width=\"152\" height=\"36\"><a href=\"logout.jsp\" ><img src=\"images/logout.gif\" border=\"0\" width=\"65\" height=\"36\" alt=\"Logout\"></a></td>\r\n");
      out.write("        </tr>\r\n");
      out.write("      </table>\r\n");
      out.write("    </td>\r\n");
      out.write("  </tr>\r\n");
      out.write("</table>\r\n");
      out.write("<table width=\"745\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r\n");
      out.write("  <tr>\r\n");
      out.write("    <td>\r\n");
      out.write("      <table width=\"745\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r\n");
      out.write("        <tr>\r\n");
      out.write("          <td width=\"73\" valign=\"top\"><img name=\"welcome_bg\" src=\"images/welcome_bg.gif\" width=\"73\" height=\"398\" border=\"0\" alt=\"Welcome\"></td>\r\n");
      out.write("          <td valign=\"top\">\r\n");
      out.write("            <table width=\"650\" border=\"0\" cellspacing=\"0\" cellpadding=\"1\" class=\"chiTextSmall\" align=\"center\" bgcolor=\"#FF9933\" height=\"100%\">\r\n");
      out.write("              <tr>\r\n");
      out.write("                <td>\r\n");
      out.write("                  <table width=\"650\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"chiTextSmall\" bgcolor=\"#FFFFFF\" height=\"100%\">\r\n");
      out.write("                    <tr>\r\n");
      out.write("                      <td>\r\n");
      out.write("                        <table width=\"650\" border=\"0\" cellspacing=\"0\" cellpadding=\"5\" class=\"engText\">\r\n");
      out.write("                          <tr>\r\n");
      out.write("                            <td>Hello! ");
      out.print((String)ht.get("CN"));
      out.write("</td>\r\n");
      out.write("                            <td>\r\n");
      out.write("                              <div align=\"right\">&nbsp;</div>\r\n");
      out.write("                            </td>\r\n");
      out.write("                          </tr>\r\n");
      out.write("                        </table>\r\n");
      out.write("                        <table width=\"650\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"bar\" height=\"1\">\r\n");
      out.write("                          <tr>\r\n");
      out.write("                            <td>&nbsp;</td>\r\n");
      out.write("                          </tr>\r\n");
      out.write("                        </table>\r\n");
      out.write("\r\n");
      out.write("                        <table width=\"650\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"engTitle\" height=\"18\">\r\n");
      out.write("                          <tr>\r\n");
      out.write("                            <td class=\"engSubTitle\" background=\"images/spacer.gif\"><img name=\"index_r7_c4\" src=\"images/point.gif\" width=\"7\" height=\"18\" border=\"0\"><img src=\"images/spacer.gif\" width=\"15\" height=\"18\"><img src=\"images/spacer.gif\" width=\"15\" height=\"18\"><img src=\"images/spacer.gif\" width=\"15\" height=\"18\"><img src=\"images/spacer.gif\" width=\"15\" height=\"18\"><img src=\"images/spacer.gif\" width=\"15\" height=\"18\"><img name=\"index_r7_c6\" src=\"images/bn_provide.gif\" width=\"60\" height=\"18\" border=\"0\" alt=\"Provide\"><img src=\"images/spacer.gif\" width=\"15\" height=\"18\"><img name=\"bn_crossbd\" src=\"images/bn_crossbd.gif\" width=\"73\" height=\"18\" border=\"0\"></td>\r\n");
      out.write("                          </tr>\r\n");
      out.write("                        </table>\r\n");
      out.write("                        <br>\r\n");
      out.write("\r\n");
      out.write("                        <table width=\"500\" border=\"1\" cellspacing=\"0\" cellpadding=\"2\" class=\"engTitle\" height=\"18\" bgcolor=\"#FFFFFF\" bordercolor=\"#000000\" align=\"center\">\r\n");
  // Create business object to retrieve a list of remote applications
	//MainAppList mainAppList = new MainAppList(DPdnsname, ht);
	MainAppList mainAppList = new MainAppList( ht ,urll,port);
	try{
		Vector result_list = mainAppList.getRemoteAppList("crossdomain","production");
		for (int i=0; i< result_list.size(); i++) {
			out.println((String)result_list.get(i));
		}
	}catch(Exception e){
		out.println("<tr><td class=\"engSubTitle\">ERROR! "+e.getMessage()+"!</td></tr>");
	}

      out.write("\r\n");
      out.write("                        <br>\r\n");
      out.write("\r\n");
      out.write("</table>\r\n");
      out.write("                        <br>\r\n");
      out.write("                        <table width=\"650\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"engTitle\" height=\"18\">\r\n");
      out.write("                          <tr>\r\n");
      out.write("                            <td class=\"engSubTitle\" background=\"images/spacer.gif\"><img name=\"index_r7_c4\" src=\"images/point.gif\" width=\"7\" height=\"18\" border=\"0\"><img src=\"images/spacer.gif\" width=\"15\" height=\"18\"><img src=\"images/spacer.gif\" width=\"15\" height=\"18\"><img src=\"images/spacer.gif\" width=\"15\" height=\"18\"><img src=\"images/spacer.gif\" width=\"15\" height=\"18\"><img src=\"images/spacer.gif\" width=\"15\" height=\"18\"><img name=\"bn_internatlappurl\" src=\"images/bn_internatlappurl.gif\" width=\"166\" height=\"18\" border=\"0\"></td>\r\n");
      out.write("                          </tr>\r\n");
      out.write("                        </table>\r\n");
      out.write("                        <br>\r\n");
      out.write("                        <table width=\"500\" border=\"1\" cellspacing=\"0\" cellpadding=\"2\" class=\"engTitle\" height=\"18\" bgcolor=\"#FFFFFF\" bordercolor=\"#000000\" align=\"center\">\r\n");

	try{

		// Create business object to retrieve a list of local applications
		Vector local_app_list = mainAppList.getLocalAppList("local");
		for (int i=0; i< local_app_list.size(); i++) {
			out.println((String)local_app_list.get(i));
		}

	}catch(Exception e){ out.println("<tr><td class=\"engSubTitle\">ERROR! "+e.getMessage()+"!</td></tr>"); }

      out.write("\r\n");
      out.write("                        </table>\r\n");
      out.write("                        <br>\r\n");
      out.write("                        <table width=\"650\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r\n");
      out.write("                          <tr>\r\n");
      out.write("                            <td>\r\n");
      out.write("                              <div align=\"right\"><img name=\"btm\" src=\"images/bottom.gif\" width=\"247\" height=\"17\" border=\"0\"></div>\r\n");
      out.write("                            </td>\r\n");
      out.write("                          </tr>\r\n");
      out.write("                        </table>\r\n");
      out.write("                      </td>\r\n");
      out.write("                    </tr>\r\n");
      out.write("                  </table>\r\n");
      out.write("\r\n");
      out.write("                </td>\r\n");
      out.write("              </tr>\r\n");
      out.write("            </table>\r\n");
      out.write("\r\n");
      out.write("          </td>\r\n");
      out.write("        </tr>\r\n");
      out.write("      </table>\r\n");
      out.write("    </td>\r\n");
      out.write("  </tr>\r\n");
      out.write("</table>\r\n");
      out.write("</body>\r\n");
      out.write("</html>");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
