package com.mycompany.app.Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Layout {
    private JPanel MainPanel;
    private JLabel MainLabel;
    private JTextField textField1;
    private JFrame frame;

    public Layout() {
        textField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               System.out.println(textField1.getText());
            }
        });

    }
    public void printErrormsg() {
        System.out.println("eee");
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run(){
                textField1.setText("Error");
            }
        });

        frame.setVisible(true);
        frame.repaint();

    }
    public void init() {
        frame = new JFrame("My main Jframe");
        frame.setContentPane(new Layout().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

