package repositories;

import database.DBConnector;
import models.User;
import models.dto.create.CreateUser;
import models.dto.update.UpdateUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {

    public boolean shtoUser(CreateUser user) {
        String query = "INSERT INTO Users (name,surname,email,username, password,role) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getEmer());
            stmt.setString(5, user.getMbiemer());
            stmt.setString(6, user.getRole().toString());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User authenticate(String username, String password) {
        String query = "SELECT * FROM Users WHERE username = ? AND password = ?";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet result = stmt.executeQuery()) {
                if (result.next()) {
                    return User.getInstance(result);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean verifyUser(String username, String verificationCode) {
        String query;
        boolean isNumeric = verificationCode.matches("\\d+");

        if (isNumeric) {
            query = "SELECT * FROM users WHERE username = ? AND id = ?";
        } else {
            query = "SELECT * FROM users WHERE username = ? AND password = ?";
        }

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);

            if (isNumeric) {
                stmt.setInt(2, Integer.parseInt(verificationCode));
            } else {
                stmt.setString(2, verificationCode);
            }

            try (ResultSet result = stmt.executeQuery()) {
                return result.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePassword(UpdateUser updateUser) {
        String query = "UPDATE users SET password = ? WHERE username = ?";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, updateUser.getNewPassword());
            stmt.setString(2, updateUser.getUsername());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
