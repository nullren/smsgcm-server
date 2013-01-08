package com.omgren.apps.smsgcm.server;

import com.omgren.apps.smsgcm.server.DSMessages;

public class DSDevice {

  private String regId;

  private DSMessages queued;
  private DSMessages received;

  public DSDevice(String regId) {
    this.regId = regId;

    this.queued = new DSMessages();
    this.received = new DSMessages();
  }

  public void setRegId(String newId){
    this.regId = newId;
  }

  public String getRegId(){
    return this.regId;
  }

  public DSMessages getQueued(){
    return this.queued;
  }

  public DSMessages getReceived(){
    return this.received;
  }

}
