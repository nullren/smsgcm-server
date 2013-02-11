package com.omgren.apps.smsgcm.server;

import com.omgren.apps.smsgcm.common.SmsMessageDummy;
import com.omgren.apps.smsgcm.server.DSMessages;
import java.util.List;

public class DSDevice {

  private String regId;
  private String nickname;

  public void setRegId(String newId){
    this.regId = newId;
  }

  public String getRegId(){
    return this.regId;
  }

  public void setNickname(String newNick){
    this.nickname = newNick;
  }

  public String getNickname(){
    return this.nickname;
  }

  private DSMessages queued;
  private DSMessages received;

  public DSDevice(String regId) {
    setRegID(regId);

    this.queued = new DSMessages();
    this.received = new DSMessages();
  }

  public DSDevice(String regId, String nickname) {
    DSDevice(regId);
    setNickname(nickname);
  }

  public String getDeviceId(){
    return getRegId();
  }

  /**
   * these are messages queued up for the phone to download and then
   * send.
   */
  public DSMessages getQueued(){
    return this.queued;
  }

  public List<SmsMessageDummy> copyQueuedMessages(){
    return getQueued().get();
  }

  public List<SmsMessageDummy> dumpQueuedMessages(){
    return getQueued().dump();
  }

  public void queueMessage(String address, String message){
    getQueued().put(address, message);
  }

  /**
   * these are messages received by the phone and queued up on the
   * server to be read by a client.
   */
  public DSMessages getReceived(){
    return this.received;
  }

  public List<SmsMessageDummy> copyReceivedMessages(){
    return getReceived().get();
  }

  public List<SmsMessageDummy> dumpReceivedMessages(){
    return getReceived().dump();
  }

  public void queueReceivedMessage(SmsMessageDummy message){
    getReceived().put(message);
  }
}
