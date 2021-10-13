package com.mycompany.app.Client.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

public class SenderThread extends Thread {
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
}
