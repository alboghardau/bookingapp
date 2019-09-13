package com.mainWindow;

import com.sun.xml.internal.messaging.saaj.soap.JpegDataContentHandler;
import org.sqlite.util.StringUtils;
import sun.security.x509.FreshestCRLExtension;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
            final int month = i+1;
            System.out.println("Luna:"+selectedMonth+1);
            if( i == selectedMonth-1) {
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

        System.out.println(year+"+"+month);

//        String [] days = new String[] {"MON","TUE","WED","THU","FRY","SAT","SUN"};
//        for (int i = 0; i < days.length; i++) {
//            JLabel label = new JLabel();
//            label.setText(days[i]);
//            panelCalendar.add(label,FramesHelper.gridSettings(i,0,5));
//        }

        SqliteDB db = new SqliteDB();
        ArrayList<String> rooms = db.listRooms();

        Calendar c = new GregorianCalendar(year,month,1);
        YearMonth yearMonth = YearMonth.of(year,month);
        int daysInMonth = yearMonth.lengthOfMonth();
        System.out.println(daysInMonth);

        for(Integer j = 0; j < rooms.size(); j = j+2) {
            int roomId = Integer.parseInt(rooms.get(j+1));

            JLabel label1 = new JLabel();
            label1.setText(rooms.get(j));
            panelCalendar.add(label1, FramesHelper.gridSettings(0, j+2, 2));
            System.out.println("Luna:"+selectedMonth);
            ArrayList<Integer> bookedList = db.getBookedDaysInMonth(roomId,selectedYear,selectedMonth);

            for (Integer i = 1; i < daysInMonth + 1; i++) {
                final int day = i;

                JLabel labelRoom = new JLabel();
                labelRoom.setText(i.toString());
                panelCalendar.add(labelRoom, FramesHelper.gridSettings(i, j+1, 2));

                if(isBooked(bookedList,i) > 0){
                    final int d = isBooked(bookedList,i);
                    final JLabel label = new JLabel();
                    label.setMaximumSize(new Dimension(32,24));
                    label.setIcon(new javax.swing.ImageIcon(ImageIcon.class.getResource("/check-box-green.png")));
                    panelCalendar.add(label, FramesHelper.gridSettings(i, j+2, 0));
                    label.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            displayBooking(d);
                            //label.setIcon(new javax.swing.ImageIcon(ImageIcon.class.getResource("/check-box-green.png")));
                        }
                    });
                }else {
                    final JLabel label = new JLabel();
                    label.setMaximumSize(new Dimension(32, 24));
                    label.setIcon(new javax.swing.ImageIcon(ImageIcon.class.getResource("/check-box.png")));
                    panelCalendar.add(label, FramesHelper.gridSettings(i, j + 2, 0));
                    label.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            SqliteDB db2 = new SqliteDB();
                            int avialbleDays = db2.isAvailable(LocalDate.of(selectedYear,selectedMonth,day),roomId);
                            db2.closeConnection();
                            addBookingFromList(day, roomId,avialbleDays);
                            //label.setIcon(new javax.swing.ImageIcon(ImageIcon.class.getResource("/check-box-green.png")));
                        }
                    });
                }
            }
        }

        db.closeConnection();
        panelCalendar.revalidate();
        panelCalendar.repaint();
    }

    //tests if a day is booked or not, RETURNS Booking id
    private static int isBooked(ArrayList<Integer> list,int day){
            for(int i = 0; i < list.size(); i= i+2){
                if(list.get(i+1) == day){
                    return list.get(i);
                }
        }
        return 0;
    }

    private static void displayBooking(int id){
        panelTopEditor.removeAll();

        SqliteDB db = new SqliteDB();
        ArrayList<String> result = db.getBooking(id); //roomId,name,phone,dateIn,dateOut,value
        db.closeConnection();

        JLabel nameLabel = new JLabel("Name:");
        panelTopEditor.add(nameLabel,FramesHelper.gridSettings(0,0,5));

        JLabel nameText = new JLabel(result.get(1));
        panelTopEditor.add(nameText,FramesHelper.gridSettings(1,0,5));

        JLabel phoneLabel = new JLabel("Phone:");
        panelTopEditor.add(phoneLabel,FramesHelper.gridSettings(2,0,5));

        JLabel phoneText = new JLabel(result.get(2));
        panelTopEditor.add(phoneText,FramesHelper.gridSettings(3,0,5));

        JLabel checkinDate  = new JLabel("Check in:");
        panelTopEditor.add(checkinDate, FramesHelper.gridSettings(4,0,5));

        JLabel checkinText = new JLabel(result.get(3));
        panelTopEditor.add(checkinText,FramesHelper.gridSettings(5,0,5));

        JLabel checkoutLabel = new JLabel("Check out:");
        panelTopEditor.add(checkoutLabel,FramesHelper.gridSettings(6,0,5));

        JLabel checkoutText = new JLabel(result.get(4));
        panelTopEditor.add(checkoutText,FramesHelper.gridSettings(7,0,5));

        JLabel valueLabel = new JLabel("Value - RON ");
        panelTopEditor.add(valueLabel,FramesHelper.gridSettings(8,0,5));

        JLabel valueText = new JLabel(result.get(5));
        panelTopEditor.add(valueText,FramesHelper.gridSettings(9,0,5));

        JButton deleteButton = new JButton("Delete");
        panelTopEditor.add(deleteButton,FramesHelper.gridSettings(10,0,5));
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SqliteDB db = new SqliteDB();
                db.deleteBooking(id);
                db.closeConnection();
                panelTopEditor.removeAll();
                panelTopEditor.revalidate();
                panelTopEditor.repaint();
                displayCallendar(selectedYear,selectedMonth);
            }
        });


        panelTopEditor.revalidate();
        panelTopEditor.repaint();
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
                displayCallendar(selectedYear,selectedMonth);
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

    private static void addBookingFromList(int dayOfTheMonth, int roomId, int availableDays){

        LocalDate localDate = LocalDate.of(selectedYear,selectedMonth,dayOfTheMonth);
        System.out.println("Add booking from date "+localDate);

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

        JLabel sumLabel = new JLabel("Sum:");
        panelTopEditor.add(sumLabel,FramesHelper.gridSettings(4,0,5));

        JTextField sumText = new JTextField();
        sumText.setPreferredSize(new Dimension(150,28));
        sumText.setMinimumSize(new Dimension(150,28));
        panelTopEditor.add(sumText,FramesHelper.gridSettings(5,0,5));

        //BUTTON TO CLOSE ADD BOOKING EDITOR
        JButton closeEditor = new JButton();
        closeEditor.setText("Close");
        closeEditor.setPreferredSize(new Dimension(120,28));
        closeEditor.setMinimumSize(new Dimension(120,28));
        panelTopEditor.add(closeEditor,FramesHelper.gridSettings(6,0,5));
        closeEditor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonAddBooking.setEnabled(true);
                panelTopEditor.removeAll();
                panelTopEditor.revalidate();
                panelTopEditor.repaint();
                displayCallendar(selectedYear,selectedMonth);
            }
        });

        JLabel checkinLabel = new JLabel();
        checkinLabel.setText("Check in:");
        panelTopEditor.add(checkinLabel,FramesHelper.gridSettings(0,1,5));

        JTextField checkinText = new JTextField();
        checkinText.setEnabled(false);
        checkinText.setText(localDate.toString());
        checkinText.setPreferredSize(new Dimension(150,28));
        checkinText.setMinimumSize(new Dimension(150,28));
        panelTopEditor.add(checkinText,FramesHelper.gridSettings(1,1,5));

        JLabel nightsLabel = new JLabel();
        nightsLabel.setText("Nights stay:");
        panelTopEditor.add(nightsLabel,FramesHelper.gridSettings(2,1,5));

        JTextField nightsText = new JTextField();
        nightsText.setText("1");
        nightsText.setPreferredSize(new Dimension(150,28));
        nightsText.setMinimumSize(new Dimension(150,28));
        panelTopEditor.add(nightsText,FramesHelper.gridSettings(3,1,5));

        //BUTTON TO ADD BOOKING
        JButton addEdit = new JButton();
        addEdit.setText("Add");
        addEdit.setPreferredSize(new Dimension(120,28));
        addEdit.setMinimumSize(new Dimension(120,28));
        panelTopEditor.add(addEdit,FramesHelper.gridSettings(6,1,5));
        addEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Integer.parseInt(nightsText.getText()) > availableDays) {
                    JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame, "Not enough nights available");
                }else if(nameText.getText() != "" && phoneText.getText() != "" && isNumeric(sumText.getText())){

                    LocalDate checkoutDate = LocalDate.of(selectedYear,selectedMonth,dayOfTheMonth).plusDays(Integer.parseInt(nightsText.getText()));
                    System.out.println(checkoutDate.toString());

                    SqliteDB db = new SqliteDB();
                    db.addBooking(roomId,nameText.getText(),phoneText.getText(),checkinText.getText(),checkoutDate.toString(),Double.parseDouble(sumText.getText()));
                    db.closeConnection();
                    buttonAddBooking.setEnabled(true);
                    panelTopEditor.removeAll();
                    panelTopEditor.revalidate();
                    panelTopEditor.repaint();
                    displayCallendar(selectedYear,selectedMonth);
                    db.closeConnection();
                }else{
                    JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame, "Name, phone or sum not set!");
                }
            }
        });

        JLabel availableLabel = new JLabel();
        availableLabel.setText("MAX: "+availableDays);
        panelTopEditor.add(availableLabel,FramesHelper.gridSettings(4,1,5));

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

    //CHECKS IF A STRING IS NUMERIC
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public static void main(String [] args){

        if(selectedMonth == null){
            selectedMonth = Calendar.getInstance().get(Calendar.MONTH)+1;
            System.out.println("Calendar:getInstance()MONTH-"+selectedMonth);
        }
        if(selectedYear == null){
            selectedYear =  Calendar.getInstance().get(Calendar.YEAR);;
        }
        initialDisplay();

        testStuff();
    }

    private static void testStuff(){


    }
}
