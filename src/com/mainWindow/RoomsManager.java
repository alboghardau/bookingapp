package com.mainWindow;

import com.sun.deploy.panel.DeleteFilesDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class RoomsManager {

    public static JFrame frame;
    public static JPanel addRoomPanel;
    public static JPanel buttonsPanel;
    public static JPanel roomsPanel;
    public static JButton addRoom;

    //contructor that checks for only one variable
    public RoomsManager(){
//        if(frame == null){
            frame = new JFrame();
//        }
//        if(buttonsPanel == null){
            buttonsPanel = new JPanel(new GridBagLayout());
//        }
//        if(roomsPanel == null){
            roomsPanel = new JPanel(new GridBagLayout());
//        }
//        if(addRoomPanel == null){
            addRoomPanel = new JPanel(new GridBagLayout());
//        }
        addRoomFrame();
    }
    //displays window to add new room
    public static void addRoomFrame(){
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setTitle("Rooms Manager");
        frame.setMinimumSize(new Dimension(700,500));
        frame.setSize(700,500);

        GridBagConstraints grid = new GridBagConstraints();
        grid.insets = new Insets(10,10,10,10);
        //main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        frame.add(mainPanel);

        //add room panel
        grid.gridy = 0;
        mainPanel.add(addRoomPanel,grid);

        //buttons panel
        grid.gridy = 1;
        mainPanel.add(buttonsPanel,grid);
        addRoom = new JButton();
        addRoom.setText("New Room");
        addRoom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayAddRoom();
                addRoom.setEnabled(false);
            }
        });
        buttonsPanel.add(addRoom);
        mainPanel.add(buttonsPanel,grid);


        //rooms display panel
        grid.gridy = 2;
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

    //display add room section into frame
    public static void displayAddRoom(){
        GridBagConstraints grid = new GridBagConstraints();
        grid.insets = new Insets(10,10,10,10);
        JLabel label = new JLabel();
        label.setText("Room Name");
        grid.gridx = 0;
        addRoomPanel.add(label,grid);

        JTextField text = new JTextField();
        text.setPreferredSize(new Dimension(200,24));
        grid.gridx = 1;
        addRoomPanel.add(text,grid);

        JButton button = new JButton();
        button.setText("Add");
        grid.gridx = 2;
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String textValue = text.getText();
                SqliteDB db = new SqliteDB();
                db.addRoom(textValue);
                db.closeConnection();
                addRoomPanel.removeAll();
                addRoomPanel.revalidate();
                addRoomPanel.repaint();
                addRoom.setEnabled(true);
                refreshRooms(roomsPanel);
            }
        });
        addRoomPanel.add(button,grid);

        addRoomPanel.revalidate();
        addRoomPanel.repaint();
        frame.revalidate();
        frame.repaint();
    }
}
