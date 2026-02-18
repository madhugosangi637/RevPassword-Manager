package com.revpm.dao;

import com.revpm.model.SecurityQuestion;
import com.revpm.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SecurityQuestionDao {

    public boolean saveQuestion(SecurityQuestion sq) {
        String sql = "INSERT INTO security_questions (user_id, question, answer) VALUES (?, ?, ?)";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, sq.getUserId());
            ps.setString(2, sq.getQuestion());
            ps.setString(3, sq.getAnswer());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public SecurityQuestion getQuestionByUserId(int userId) {
        String sql = "SELECT question, answer FROM security_questions WHERE user_id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                SecurityQuestion sq = new SecurityQuestion();
                sq.setUserId(userId);
                sq.setQuestion(rs.getString("question"));
                sq.setAnswer(rs.getString("answer"));
                return sq;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    

}
