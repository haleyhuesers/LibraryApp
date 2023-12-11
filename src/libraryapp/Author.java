package libraryapp;

import java.sql.SQLException;
import java.sql.*;
/**
 *
 * @author hhues
 */
public class Author {
        
    private int authorId;
    private String firstName;
    private String lastName;
    
    public Author(int id, String fName, String lName) {
        authorId  = id;
        firstName = fName;
        lastName = lName;
    }

    public Author(int id) {
        authorId  = id;
        retrieveAuthorInfoFromDatabase();
    }
    
    private void retrieveAuthorInfoFromDatabase() {
        String selectQuery = "SELECT firstName, lastName FROM author WHERE authorId = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setInt(1, this.authorId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Set the class variables based on the database result
                    this.firstName = resultSet.getString("firstName");
                    this.lastName = resultSet.getString("lastName");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAuthorInfoInDatabase() {
        String updateQuery = "UPDATE author SET firstName = ?, lastName = ? WHERE authorId = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setString(1, this.firstName);
            preparedStatement.setString(2, this.lastName);
            preparedStatement.setInt(3, this.authorId);

            // Execute the update query
            preparedStatement.executeUpdate();

            System.out.println("Author information updated successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Getters
    public int getAuthorId() {
        return authorId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    // Setters
    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public void addAuthor() {
        String insertQuery = "INSERT INTO author (authorid, firstname, lastname) VALUES (?, ?, ?)";
        try {
        Connection connection = DatabaseManager.getConnection();

        PreparedStatement stmt = connection.prepareStatement(insertQuery); 

        stmt.setInt(1, this.authorId);
        stmt.setString(2, this.firstName);
        stmt.setString(3, this.lastName);

        // Execute the query
        stmt.executeUpdate();

        System.out.println("Author added successfully.");
                } catch (SQLException e) {
            e.printStackTrace();
        }      
    }
    
    // lists books by book id
    public void listBooks() {
        System.out.println("Books written by " + this.firstName + " " + this.lastName);

        // Fetch and display books written by the author from the database
        String selectQuery = "SELECT b.title, b.bookid FROM book b WHERE b.authorId = ?";
        
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setInt(1, this.authorId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String bookTitle = resultSet.getString("title");
                    int bookId = resultSet.getInt("bookid");
                    System.out.println(bookId + ") " + bookTitle);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); 
        }
    }
    
    
    
}
