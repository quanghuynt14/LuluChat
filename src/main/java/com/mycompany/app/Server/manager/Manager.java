package com.mycompany.app.Server.manager;

import com.google.gson.Gson;
import com.mycompany.app.Message.Message;
import com.mycompany.app.Server.connexion.Connexion;

import java.util.*;

public class Manager {
    private final Map<String, String> socket_id_to_room;
    private final Map<String, Map<String, Connexion>> room_id_to_sockets;

    public Manager() {
        this.socket_id_to_room = new HashMap<>();
        this.room_id_to_sockets = new HashMap<>();
    }

    public Map<String, String> getSocket_id_to_room() {
        return socket_id_to_room;
    }

    public void add_to_room(String id_room, Connexion client_connexion) {

        // Remove user to the room list
        String prev_id_room = this.socket_id_to_room.get(client_connexion.getUser_id());
        if (prev_id_room != null) {
            Map<String, Connexion> map_connect = room_id_to_sockets.get(prev_id_room);
            map_connect.remove(client_connexion.getUser_id());
        }

        // add client info to room
        if (!this.room_id_to_sockets.containsKey(id_room)) {
            HashMap<String, Connexion> connexions = new HashMap<>();
            connexions.put(client_connexion.getUser_id(), client_connexion);
            room_id_to_sockets.put(id_room, connexions);
        } else {
            room_id_to_sockets.get(id_room).put(client_connexion.getUser_id(), client_connexion);
        }

        // add room to client info
        this.socket_id_to_room.put(client_connexion.getUser_id(), id_room);
    }


    public void send_msg(Connexion my_connexion, String msg) {
        Map<String, Connexion> mapConnect = room_id_to_sockets.get(socket_id_to_room.get(my_connexion.getUser_id()));
        for (Map.Entry<String, Connexion> entry : mapConnect.entrySet()) {
            Connexion connexion = entry.getValue();
            if (connexion.getUser_id()==my_connexion.getUser_id()){
                connexion.send_msg(new Message("MsgSent",msg));
            }else {
                connexion.send_msg(new Message("MsgReceive",msg));
            }
        }
    }
    public void getAllRoom(Connexion my_connexion) {
        Gson g = new Gson();
        Map <String , ArrayList> mymap= new HashMap<>();
        mymap.put("value",new ArrayList<String>());
        System.out.println(room_id_to_sockets.entrySet());
        for (Map.Entry<String, Map<String,Connexion>> entry : room_id_to_sockets.entrySet()) {
            String connexion = entry.getKey();
            mymap.get("value").add(connexion);
        }
        String jsonString = g.toJson(mymap.get("value"));
        Message msg =new Message("ListRoom",jsonString);

        String final_json = g.toJson(msg);
        my_connexion.send_msg(final_json);
        System.out.println(final_json);
    }
    public void changeName(Connexion my_connexion,String value) {
        my_connexion.setUsername(value);
    }

    public void send_pv_msg(Connexion my_connexion, String msg) {
        my_connexion.send_msg(my_connexion.getMy_username()+" : "+ msg);
    }
    public void send_pv_userId(Connexion my_connexion, String msg) {
        my_connexion.send_msg(new Message("userId",msg));
    }

    public void add_user(Connexion new_connexion) {
        socket_id_to_room.put(new_connexion.getUser_id(), null);
    }
}
