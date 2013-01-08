package com.omgren.apps.smsgcm.server;

import javax.servlet.http.HttpServletRequest;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import java.security.cert.X509Certificate;
import java.io.IOException;

public class Utilities {

  public static String getSSLClientDN(HttpServletRequest req){
    Object certObj = req.getAttribute("javax.servlet.request.X509Certificate");
    if( certObj != null ){
      X509Certificate certs[] = (X509Certificate[]) certObj;
      X509Certificate cert = certs[0];
      return cert.getSubjectDN().getName();
    }
    return null;
  }

  public static String getEmailFromDN(String dn) throws IOException {
    try {
      LdapName ldapDN = new LdapName(dn);
      for(Rdn rdn : ldapDN.getRdns()){
        if(rdn.getType().equals("EMAILADDRESS"))
          return rdn.getValue().toString();
      }
    } catch (Exception e) {
      throw new IOException("getEmailFromDN: " + e);
    }
    return null;
  }

  public static String getSSLClientEmail(HttpServletRequest req) throws IOException {
    String dn = getSSLClientDN(req);
    if (dn == null) {
      throw new IOException("no .. subject dn????? fuck this user!");
    }

    String email = getEmailFromDN(dn);
    if (email == null) {
      throw new IOException("user does not have an email");
    }

    return email;
  }
}
