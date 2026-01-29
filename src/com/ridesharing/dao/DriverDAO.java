package com.ridesharing.dao;

import com.ridesharing.db.DBConnection;
import java.sql.*;

public class DriverDAO {

    public boolean isValidDriver(int userId) {
        String sql =
            "SELECT * FROM driver_license " +
            "WHERE userid=? AND verified=TRUE AND expiry_date>=CURDATE()";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, userId);
            return ps.executeQuery().next();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addLicense(int userId, String licenseNo, Date expiry) {

        if (expiry.before(new java.util.Date())) {
            System.out.println("License already expired");
            return false;
        }

        String sql =
            "INSERT INTO driver_license(userid,license_number,expiry_date,verified) " +
            "VALUES(?,?,?,TRUE)";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, licenseNo);
            ps.setDate(3, expiry);
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("License already exists");
        }
        return false;
    }
}
