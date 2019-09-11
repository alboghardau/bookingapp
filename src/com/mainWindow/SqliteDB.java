package com.mainWindow;

import jdk.nashorn.internal.ir.WhileNode;

import java.io.StringReader;
import java.sql.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;

public class SqliteDB {

    Connection connection = null;
    Statement statement = null;

    SqliteDB(){
        try{
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:Bookings.db");
            System.out.println("Connected to DB!");
            //connection.close();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Connection to DB failed!");
        }
    }

    //returns all rooms into ArrayList or null if no room is added
    public ArrayList<String> listRooms() {
        Integer id;
        String roomName;
        ArrayList<String> list= new ArrayList<String>();

        try {
            this.statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM rooms");
            while(resultSet.next()){
              id = resultSet.getInt("id");
              roomName = resultSet.getString("roomName");
              list.add(roomName);
              list.add(id.toString());
              System.out.println("Room id - "+id+" | Room name - "+roomName);
            };
        }catch (Exception e){
            System.out.println("Statement failed in listRooms");
            System.out.println(e.toString());
        }

        if(!list.isEmpty()){
            return list;
        }else{ return null;
        }
    }

    //return from database booked nights and room id in ArrayLisT
    public ArrayList<Integer> getBookedDaysInMonth(int room_id, int year, int month){
        ArrayList<Integer> list = new ArrayList<>();
        try (Statement statement = this.statement = connection.createStatement()) {
            this.statement = connection.createStatement();
            ResultSet set = statement.executeQuery("SELECT * FROM bookings WHERE room_id = '" + room_id+"'");

            YearMonth yearMonth = YearMonth.of(year,month);
            int daysInMonth = yearMonth.lengthOfMonth();


            while(set.next()){
                if(testDateFromString(set.getString("date_in"),set.getString("date_out"),year,month)){

                    for( int i = 1; i<= daysInMonth; i++){
                       if(checkDateBetween(LocalDate.of(year,month,i),LocalDate.parse(set.getString("date_in")),LocalDate.parse(set.getString("date_out")))){
                            list.add(set.getInt("id"));
                            list.add(i);
                       }
                    }
                }
            }
        }catch (Exception e){
            System.out.println("Failed to get booked months");
            System.out.println(e.toString());
        }

        System.out.println("Booked days array"+list.toString());
        return list;
    }

    //returs how many days a rooms is booked
    private static int daysBooked (String cIn, String cOut){
        int daysBooked = 1;
        int dayIn = Integer.parseInt(cIn.substring(8));
        int dayOut = Integer.parseInt(cOut.substring(8));
        daysBooked = dayOut - dayIn;

        return  daysBooked;
    }

    //checks if a date is between two other dayes
    private static boolean checkDateBetween(LocalDate date, LocalDate startDate, LocalDate endDate){
            if(date.isEqual(startDate) || date.isAfter(startDate) && date.isBefore(endDate)){
                return true;
            }else {
                return false;
            }
    }

    //checks if a booking entry is in the present month
    private static boolean testDateFromString(String cIn, String cOut,int year, int month){
        String m ;
        if(month < 10){
            m = "0"+month;
        }else{
            m = month+"";
        }
        if(cIn.substring(0,7).equals(year+"-"+m) || cOut.substring(0,7).equals(year+"-"+m)){
            return true;
        }else {
            return false;
        }
    }

    //adds booking to the database
    public void addBooking(int room_id,String name, String telephone, String date_in, String date_out, Double value){

        try{
            this.statement = connection.createStatement();
            statement.executeUpdate(String.format("INSERT INTO bookings (room_id,name,telephone,date_in,date_out,value) VALUES ('%d','%s','%s','%s','%s','%f')",room_id,name,telephone,date_in,date_out,value));
            System.out.println("Added new booking");

        }catch (Exception e){
            System.out.println("Failed to add booking");
            System.out.println(e.toString());
        }
    }

    //adds room to rooms
    public void addRoom(String roomName){
        try{
            this.statement = connection.createStatement();
            statement.executeUpdate(String.format("INSERT INTO rooms (roomName) VALUES('%s')",roomName));
        }catch (Exception e){
            System.out.println("Can't add new room!");
            System.out.println(e.toString());
        }
    }

    //deletes a room from
    public void deleteRoom(Integer id){
        try {
            this.statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("DELETE FROM rooms WHERE id="+id);
            System.out.println("Deleted room with id number ="+id.toString());
        }catch (Exception e){
            System.out.println("Failed delete statement on room id="+id);
            System.out.println(e.toString());
        }
    }

    //closes connection
    public void closeConnection(){
        try{
            connection.close();
        }catch (Exception e){
            System.out.println("DB close connection failed");
        }
    }
}
