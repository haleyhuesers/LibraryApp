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
public class User {
    public int userID;
    
    public String firstname;
    
    public String lastname;
    
    public String email;
    
    public String password;
    
    //constructor
    public User(){
    }
    
    public int getId() {
        int userIdFromDatabase = -1; // Default value if user is not found

        try (Connection connection = DatabaseManager.getConnection();) {
            String sql = "SELECT userID FROM users WHERE firstName = ? AND lastName = ? AND email = ? AND password = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, this.firstname);
                preparedStatement.setString(2, this.lastname);
                preparedStatement.setString(3, this.email);
                preparedStatement.setString(4, this.password);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        userIdFromDatabase = resultSet.getInt("userID");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userIdFromDatabase;
    }
    
    public void fillUser(){
        String findUser = "Select * from Users where userID = ?";
        try{
        Connection connection = DatabaseManager.getConnection();
        PreparedStatement findUserPstmt = connection.prepareStatement(findUser);
        
        findUserPstmt.setInt(1, userID);
        ResultSet selectedUser = findUserPstmt.executeQuery();
        while (selectedUser.next()){
               firstname = selectedUser.getString("firstName");
               lastname = selectedUser.getString("lastName");
               email = selectedUser.getString("email");
               password = selectedUser.getString("password");
           }
        }
        catch (SQLException e){
            System.out.println("Couldn't find user.");
        }
    }
    
    public void registerUser(){
        String insertUser = "Insert into Users "
                + "(firstName, lastName, email, password) "
                + "values (?, ?, ?, ?)";
        try{
        Connection connection = DatabaseManager.getConnection();
        PreparedStatement insertUserPstmt = connection.prepareStatement(insertUser);
        
        insertUserPstmt.setString(1, firstname);
        insertUserPstmt.setString(2, lastname);
        insertUserPstmt.setString(3, email);
        insertUserPstmt.setString(4, password);
        insertUserPstmt.executeUpdate();
        System.out.println("User successfully registered!");
        }
        catch(SQLException e){
            System.out.println("Email already in use.");
        }
    }
    
    public boolean loginUser() {
        String getUser = "Select * from Users where email = ? and password = ?";
        boolean validated = false;
        try{
        Connection connection = DatabaseManager.getConnection();
        PreparedStatement getUserPstmt = connection.prepareStatement(getUser);
        
        getUserPstmt.setString(1, email);
        getUserPstmt.setString(2, password);
        ResultSet selectedUser = getUserPstmt.executeQuery();
        while (selectedUser.next()){
               validated = true; //if user is found, login returns true
               //fill rest of user information
               userID = selectedUser.getInt("userID");
               firstname = selectedUser.getString("firstName");
               lastname = selectedUser.getString("lastName");
           }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return validated;
    }
    public void updateUserDetails(){
        String updateUser = "UPDATE users SET firstname = ?, lastname = ?, email = ?, "
                + "password = ? WHERE userID = ?";
        
        try{
        Connection connection = DatabaseManager.getConnection();
        PreparedStatement updateUserPstmt = connection.prepareStatement(updateUser);
        
        updateUserPstmt.setString(1, firstname);
        updateUserPstmt.setString(2, lastname);
        updateUserPstmt.setString(3, email);
        updateUserPstmt.setString(4, password);
        updateUserPstmt.setInt(5, userID);
        updateUserPstmt.executeUpdate();
        System.out.println("Successfully updated user information.");
        }
        catch(SQLException e){
            System.out.println("Could not update user information.");
            fillUser();
        }
    }
    
    public void deleteUser(){
        String deleteUser = "DELETE FROM users WHERE userID = ?";
        
        try{
        Connection connection = DatabaseManager.getConnection();
        PreparedStatement deleteUserPstmt = connection.prepareStatement(deleteUser);
        deleteUserPstmt.setInt(1, userID);
        deleteUserPstmt.executeUpdate();
        System.out.println("Successfully deleted user.");
        }
        catch(SQLException e){
            System.out.println("Could not delete user.");
        }
    }
}
