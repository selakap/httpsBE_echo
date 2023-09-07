# httpsBE_echo

This is a simulation of an echo backend using Java. 
This backend will respond with the data inside received TCP packets (header + payload) without waiting for the complete message

You need to provide a keystore since this is a HTTPS backend

System.setProperty("javax.net.ssl.keyStore", "/Users/selakapiumal/My_tickets/wso2am-3.2.0/repository/resources/security/wso2carbon.jks");
System.setProperty("javax.net.ssl.keyStorePassword", "wso2carbon");
