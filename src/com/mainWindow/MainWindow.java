package com.mainWindow;

import com.sun.xml.internal.messaging.saaj.soap.JpegDataContentHandler;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
        panelMain.add(panelLeftSide,FramesHelper.gridSettings(0,0,10));


        panelRightSide = new JPanel(new GridBagLayout());
        panelMain.add(panelRightSide,FramesHelper.gridSettings(1,0,10));

        panelTopButtons = new JPanel(new GridBagLayout());
        panelTopEditor = new JPanel(new GridBagLayout());
        panelCalendar = new JPanel(new GridBagLayout());
        panelRightSide.add(panelTopButtons,FramesHelper.gridSettings(0,0,10));
        panelRightSide.add(panelTopEditor,FramesHelper.gridSettings(0,1,10));
        panelRightSide.add(panelCalendar,FramesHelper.gridSettings(0,2,10));

        //buttons definition area
        buttonRoomsEditor = new JButton();
        buttonRoomsEditor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RoomsManager();
            }
        });
        buttonRoomsEditor.setText("Rooms Editor");
        panelTopButtons.add(buttonRoomsEditor,FramesHelper.gridSettings(0,0,10));

        buttonAddBooking = new JButton();
        buttonAddBooking.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayAddBooking();
                buttonAddBooking.setEnabled(false);
            }
        });
        buttonAddBooking.setText("Add Booking");
        panelTopButtons.add(buttonAddBooking);

        displaySelectors();
        displayCallendar(selectedYear,selectedMonth);

        frame.setMinimumSize(new Dimension(700,500));
        frame.pack();
        frame.setVisible(true);
    }

    private static void displaySelectors(){
        panelLeftSide.removeAll();

        String[] yearsArray = new String[] {"2019","2020"};
        for(int i = 0; i <yearsArray.length; i++){
            JButton buttonYear = new JButton();
            buttonYear.setText(yearsArray[i]);
            buttonYear.setPreferredSize(new Dimension(120,28));
            buttonYear.setMinimumSize(new Dimension(120,28));
            panelLeftSide.add(buttonYear,FramesHelper.gridSettings(0,i,5));
            final int year = Integer.parseInt(yearsArray[i]);
            if(Integer.parseInt(yearsArray[i]) == selectedYear){
                buttonYear.setEnabled(false);
            }
            buttonYear.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setYear(year);
                    buttonYear.setEnabled(false);
                    displaySelectors();
                }
            });
        }

        JSeparator separator = new JSeparator();
        separator.setOrientation(SwingConstants.HORIZONTAL);
        panelLeftSide.add(separator,FramesHelper.gridSettings(1+yearsArray.length,0,5));

        String[] monthsArray = new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

        for (int i = 0; i < monthsArray.length; i++) {
            JButton buttonMonth = new JButton();
            buttonMonth.setPreferredSize(new Dimension(120,28));
            buttonMonth.setMinimumSize(new Dimension(120,28));
            buttonMonth.setText(monthsArray[i]);
            panelLeftSide.add(buttonMonth,FramesHelper.gridSettings(0,1+i+yearsArray.length,5));
            final int month = i;
            if( i == selectedMonth) {
                buttonMonth.setEnabled(false);
            }
            buttonMonth.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setMonth(month);
                    buttonMonth.setEnabled(false);
                    displaySelectors();
                }
            });

        }

        panelLeftSide.revalidate();
        panelLeftSide.repaint();
        frame.revalidate();
        frame.repaint();
    }

    private static void displayCallendar(int year, int month){

        panelCalendar.removeAll();

//        String [] days = new String[] {"MON","TUE","WED","THU","FRY","SAT","SUN"};
//        for (int i = 0; i < days.length; i++) {
//            JLabel label = new JLabel();
//            label.setText(days[i]);
//            panelCalendar.add(label,FramesHelper.gridSettings(i,0,5));
//        }

        SqliteDB db = new SqliteDB();
        ArrayList<String> rooms = db.listRooms();
        db.closeConnection();

        Calendar c = new GregorianCalendar(year,month,1);
        YearMonth yearMonth = YearMonth.of(year,month+1);
        int daysInMonth = yearMonth.lengthOfMonth();
        System.out.println(daysInMonth);

        for(Integer j = 0; j < rooms.size(); j = j+2) {
            JLabel label = new JLabel();
            label.setText(rooms.get(j));
            panelCalendar.add(label, FramesHelper.gridSettings(0, j+2, 2));

            for (Integer i = 1; i < daysInMonth + 1; i++) {

                JLabel labelRoom = new JLabel();
                labelRoom.setText(i.toString());
                panelCalendar.add(labelRoom, FramesHelper.gridSettings(i, j+1, 2));

                label = new JLabel();

                label.setMaximumSize(new Dimension(32,24));
                label.setIcon(new javax.swing.ImageIcon(ImageIcon.class.getResource("/check-box.png")));
                panelCalendar.add(label, FramesHelper.gridSettings(i, j+2, 2));
            }
        }

        panelCalendar.revalidate();
        panelCalendar.repaint();
    }

    private static void displayAddBooking(){
        panelTopEditor.removeAll();

        JLabel nameLabel = new JLabel();
        nameLabel.setText("Name:");
        panelTopEditor.add(nameLabel,FramesHelper.gridSettings(0,0,5));

        JTextField nameText = new JTextField();
        nameText.setPreferredSize(new Dimension(150,28));
        nameText.setMinimumSize(new Dimension(150,28));
        panelTopEditor.add(nameText,FramesHelper.gridSettings(1,0,5));

        JLabel phoneLabel = new JLabel();
        phoneLabel.setText("Phone:");
        panelTopEditor.add(phoneLabel,FramesHelper.gridSettings(2,0,5));

        JTextField phoneText = new JTextField();
        phoneText.setPreferredSize(new Dimension(150,28));
        phoneText.setMinimumSize(new Dimension(150,28));
        panelTopEditor.add(phoneText,FramesHelper.gridSettings(3,0,5));

        JButton closeEditor = new JButton();
        closeEditor.setText("Close");
        closeEditor.setPreferredSize(new Dimension(120,28));
        closeEditor.setMinimumSize(new Dimension(120,28));
        panelTopEditor.add(closeEditor,FramesHelper.gridSettings(4,0,5));
        closeEditor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonAddBooking.setEnabled(true);
                panelTopEditor.removeAll();
                panelTopEditor.revalidate();
                panelTopEditor.repaint();
            }
        });

        JLabel checkinLabel = new JLabel();
        checkinLabel.setText("Check in:");
        panelTopEditor.add(checkinLabel,FramesHelper.gridSettings(0,1,5));

        JTextField checkinText = new JTextField();
        checkinText.setPreferredSize(new Dimension(150,28));
        checkinText.setMinimumSize(new Dimension(150,28));
        panelTopEditor.add(checkinText,FramesHelper.gridSettings(1,1,5));

        JLabel checkoutLabel = new JLabel();
        checkoutLabel.setText("Check out:");
        panelTopEditor.add(checkoutLabel,FramesHelper.gridSettings(2,1,5));

        JTextField checkoutText = new JTextField();
        checkoutText.setPreferredSize(new Dimension(150,28));
        checkoutText.setMinimumSize(new Dimension(150,28));
        panelTopEditor.add(checkoutText,FramesHelper.gridSettings(3,1,5));

        JButton addEdit = new JButton();
        addEdit.setText("Add");
        addEdit.setPreferredSize(new Dimension(120,28));
        addEdit.setMinimumSize(new Dimension(120,28));
        panelTopEditor.add(addEdit,FramesHelper.gridSettings(4,1,5));

        panelTopEditor.revalidate();
        panelTopEditor.repaint();
    }

    private static void setMonth(int month){
        selectedMonth = month;
        displayCallendar(selectedYear,selectedMonth);
    }

    private static void setYear(int year){
        selectedYear = year;
        displayCallendar(selectedYear,selectedMonth);
    }

    public static void main(String [] args){

        if(selectedMonth == null){
            selectedMonth = Calendar.getInstance().get(Calendar.MONTH);
        }
        if(selectedYear == null){
            selectedYear =  Calendar.getInstance().get(Calendar.YEAR);;
        }
        initialDisplay();


    }
}
