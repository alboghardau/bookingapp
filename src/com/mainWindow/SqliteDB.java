package com.mainWindow;

import java.sql.*;
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
