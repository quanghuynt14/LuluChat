package com.mycompany.app.Client.threads;

import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Observable;
import java.util.Observer;

public class SenderThread extends Thread implements Observer {
    private BufferedReader buffreader;
    private PrintStream printStream;

    public SenderThread(BufferedReader var1, PrintStream var2) {
        this.buffreader = var1;
        this.printStream = var2;
    }

    public void run() {
        while(true) {
            try {
                String var1 = this.buffreader.readLine();
                this.printStream.println(var1);
            } catch (IOException var3) {
                System.out.println(var3);
            }
        }
    }
    @Override
    public void update(Observable o, Object arg) {
        try {
            JsonObject json = (JsonObject) arg;
            String type = json.get("type").getAsString();
            System.out.println(type);
            switch (type) {
                case "nameChange":
                    System.out.println("SenderThread as receive nameChange" + json.get("value").getAsString());
                    this.printStream.println(json);
                    break;
                default:
                    throw new Exception("Unknown type");
            }


        } catch (Exception e) {
            System.out.println("Unknown Exception : " + e);
        }

    }
}
