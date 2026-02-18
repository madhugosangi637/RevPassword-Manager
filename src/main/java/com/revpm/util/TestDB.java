package com.revpm.util;

public class TestDB {
    public static void main(String[] args) {
        try {
            DBUtil.getConnection();
            System.out.println("Connection successful!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
