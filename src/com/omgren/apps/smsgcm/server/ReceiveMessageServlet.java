package com.omgren.apps.smsgcm.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.omgren.apps.smsgcm.common.SmsMessageDummy;

@SuppressWarnings("serial")
public class ReceiveMessageServlet extends BaseServlet {

  static final String ATTRIBUTE_STATUS = "status";

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException, ServletException {
    resp.setContentType("text/html");
    PrintWriter out = resp.getWriter();

    SmsMessageDummy msg = new SmsMessageDummy();
    msg.name = req.getParameter("name");
    msg.address = req.getParameter("address");
    msg.message = req.getParameter("message");

    if( msg.address != null ){
      Datastore.lookupUser(req).getDevice(0).getReceived().put(msg);
      out.print("added: ");
      out.print(msg.name + " (" + msg.address + "): " + msg.message);
      out.print("<br/><br/>");
    }

    out.print("have:<br/>");

    List<SmsMessageDummy> msgs = Datastore.lookupUser(req).getDevice(0).getReceived().get();
    int counter = 0;
    for (Iterator<SmsMessageDummy> it = msgs.iterator(); it.hasNext();) {
      SmsMessageDummy m = it.next();
      counter++;
      out.print(m.name + " (" + m.address + "): " + m.message);
      out.print("<br />");
    }

    out.print("total " + counter + " messages");

    resp.setStatus(HttpServletResponse.SC_OK);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws IOException, ServletException {
    doGet(req, resp);
  }

}
