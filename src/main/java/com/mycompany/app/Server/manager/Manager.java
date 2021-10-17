package com.mycompany.app.Server.manager;

import com.google.gson.Gson;
import com.mycompany.app.Message.Message;
import com.mycompany.app.Server.connexion.Connexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Manager {
    private final Map<String, String> socket_id_to_room;
    private final Map<String, Map<String, Connexion>> room_id_to_sockets;

    private final String jdbcUrl = "jdbc:sqlite:luluchat.db";
    private final Connection connection = DriverManager.getConnection(jdbcUrl);

    public Manager() throws SQLException {
        this.socket_id_to_room = new HashMap<>();
        this.room_id_to_sockets = new HashMap<>();
        this.retrieve_old_all_tables();
    }

    public Map<String, String> getSocket_id_to_room() {
        return socket_id_to_room;
    }

    public void add_to_room(String id_room, Connexion client_connexion) throws SQLException {

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

            for (Map.Entry<String, Map<String,Connexion>> entry : room_id_to_sockets.entrySet()) {
                Map<String,Connexion> my_map= entry.getValue();
                for (Map.Entry<String, Connexion> entry2 : my_map.entrySet()) {
                    Connexion conn = entry2.getValue();
                    getAllRoom(conn);
                }

            }
        } else {
            room_id_to_sockets.get(id_room).put(client_connexion.getUser_id(), client_connexion);
        }

        // add room to client info
        this.socket_id_to_room.put(client_connexion.getUser_id(), id_room);

        // create table id_room if not exists
        create_table_if_not_exists(id_room);

        // retrieve message from database
        retrieve_messages(id_room, client_connexion);

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
    public void send_msg(Connexion my_connexion, String msg) throws SQLException {
        // save msg to database
        save_message(my_connexion, msg);

        Map<String, Connexion> mapConnect = room_id_to_sockets.get(socket_id_to_room.get(my_connexion.getUser_id()));
        for (Map.Entry<String, Connexion> entry : mapConnect.entrySet()) {
            Connexion connexion = entry.getValue();
            if (Objects.equals(connexion.getUser_id(), my_connexion.getUser_id())){
                connexion.send_msg(new Message("MsgSent",msg));
            }else {
                connexion.send_msg(new Message("MsgReceive",msg));
            }
        }
    }
    public void change_name(Connexion my_connexion,String value) {
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

    public void create_table_if_not_exists(String id_room) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS " + id_room;
        sql += " (message_id INTEGER PRIMARY KEY AUTOINCREMENT, ";
        sql += " sender_id VARCHAR(50), ";
        sql += " content TEXT, ";
        sql += " created_at DATETIME);";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);

        System.out.println("Table " + id_room + " ok");
    }
    public void retrieve_old_all_tables() throws SQLException {
        ResultSet rs = connection.getMetaData().getTables(null, null, null, null);
        while (rs.next()) {
            System.out.println(rs.getString("TABLE_NAME"));
            String tablesname= rs.getString("TABLE_NAME");
            HashMap<String, Connexion> connexions = new HashMap<>();
            room_id_to_sockets.put(tablesname,connexions);
        }

    }

    public void retrieve_messages(String id_room, Connexion client_connexion) throws SQLException {
        String sql = "SELECT * FROM " + id_room ;
        sql += " ORDER BY created_at";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        ArrayList<Map<String,String>> finallist= new ArrayList<Map<String,String>>();
        while (resultSet.next()) {
            int message_id = resultSet.getInt("message_id");
            String sender_id = resultSet.getString("sender_id");
            String content = resultSet.getString("content");
            Date created_at = resultSet.getDate("created_at");
            Map<String,String> message = new HashMap<>();
            message.put("msg",content);
            message.put("from",sender_id);
            finallist.add(message);
            System.out.println(message_id + " | " + sender_id + " | " + content + " | " + created_at);
        }
        Gson g= new Gson();
        String jsonString = g.toJson(finallist);
        Message msg =new Message("AllMessage",jsonString);
        String final_json = g.toJson(msg);
        client_connexion.send_msg(final_json);
        System.out.println(final_json);


    }

    public void save_message(Connexion my_connexion, String msg) throws SQLException {
        String sql = "INSERT INTO " + socket_id_to_room.get(my_connexion.getUser_id()) + " (sender_id, content, created_at) VALUES ";
        sql += " ('" + my_connexion.getMy_username() + "', ";
        sql += "'" + msg + "', ";
        sql += "datetime());";
        Statement statement = connection.createStatement();
        int rows = statement.executeUpdate(sql);

        if (rows > 0) {
            System.out.println("A message inserted to database");
        }
    }
}
