package com.mycompany.app.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class LuluChat {
    private static boolean checkConnecion(String host, String port){

        Socket echoSocket = null;
        PrintStream socOut = null;
        BufferedReader stdIn = null;
        BufferedReader socIn = null;


        try {
            // creation socket ==> connexion
            echoSocket = new Socket(host,new Integer(port));
            socIn = new BufferedReader(
                    new InputStreamReader(echoSocket.getInputStream()));
            socOut= new PrintStream(echoSocket.getOutputStream());
            stdIn = new BufferedReader(new InputStreamReader(System.in));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host:" + host);
            return false;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to:"+ host);
            return false;
        }
        return true;
    }

    public static void main(String[] args){

        if (args.length != 2) {
            System.out.println("Usage: java EchoClient <EchoServer host> <EchoServer port>");
            System.exit(1);
        }

        Layout layout = new Layout();

        if (! checkConnecion(args[0],args[1])){
            layout.printErrormsg();
        }

    }


}
