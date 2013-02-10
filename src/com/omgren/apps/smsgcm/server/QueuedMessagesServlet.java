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
public class QueuedMessagesServlet extends BaseServlet {

  static final String ATTRIBUTE_STATUS = "status";

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException, ServletException {
    resp.setContentType("application/json");
    resp.setCharacterEncoding("utf-8");
    PrintWriter out = resp.getWriter();

    DSDevice phone = Datastore.lookupUser(req).getDevice(0);

    if( phone != null ){
      List<SmsMessageDummy> messages = phone.copyReceivedMessages();
      out.print((new Gson()).toJson(messages));
    } else {
      out.print("no devices connected.");
    }

    resp.setStatus(HttpServletResponse.SC_OK);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws IOException, ServletException {
    doGet(req, resp);
  }

}
