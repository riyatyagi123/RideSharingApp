package com.ridesharing.dao;

import com.ridesharing.db.DBConnection;
import java.sql.*;

public class BookingDAO {

    public void bookRide(int uid, int rideId, int seats) {

        try (Connection c = DBConnection.getConnection()) {

            c.setAutoCommit(false);

            PreparedStatement ps1 =
                    c.prepareStatement(
                            "SELECT seats,fare FROM ride WHERE rideid=? AND status='ACTIVE'");
            ps1.setInt(1, rideId);
            ResultSet rs = ps1.executeQuery();

            if (!rs.next() || rs.getInt(1) < seats) {
                System.out.println("Booking Failed");
                c.rollback();
                return;
            }

            double total = rs.getDouble(2) * seats;

            PreparedStatement ps2 =
                    c.prepareStatement(
                            "INSERT INTO booking(userid,rideid,seats_booked,total,booking_time,status) " +
                            "VALUES(?,?,?,?,NOW(),'CONFIRMED')");
            ps2.setInt(1, uid);
            ps2.setInt(2, rideId);
            ps2.setInt(3, seats);
            ps2.setDouble(4, total);
            ps2.executeUpdate();

            PreparedStatement ps3 =
                    c.prepareStatement("UPDATE ride SET seats=seats-? WHERE rideid=?");
            ps3.setInt(1, seats);
            ps3.setInt(2, rideId);
            ps3.executeUpdate();

            c.commit();
            System.out.println("Ride Booked Successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewMyBookings(int uid) {

        String sql =
                "SELECT b.bookingid,r.source,r.destination,b.total,b.status " +
                "FROM booking b JOIN ride r ON b.rideid=r.rideid WHERE b.userid=?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, uid);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println(
                        rs.getInt(1) + " | " +
                        rs.getString(2) + " -> " +
                        rs.getString(3) +
                        " Total:" + rs.getDouble(4) +
                        " Status:" + rs.getString(5)
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
