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
      throws IOException, ServletException
  {
    String address = req.getParameter("address");
    String message = req.getParameter("message");
    String dump = req.getParameter("dump");

    boolean page_reload = address != null && message != null && address.length() > 0;

    resp.setCharacterEncoding("utf-8");
    PrintWriter out = resp.getWriter();

    /* print a form and be nice */
    if( !page_reload ){
      resp.setContentType("text/html");
      out.print("<html><head><title>send stuff</title></head>");
      out.print("<body onload=\"document.forms[0].address.focus();\"><form action=\"\" method=\"get\">");
      out.print("<input type=\"text\" name=\"address\" />");
      out.print("<input type=\"text\" name=\"message\" />");
      out.print("<input type=\"submit\" name=\"submit\" />");
      out.print("</form></body></html>");
      resp.setStatus(HttpServletResponse.SC_OK);
      return;
    }

    /* TODO: find the right phone */
    DSDevice phone = Datastore.lookupUser(req).getDevice(0);

    /* tell there is no phone */
    if( phone == null ){
      resp.setContentType("text/plain");
      out.print("no devices connected");
      resp.setStatus(HttpServletResponse.SC_OK);
      return;
    }

    /* queue up the message and notify the phone */
    phone.queueMessage(address, message);

    List<String> devices = new LinkedList();
    devices.add(phone.getDeviceId());

    GCMNotify.notify(req, devices);

    String url = "/received";
    if( dump != null )
      url += "?dump";
    else
      url += "?list";

    resp.setContentType("application/json");
    getServletContext().getRequestDispatcher(url).include(req, resp);
    resp.setStatus(HttpServletResponse.SC_OK);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws IOException, ServletException {
    doGet(req, resp);
  }

}
