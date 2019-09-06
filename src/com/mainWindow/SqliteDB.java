package com.mainWindow;

import java.sql.*;

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

    public void listRooms() {
        int id;
        String roomName;

        try {
            this.statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM rooms");
            while(resultSet.next()){
              id = resultSet.getInt("id");
              roomName = resultSet.getString("roomName");

              System.out.println("Room id - "+id+" | Room name - "+roomName);
            };
        }catch (Exception e){
            System.out.println("Statement failed in listRooms");
            System.out.println(e.toString());
        }

    }

    public void closeConnection(){
        try{
            connection.close();
        }catch (Exception e){
            System.out.println("DB close connection failed");
        }
    }
}
