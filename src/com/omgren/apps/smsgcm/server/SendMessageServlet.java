package com.omgren.apps.smsgcm.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class SendMessageServlet extends BaseServlet {

  static final String ATTRIBUTE_STATUS = "status";

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException, ServletException {
    resp.setContentType("text/html");
    resp.setCharacterEncoding("utf-8");
    PrintWriter out = resp.getWriter();

    String address = req.getParameter("address");
    String message = req.getParameter("message");

    boolean page_reload = address != null && message != null && address.length() > 0;

    out.print("<html><head><title>send stuff</title></head>");
    out.print("<body onload=\"document.forms[0]."+(page_reload?"message":"address")+".focus();\"><form action=\"\" method=\"get\">");

    if( page_reload ){
      /* TODO: find the right phone */
      DSDevice phone = Datastore.lookupUser(req).getDevice(0);
      if( phone != null ){
        phone.queueMessage(address, message);
        out.print("sending \"" + message + "\" to " + address);

        List<String> devices = new LinkedList();
        devices.add(phone.getDeviceId());

        GCMNotify.notify(req, devices);

        out.print("<br />");
      } else {
        out.print("not connected to any devices.</br>");
      }
      out.print("<input type=\"text\" name=\"address\" value=\"" + address + "\" />");
    }else{
      out.print("<input type=\"text\" name=\"address\" />");
    }

    out.print("<input type=\"text\" name=\"message\" />");
    out.print("<input type=\"submit\" name=\"submit\" />");
    out.print("</form></body></html>");

    resp.setStatus(HttpServletResponse.SC_OK);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws IOException, ServletException {
    doGet(req, resp);
  }

}
