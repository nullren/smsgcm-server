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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.omgren.apps.smsgcm.common.SmsMessageDummy;

/**
 * Simple implementation of a data store using standard Java collections.
 * <p>
 * This class is thread-safe but not persistent (it will lost the data when the
 * app is restarted) - it is meant just as an example.
 */
public final class Datastore {

  private static final List<String> regIds = new ArrayList<String>();
  private static final List<SmsMessageDummy> msgs = new ArrayList<SmsMessageDummy>();
  private static final List<SmsMessageDummy> recMsgs = new ArrayList<SmsMessageDummy>();
  private static final Logger logger = Logger.getLogger(Datastore.class.getName());

  private Datastore() {
    throw new UnsupportedOperationException();
  }

  /**
   * Registers a device.
   */
  public static void register(String regId) {
    logger.info("Registering " + regId);
    synchronized (regIds) {
      regIds.add(regId);
    }
  }
  public static void unregister(String regId) {
    logger.info("Unregistering " + regId);
    synchronized (regIds) {
      regIds.remove(regId);
    }
  }
  public static void updateRegistration(String oldId, String newId) {
    logger.info("Updating " + oldId + " to " + newId);
    synchronized (regIds) {
      regIds.remove(oldId);
      regIds.add(newId);
    }
  }
  public static List<String> getDevices() {
    synchronized (regIds) {
      return new ArrayList<String>(regIds);
    }
  }

  /**
   * Store messages to be picked up by the phone and sent
   */
  public static void queueMsg(String address, String message){
    synchronized (msgs) {
      SmsMessageDummy m = new SmsMessageDummy();
      m.address = address;
      m.message = message;
      msgs.add(m);
    }
  }
  public static List<SmsMessageDummy> getMsgs(){
    synchronized (msgs) {
      return new ArrayList<SmsMessageDummy>(msgs);
    }
  }
  public static void clearMsgs(){
    synchronized (msgs) {
      msgs.clear();
    }
  }
  public static List<SmsMessageDummy> dumpMsgs(){
    synchronized (msgs) {
      List<SmsMessageDummy> ret = new ArrayList<SmsMessageDummy>(msgs);
      msgs.clear();
      return ret;
    }
  }

  /**
   * Store messages received by the phone
   */
  public static void putRecMsg(SmsMessageDummy m){
    synchronized (recMsgs) {
      recMsgs.add(m);
    }
  }
  public static List<SmsMessageDummy> getRecMsgs() {
    synchronized (recMsgs) {
      return new ArrayList<SmsMessageDummy>(recMsgs);
    }
  }
  public static List<SmsMessageDummy> dumpRecMsgs() {
    synchronized (recMsgs) {
      List<SmsMessageDummy> ret = new ArrayList<SmsMessageDummy>(recMsgs);
      recMsgs.clear();
      return ret;
    }
  }
  public static void clearRecMsgs() {
    synchronized (recMsgs) {
      recMsgs.clear();
    }
  }
}
