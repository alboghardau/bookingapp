package com.mainWindow;

import com.sun.xml.internal.messaging.saaj.soap.JpegDataContentHandler;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainWindow {
    private static JFrame frame;
    private static JPanel panelMain;
    private static JPanel panelLeftSide;
    private static JPanel panelRightSide;
    private static JPanel panelTopButtons;
    private static JPanel panelTopEditor;
    private static JPanel panelCalendar;
    private static JButton buttonRoomsEditor;
    private static JButton buttonAddBooking;
    private static Integer selectedMonth;
    private static Integer selectedYear;

    public MainWindow(){

    }

    private static void initialDisplay(){
        frame = new JFrame("Booking Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GridBagConstraints grid = new GridBagConstraints();

        //panels definition area
        panelMain = new JPanel(new GridBagLayout());
        frame.add(panelMain);

        panelLeftSide = new JPanel(new GridBagLayout());
        panelMain.add(panelLeftSide,FramesHelper.gridSettings(0,0));

        panelRightSide = new JPanel(new GridBagLayout());
        panelMain.add(panelRightSide,FramesHelper.gridSettings(1,0));

        panelTopButtons = new JPanel(new GridBagLayout());
        panelTopEditor = new JPanel(new GridBagLayout());
        panelCalendar = new JPanel(new GridBagLayout());
        panelRightSide.add(panelTopButtons,FramesHelper.gridSettings(0,0));
        panelRightSide.add(panelTopEditor,FramesHelper.gridSettings(1,0));
        panelRightSide.add(panelCalendar,FramesHelper.gridSettings(2,0));

        //buttons definition area
        buttonRoomsEditor = new JButton();
        buttonRoomsEditor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RoomsManager();
            }
        });
        buttonRoomsEditor.setText("Rooms Editor");
        panelTopButtons.add(buttonRoomsEditor,FramesHelper.gridSettings(0,0));

        buttonAddBooking = new JButton();
        buttonAddBooking.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        buttonAddBooking.setText("Add Booking");
        panelTopButtons.add(buttonAddBooking);

        displaySelectors();

        frame.setMinimumSize(new Dimension(700,500));
        frame.pack();
        frame.setVisible(true);
    }

    private static void displaySelectors(){
        String[] yearsArray = new String[] {"2019","2020"};
        for(int i = 0; i <yearsArray.length; i++){
            JButton buttonYear = new JButton();
            buttonYear.setText(yearsArray[i]);
            panelLeftSide.add(buttonYear,FramesHelper.gridSettings(0,i));
            if(Integer.parseInt(yearsArray[i]) == selectedYear){
                buttonYear.setEnabled(false);
            }
        }

        String[] monthsArray = new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

        for (int i = 0; i < monthsArray.length; i++) {
            JButton buttonMonth = new JButton();
            buttonMonth.setText(monthsArray[i]);
            panelLeftSide.add(buttonMonth,FramesHelper.gridSettings(0,i+yearsArray.length));
            if( i+1 == selectedMonth) {
                buttonMonth.setEnabled(false);
            }
        }
    }

    public static void main(String [] args){
        if(selectedMonth == null){
            selectedMonth = 9;
        }
        if(selectedYear == null){
            selectedYear = 2019;
        }
        initialDisplay();

    }
}
