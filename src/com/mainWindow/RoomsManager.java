package com.mainWindow;

import javax.swing.*;
import java.util.ArrayList;

public class RoomsManager {

    public static JFrame frame;

    public RoomsManager(){
        if(frame == null){
            frame = new JFrame();
        }

        addRoomFrame();
    }
    //displays window to add new room
    public static void addRoomFrame(){
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setTitle("Add New Room");
        frame.setSize(500,200);

        JPanel panel = new JPanel();
        frame.add(panel);

        JTextField roomName = new JTextField();
        panel.add(roomName);

        JButton addRoom = new JButton();
        addRoom.setText("New Room");
        panel.add(addRoom);

        JPanel panelRooms = new JPanel();
        panelRooms.setAlignmentY(250);
        //frame.add(panelRooms);

        SqliteDB db = new SqliteDB();
        ArrayList<String> roomsList= db.listRooms();
        db.closeConnection();

        for(int i = 0; i < roomsList.size(); i++){
            JLabel j = new JLabel(roomsList.get(i));
            panelRooms.add(j);
        }

        frame.setVisible(true);
    }

}
