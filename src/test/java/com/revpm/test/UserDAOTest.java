package com.revpm.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.revpm.dao.UserDAO;
import com.revpm.model.User;

public class UserDAOTest {

    @Test
    void testLoginWithValidCredentials() {

        UserDAO dao = new UserDAO();

        // must exist in DB
        String email = "santosh@gmail.com";
        String password = "1I8ys&lF";

        User user = dao.login(email, password);

        assertNotNull(user);
        assertEquals(email, user.getEmail());
    }
}
