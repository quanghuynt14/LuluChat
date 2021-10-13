package com.mycompany.app.Server.connexion;

import com.google.gson.Gson;

import com.google.gson.JsonSyntaxException;
import com.mycompany.app.Message.Message;
import com.mycompany.app.Server.actions.Action;
import com.mycompany.app.Server.manager.Manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.UUID;



public class Connexion extends Thread{

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public Socket getMy_socket() {
        return my_socket;
    }

    private Socket my_socket;
    private String custom_id;
    private Manager my_manager;

    public void debug_print (String message,String Color){
        switch(Color){
            case "purple":
                System.out.println(ANSI_PURPLE+"[Connexion.java] : "+ message+ANSI_RESET);
                break;
            case "black":
                System.out.println(ANSI_BLACK+"[Connexion.java] : "+message+ANSI_RESET);
                break;
            case "red":
                System.out.println(ANSI_RED+"[Connexion.java] : "+message+ANSI_RESET);
                break;
            case "green":
                System.out.println(ANSI_GREEN+"[Connexion.java] : "+message+ANSI_RESET);
                break;
            case "yellow":
                System.out.println(ANSI_YELLOW+"[Connexion.java] : "+message+ANSI_RESET);
                break;
            case "blue":
                System.out.println(ANSI_BLUE+"[Connexion.java] : "+message+ANSI_RESET);
                break;
            case "cyan":
                System.out.println(ANSI_CYAN+"[Connexion.java] : "+message+ANSI_RESET);
                break;
            default:
                System.out.println(ANSI_WHITE+"[Connexion.java] : "+message+ANSI_RESET);
                break;
        }

    }
    public Connexion(Socket server_socket,Manager my_manager){
        this.my_socket = server_socket;
        this.custom_id = UUID.randomUUID().toString();
        this.my_manager=my_manager;
    }

    public String getCustom_id() {
        return custom_id;
    }


    /**
     * Main Listener Thread for a connexion
     * Running as long as a client is connected
     */
    public void run() {
        debug_print("Beggining of Connexion.Class Main Thread","green");
        try {
            BufferedReader socIn = null;
            socIn = new BufferedReader(new InputStreamReader(my_socket.getInputStream()));
            String line=null;
            while ((line = socIn.readLine())!=null) {
                debug_print("Msg Detected! : "+line,"cyan");
                Gson g = new Gson();
                Message msg= null;
                try{
                    msg= g.fromJson(line, Message.class);
                    if (msg == null){
                        throw new IllegalArgumentException("Detected an empty msg");
                    }
                }catch (JsonSyntaxException e){
                    debug_print("Not a good Json file detected: "+e.getMessage(),"yellow");
                    msg = new Message("BadJson","Little Dickhead, please send a correct message, not :"+line,
                            null);
                }catch (IllegalArgumentException e){
                    debug_print("Not a good Json file detected: "+e.getMessage(),"yellow");
                    msg = new Message("BadJson","Little Dickhead, please send a correct message, not :"+line,
                            null);
                }
                Action action = new Action(msg,this,my_manager);
                action.start();
            }
            my_socket.close();
            debug_print("End of Connexion.Class Main Thread","green");

        } catch (Exception e) {
            debug_print("Error in EchoServer"+e.toString(),"red");
            System.err.println("Error in EchoServer:" + e);
        }
    }
    public boolean isConnected(){
        boolean return_bool = false;
        try{
            return_bool=this.my_socket.getInetAddress().isReachable(50);
            System.out.println(this.my_socket.getInetAddress());
            System.out.println(return_bool);
        }catch (IOException e){
            debug_print("Excetion handled in Connexion.isConnected "+e.toString(),"red");
        }
        return return_bool;

    }

    public void send_msg(String msg) {
        try {
            PrintStream socOut = new PrintStream(this.my_socket.getOutputStream(),true);
            socOut.println(msg);
            socOut.flush();

        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
    }
}

