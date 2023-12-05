/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libraryapp;
import java.sql.*;
/**
 *
 * @author Ligia
 */
public class DatabaseManager {
    private static String url = "jdbc:postgresql://localhost:5432/FinalProject";
    private static String username = "postgres";
    private static String pwd = "KT2me369121518";
    private static Connection connection;
    public static void setConnection(){
        try{
           Class.forName("org.postgresql.Driver");
           connection = DriverManager.getConnection(url,username, pwd);
        }
        catch(ClassNotFoundException e){
            System.out.println("Cannot load driver");
        }
        catch(SQLException e){
            System.out.println("SQLException");
        }
    }
    public static Connection getConnection(){
        return connection;
    }
    public static void closeConnection(){
        try{
            connection.close();
        }
        catch(SQLException e){
            System.out.println("SQLException");
        }
    }
}
