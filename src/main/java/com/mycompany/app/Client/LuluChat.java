package com.mycompany.app.Client;

import com.mycompany.app.Client.GUI.ConnectingGUI;
import com.mycompany.app.Client.GUI.MainLayout;
import com.mycompany.app.Client.threads.ListenerThreads;
import com.mycompany.app.Client.threads.SenderThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

public class LuluChat {

    SenderThread ct2 ;
    ListenerThreads ct3;

    public LuluChat() {
    }

    private boolean checkConnecion(String host, String port){

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
        ct2 = new SenderThread(stdIn, socOut);
        ct3 = new ListenerThreads(socIn);
        ct2.start();
        ct3.start();
        return true;
    }

    public SenderThread getCt2() {
        return ct2;
    }

    public ListenerThreads getCt3() {
        return ct3;
    }

    public static void main(String[] args){
        LuluChat my_app= new LuluChat();
        if (args.length != 2) {
            System.out.println("Usage: java EchoClient <EchoServer host> <EchoServer port>");
            System.exit(1);
        }

        final MainLayout mainLayout = new MainLayout();
        // Verify Connexion
        if (! my_app.checkConnecion(args[0],args[1])){
            System.out.println("sded");
            mainLayout.print_error_msg("ConnectError");
        }else{
            mainLayout.rm_modal();
        }
        mainLayout.addObserver(my_app.getCt2());

        //Ask user Username
        mainLayout.sh_user_name();



    }



}