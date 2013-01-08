package com.omgren.apps.smsgcm.server;

import java.util.List;
import java.util.LinkedList;
import com.omgren.apps.smsgcm.common.SmsMessageDummy;

public class DSMessages {

  private List<SmsMessageDummy> messages;

  public DSMessages() {
    this.messages = new LinkedList<SmsMessageDummy>();
  }

  public void put(SmsMessageDummy message){
    synchronized (messages) {
      messages.add(message);
    }
  }

  public void put(String address, String message){
    SmsMessageDummy m = new SmsMessageDummy();
    m.address = address;
    m.message = message;
    put(m);
  }

  public List<SmsMessageDummy> dump(){
    synchronized (messages) {
      List<SmsMessageDummy> ret = new LinkedList<SmsMessageDummy>(messages);
      messages.clear();
      return ret;
    }
  }

  public List<SmsMessageDummy> get(){
    synchronized (messages) {
      return new LinkedList<SmsMessageDummy>(messages);
    }
  }

  public void clear(){
    synchronized (messages) {
      messages.clear();
    }
  }

}
