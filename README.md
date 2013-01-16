SMSGCM
------

This is just a quick little thing based off of 
[Google's GCM Demo](http://developer.android.com/google/gcm/demo.html) application.

## What is this?

This is the server component of my SMSGCM project. The client can also be found
[over here on github](https://github.com/nullren/smsgcm-client).

## Configuration

#### GCM

Make sure you get your own GCM API key and set it in `GCMNotify`.

#### Tomcat

There are a few options in `build.xml` that need to be set to deploy your war to Tomcat.

#### SSL

You need to set up, as well, SSL keys for tomcat. This is done by creating a *keystore*
and *truststore* file read by the ssl connector in your `server.xml` for tomcat. If this
is not set up, there is effectively no user accounts.

## Installation

`ant dist` should be enough to get you a `.war` that you can then deploy.
