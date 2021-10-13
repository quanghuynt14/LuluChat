package com.mycompany.app.Client;

import com.mycompany.app.Client.threads.ListenerThreads;
import com.mycompany.app.Client.threads.SenderThread;

import java.io.*;
import java.net.*;

public class Client {
    /**
     * main method
     * accepts a connection, receives a message from client then sends an echo to the client
     **/
    public static void main(String[] args) {

        Socket echoSocket;
        PrintStream socOut = null;
        BufferedReader stdIn = null;
        BufferedReader socIn = null;

        if (args.length != 3) {
            System.out.println("Usage: java EchoClient <EchoServer host> <EchoServer port> <EchoServer salle>");
            System.exit(1);
        }

        try {
            // creation socket ==> connexion
            echoSocket = new Socket(args[0], Integer.parseInt(args[1]));
            socIn = new BufferedReader(
                    new InputStreamReader(echoSocket.getInputStream()));
            socOut = new PrintStream(echoSocket.getOutputStream());
            stdIn = new BufferedReader(new InputStreamReader(System.in));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host:" + args[0]);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to:" + args[0]);
            System.exit(1);
        }

        SenderThread ct2 = new SenderThread(stdIn, socOut);
        ListenerThreads ct3 = new ListenerThreads(socIn);
        ct2.start();
        ct3.start();
    }

}

