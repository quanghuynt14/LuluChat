/***
 * EchoServer
 * Example of a TCP server
 * Date: 10/01/04
 * Authors:
 */

package com.mycompany.app.Server;


import com.mycompany.app.Server.connexion.Connexion;
import com.mycompany.app.Server.manager.Manager;

import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.UUID;

public class Server  {

    public Manager getMy_manager() {
        return my_manager;
    }

    private Manager my_manager;

    public Server(){
        this.my_manager= new Manager();
    }



    public static void main(String args[]){
        Server server= new Server();
        //SocketManager mysockmanager= new SocketManager();
        ServerSocket listenSocket;


        if (args.length != 1) {
            System.out.println("Usage: java EchoServer <EchoServer port>");
            System.exit(1);
        }
        try {
            listenSocket = new ServerSocket(Integer.parseInt(args[0])); //port
            System.out.println("Server listening to port "+ args[0]);
            while (true) {
                // accept sockets
                Socket clientSocket = listenSocket.accept();
                System.out.println("Connexion from:" + clientSocket.getInetAddress());

                //create a connexion object and add to server_list
                Connexion new_connexion = new Connexion(clientSocket,server.getMy_manager());
                server.getMy_manager().add_user(new_connexion);
                new_connexion.start();
            }
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
    }
}




