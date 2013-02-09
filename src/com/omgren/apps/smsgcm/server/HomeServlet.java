/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.omgren.apps.smsgcm.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Iterator;
import com.omgren.apps.smsgcm.server.DSUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that adds display number of devices and button to send a message.
 * <p>
 * This servlet is used just by the browser (i.e., not device) and contains the
 * main page of the demo app.
 */
@SuppressWarnings("serial")
public class HomeServlet extends BaseServlet {

  static final String ATTRIBUTE_STATUS = "status";

  /**
   * Displays the existing messages and offer the option to send a new one.
   */
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    resp.setContentType("text/html");
    resp.setCharacterEncoding("utf-8");
    PrintWriter out = resp.getWriter();

    out.print("<html>");
    out.print("<head>");
    out.print("<title>SMSGCM Connected Devices</title><link rel='icon' href='favicon.png'/>");
    out.print("</head><body>");

    String status = (String) req.getAttribute(ATTRIBUTE_STATUS);
    if (status != null) {
      out.print(status);
    }

    List<String> devices = Datastore.lookupUser(req).getDeviceIds();
    if (devices.isEmpty()) {
      out.print("<h2>No devices registered!</h2>");
    } else {
      out.print("<h2>" + devices.size() + " device(s) registered!</h2>");
      out.print("<h4>your devices:</h4><ul>");
      for(String d : devices)
        out.print("<li>" + d + "</li>");
      out.print("</ul>");
    }

    out.print("</br><pre>SSL Cert Info: " + Utilities.getSSLClientDN(req) + "</pre>");

    // if me, show users and their devices
    try {
      if(Utilities.getSSLClientEmail(req).equals("ren@omgren.com")){
        List<DSUser> users = Datastore.getUsers();
        out.print("<hr /><h2>Connected users and their devices</h2><ul>");
        for(Iterator<DSUser> it = users.iterator(); it.hasNext();){
          DSUser user = it.next();
          out.print("<li>" + user.getDN());
          List<String> devs = user.getDeviceIds();
          out.print("<ul>");
          for(Iterator<String> i = devs.iterator(); i.hasNext();)
            out.print("<li>" + i.next() + "</li>");
          out.print("</ul></li>");
        }
        out.print("</ul>");
      }
    } catch (IOException e) {
      //pass
    }

    out.print("</body></html>");
    resp.setStatus(HttpServletResponse.SC_OK);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    doGet(req, resp);
  }

}
