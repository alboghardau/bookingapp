package com.mainWindow;

import com.sun.javaws.util.JfxHelper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class FramesHelper {

    //returns dimensions for new frames
    public static Dimension setDimension(int height, int width){
        Dimension dimension = new Dimension();
        dimension.height = height;
        dimension.width = width;
        return dimension;
    }

    public static GridBagConstraints gridSettings(int x, int y,int insets){
        GridBagConstraints grid = new GridBagConstraints();
        grid.insets = new Insets(insets,insets,insets,insets);
        grid.gridx = x;
        grid.gridy = y;
        return grid;
    }

    //displays window to add new room
    public static void addRoomFrame(){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Add New Room");
        frame.setMinimumSize(setDimension(250,500));

        JPanel panel = new JPanel();
        frame.add(panel);

        JTextField roomName = new JTextField();
        panel.add(roomName);




        JButton addRoom = new JButton();
        addRoom.setText("Add Room");
        panel.add(addRoom);

        frame.setVisible(true);
    }
}
