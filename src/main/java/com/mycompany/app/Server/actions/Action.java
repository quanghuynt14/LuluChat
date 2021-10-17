package com.mycompany.app.Server.actions;

import com.mycompany.app.Message.Message;
import com.mycompany.app.Server.connexion.Connexion;
import com.mycompany.app.Server.manager.Manager;

public class Action extends Thread {

    private final Message msg;
    private final Manager my_manager;
    private final Connexion my_connexion;

    public Action(Message msg, Connexion my_connexion, Manager my_manager) {
        this.msg = msg;
        this.my_manager = my_manager;
        this.my_connexion = my_connexion;
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
                System.out.println(ANSI_PURPLE + "[Action.java] : " + message + ANSI_RESET);
                break;
            case "black":
                System.out.println(ANSI_BLACK + "[Action.java] : " + message + ANSI_RESET);
                break;
            case "red":
                System.out.println(ANSI_RED + "[Action.java] : " + message + ANSI_RESET);
                break;
            case "green":
                System.out.println(ANSI_GREEN + "[Action.java] : " + message + ANSI_RESET);
                break;
            case "yellow":
                System.out.println(ANSI_YELLOW + "[Action.java] : " + message + ANSI_RESET);
                break;
            case "blue":
                System.out.println(ANSI_BLUE + "[Action.java] : " + message + ANSI_RESET);
                break;
            case "cyan":
                System.out.println(ANSI_CYAN + "[Action.java] : " + message + ANSI_RESET);
                break;
            default:
                System.out.println(ANSI_WHITE + "[Action.java] : " + message + ANSI_RESET);
                break;
        }
    }

    public void run() {
        debug_print("Beginning of Action.class Main Thread", "green");
        try {
            switch (msg.getType()) {
                case "Entry":
                    debug_print("Detect Message of type `Entry`", "cyan");
                    String groupId = msg.getValue();
                    my_manager.add_to_room(groupId, my_connexion);
                    break;
                case "Msg":
                    debug_print("Detect Message of type `Msg`", "cyan");
                    if (my_manager.getSocket_id_to_room().get(my_connexion.getUser_id()) == null) {
                        my_manager.send_pv_msg(my_connexion, "User cannot send message: No room for user :" + my_connexion.getUser_id());
                    } else {
                        my_manager.send_msg(my_connexion, msg.getValue());
                    }
                    break;
                case "BadJson":
                    debug_print("Detect Message of type `BadJson`", "cyan");
                    my_manager.send_pv_msg(my_connexion, msg.getValue());
                    break;
                case "getAllRoom":
                    debug_print("Detect Message of type `getAllRoom`", "cyan");
                    my_manager.getAllRoom(my_connexion);
                    break;
                case "nameChange":
                    debug_print("Detect Message of type `nameChange`", "cyan");
                    my_manager.change_name(my_connexion,msg.getValue());
                    break;
                default:
                    debug_print("Unknown Type of type Message", "yellow");
                    my_manager.send_pv_msg(my_connexion, "Unknown Message Type");
                    break;
            }
        } catch (Exception e) {
            debug_print("Error in Server" + e, "red");
        }
        debug_print("End of Action.class Main Thread", "green");
    }
}


