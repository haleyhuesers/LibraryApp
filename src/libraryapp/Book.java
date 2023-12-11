package libraryapp;

import java.sql.*;
/**
 *
 * @author hhues
 */
public class Book {
    
    private int bookId;
    private String title;
    private int authorId;
    private int numAvailable;
       
    public Book(int bookId, String title, int authorId, int numAvail) {
        this.bookId = bookId;
        this.title = title;
        this.authorId = authorId;
        numAvailable = numAvail;
    }
    
    public Book(int bookId) {
        this.bookId = bookId;
        retrieveBookInfoFromDatabase();
    }
    
    private void retrieveBookInfoFromDatabase() {
        String selectQuery = "SELECT title, authorId, numAvailable FROM book WHERE bookId = ?";
        
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setInt(1, this.bookId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Set the class variables based on the database result
                    this.title = resultSet.getString("title");
                    this.authorId = resultSet.getInt("authorId");
                    this.numAvailable = resultSet.getInt("numAvailable");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void updateBookInfoInDatabase() {
        String updateQuery = "UPDATE book SET title = ?, authorId = ?, numAvailable = ? WHERE bookId = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setString(1, this.title);
            preparedStatement.setInt(2, this.authorId);
            preparedStatement.setInt(3, this.numAvailable);
            preparedStatement.setInt(4, this.bookId);

            // Execute the update query
            preparedStatement.executeUpdate();

            System.out.println("Book information updated successfully.");

        } catch (SQLException e) {
            e.printStackTrace(); 
        }
    }
    
    // Getters
    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public int getAuthorId() {
        return authorId;
    }

    public int getNumAvailable() {
        return numAvailable;
    }

    // Setters
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public void setNumAvailable(int numAvailable) {
        this.numAvailable = numAvailable;
    }
    
    // do we need this?
    public void addBook() {
        String insertQuery = "INSERT INTO book (bookid, title, numavailable, authorid) VALUES (?, ?, ?, ?)";

        try {
            Connection connection = DatabaseManager.getConnection();

            PreparedStatement stmt = connection.prepareStatement(insertQuery); 
        
            stmt.setInt(1, this.bookId);
            stmt.setString(2, this.title);
            stmt.setInt(3, this.numAvailable);
            stmt.setInt(4, this.authorId);

            // Execute the query
            stmt.executeUpdate();

            System.out.println("Book added successfully.");

        } catch (SQLException e) {
            e.printStackTrace(); 
        }      
    }
         
    public boolean checkAvailability() {
        if (bookId == 0) {
            return false;
        }
        return true;
    }
    
    public void returnBook() {
        this.numAvailable = this.numAvailable + 1;
    }
    
    public void checkoutBook() {
        if (checkAvailability()) {
            this.numAvailable = this.numAvailable - 1;            
        }
        else
            System.out.println("Book not available.");
    }
}
