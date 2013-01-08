package com.omgren.apps.smsgcm.server;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;
import java.util.logging.Logger;

import com.omgren.apps.smsgcm.common.SmsMessageDummy;
import com.omgren.apps.smsgcm.server.DSUser;

import javax.servlet.http.HttpServletRequest;

public final class Datastore {

  private static final List<DSUser> users = new LinkedList<DSUser>();
  private static final Logger logger = Logger.getLogger(Datastore.class.getName());

  private Datastore() {
    throw new UnsupportedOperationException();
  }

  public static DSUser lookupUser(String dn){
    synchronized(users){
      for(Iterator<DSUser> it = users.iterator(); it.hasNext();){
        DSUser u = it.next();
        if(u.getDN().equals(dn))
          return u;
      }

      DSUser nu = new DSUser(dn);
      users.add(nu);
      return nu;
    }
  }

  public static DSUser lookupUser(HttpServletRequest req){
    String dn = Utilities.getSSLClientDN(req);
    return lookupUser(dn);
  }

  public static List<DSUser> getUsers(){
    synchronized(users){
      return new LinkedList<DSUser>(users);
    }
  }

}
