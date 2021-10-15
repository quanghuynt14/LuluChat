package com.mycompany.app.Server.connexion;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mycompany.app.Message.Message;
import com.mycompany.app.Server.actions.Action;
import com.mycompany.app.Server.manager.Manager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.UUID;

public class Connexion extends Thread {

    private final Socket my_socket;
    private final String user_id;
    private final Manager my_manager;
    private String my_username;

    public Connexion(Socket server_socket, Manager my_manager) {
        this.my_socket = server_socket;
        this.user_id = UUID.randomUUID().toString();
        this.my_manager = my_manager;
        my_username = null;
    }
    public void setUsername(String value){
        this.my_username = value;
    }


    public String getUser_id() {
        return user_id;
    }

    public String getMy_username() {
        String return_value = my_username!=null ?  my_username :  "Private person";
        return return_value;
    }

    public void debug_print(String message, String Color) {

        String ANSI_RESET = "\u001B[0m";
        String ANSI_BLACK = "\u001B[30m";
        String ANSI_RED = "\u001B[31m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_YELLOW = "\u001B[33m";
        String ANSI_BLUE = "\u001B[34m";
        String ANSI_PURPLE = "\u001B[35m";
        String ANSI_CYAN = "\u001B[36m";
        String ANSI_WHITE = "\u001B[37m";

        switch (Color) {
            case "purple":
                System.out.println(ANSI_PURPLE + "[Connexion.java] : " + message + ANSI_RESET);
                break;
            case "black":
                System.out.println(ANSI_BLACK + "[Connexion.java] : " + message + ANSI_RESET);
                break;
            case "red":
                System.out.println(ANSI_RED + "[Connexion.java] : " + message + ANSI_RESET);
                break;
            case "green":
                System.out.println(ANSI_GREEN + "[Connexion.java] : " + message + ANSI_RESET);
                break;
            case "yellow":
                System.out.println(ANSI_YELLOW + "[Connexion.java] : " + message + ANSI_RESET);
                break;
            case "blue":
                System.out.println(ANSI_BLUE + "[Connexion.java] : " + message + ANSI_RESET);
                break;
            case "cyan":
                System.out.println(ANSI_CYAN + "[Connexion.java] : " + message + ANSI_RESET);
                break;
            default:
                System.out.println(ANSI_WHITE + "[Connexion.java] : " + message + ANSI_RESET);
                break;
        }
    }

    /**
     * Main Listener Thread for a connexion
     * Running as long as a client is connected
     */
    public void run() {
        debug_print("Beginning of Connexion.class Main Thread", "green");
        try {
            BufferedReader socIn = new BufferedReader(new InputStreamReader(my_socket.getInputStream()));
            String line;
            while ((line = socIn.readLine()) != null) {
                debug_print("Msg Detected! : " + line, "cyan");
                Gson g = new Gson();
                Message msg;
                try {
                    msg = g.fromJson(line, Message.class);
                    if (msg == null) {
                        throw new IllegalArgumentException("Detected an empty msg");
                    }
                } catch (JsonSyntaxException | IllegalArgumentException e) {
                    debug_print("Not a good Json file detected: " + e.getMessage(), "yellow");
                    msg = new Message("BadJson", "Little Dickhead, please send a correct message, not :" + line,
                            null);
                }
                Action action = new Action(msg, this, my_manager);
                action.start();
            }
            my_socket.close();
            socIn.close();
            debug_print("End of Connexion.Class Main Thread", "green");

        } catch (Exception e) {
            debug_print("Error in Server" + e, "red");
        }
    }

    public void send_msg(String msg) {
        try {
            PrintStream socOut = new PrintStream(this.my_socket.getOutputStream(), true);
            socOut.println(msg);
        } catch (Exception e) {
            debug_print("Error in Server" + e, "red");
        }
    }
}

