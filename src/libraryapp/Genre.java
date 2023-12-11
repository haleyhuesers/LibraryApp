package libraryapp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Haley S
 */
public class Genre {
    
    private static final String URL = "jdbc:postgresql://localhost:5432/library";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "";
    
    private int genreId;
    private String name;
    
    public Genre(int genreId, String name) {
        this.genreId = genreId;
        this.name = name;
    }
    
    // Retrieves genre details from the database based on genreId
    public void retrieveGenreInfoFromDatabase() {
        String selectQuery = "SELECT name FROM genre WHERE genreId = ?";
        
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setInt(1, this.genreId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    this.name = resultSet.getString("name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Updates the genre's details in the database
    public void updateGenreInfoInDatabase() {
        String updateQuery = "UPDATE genre SET name = ? WHERE genreId = ?";

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setString(1, this.name);
            preparedStatement.setInt(2, this.genreId);
            preparedStatement.executeUpdate();

            System.out.println("Genre information updated successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lists all books in this genre
    public List<Book> listBooksByGenre() {
        List<Book> booksInGenre = new ArrayList<>();
        String selectQuery = "SELECT b.* FROM book b " +
                "INNER JOIN genre_of_book gb ON b.bookid = gb.bookid " +
                "WHERE gb.genreid = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setInt(1, this.genreId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int bookId = resultSet.getInt("bookid");
                    String title = resultSet.getString("title");
                    int authorId = resultSet.getInt("authorid");
                    int numAvailable = resultSet.getInt("numavailable");
                    // Assuming there is a Book constructor that takes these parameters
                    Book book = new Book(bookId, title, authorId, numAvailable);
                    booksInGenre.add(book);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return booksInGenre;
    }
    
    // Getters and Setters
    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
