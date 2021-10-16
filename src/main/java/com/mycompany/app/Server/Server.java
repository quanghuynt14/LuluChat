package com.mycompany.app.Server;

import com.mycompany.app.Server.connexion.Connexion;
import com.mycompany.app.Server.manager.Manager;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final Manager my_manager;

    public Server() {
        this.my_manager = new Manager();
    }

    public Manager getMy_manager() {
        return my_manager;
    }

    public static void main(String[] args) {

        Server server = new Server();
        ServerSocket listenSocket;

        if (args.length != 1) {
            System.out.println("Usage: java Server <Server port>");
            System.exit(1);
        }
        try {
            listenSocket = new ServerSocket(Integer.parseInt(args[0])); //port
            System.out.println("Server listening to port " + args[0]);
            while (true) {
                // accept sockets
                Socket clientSocket = listenSocket.accept();
                System.out.println("Connexion from:" + clientSocket.getInetAddress());

                //create a connexion object and add to server_list
                Connexion new_connexion = new Connexion(clientSocket, server.getMy_manager());
                server.getMy_manager().add_user(new_connexion);
                server.getMy_manager().send_pv_userId(new_connexion,new_connexion.getUser_id());
                new_connexion.start();
            }
        } catch (Exception e) {
            System.err.println("Error in Server:" + e);
        }
    }
}




