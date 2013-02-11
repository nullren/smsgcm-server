package com.omgren.apps.smsgcm.server;

import com.google.gson.Gson;
import com.omgren.apps.smsgcm.common.SmsMessageDummy;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

@SuppressWarnings("serial")
public class ReceivedMessageServlet extends BaseServlet {

  static final String ATTRIBUTE_STATUS = "status";

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException, ServletException
  {
    resp.setContentType("application/json");
    resp.setCharacterEncoding("utf-8");
    PrintWriter out = resp.getWriter();

    DSDevice phone = Datastore.lookupUser(req).getDevice(0);
    if( phone == null ){
      out.print("no devices connected");
      resp.setStatus(HttpServletResponse.SC_OK);
      return;
    }

    String dump = req.getParameter("dump");
    String list = req.getParameter("list");

    List<SmsMessageDummy> messages;

    if( list != null || dump != null ){
      if( dump != null ){
        messages = phone.dumpReceivedMessages();
      } else {
        messages = phone.copyReceivedMessages();
      }

      out.print((new Gson()).toJson(messages);
      resp.setStatus(HttpServletResponse.SC_OK);
      return;
    }

    SmsMessageDummy msg = new SmsMessageDummy();
    msg.name = req.getParameter("name");
    msg.address = req.getParameter("address");
    msg.message = req.getParameter("message");
    try {
      msg.time = Long.valueOf(req.getParameter("time"));
    } catch (NumberFormatException e){
      msg.time = null;
    }

    if( msg.address != null ){
      phone.queueReceivedMessage(msg);
      out.print("OK");
      resp.setStatus(HttpServletResponse.SC_OK);
      return;
    }

    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws IOException, ServletException {
    doGet(req, resp);
  }

}
