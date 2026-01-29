package com.ridesharing.dao;

import com.ridesharing.db.DBConnection;
import java.sql.*;

public class RideDAO {

    public void createRide(String src, String dest, int seats, double fare, int uid) {
        String sql =
                "INSERT INTO ride(source,destination,seats,fare,createdby,status,ride_time) " +
                "VALUES(?,?,?,?,?,'ACTIVE',NOW())";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, src);
            ps.setString(2, dest);
            ps.setInt(3, seats);
            ps.setDouble(4, fare);
            ps.setInt(5, uid);
            ps.executeUpdate();

            System.out.println("Ride Created");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewAllRides() {
        String sql = "SELECT * FROM ride WHERE status='ACTIVE' AND seats>0";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                System.out.println(
                        rs.getInt("rideid") + " | " +
                        rs.getString("source") + " -> " +
                        rs.getString("destination") +
                        " Seats:" + rs.getInt("seats") +
                        " Fare:" + rs.getDouble("fare")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewMyRides(int uid) {
        String sql = "SELECT * FROM ride WHERE createdby=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, uid);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println(
                        rs.getInt("rideid") + " | " +
                        rs.getString("source") + " -> " +
                        rs.getString("destination") +
                        " Seats:" + rs.getInt("seats") +
                        " Status:" + rs.getString("status")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelMyRide(int rideId, int uid) {
        String sql =
                "UPDATE ride SET status='CANCELLED' WHERE rideid=? AND createdby=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, rideId);
            ps.setInt(2, uid);
            int r = ps.executeUpdate();

            System.out.println(r > 0 ? "Ride Cancelled" : "Cannot Cancel Ride");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
