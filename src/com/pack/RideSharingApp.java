package com.pack;

import java.sql.*;
import java.util.Scanner;

public class RideSharingApp {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        while (true) {
            System.out.println("\n1 Register  2 Login  3 Exit");
            int ch = sc.nextInt();
            sc.nextLine();

            if (ch == 1) register();
            else if (ch == 2) login();
            else break;
        }
    }

    // ---------------- REGISTER ----------------
    static void register() {
        try (Connection con = DBConnection.getConnection()) {

            System.out.print("Name: ");
            String name = sc.nextLine();
            System.out.print("Mobile: ");
            String mobile = sc.nextLine();
            System.out.print("Password: ");
            String pass = sc.nextLine();

            String sql =
                "INSERT INTO user(name,mobile,password) VALUES(?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, mobile);
            ps.setString(3, pass);
            ps.executeUpdate();

            System.out.println("User Registered");

        } catch (Exception e) {
            System.out.println("Registration failed");
        }
    }

    // ---------------- LOGIN ----------------
    static void login() {
        try (Connection con = DBConnection.getConnection()) {

            System.out.print("Name: ");
            String name = sc.nextLine();
            System.out.print("Password: ");
            String pass = sc.nextLine();

            String sql =
                "SELECT userid, role FROM user WHERE name=? AND password=?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, pass);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Session.userId = rs.getInt("userid");
                Session.role = rs.getString("role");
                Session.userName = name;

                System.out.println("Login Successful");
                userMenu();
            } else {
                System.out.println("Login Failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- MENU ----------------
    static void userMenu() {
        while (true) {
            System.out.println("\nWelcome " + Session.userName +
                               " [" + Session.role + "]");
            System.out.println("1 View Rides");
            System.out.println("2 Book Ride");
            System.out.println("3 My Bookings");
            System.out.println("4 Create Ride");
            System.out.println("0 Logout");

            int ch = sc.nextInt();
            sc.nextLine();

            if (ch == 1) viewRides();
            else if (ch == 2) bookRide();
            else if (ch == 3) viewMyBookings();
            else if (ch == 4) createRide();
            else break;
        }
    }

    // ---------------- VIEW RIDES ----------------
    static void viewRides() {
        try (Connection con = DBConnection.getConnection()) {

            String sql =
                "SELECT rideid,source,destination,seats,fare FROM ride WHERE status='ACTIVE'";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                System.out.println(
                    rs.getInt("rideid") + " " +
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

    // ---------------- BOOK RIDE ----------------
    static void bookRide() {
        try (Connection con = DBConnection.getConnection()) {

            System.out.print("Enter Ride ID: ");
            int rideId = sc.nextInt();
            System.out.print("Seats to book: ");
            int seats = sc.nextInt();
            sc.nextLine();

            con.setAutoCommit(false);

            PreparedStatement ps1 =
                con.prepareStatement(
                    "SELECT seats,fare FROM ride WHERE rideid=? AND status='ACTIVE'");
            ps1.setInt(1, rideId);
            ResultSet rs = ps1.executeQuery();

            if (!rs.next() || rs.getInt("seats") < seats) {
                System.out.println("Not enough seats available");
                con.rollback();
                return;
            }

            double total = rs.getDouble("fare") * seats;

            PreparedStatement ps2 =
                con.prepareStatement(
                    "INSERT INTO booking(userid,rideid,seats_booked,total,booking_time) " +
                    "VALUES(?,?,?,?,NOW())");
            ps2.setInt(1, Session.userId);
            ps2.setInt(2, rideId);
            ps2.setInt(3, seats);
            ps2.setDouble(4, total);
            ps2.executeUpdate();

            PreparedStatement ps3 =
                con.prepareStatement(
                    "UPDATE ride SET seats=seats-? WHERE rideid=?");
            ps3.setInt(1, seats);
            ps3.setInt(2, rideId);
            ps3.executeUpdate();

            con.commit();
            System.out.println("Ride Booked Successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- MY BOOKINGS ----------------
    static void viewMyBookings() {
        try (Connection con = DBConnection.getConnection()) {

            String sql =
                "SELECT b.bookingid,r.source,r.destination,b.seats_booked,b.total " +
                "FROM booking b JOIN ride r ON b.rideid=r.rideid " +
                "WHERE b.userid=?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, Session.userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println(
                    rs.getInt(1) + " " +
                    rs.getString(2) + " -> " +
                    rs.getString(3) +
                    " Seats:" + rs.getInt(4) +
                    " Total:" + rs.getDouble(5)
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- CREATE RIDE ----------------
    static void createRide() {

        if (!isValidDriver()) {
            System.out.print(
                "You are not a driver. Do you want to become a driver? (Y/N): ");
            String c = sc.nextLine();
            if (!c.equalsIgnoreCase("Y")) return;
            if (!addLicenseAndBecomeDriver()) return;
        }

        try (Connection con = DBConnection.getConnection()) {

            System.out.print("Source: ");
            String src = sc.nextLine();
            System.out.print("Destination: ");
            String dest = sc.nextLine();
            System.out.print("Seats: ");
            int seats = sc.nextInt();
            System.out.print("Fare: ");
            double fare = sc.nextDouble();
            sc.nextLine();

            String sql =
                "INSERT INTO ride(source,destination,seats,fare,createdby,ride_time) " +
                "VALUES(?,?,?,?,?,NOW())";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, src);
            ps.setString(2, dest);
            ps.setInt(3, seats);
            ps.setDouble(4, fare);
            ps.setInt(5, Session.userId);
            ps.executeUpdate();

            System.out.println("Ride Created");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- LICENSE CHECK ----------------
    static boolean isValidDriver() {
        try (Connection con = DBConnection.getConnection()) {

            String sql =
                "SELECT 1 FROM driver_license " +
                "WHERE userid=? AND verified=TRUE AND expiry_date>=CURDATE()";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, Session.userId);
            return ps.executeQuery().next();

        } catch (Exception e) {
            return false;
        }
    }

    // ---------------- ADD LICENSE ----------------
    static boolean addLicenseAndBecomeDriver() {
        try (Connection con = DBConnection.getConnection()) {

            System.out.print("Enter License Number: ");
            String license = sc.nextLine();
            System.out.print("Enter Expiry Date (yyyy-mm-dd): ");
            Date expiry = Date.valueOf(sc.nextLine());

            if (expiry.before(new java.util.Date())) {
                System.out.println("License already expired");
                return false;
            }

            PreparedStatement ps =
                con.prepareStatement(
                    "INSERT INTO driver_license(userid,license_number,expiry_date,verified) " +
                    "VALUES(?,?,?,TRUE)");
            ps.setInt(1, Session.userId);
            ps.setString(2, license);
            ps.setDate(3, expiry);
            ps.executeUpdate();

            PreparedStatement ps2 =
                con.prepareStatement(
                    "UPDATE user SET role='DRIVER' WHERE userid=?");
            ps2.setInt(1, Session.userId);
            ps2.executeUpdate();

            Session.role = "DRIVER";
            System.out.println("You are now registered as DRIVER");
            return true;

        } catch (Exception e) {
            System.out.println("License already exists or error occurred");
            return false;
        }
    }
}
