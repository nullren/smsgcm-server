package com.omgren.apps.smsgcm.server;

import com.omgren.apps.smsgcm.common.SmsMessageDummy;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

@SuppressWarnings("serial")
public class WebUserServlet extends BaseServlet {

  static final String ATTRIBUTE_STATUS = "status";

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException, ServletException
  {
    resp.setContentType("text/html");
    resp.setCharacterEncoding("utf-8");
    PrintWriter out = resp.getWriter();

    String address = req.getParameter("address");
    String message = req.getParameter("message");

    boolean page_reload = address != null && message != null && address.length() > 0;

    out.print("<html><head><title>send stuff</title></head>");
    out.print("<meta http-equiv=\"refresh\" content=\"5;url=https://smsgcm.omgren.com/html\" />");
    out.print("<body onload=\"document.forms[0].address.focus();\">");

    DSDevice phone = Datastore.lookupUser(req).getDevice(0);
    if( phone == null ){
      out.print("no devices connected");
      resp.setStatus(HttpServletResponse.SC_OK);
      return;
    }

    /**
     * print the messages we have and make it pretty.
     */

    List<SmsMessageDummy> messages = phone.copyReceivedMessages();

    out.print("<div id=\"messages\">");
    for(Iterator<SmsMessageDummy> it = messages.iterator(); it.hasNext(); ){
      SmsMessageDummy m = it.next();
      //m.message;
      //m.name;
      //m.address;
      //m.time;

      out.print("<div class=\"message\"><span class=\"time\">" + m.time + "</span><span class=\"name\">" + m.name + "</span><span class=\"address\">" + m.address + "</span><span class=\"body\">" + m.message + "</span></div>");
    }
    out.print("</div>");

    /* print a form and be nice */
    out.print("<div id=\"send\">");
    out.print("<form action=\"\" method=\"get\">");
    out.print("<input type=\"text\" name=\"address\" />");
    out.print("<input type=\"text\" name=\"message\" />");
    out.print("<input type=\"hidden\" name=\"list\" value=\"1\" />");
    out.print("<input type=\"submit\" name=\"submit\" />");
    out.print("</form>");
    out.print("</div>");
    out.print("</body></html>");

    if(!page_reload){
      resp.setStatus(HttpServletResponse.SC_OK);
      return;
    }

    /**
     * if we get a message, then send it appropriately.
     */

    phone.queueMessage(address, message);

    List<String> devices = new LinkedList();
    devices.add(phone.getDeviceId());

    GCMNotify.notify(req, devices);

    out.print("message sent!");
    resp.setStatus(HttpServletResponse.SC_OK);
    return;
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws IOException, ServletException {
    doGet(req, resp);
  }

}
