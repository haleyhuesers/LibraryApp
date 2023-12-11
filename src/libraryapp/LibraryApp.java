/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libraryapp;

import java.sql.*;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author hhues
 */
public class LibraryApp {

    public static User login() {
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
            System.out.print("Please enter email: ");
                String email = scan.nextLine().strip();
                System.out.print("Please enter password: ");
                String password = scan.nextLine().strip();
                User user = new User();
                user.email = email;
                user.password = password;
                if (user.loginUser()){
                    System.out.println("Login successful!");
                    return user;
                }
                else{
                    System.out.println("Invalid login credentials");
                    return login();
                }
        }
        else  {
            User newUser = new User();
            System.out.print("Enter first name: ");
            newUser.firstname = scan.nextLine().strip();
            System.out.print("Enter last name: ");
            newUser.lastname = scan.nextLine().strip();
            System.out.print("Enter email: ");
            newUser.email = scan.nextLine().strip();
            System.out.print("Enter password: ");
            newUser.password = scan.nextLine().strip();
            newUser.registerUser();
            return login();
        }
    }
    
    // possibly make this recursive at some point or call it until it returns Q in main
    public static String homepage(User user) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Hello, "+user.firstname+" "+user.lastname);
        System.out.println("---------------------------");
        System.out.println("1) Browse all books");
        System.out.println("2) Browse by author");
        System.out.println("3) Browse by genre");
        System.out.println("4) View your books");
        System.out.println("5) Return book");
        System.out.println("6) Edit information");
        System.out.println("7) Delete profile");
        System.out.println("Q) Exit");
        System.out.println("Enter selection: ");
        String selection = scan.nextLine();
        while (!(selection.equals("1") || selection.equals("2") || selection.equals("3") ||
             selection.equals("4") || selection.equals("5") || selection.equals("6") || 
                selection.equals("7") || selection.equalsIgnoreCase("Q")))
        {
            System.out.println("Enter selection: ");
            selection = scan.nextLine();
        }
        return selection;
    }
    
    public static void listAllAuthors() {
        System.out.println("Authors:");

        String selectQuery = "SELECT authorId, firstName, lastName FROM author";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int authorId = resultSet.getInt("authorId");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                System.out.println(authorId + ") " + firstName + " " + lastName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void listAllBooks() {
        System.out.println("Books:");

        String selectQuery = "SELECT bookid, title FROM book";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String id = resultSet.getString("bookid");
                System.out.println(id + ") " + title);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
//public static void listCheckedOutBooks(User user) {
//        try (Connection connection = DatabaseManager.getConnection()) {
//        String sql = "SELECT b.* FROM book b " +
//                     "JOIN checked_out co ON b.bookID = co.bookID " +
//                     "WHERE co.userID = ?";
//
//        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//            preparedStatement.setInt(1, user.getId());
//
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                while (resultSet.next()) {
//                    int bookID = resultSet.getInt("bookID");
//                    String title = resultSet.getString("title");
//                    System.out.println(bookID + ") " + title);
//                }
//            }
//        }
//    } catch (SQLException e) {
//        e.printStackTrace();
//    }
//}    

     public static void EditUser(User user)
    {
        Scanner scan = new Scanner(System.in);
        System.out.print("Please enter your new first name "
                                + "(or press enter without typing to keep it the same): ");
        String newFname = scan.nextLine().strip();
        if (newFname.equals("")){
            newFname = user.firstname;
        }
        System.out.print("Please enter your new last name "
                                + "(or press enter without typing to keep it the same): ");
        String newLname = scan.nextLine().strip();
        if (newLname.equals("")){
            newLname = user.lastname;
        }
        System.out.print("Please enter your new email "
                                + "(or press enter without typing to keep it the same): ");
        String newEmail = scan.nextLine().strip();
        if (newEmail.equals("")){
            newEmail = user.email;
        }
        System.out.println("Name: "+newFname+" "+newLname);
        System.out.println("Email: "+newEmail);
        System.out.print("Confirm update by typing Y, or type N to go back.");
        String confirm = scan.nextLine();
        if (confirm.equalsIgnoreCase("y")){
            user.firstname = newFname;
            user.lastname = newLname;
            user.email = newEmail;
            user.updateUserDetails();
        }
    }
     
    public static void browseByGenre() 
    {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter genre name to browse books: ");
        String genreName = scan.nextLine().trim();

        // Find the genre ID based on the genre name
        int genreId = getGenreIdByName(genreName);
        if (genreId == -1) {
            System.out.println("Genre not found.");
            return;
        }

        Genre genre = new Genre(genreId, genreName);
        genre.retrieveGenreInfoFromDatabase();
        System.out.println("Books in the genre: " + genreName);
        List<Book> books = genre.listBooksByGenre();
        for (Book book : books) {
            System.out.println(book.getBookId() + ": " + book.getTitle() + " by Author ID " + book.getAuthorId());
        }
    } 
    private static int getGenreIdByName(String genreName) {
        String selectQuery = "SELECT genreid FROM genre WHERE genrename = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setString(1, genreName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("genreid");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return an invalid ID if not found
    }
    
    public static void main(String[] args) {
        DatabaseManager.setConnection();
        Scanner scan = new Scanner(System.in);
        
        // login or create account
        User user = login();
        
        String action = homepage(user);
        
        while (!action.equalsIgnoreCase("Q"))
        {
            if (action.equals("1")) {
                listAllBooks();
                System.out.println("Enter selection: ");
                int selection = scan.nextInt();
                // do checkout stuff
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
                browseByGenre();
                break;
            }
            else if  (action.equals("4")) {
               // listCheckedOutBooks(user);
            }
            else if  (action.equals("5")) {
                
            }
            else if  (action.equals("6")) {
                EditUser(user);
            }
            else if  (action.equals("7")) {
                System.out.println("Are you sure you would like to delete your profile?"); 
                System.out.print("Confirm by typing Y, or type N to go back.");
                String confirm = scan.nextLine();
                if (confirm.equalsIgnoreCase("y")){
                    user.deleteUser();
                    user = login();
                    }
            }
            action = homepage(user);
        }
        DatabaseManager.closeConnection();
        System.exit(0);
    }
    
}
