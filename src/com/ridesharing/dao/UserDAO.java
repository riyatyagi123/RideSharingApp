package com.ridesharing.dao;

import com.ridesharing.db.DBConnection;
import java.sql.*;

public class UserDAO {

    public void register(String name, String mobile, String password) {
        String sql = "INSERT INTO user(name,mobile,password) VALUES(?,?,?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, mobile);
            ps.setString(3, password);
            ps.executeUpdate();
            System.out.println("User Registered Successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int login(String mobile, String password) {
        String sql = "SELECT userid FROM user WHERE mobile=? AND password=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, mobile);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                return rs.getInt("userid");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
