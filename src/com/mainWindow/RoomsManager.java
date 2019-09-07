package com.mainWindow;

import com.sun.deploy.panel.DeleteFilesDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class RoomsManager {

    public static JFrame frame;
    public static JPanel buttonsPanel;
    public static JPanel roomsPanel;

    public RoomsManager(){
        if(frame == null){
            frame = new JFrame();
        }
        if(buttonsPanel == null){
            buttonsPanel = new JPanel(new GridBagLayout());
        }
        if(roomsPanel == null){
            roomsPanel = new JPanel(new GridBagLayout());
        }

        addRoomFrame();
    }
    //displays window to add new room
    public static void addRoomFrame(){
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setTitle("Rooms Manager");
        frame.setSize(500,200);

        GridBagConstraints grid = new GridBagConstraints();
        //main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        frame.add(mainPanel);

        //buttons panel
        grid.insets = new Insets(10,10,10,10);
        grid.gridx = 0;
        JPanel buttonsPanel = new JPanel();
        mainPanel.add(buttonsPanel,grid);

        JButton addRoom = new JButton();
        addRoom.setText("New Room");
        mainPanel.add(addRoom);

        //rooms display panel
        grid.gridx = 1;
        roomsPanel.setAlignmentY(250);
        mainPanel.add(roomsPanel,grid);

        refreshRooms(roomsPanel);

        frame.setVisible(true);
    }

    public static void refreshRooms(JPanel panelRooms){
        SqliteDB db = new SqliteDB();
        ArrayList<String> roomsList= db.listRooms();
        db.closeConnection();

        panelRooms.removeAll();

        GridBagConstraints c = new GridBagConstraints();
        for(int i = 0; i < roomsList.size(); i = i +2){
            JLabel j = new JLabel(roomsList.get(i));
            c.gridy = i;
            c.insets = new Insets(10,10,10,10);
            panelRooms.add(j,c);

            final Integer id = Integer.parseInt(roomsList.get(i+1));
            JButton delete = new JButton();
            delete.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SqliteDB db = new SqliteDB();
                    db.deleteRoom(id);
                    db.closeConnection();
                    refreshRooms(panelRooms);
                }
            });
            delete.setText("Delete");
            panelRooms.add(delete,c);
        }

        panelRooms.revalidate();
        panelRooms.repaint();
    }
}
