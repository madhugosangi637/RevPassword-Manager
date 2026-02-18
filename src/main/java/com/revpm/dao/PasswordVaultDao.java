package com.revpm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.revpm.model.PasswordEntry;
import com.revpm.util.DBUtil;
import com.revpm.util.PasswordUtil;

public class PasswordVaultDao {
	
	private static final Logger logger =
            LogManager.getLogger(PasswordVaultDao.class);

    // ================= ADD PASSWORD =================
    public void addPassword(PasswordEntry entry) {

    	logger.info("Adding password for userId: {}", entry.getUserId());
    	
        String sql = "INSERT INTO password_vault " +
                     "(user_id, account_name, account_username, account_password) " +
                     "VALUES (?, ?, ?, ?)";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, entry.getUserId());
            ps.setString(2, entry.getAccountName());
            ps.setString(3, entry.getAccountUsername());

            // 🔐 ENCRYPT before storing
            ps.setString(4, PasswordUtil.encrypt(entry.getAccountPassword()));

            ps.executeUpdate();
            //System.out.println("Password saved successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= VIEW ALL PASSWORDS =================
    public List<PasswordEntry> getPasswordsByUser(int userId) {

        List<PasswordEntry> list = new ArrayList<>();
        String sql = "SELECT * FROM password_vault WHERE user_id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PasswordEntry p = new PasswordEntry();
                p.setVaultId(rs.getInt("vault_id"));
                p.setUserId(rs.getInt("user_id"));
                p.setAccountName(rs.getString("account_name"));
                p.setAccountUsername(rs.getString("account_username"));

                // 🔓 DECRYPT before showing
                p.setAccountPassword(
                    PasswordUtil.decrypt(rs.getString("account_password"))
                );

                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ================= SEARCH BY ACCOUNT NAME =================
    public PasswordEntry searchByAccountName(int userId, String accountName) {

        String sql = "SELECT * FROM password_vault WHERE user_id = ? AND account_name = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, accountName);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                PasswordEntry p = new PasswordEntry();
                p.setVaultId(rs.getInt("vault_id"));
                p.setUserId(rs.getInt("user_id"));
                p.setAccountName(rs.getString("account_name"));
                p.setAccountUsername(rs.getString("account_username"));

                // 🔓 DECRYPT
                p.setAccountPassword(
                    PasswordUtil.decrypt(rs.getString("account_password"))
                );

                return p;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ================= UPDATE PASSWORD =================
    public boolean updatePassword(int userId, String accountName, String newPassword) {

        String sql = "UPDATE password_vault SET account_password = ? " +
                     "WHERE user_id = ? AND account_name = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // 🔐 ENCRYPT new password
            ps.setString(1, PasswordUtil.encrypt(newPassword));
            ps.setInt(2, userId);
            ps.setString(3, accountName);

            return ps.executeUpdate() == 1;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ================= DELETE PASSWORD =================
    public boolean deletePassword(int userId, String accountName) {

        String sql = "DELETE FROM password_vault WHERE user_id = ? AND account_name = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, accountName);

            return ps.executeUpdate() == 1;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
