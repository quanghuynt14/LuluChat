package com.mycompany.app.Client.threads;

import java.io.*;
import java.net.*;

public class ListenerThreads extends Thread {

    private BufferedReader buffreader;
    String line;
    public ListenerThreads(BufferedReader buffreader) {
        this.buffreader = buffreader;
    }

    public void run() {
        while (getLine()){
                System.out.println("Msg - receive: " + this.line);
        }

    }
    public boolean getLine(){
        try{
            String buff_reader= buffreader.readLine();
            if(buff_reader== null){

                return false;
            }else{
                this.line= buff_reader;
                return true;
            }

        }catch(IOException e){
            return false;
        }
    }

}


