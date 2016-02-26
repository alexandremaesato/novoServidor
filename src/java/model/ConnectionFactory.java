/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Guilherme
 */
public class ConnectionFactory {
    
    //static reference to itself
    private static ConnectionFactory instance = new ConnectionFactory();
     
    //private constructor
    private ConnectionFactory() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
     
    private Connection createConnection() {
        Connection connection = null;
        try {

            connection = DriverManager.getConnection("jdbc:mysql://localhost/app_prototipo", "root", "1234");
//            connection = DriverManager.getConnection("jdbc:mysql://localhost/restaurante?useUnicode=true&characterEncoding=UTF-8", "root", "");

        } catch (SQLException e) {
            System.out.println("ERROR: Unable to Connect to Database.");
        }
        return connection;
    }   
     
    public static Connection getConnection() {
        return instance.createConnection();
    }
    
}
