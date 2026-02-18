package com.revpm.test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.revpm.dao.PasswordVaultDao;
import com.revpm.model.PasswordEntry;
import com.revpm.util.DBUtil;
import com.revpm.util.PasswordUtil;

public class PasswordVaultDaoTest {

    private static int TEST_USER_ID;

    // ✅ Fetch an existing user_id BEFORE tests
    @BeforeAll
    static void setupUserId() throws Exception {

        String sql = "SELECT user_id FROM users FETCH FIRST 1 ROWS ONLY";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                TEST_USER_ID = rs.getInt("user_id");
            } else {
                throw new RuntimeException("❌ No users found in USERS table");
            }
        }
    }

    @Test
    void testAddPassword() {

        PasswordVaultDao dao = new PasswordVaultDao();

        PasswordEntry entry = new PasswordEntry();
        entry.setUserId(TEST_USER_ID);
        entry.setAccountName("JUnit-Gmail");
        entry.setAccountUsername("junit@gmail.com");
        entry.setAccountPassword(
                PasswordUtil.encrypt("JUnitPassword@123")
        );

        assertDoesNotThrow(() -> dao.addPassword(entry));
    }

    @Test
    void testGetPasswordsByUser() {

        PasswordVaultDao dao = new PasswordVaultDao();

        List<PasswordEntry> list =
                dao.getPasswordsByUser(TEST_USER_ID);

        assertNotNull(list);
    }

    @Test
    void testSearchByAccountName() {

        PasswordVaultDao dao = new PasswordVaultDao();

        String accountName = "JUnit-Search-" + System.currentTimeMillis();

        // Insert fresh data
        PasswordEntry entry = new PasswordEntry();
        entry.setUserId(TEST_USER_ID);
        entry.setAccountName(accountName);
        entry.setAccountUsername("search@gmail.com");
        entry.setAccountPassword(
                PasswordUtil.encrypt("JUnitPassword@123")
        );

        dao.addPassword(entry);

        // Search
        PasswordEntry result =
                dao.searchByAccountName(TEST_USER_ID, accountName);

        assertNotNull(result);

        String decrypted =
                PasswordUtil.decrypt(result.getAccountPassword());

        assertEquals("JUnitPassword@123", decrypted);

        // Cleanup
        dao.deletePassword(TEST_USER_ID, accountName);
    }



    @Test
    void testUpdatePassword() {

        PasswordVaultDao dao = new PasswordVaultDao();

        String accountName = "JUnit-Update-" + System.currentTimeMillis();

        PasswordEntry entry = new PasswordEntry();
        entry.setUserId(TEST_USER_ID);
        entry.setAccountName(accountName);
        entry.setAccountUsername("update@gmail.com");
        entry.setAccountPassword(
                PasswordUtil.encrypt("Old@123")
        );

        dao.addPassword(entry);

        boolean updated =
                dao.updatePassword(
                        TEST_USER_ID,
                        accountName,
                        PasswordUtil.encrypt("UpdatedJUnit@123")
                );

        assertTrue(updated);

        PasswordEntry updatedEntry =
                dao.searchByAccountName(TEST_USER_ID, accountName);

        assertEquals(
                "UpdatedJUnit@123",
                PasswordUtil.decrypt(updatedEntry.getAccountPassword())
        );

        dao.deletePassword(TEST_USER_ID, accountName);
    }

    @Test
    void testDeletePassword() {

        PasswordVaultDao dao = new PasswordVaultDao();

        // 🔹 Insert fresh data JUST for this test
        PasswordEntry entry = new PasswordEntry();
        entry.setUserId(TEST_USER_ID);
        entry.setAccountName("JUnit-Delete");
        entry.setAccountUsername("delete@gmail.com");
        entry.setAccountPassword(
                PasswordUtil.encrypt("DeletePass@123")
        );

        dao.addPassword(entry);

        // 🔹 Now delete
        boolean deleted =
                dao.deletePassword(TEST_USER_ID, "JUnit-Delete");

        assertTrue(deleted);
    }
    
    @Test
    void sanityCheck() {
        System.out.println("Sanity test running");
    }


}
