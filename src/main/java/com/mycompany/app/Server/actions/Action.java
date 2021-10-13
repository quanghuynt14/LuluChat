package com.mycompany.app.Server.actions;

import com.mycompany.app.Message.Message;
import com.mycompany.app.Server.connexion.Connexion;
import com.mycompany.app.Server.manager.Manager;

import java.net.Socket;
import java.sql.Struct;

public class Action extends Thread{
    String escapeCode = "\033[31m";
    String resetCode = "\033[0m";

    private Message msg;
    private Manager my_manager;
    private Connexion my_connexion;

    public Boolean getFailure() {
        return failure;
    }

    private Boolean failure;

    public void debug_print (String message,String Color){

        String ANSI_RESET = "\u001B[0m";
        String ANSI_BLACK = "\u001B[30m";
        String ANSI_RED = "\u001B[31m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_YELLOW = "\u001B[33m";
        String ANSI_BLUE = "\u001B[34m";
        String ANSI_PURPLE = "\u001B[35m";
        String ANSI_CYAN = "\u001B[36m";
        String ANSI_WHITE = "\u001B[37m";

        switch(Color){
            case "purple":
                System.out.println(ANSI_PURPLE+"[Action.java] : "+ message+ANSI_RESET);
                break;
            case "black":
                System.out.println(ANSI_BLACK+"[Action.java] : "+message+ANSI_RESET);
                break;
            case "red":
                System.out.println(ANSI_RED+"[Action.java] : "+message+ANSI_RESET);
                break;
            case "green":
                System.out.println(ANSI_GREEN+"[Action.java] : "+message+ANSI_RESET);
                break;
            case "yellow":
                System.out.println(ANSI_YELLOW+"[Action.java] : "+message+ANSI_RESET);
                break;
            case "blue":
                System.out.println(ANSI_BLUE+"[Action.java] : "+message+ANSI_RESET);
                break;
            case "cyan":
                System.out.println(ANSI_CYAN+"[Action.java] : "+message+ANSI_RESET);
                break;
            default:
                System.out.println(ANSI_WHITE+"[Action.java] : "+message+ANSI_RESET);
                break;
        }

    }

    public Action(Message msg, Connexion my_connexion, Manager my_manager){
        this.msg= msg;
        this.my_manager = my_manager;
        this.my_connexion = my_connexion;
        this.failure= false;
    }

    public void run() {
        debug_print("Beginning of Main Thread","green");
        try {
            if (this.msg.getType().equals("Entry")){
                debug_print("Detect Message of type `Entry`","cyan");
                String groupId= this.msg.getValue();
                my_manager.add_to_room(groupId,my_connexion);
            }else if(this.msg.getType().equals("Msg")){
                System.out.println("Detect msg msg");
                debug_print("Detect Message of type `Msg`","cyan");
                if (my_manager.getSocket_id_to_room().get(my_connexion.getCustom_id()) == null){
                    my_manager.send_pv_msg(my_connexion,"User cannot send message: No room for user :"+my_connexion.getCustom_id());
                }else {
                    my_manager.send_msg(my_connexion,this.msg.getValue());
                }

            }else if(this.msg.getType().equals("BadJson")){
                debug_print("Detect Message of type `BadJson`","cyan");
                String msgValue= this.msg.getValue();
                my_manager.send_pv_msg(my_connexion,msgValue);
            }else{
                debug_print("Unknown Type of type Message","yellow");
                my_manager.send_pv_msg(my_connexion,"Unknown Message Type");
            }
        }catch (Exception e){
            debug_print(e.toString(),"red");
            this.failure= true;
        }
        debug_print("End of Main Thread","green");

    }
}


