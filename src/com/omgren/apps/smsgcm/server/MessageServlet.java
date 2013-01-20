package com.omgren.apps.smsgcm.server;

import com.google.gson.Gson;
import com.omgren.apps.smsgcm.common.SmsMessageDummy;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that adds display number of devices and button to send a message.
 * <p>
 * This servlet is used just by the browser (i.e., not device) and contains the
 * main page of the demo app.
 */
@SuppressWarnings("serial")
public class MessageServlet extends BaseServlet {

  static final String ATTRIBUTE_STATUS = "status";

  /**
   * Displays the existing messages and offer the option to send a new one.
   */
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    resp.setContentType("application/json");
    resp.setCharacterEncoding("utf-8");
    PrintWriter out = resp.getWriter();
    DSDevice phone = Datastore.lookupUser(req).getDevice(0);
    if( phone != null )
      out.print((new Gson()).toJson(phone.getQueued().dump()));
    resp.setStatus(HttpServletResponse.SC_OK);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    doGet(req, resp);
  }

}
