package com.mainWindow;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow {
    private JPanel panelMain;
    private JButton addRoom;

    public MainWindow(){
        addRoom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Muie PSD!");
                SqliteDB db = new SqliteDB();
                db.listRooms();
                db.closeConnection();
            }
        });
    }

    public static void main(String [] args){

        //sets the frame visible
        JFrame frame = new JFrame("Booking Manager");
        frame.setContentPane(new MainWindow().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setMinimumSize(FramesHelper.setDimension(500,500));
        frame.pack();
        frame.setVisible(true);
    }
}
