/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libraryapp;

import java.sql.*;
import java.util.Scanner;

/**
 *
 * @author hhues
 */
public class LibraryApp {

    private static String url = "jdbc:postgresql://localhost:5432/library";
    private static String username = "postgres";
    private static String pwd = "";

    public static void login() {
        Scanner scan = new Scanner(System.in);
        // login / create account
        System.out.println("Welcome to the the library!");
        System.out.println("Login (L/l) or create account (C/c): ");
        String action = scan.nextLine();
        while (!(action.equalsIgnoreCase("L") || action.equalsIgnoreCase("C"))) {
            System.out.println("Login (L/l) or create account (C/c): ");
            action = scan.nextLine();
        }
        if (action.equalsIgnoreCase("L")) {
            // login stuff
        }
        else if (action.equalsIgnoreCase("C")) {
            // create user stuff
            //prompts user to login after creating an account
            login();
        }
    }
    
    // possibly make this recursive at some point or call it until it returns Q in main
    public static String homepage() {
        Scanner scan = new Scanner(System.in);
        System.out.println("---------------------------");
        System.out.println("1) Browse all books");
        System.out.println("2) Browse by author");
        System.out.println("3) Browse by genre");
        System.out.println("4) View your books");
        System.out.println("5) Return book");
        System.out.println("Q) Exit");
        System.out.println("Enter selection: ");
        String selection = scan.nextLine();
        while (!(selection.equals("1") || selection.equals("2") || selection.equals("3") ||
             selection.equals("4") || selection.equals("5") || selection.equalsIgnoreCase("Q")))
        {
            System.out.println("Enter selection: ");
            selection = scan.nextLine();
        }
        return selection;
    }
    
    public static void listAllAuthors() {
        System.out.println("List of all authors:");

        String selectQuery = "SELECT authorId, firstName, lastName FROM author";

        try (Connection connection = DriverManager.getConnection(url, username, pwd);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int authorId = resultSet.getInt("authorId");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                System.out.println(authorId + ") " + firstName + " " + lastName);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception properly in a real-world scenario
        }
    }
    
    public static void main(String[] args) {
        
        Scanner scan = new Scanner(System.in);
        
        // login or create account
        login();
        
        String action = homepage();
        
        while (!action.equalsIgnoreCase("Q"))
        {
            if (action.equals("1")) {
                
            }
            else if (action.equals("2")) {
                listAllAuthors();
                System.out.println("Enter selection: ");
                int selection = scan.nextInt();
                Author selAuthor = new Author(selection);
                selAuthor.listBooks();
                // do checkout stuff
            }
            else if  (action.equals("3")) {
                
            }
            else if  (action.equals("4")) {
                
            }
            else if  (action.equals("4")) {
                
            }
            action = homepage();
        }
        
    }
    
}
