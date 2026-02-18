package com.revpm.dao;

import com.revpm.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.revpm.util.DBUtil;
import com.revpm.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // Register user
	
	
	
    private static final Logger logger =
            LogManager.getLogger(UserDAO.class);
    
	public boolean registerUser(User user) {
		logger.info("Registering user with email: {}", user.getEmail());
		String insertQuery =
	            "INSERT INTO users (username, master_password, full_name, email) VALUES (?, ?, ?, ?)";

	        try (Connection con = DBUtil.getConnection();
	             PreparedStatement ps =
	                 con.prepareStatement(insertQuery, new String[] { "USER_ID" })) {

	            ps.setString(1, user.getUsername());
	            ps.setString(2, PasswordUtil.hashMasterPassword(user.getMasterPassword()));
	            ps.setString(3, user.getFullName());
	            ps.setString(4, user.getEmail());

	            int rows = ps.executeUpdate();

	            if (rows > 0) {
	                ResultSet rs = ps.getGeneratedKeys();
	                if (rs.next()) {
	                    user.setUserId(rs.getInt(1));
	                }
	                return true;
	            }
	            
	            logger.info("User registered successfully: {}", user.getEmail());
	            return true;

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return false;
	    }

	

    // Login user
    public User login(String email, String password) {
            String sql =
                "SELECT user_id, username, master_password, full_name, email " +
                "FROM users WHERE email = ?";

            try (Connection con = DBUtil.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {

                    String dbHash = rs.getString("master_password");
                    String inputHash = PasswordUtil.hashMasterPassword(password);

                    // 🔐 compare hash with hash
                    if (dbHash.equals(inputHash)) {
                        User user = new User();
                        user.setUserId(rs.getInt("user_id"));
                        user.setUsername(rs.getString("username"));
                        user.setFullName(rs.getString("full_name"));
                        user.setEmail(rs.getString("email"));
                        return user;
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        }
    
    public User findByUsername(String username) {
        User user = null;
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setMasterPassword(rs.getString("master_password"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
    public boolean updatePassword(int userId, String newPassword) {

        String sql = "UPDATE users SET master_password = ? WHERE user_id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // 🔐 HASH the new password before saving
            String hashedPassword = PasswordUtil.hashMasterPassword(newPassword);

            ps.setString(1, hashedPassword);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    
    public boolean updateUserProfile(int userId, String fullName, String email) {
        String sql = "UPDATE users SET full_name = ?, email = ? WHERE user_id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, fullName);
            ps.setString(2, email);
            ps.setInt(3, userId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean verifyMasterPassword(int userId, String inputPassword) {

        String sql = "SELECT master_password FROM users WHERE user_id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("master_password");

                // 🔐 HASH THE INPUT PASSWORD
                String inputHash = PasswordUtil.hashMasterPassword(inputPassword);

                // ✅ COMPARE HASH vs HASH
                return storedHash.equals(inputHash);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


 // In UserDAO.java
 // Checks if email already exists without inserting
    public boolean isEmailAvailable(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0; // true if email is free
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // assume not available if error
    }

    public User findByEmail(String email) {

        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("user_id"));
                u.setFullName(rs.getString("full_name"));
                u.setEmail(rs.getString("email"));
                u.setMasterPassword(rs.getString("master_password"));
                return u;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
